package com.nsh.domain;

import java.util.List;

import lombok.Data;

@Data
public class ReplyPageDTO {

	private int replyCnt;
	private List<ReplyVO> list;
	
	public ReplyPageDTO() {
		
	}
	
	public ReplyPageDTO(int replyCnt, List<ReplyVO> list) {
		this.replyCnt = replyCnt;
		this.list = list;
	}
}
