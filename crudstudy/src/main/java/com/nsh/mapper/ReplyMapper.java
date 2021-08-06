package com.nsh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nsh.domain.Paging;
import com.nsh.domain.ReplyVO;

public interface ReplyMapper {
	
	/**
	 * 댓글 작성(Create)
	 */
	public int insert(ReplyVO replyInfo);
	
	/**
	 * 댓글 조회(Read)
	 */
	public ReplyVO read(Long rno);
	
	/**
	 * 댓글 수정(Upate) - 내용과 updateDate를 수정합니다.
	 */
	public int update(ReplyVO reply);
	
	/**
	 * 댓글 삭제(Delete) - rno만으로 처리 가능
	 */
	public int delete (Long rno);

	/**
	 * 댓글 페이징처리는 게시글 안에서 이루어지기 때문에 게시물의 번호가 필요함
	 * @Param 어노테이션을 사용해서 두 개 이상의 파라미터를 전달 받도록 하자.
	 * 댓글 페이징처리는 게시글 페이징처리 때 이용한 Paging 클래스를 이용한다.
	 */
	public List<ReplyVO> getListWithPaging(
			@Param("page") Paging page,
			@Param("bno") Long bno
			);
	

	/**
	 * 댓글을 페이징 처리하기 위해서 전체 댓글의 숫자가 필요함
	 */
	public int getCountByBno(Long bno);
}
