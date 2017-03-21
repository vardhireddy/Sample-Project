/*
 * AppConfig.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

/**
 * @author 212071558
 *
 */
@ComponentScan ( {"com.gehc"} )
@PropertySource({"classpath:application.yml"})
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter{
    
    @Bean
    public DataCatalogInterceptor dataCatalogInterceptor() {
        return new DataCatalogInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
       // registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/**");
    	 //registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/dataCollection");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/createDataCollection");
    }
    
    /** The dataSource */
    @Autowired
    private DataSource dataSource;
    
    /**
     * Jdbc template.
     *
     * @return the jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource( dataSource );
        return jdbcTemplate;
    }
}
