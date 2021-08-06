package com.nsh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsh.domain.SampleVO;
import com.nsh.domain.Ticket;

import lombok.extern.log4j.Log4j;

/**
 * @RestController: 해당 클래스를  REST방식으로 처리할 것임을 명시
 * @PathVariable: URL경로의 일부를 파라미터로 사용하기 위해 씀
 * @RequestBody: JSON 데이터를 원하는 타입의 객체로 변환해야 할때 씀
 */
@RestController
@RequestMapping("/sample/*")
@Log4j
public class SampleController {
	
	/**
	 * 문자열 반환
	 * produces: 해당 메서드가 생산하는 MIME타입을 의미
	 * 문자열로 직접 지정할 수 있고, 메서드 내 MediaType이라는 클래스를 사용할 수 있음
	 */
	@GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
	public String getText() {
		
		log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";
	}

	/**
	 * 객체 반환
	 * APPLICATION_JSON_UTF8_VALUE는 스프링 5.2버전부터 Deprecated되었다.
	 * APPLICATION_JSON_VALUE를 사용함
	 * produces 속성은 굳이 선언하지 않아도 됨 (생략되니까)
	 */
	@GetMapping(value = "/getInstance",
			produces = { MediaType.APPLICATION_JSON_UTF8_VALUE,
							 MediaType.APPLICATION_XML_VALUE })
	public SampleVO getInstance() {
		
		return new SampleVO(112, "취업", "성공");
	}
	
	/**
	 * 컬렉션 타입 반환1
	 */
	@GetMapping(value = "/getList")
	public List<SampleVO> getList() {
		/**
		 * 1부터 10까지 integer를 스트림으로 방출
		 * 스트림을 받아서 SampleVO를 객체화시키고
		 * collect()를 사용해 List형식으로 만들어준다.
		 */
		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i+"번째 first", i+ "번째 last")).collect(Collectors.toList());
	}
	
	/**
	 * 컬렉션 타입 반환2
	 */
	@GetMapping(value = "/getMap")
	public Map<String, SampleVO> getMap() {
		
		Map<String, SampleVO> map = new HashMap<String, SampleVO>();
		map.put("first", new SampleVO(111, "아임", "그루트"));
		return map;
	}
	
	/**
	 * ResponseEntity는 데이터와 함께 HTTP 헤더의 상태 메세지 등을 같이 전달하는 용도로 사용
	 * HTTP의 상태 코드와 에러 메세지 등을 함께 데이터를 전달할 수 있기 때문에
	 * 받는 이방에서 확실하게 결과를 알 수 있음
	 */
	@GetMapping(value = "/check", params = { "height", "weight" })
	public ResponseEntity<SampleVO> check(Double height, Double weight) {
		/**
		 * SampleVO객체를 생성해 vo에 대입
		 * ResponseEntity<SampleVO>타입을 받는 result변수를 null로 초기화
		 * 만약 height가 150보다 작다면 502상태 코드와 데이터 전송
		 * 그렇지 않다면, 200상태와 데이터 전송
		 */
		SampleVO vo = new SampleVO(0, "" + height, "" + weight);
		
		ResponseEntity<SampleVO> result = null;
		
		if (height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		} else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		
		return result;
	}
	
	/**
	 * 파라미터를 경로의 일부분으로 사용하는 방법1
	 * 적용하고 싶은 경우 '{ }'를 이용해 변수명 지정
	 * @PathVariable을 이용해 지정된 이름의 변수의 값을 얻을 수 있음
	 * 값을 얻을 때 기본 자료형은 사용할 수 없음
	 */
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(
			@PathVariable("cat") String cat,
			@PathVariable("pid") Integer pid) {
		
		return new String[] { "category: " + cat, "productid: " + pid };
	}
	
	/**
	 * @RequestBody: 전달된 request의 body를 이용해 해당 파라미터의 타입으로 변환 요구
	 * 내부적으로 HttpMessageConverter 타입의 객체를 이용해 다양한 포맷의 입력데이터를 변환 가능
	 * 대부분 JSON 데이터를 서버에 보내서 원하는 타입의 객체로 변환하는 용으로 쓰지만,
	 * 경우에 따라 원하는 포맷의 데이터를 보내고, 이를 해석해 원하는 타입으로 사용하기도 함
	 * com.nsh.domain 패키지에 Ticket 클래스 작성
	 * 
	 * @PostMapping으로 설계된 이유는 @RequestBody가 request한 body를 처리하기 때문이다.
	 * REST방식의 데이터를 전송하는 POSTMAN 툴을 이용하거나 JUnit과 spring-test를 이용해서 테스트 케이스를
	 * 작성하자.
	 */
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		
		log.info("컨버트....ticket " + ticket);
		
		return ticket;
	}
	
}
