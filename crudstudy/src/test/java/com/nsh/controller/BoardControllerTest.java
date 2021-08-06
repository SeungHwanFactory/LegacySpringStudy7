package com.nsh.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nsh.config.RootConfig;
import com.nsh.config.ServletConfig;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
		RootConfig.class,
		ServletConfig.class
})
@Log4j
public class BoardControllerTest {

	@Autowired
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;
	
	@Before
	public void 셋업() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	//@Test
	public void 목록테스트() throws Exception {
		log.info(
				mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
				.andReturn()
				.getModelAndView()
				.getModelMap());
	}
	
	//@Test
	public void 작성테스트() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
				.param("title", "new TestTitle")
				.param("content", "new TestContent")
				.param("writer", "new TestUser")
				).andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);	
	}
	
	//@Test
	public void 조회테스트() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders
				.get("/board/get")
				.param("bno", "3"))
				.andReturn()
				.getModelAndView().getModelMap());
	}
	
	//@Test
	public void 수정테스트() throws Exception {
		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders.post("/board/modify")
						.param("bno", "1")
						.param("title", "update test title")
						.param("content", "update test content")
						.param("writer", "user"))
				.andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	}
	
	//@Test
	public void 삭제테스트() throws Exception {
		String resultPage = mockMvc
				.perform(MockMvcRequestBuilders.post("/board/remove")
						.param("bno", "3"))
				.andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	}
	
	/*
	 * 2페이지에 50개 표시
	 * */
	@Test
	public void 컨트롤러페이징테스트() throws Exception {
		log.info(mockMvc.perform(
				MockMvcRequestBuilders.get("/board/list")
				.param("pageNum", "2")
				.param("amount", "50"))
				.andReturn().getModelAndView().getModelMap());
	}
}
