package com.nsh.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {

	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updateDate;
	
	//아 댓글처리 증발했네.. git 잘못써서...
	
	//업로드 파일 정보 다 받는
	private List<BoardAttachVO> attachList;
	
	
}
