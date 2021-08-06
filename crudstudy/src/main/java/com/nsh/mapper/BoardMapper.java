package com.nsh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.nsh.domain.BoardVO;
import com.nsh.domain.Paging;

public interface BoardMapper {

	/*
	 * xml파일로 처리했으면 필요없다.
	 * */
	//@Select("select * from tbl_board where bno > 0")
	public List<BoardVO> getList();
	
	public void insert(BoardVO board);
	
	public void insertSelectKey(BoardVO board);
	
	public BoardVO read(Long bno);
	
	public int delete(Long bno);
	
	public int update(BoardVO board);
	
	/*
	 * 페이징 처리 추상 메서드
	 * */
	public List<BoardVO> getListWithPaging(Paging pag);
	
	/*
	 * 전체 데이터 개수 처리
	 * */
	public int getTotalCount(Paging page);
}
