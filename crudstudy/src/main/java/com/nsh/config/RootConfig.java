package com.nsh.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * root-context.xml 대신 작성
 * */
@Configuration
/*
 * mybatis-spring에서 사용하는 @MapperScan을 통해 mapper인식
 * */
@MapperScan(basePackages = {"com.nsh.mapper"})
@ComponentScan(basePackages = {"com.nsh.service"})
@ComponentScan(basePackages = {"com.nsh.task"})//com.nsh.task 패키지 스캔하도록 지정
@EnableScheduling //Quartz 사용
@EnableTransactionManagement
public class RootConfig {

	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig(); //HikariCP 객체화
		hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		hikariConfig.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@localhost:1521:XE");
		hikariConfig.setUsername("book_ex");
		hikariConfig.setPassword("1234");
		
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		
		return dataSource;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		return (SqlSessionFactory) sqlSessionFactory.getObject();
	}
	
}
