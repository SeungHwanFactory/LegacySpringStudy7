package com.nsh.domain;

import lombok.Data;

/**
 * 파일 정보를 처리하기 위한 클래스 설계
 * 
 * 기존 BoardVO에 등록 시 한번에 BoardAttachVO를 처리할 수 있게 필드 추가해야됨
 */
@Data
public class BoardAttachVO {

	private String uuid;
	private String uploadPath;
	private String fileName;
	private boolean fileType;
	
	private Long bno;
}
