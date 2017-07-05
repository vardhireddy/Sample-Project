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
       // registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/**");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/dataCollection");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/createDataCollection");
    	//Will not set the interceptor for insert image-set, annotation, patient and study bcoz it being called from SNS manager using skynet id
    	// Also get image set by series instanse uid is being called from sns magaer
    	//TODO:Once SNS manger will use the right credentials then will check the caller of these API is LF APP
    	//registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/image-set");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/imgSetByDataCollectionId");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/data-collection-target");
    	//Excluding the below as Countor2Mask is going to use below API
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/annotation/*").excludePathPatterns("/api/v1/annotation/{ids}");
    	//commented above and add below to make sure delete is working. Will add the logic for get method for C2M
    	//registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/annotation/{ids}");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/annotation");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/patient/{ids}");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/patient/{patientId}/study");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/study/{ids}");
    	//Commented below as this api is also being used in SNS manager
    	//registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/image-set-by-patientid");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/study/{studyId}/image-set");
    	//Following will be used in experiment service so will not get the org
    	//registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/annotation-by-datacollectionid");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/patient");
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/dataCatalog/study");
    	
    	//Added for new APIs after refactoring
    	registry.addInterceptor(dataCatalogInterceptor()).addPathPatterns("/api/v1/datacatalog/data-set/type/{type}");
    	
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
