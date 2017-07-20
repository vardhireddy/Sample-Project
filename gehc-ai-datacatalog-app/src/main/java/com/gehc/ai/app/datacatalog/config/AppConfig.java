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
package com.gehc.ai.app.datacatalog.config;

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

    /*
    *dataCatalogInterceptor
    * @return data catalog interceptor
     */
    @Bean
    public DataCatalogInterceptor dataCatalogInterceptor() {
        return new DataCatalogInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
    	//Excluding the below as Countor2Mask is going to use below API
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/annotation/*").excludePathPatterns("/api/v1/annotation/{ids}");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/annotation");
    	//Added for new APIs after refactoring
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/study/{studyId}/image-set");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/image-set/{id}");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/data-collection");   
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/data-collection/{id}");    
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/patient/{ids}");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/patient");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/study");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/study/{ids}");
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
