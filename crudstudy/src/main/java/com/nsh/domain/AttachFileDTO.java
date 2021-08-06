package com.nsh.domain;

import lombok.Data;

/**
 * 업로드는 완료 됐지만 브라우저 쪽에 데이터를 전달하지 않았음
 * 업로드된 파일을 피드백 받기위해 이와 같이 설계해야됨
 * - 업로드된 파일의 이름과 원본 파일 이름
 * - 파일이 저장된 경로
 * - 업로드된 파일이 이미지인지 아닌지
 * 
 * >> 별도의 객체를 생성해서 처리한다.
 * >> 그러므로 AttachFileDTO 클래스를 만든것
 */
@Data
public class AttachFileDTO {

	private String fileName; //원본 이름
	private String uploadPath; //파일이 저장된 경로
	private String uuid; //uuid
	private boolean image; //이미지 체크
}
