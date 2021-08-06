package com.nsh.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {

	/*
	 * startPage: 시작페이지
	 * endPage: 끝페이지
	 * prev: 이전
	 * next: 다음
	 * total: 전체 데이터 수
	 * page: 페이징 처리 데이터 수
	 * */
	private int startPage;
	private int endPage;
	private boolean prev, next;
	
	private int total;
	private Paging page;
	
	/*
	 * 생성자
	 * 페이징 처리 객체와, 총 데이터 수를 파라미터로 받음
	 * endPage: getPageNum()을 통해 PageNum을 받아서 10.0을 나눈 후, ceil()을
	 * 사용해 올림처리하고 곱하기 10한 것을 int로 형변환 후 endPage변수에 저장.
	 * startPage: endPage의 9를 뺀 결과를 저장한다.
	 * realEnd: 만약, endPage보다 realEnd가 작다면 realEnd값을 endPage에 저장
	 * prev: startPage가 1보다 크거나 그렇지 않다면 true/false 저장
	 * next: endPage가 realEnd보다 작거나 그렇지 않다면 true/false 저장
	 * */
	public PageDTO(Paging page, int total) {
		this.page = page;
		this.total = total;
		
		this.endPage = (int) (Math.ceil(page.getPageNum() / 10.0)) * 10;
		
		this.startPage = this.endPage - 9;
		
		int realEnd = (int) (Math.ceil((total * 1.0) / page.getAmount()));
		
		if (realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		this.prev = this.startPage > 1;
		
		this.next = this.endPage < realEnd;
	}
}
