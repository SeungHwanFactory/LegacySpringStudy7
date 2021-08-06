package com.nsh.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nsh.config.RootConfig;
import com.nsh.domain.BoardVO;
import com.nsh.domain.Paging;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {RootConfig.class})
@Log4j
public class BoardMapperTest {

	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;
	
	//@Test
	public void bno가0보다큰데이터조회() {
		mapper.getList().forEach(board -> log.info(board));
	}
	
	//@Test
	public void 게시글삽입테스트() {
		BoardVO board = new BoardVO();
		board.setTitle("new title");
		board.setContent("new Content");
		board.setWriter("new user");
		
		mapper.insert(board);
	}
	
	//@Test
	public void 게시글번호선택테스트() {
		BoardVO board = new BoardVO();
		board.setTitle("new title selectKey");
		board.setContent("new Content selectKey");
		board.setWriter("new user");
		
		mapper.insertSelectKey(board);
	}
	
	//@Test
	public void 게시글조회테스트() {
		BoardVO board = mapper.read(4L);
		
		log.info(board);
	}
	
	//@Test
	public void 게시글삭제테스트() {
		log.info("delete count: " + mapper.delete(4L));
	}
	
	//@Test
	public void 업데이트테스트() {
		BoardVO board = new BoardVO();
		board.setBno(3L);
		board.setTitle("update title");
		board.setContent("update content");
		board.setWriter("update user");
		
		int count = mapper.update(board);
		
		log.info("update count: " + count);
	}
	
	//@Test
	public void 페이징처리테스트() {
		Paging pag = new Paging();
		
		/*
		 * default 생성자로 초기화한 (1,10)이 파라미터로 전달됨 
		 * */
		List<BoardVO> list = mapper.getListWithPaging(pag);
		
		list.forEach(board -> log.info(board));
	}
	
	@Test
	public void 페이징처리테스트2() {
		Paging page = new Paging();
		
		/*
		 * 10개씩 3페이지 내용보기
		 * */
		page.setPageNum(3);
		page.setAmount(10);
		
		List<BoardVO> list = mapper.getListWithPaging(page);
		
		list.forEach(board -> log.info(board.getBno()));
	}
}
