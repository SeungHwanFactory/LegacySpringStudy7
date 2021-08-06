package com.nsh.service;

import java.util.List;

import com.nsh.domain.Paging;
import com.nsh.domain.ReplyPageDTO;
import com.nsh.domain.ReplyVO;

public interface ReplyService {
	
	/**
	 * 댓글작성
	 */
	public int registerReply(ReplyVO vo);
	
	/**
	 * 댓글조회
	 */
	public ReplyVO getReplyInfo(Long rno);
	
	/**
	 * 댓글수정
	 */
	public int modifyReply(ReplyVO vo);
	
	/**
	 * 댓글삭제
	 */
	public int removeReply(Long rno);
	
	/**
	 * 게시글에 달린 댓글조회
	 */
	public List<ReplyVO> getListReply(Paging page, Long bno);

	/**
	 * 댓글리스트와 댓글개수를 받는 추상메서드
	 */
	public ReplyPageDTO getListPage(Paging page, Long bno);
}
