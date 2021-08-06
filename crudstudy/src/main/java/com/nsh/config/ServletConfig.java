package com.nsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/*
 * servlet-context.xml을 대신하는 클래스 파일
 * @EnableWebMvc 어노테이션과 WebMvcConfigurer 인터페이스를 구현하는 방식을 사용
 * 스프링 5.0버전부터는 Deprecated되었다.
 * */
@EnableWebMvc
@ComponentScan(basePackages = {"com.nsh.controller"})
public class ServletConfig implements WebMvcConfigurer {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		/*
		 * InternalResourceViewResolver는
		 * ViewResolver가 Controller가 반환한 결과를 
		 * 어떤 View를 통해서 처리하는 것이 좋을지 해석하는 역할 
		 * */
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
			bean.setViewClass(JstlView.class);
			bean.setPrefix("/WEB-INF/views/");
			bean.setSuffix(".jsp");
			registry.viewResolver(bean);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	@Bean
	public MultipartResolver multipartResolver( ) {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		
		return resolver;
	}

}
