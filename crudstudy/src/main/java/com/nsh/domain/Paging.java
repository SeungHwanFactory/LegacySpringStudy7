package com.nsh.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Paging {
	
	/*
	 * pageNum : 페이지 번호
	 * amount : 한 페이지 당 보여줄 데이터 수
	 * */
	private int pageNum;
	private int amount;
	
	/*
	 * default 생성자
	 * */
	public Paging() {
		this(1,10);
	}
	
	public Paging(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	/**
	 * 게시물 삭제 후에 페이지 번호를 유지하기 위해 필요함
	 */
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount());
		
		return builder.toUriString();
	}

}
