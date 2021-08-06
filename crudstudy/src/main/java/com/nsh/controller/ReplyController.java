package com.nsh.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nsh.domain.Paging;
import com.nsh.domain.ReplyPageDTO;
import com.nsh.domain.ReplyVO;
import com.nsh.service.ReplyService;

import lombok.extern.log4j.Log4j;

/**
 * 
 * REST방식으로 동작하는 URL을 설계 시 PK기준으로 작성하면
 * PK만으로 조회, 수정, 삭제가 가능해서 좋다.
 * 
 * 그러나, 댓글 목록은 PK를 사용할 수 없기에
 * 파라미터로 게시물 번호(bno) 페이지 번호(page) 정보를 URL에서
 * 표현하도록 한다.
 *
 */
@RequestMapping("/replies/*")
@RestController
@Log4j
public class ReplyController {
	
	private ReplyService service;
	
	@Autowired
	public ReplyController (ReplyService service) {
		this.service = service;
	}
	
	/**
	 * 일단 경로가 잘 되는지 확인하기 위해 임의의 GetMapping 사용
	 * 문자열을 반환 받으려면 produces없이 ?로 나오기 때문에 생략할 수 없는 것 같다.
	 */
	@GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
	public String urlTest() {
		
		return "나를 반환했나요?";
	}
	
	/**
	 * <댓글 작성> 
	 * 
	 * REST 방식으로 처리할 때 데이터의 포맷과 서버에서 보내주는 데이터 타입을
	 * 명학하게 설계해야 한다.
	 * 
	 * 예를 들어 댓글 등록 정보를 JSON으로 받으면 서버에서 JSON으로 받은 데이터를
	 * 문자열로 결과를 알려주는 것
	 * 
	 * Question)
	 * GetMapping때는 produces 속성 생략가능했는데
	 * PostMapping은 될까? 일단 빼고 진행해보자.
	 * 
	 * 테스트1) 일단 404에러뜸
	 * 테스트2) produces 속성을 넣었더니 작동했다. 
	 * 그러나, 톰캣오류일 수 있기 때문에 빼고 다시 진행해보기로 했다.
	 * 테스트3) produces 속성을 빼도 작동했다.
	 * 테스트4) ajax처리를 했더니 전달이 안된다...?
	 * 415에러가 떴고, 이 에러는 content-type을 명시해야됨 오타확인해보자
	 * => 오타였다.
	 */
	@PostMapping(value = "/new",
			consumes = "application/json")
	public ResponseEntity<String> create(@RequestBody ReplyVO replyInfo) {
		log.info("댓글정보: " + replyInfo);
		
		/**
		 * insert수행한 횟수 반환
		 */
		int insertCount = service.registerReply(replyInfo);
		
		log.info(insertCount + "개의 댓글이 등록되었습니다.");
		
		/**
		 * 삼항 연산자
		 * 
		 * 여기서 댓글작성의 반환타입이 왜 int였는지 해결됐다.
		 * 만약, 댓글작성에 실패하면 insertCount에 반환되는 값이 없기 때문에
		 * 서버에러를 호출시키기 위함이였구나.
		 */
		return insertCount == 1 
				? new ResponseEntity<String>("success", HttpStatus.OK)
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * <특정 게시글의 댓글 확인>
	 * Q. 여기도 마찬가지로 produces 속성을 생략해봤다.
	 * A. 생략해도 문제없이 동작했다.
	 */
//	@GetMapping(value = "/pages/{bno}/{page}")
//	public ResponseEntity<List<ReplyVO>> getReplyList(
//			@PathVariable("page") int page,
//			@PathVariable("bno") Long bno) {
//		
//		log.info("=====특정 게시글의 댓글 목록 조회======");
//		/**
//		 * page: 해당 게시글이 위치한 페이지 번호
//		 * 뒤에 10개를 준이유는 댓글도 페이징 처리 하기 위해서?
//		 */
//		Paging pageInfo = new Paging(page, 10);
//		
//		log.info("Paging result: " + pageInfo);
//		/**
//		 * getListReply()는 Paging클래스를 이용해서 파라미터를 수집하는데 '/{bno}/{page}'의 page값은
//		 * Paging을 생성해서 직접 처리
//		 * 게시물 번호는 @PathVariable 이용해서 파라미터로 처리
//		 */
//		return  new ResponseEntity<List<ReplyVO>>(service.getListReply(pageInfo, bno), HttpStatus.OK);
//	}
	
	/**
	 * ReplyPageDTO를 받을 수 있게 수정.
	 * @param page
	 * @param bno
	 * @return
	 */
	@GetMapping(value = "/pages/{bno}/{page}")
	public ResponseEntity<ReplyPageDTO> getReplyList(
			@PathVariable("page") int page,
			@PathVariable("bno") Long bno) {
		
		log.info("=====특정 게시글의 댓글 목록 조회======");
		/**
		 * page: 해당 게시글이 위치한 페이지 번호
		 * 뒤에 10개를 준이유는 댓글도 페이징 처리 하기 위해서?
		 */
		Paging pageInfo = new Paging(page, 10);
		
		log.info("Paging result: " + pageInfo);
		/**
		 * getListReply()는 Paging클래스를 이용해서 파라미터를 수집하는데 '/{bno}/{page}'의 page값은
		 * Paging을 생성해서 직접 처리
		 * 게시물 번호는 @PathVariable 이용해서 파라미터로 처리
		 */
		return  new ResponseEntity<ReplyPageDTO>(service.getListPage(pageInfo, bno), HttpStatus.OK);
	}
	
	/**
	 * <댓글조회>
	 * Q.여기도 produces 생략해봄
	 * A.문제없이 작동했다.
	 */
	@GetMapping(value = "/{rno}")
	public ResponseEntity<ReplyVO> getReply(@PathVariable("rno") Long rno) {
		
		log.info("댓글조회: " + rno);
		
		return new ResponseEntity<ReplyVO>(service.getReplyInfo(rno), HttpStatus.OK);
	}
	
	/**
	 * <댓글삭제>
	 * Q.여기도 produces 생략
	 * A.제대로 동작했고, 테스트는 POSTMAN으로 했다.
	 */
	@DeleteMapping(value= "/{rno}")
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
		
		log.info("remove: " + rno);
		
		return service.removeReply(rno) == 1
				? new ResponseEntity<String>("success", HttpStatus.OK)
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * <댓글수정>
	 * Q.여기도 produces 생략
	 * A.정상적으로 작동했다.
	 * 
	 * Q.consumes을 생략해볼까?
	 * A.정상작동했다. 근데 왜쓴거지? 이유를 모르겠으니 일단 명시해놓자.
	 * 
	 * 파라미터로 댓글정보와 댓글번호를 받습니다.
	 * replyer가 수정안된 이유는 mapper.xml에 설정을 안했고,
	 * 나중에 시큐리티를 활용해서 고정될 값이기 때문이다.
	 * 
	 * consumes을 사용하는 이유는 request body에 담는 타입을 제한할 수 있다고 함
	 */
	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.PATCH },
			value = "/{rno}",
			consumes = "application/json")
	public ResponseEntity<String> modifyReplyInfo(
			@RequestBody ReplyVO vo,
			@PathVariable("rno") Long rno) {
		
		vo.setRno(rno);
		
		log.info("rno가 제대로 들어갔나요: " + rno);
		
		log.info("수정을 시작합니다: " + vo);
		
		return service.modifyReply(vo) == 1
				? new ResponseEntity<String>("success", HttpStatus.OK)
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
