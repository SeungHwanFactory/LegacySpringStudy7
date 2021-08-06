package com.nsh.service;

import static org.junit.Assert.assertNotNull;

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
public class BoardServiceTest {

	@Setter(onMethod_ = @Autowired)
	private BoardService service;
	
	
	@Test
	public void 서비스동작테스트() {
		log.info(service);
		assertNotNull(service);
	}
	
	//@Test
	public void 작성테스트() {
		
		BoardVO board = new BoardVO();
		board.setTitle("new Title");
		board.setContent("new Content");
		board.setWriter("new User");
		
		service.register(board);
		
		log.info("create new board:" + board.getBno());
	}
	
	//@Test
	public void 조회수목록() {
		//service.getList().forEach(board -> log.info(board));
	}
	
	//@Test
	public void 번호로조회() {
		log.info(service.get(1L));
	}
	
	//@Test
	public void 업데이트테스트() {
		BoardVO board = service.get(1L);//1번 게시글 가져오기
		if(board == null) { //만약 게시글이 없다면
			return;
		}
		board.setTitle("제목 수정했음");//제목 수정
		log.info("수정 결과: " + service.modify(board));
	}
	
	//@Test
	public void 삭제테스트() {
		log.info("삭제 결과: " + service.remove(2L));
	}
	
	/*
	 * 2페이지의 10개 보여주기.
	 * */
	@Test
	public void 서비스페이징처리테스트() {
		service.getList(new Paging(2, 10)).forEach(board -> log.info(board));
	}
}
