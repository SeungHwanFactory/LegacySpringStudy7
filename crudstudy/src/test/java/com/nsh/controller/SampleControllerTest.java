package com.nsh.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.nsh.config.RootConfig;
import com.nsh.config.ServletConfig;
import com.nsh.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
		RootConfig.class,
		ServletConfig.class
})
@Log4j
public class SampleControllerTest {

	@Autowired
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;
	
	@Before
	public void 셋업() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void REST방식테스트() throws Exception {
		
		Ticket ticket = new Ticket();
		ticket.setTno(123);
		ticket.setOwner("노승환");
		ticket.setGrade("VIP");
		/**
		 * Gson 라이브러리를 사용해 Java의 객체를 JSON문자열로 변환
		 */
		String jsonStr = new Gson().toJson(ticket);
		
		log.info("Gson을 사용해서 변환한 JSON문자열 " + jsonStr);
		
		/**
		 * post방식의 /sample/ticket url로 이동하고
		 * 컨텐츠 타입은 JSON
		 * 컨텐츠는 Gson으로 변환한 JSON문자열
		 * 기대한 결과는 200상태
		 */
		mockMvc.perform(post("/sample/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonStr)).andExpect(status().is(200));
				
		
	}
}
