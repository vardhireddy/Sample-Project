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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 212071558
 *
 */
@ComponentScan ( {"com.gehc"} )
@PropertySource({"classpath:application.yml"})
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter{

    @Bean
    public Docket demoApi() {
        return new Docket(DocumentationType.SWAGGER_2)//<3>
                .select()//<4>
                .apis(RequestHandlerSelectors.basePackage("com.gehc.ai.app.datacatalog"))//<5>
                .build();
    }
    
}
