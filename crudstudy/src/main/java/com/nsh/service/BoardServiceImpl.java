package com.nsh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsh.domain.BoardAttachVO;
import com.nsh.domain.BoardVO;
import com.nsh.domain.Paging;
import com.nsh.mapper.BoardAttachMapper;
import com.nsh.mapper.BoardMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class BoardServiceImpl implements BoardService{

	private BoardMapper mapper;
	
	private BoardAttachMapper attachMapper;
	
	/**
	 * 업로드 정보를 받는 attachMapper를 DI
	 */
	@Autowired
	public BoardServiceImpl(BoardMapper mapper, BoardAttachMapper attachMapper) {
		this.mapper = mapper;
		this.attachMapper = attachMapper;
	}	
	
	/**
	 * 게시글 등록작업은 tbl_board와 tbl_attach 모두 insert가 진행되기 때문에
	 * 트랜잭션 처리 필수
	 * 
	 * selectKey를 정의했기 때문에 시퀀스 쓸필요없음
	 * 
	 * 트랜잭션 하에 tbl_board에 먼저 게시물을 등록하고,
	 * 각 첨부파일은 생성된 게시물 번호를 세팅한 후 tbl_attach 테이블에 데이터를 추가하게 됨
	 */
	@Transactional
	@Override
	public void register(BoardVO board) {
		
		log.info("게시글 등록: " + board);
		mapper.insertSelectKey(board);
		
		//업로드 파일 정보가 없거나 0보다 작거나 같을때 그냥 종료
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		
		board.getAttachList().forEach(attach -> {
			log.info("BoardServiceImpl : " + attach);
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	@Override
	public BoardVO get(Long bno) {
		// TODO Auto-generated method stub
		log.info("조회: " + bno);
		return mapper.read(bno);
	}

	/**
	 * 첨부파일 중 어떤 파일을 수정했고,
	 * 어떤 파일이 삭제됐는지 알아야 한다
	 * 
	 * 문제는 데이터베이스에서는 지워졌지만
	 * 업로드 폴더에는 그대로 남아있기 때문에
	 * 주기적으로 DB와 비교해서 삭제해야됨
	 */
	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		
		log.info("수정: " + board);
		
		attachMapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board) ==1;
		
		if (modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0) {
			
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		
		return modifyResult;
	}

	/**
	 * 첨부파일 삭제와 실제 게시물 삭제가 같이 처리디도록
	 * 트랙잭션처리
	 */
	@Transactional
	@Override
	public boolean remove(Long bno) {
		
		log.info("삭제: " + bno);
		
		attachMapper.deleteAll(bno);
		
		return mapper.delete(bno) == 1;
	}

//	@Override
//	public List<BoardVO> getList() {
//		// TODO Auto-generated method stub
//		log.info("list.....");
//		return mapper.getList();
//	}

	@Override
	public List<BoardVO> getList(Paging page) {
		log.info("서비스에서 페이징 처리를 요청합니다: " + page);
		return mapper.getListWithPaging(page);
	}

	@Override
	public int getTotal(Paging page) {
		log.info("서비스에서 전체 데이터의 개수 처리를 요청합니다: " + page);
		
		return mapper.getTotalCount(page);
	}

	/**
	 * 게시물의 첨부파일 목록들을 가져옴
	 */
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		
		log.info("서비스에서 첨부파일 목록을 알고싶어합니다: " + bno);
		return attachMapper.findByBno(bno);
	}

}
