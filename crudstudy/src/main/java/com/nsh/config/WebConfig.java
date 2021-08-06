package com.nsh.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {RootConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {ServletConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(
			ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		
		/**
		 * 파일 업로드를 위한 MultiPartConfig 설정을 추가
		 * javax.servlet.MultipartConfigElement 클래스 이용
		 */
		MultipartConfigElement multipartConfig = new MultipartConfigElement("D:\\ImageRepository\\temp", 20971520, 41943040, 20971520);
		registration.setMultipartConfig(multipartConfig);
	}
	
	/*
	 * 한글 깨짐 솔루션
	 * */
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		
		return new Filter[] { characterEncodingFilter };
	}
}
