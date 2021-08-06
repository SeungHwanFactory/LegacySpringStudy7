package com.nsh.domain;

import lombok.Data;

/**
 * @author nsh
 * 객체 반환하기 위해 클래스 생성
 */
@Data
public class SampleVO {
	
	private Integer mno;
	private String firstName;
	private String lastName;
	
	public SampleVO() {
		
	}
	
	public SampleVO(Integer mno, String firstName, String lastName) {
		this.mno = mno;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
}
