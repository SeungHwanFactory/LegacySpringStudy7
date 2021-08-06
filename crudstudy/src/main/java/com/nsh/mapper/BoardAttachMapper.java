package com.nsh.mapper;

import java.util.List;

import com.nsh.domain.BoardAttachVO;

public interface BoardAttachMapper {

	//파일업로드
	public void insert(BoardAttachVO vo);
	
	//파일 삭제
	public void delete(String uuid);
	
	//파일 정보
	public List<BoardAttachVO> findByBno(Long bno);
	
	//게시글이 삭제되면 첨부파일도 삭제
	public void deleteAll(Long bno);
	
	//데이터 베이스에 어제 등록된 모든 파일 목록이 필요하기에
	public List<BoardAttachVO> getOldFiles();
	
}
