package com.nsh.mapper;


import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nsh.config.RootConfig;
import com.nsh.domain.Paging;
import com.nsh.domain.ReplyPageDTO;
import com.nsh.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {RootConfig.class})
@Log4j
public class ReplyMapperTest {

	private Long[] bnoArr = { 1L, 2L, 3L, 4L, 5L };
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	//@Test
	public void 댓글매퍼테스트() {
		log.info(mapper);
	}
	
	//@Test
	public void 댓글작성테스트() {
		/**
		 * range v rangeClosed
		 * rangeClosed는 종료지점도 포함
		 */
		IntStream.rangeClosed(1, 10).forEach(i -> {
			ReplyVO vo = new ReplyVO();
			vo.setBno(bnoArr[i % 5]);
			vo.setReply("댓글 테스트 " + i);
			vo.setReplyer("작성자 " + i);
			
			mapper.insert(vo);
		});
	}
	
	//@Test
	public void 댓글조회테스트() {
		Long targetRno = 1L;
		
		ReplyVO vo = mapper.read(targetRno);
		
		log.info("조회한 댓글 정보: " + vo);
	}
	
	//@Test
	public void 댓글삭제테스트() {
		Long targetRno = 3L;
		
		int count = mapper.delete(targetRno);
		
		log.info("삭제한 댓글 개수 " + count);
	}
	
	//@Test
	public void 댓글수정테스트() {
		Long targetRno = 1L;
		
		ReplyVO vo = mapper.read(targetRno);
		
		vo.setReply("수정한 댓글내용");
		
		int count = mapper.update(vo);
		
		log.info("수정한 댓글 개수: " + count);
	}
	
	//@Test
	public void 게시글에달린댓글테스트() {
		Paging page = new Paging();
		
		List<ReplyVO> replies = mapper.getListWithPaging(page, bnoArr[1]);
		
		replies.forEach(reply -> log.info(reply));
	}
	
	//@Test
	public void 게시글에달린댓글개수() {
		
		log.info("개수: " + mapper.getCountByBno(bnoArr[1]));
	}
	
	@Test
	public void ReplyPageDTO테스트() {
		Paging page = new Paging();
		ReplyPageDTO result = new ReplyPageDTO(
				mapper.getCountByBno(2L),
				mapper.getListWithPaging(page, 2L));
		
		log.info("결과: " + result);
	}
}
