package com.nsh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsh.domain.Paging;
import com.nsh.domain.ReplyPageDTO;
import com.nsh.domain.ReplyVO;
import com.nsh.mapper.ReplyMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {

	private ReplyMapper mapper;
	
	@Autowired
	public ReplyServiceImpl(ReplyMapper mapper) {
		this.mapper = mapper;
	}
	
	
	@Override
	public int registerReply(ReplyVO vo) {
		
		log.info("서비스 계층에서 댓글작성 요청을 받았다!!! " + vo);
		
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO getReplyInfo(Long rno) {
		
		log.info("서비스 계층에서 댓글조회 요청을 받았다!!! " + rno);
		
		return mapper.read(rno);
	}

	@Override
	public int modifyReply(ReplyVO vo) {
		
		log.info("서비스 게층에서 댓글수정 요청을 받았다!!!" + vo);
		
		return mapper.update(vo);
	}

	@Override
	public int removeReply(Long rno) {
		
		log.info("서비스 계층에서 댓글삭제 요청을 받았다!!! " + rno);
		
		return mapper.delete(rno);
	}

	@Override
	public List<ReplyVO> getListReply(Paging page, Long bno) {
		
		log.info("서비스 계층에서 해당 게시글의 댓글목록을 요청했다!!! " + bno);
		
		return mapper.getListWithPaging(page, bno);
	}


	@Override
	public ReplyPageDTO getListPage(Paging page, Long bno) {
		
		return new ReplyPageDTO(
				mapper.getCountByBno(bno),
				mapper.getListWithPaging(page, bno));
	}

}
