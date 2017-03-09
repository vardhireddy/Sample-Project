/*
 * App.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author 212071558
 *
 */
@Configuration
@ComponentScan ( {"com.gehc"} )
@SpringBootApplication
@PropertySource({"classpath:application.yml"})
public class App {
    /**
     * 
     * @param args accept arguments
     */
    public static void main( String[] args ) {
           SpringApplication.run( App.class, args );
    }
    
    @Bean
    public RestTemplate restTemplate() {

        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                try {
                    List<org.springframework.http.MediaType> supportedMediaTypes = new ArrayList<org.springframework.http.MediaType>();

                    supportedMediaTypes.add(new org.springframework.http.MediaType("text","html", Charset.forName("UTF-8")));
                            supportedMediaTypes.add(new org.springframework.http.MediaType("application","json", Charset.forName("UTF-8")));
                    // Add default media type in case marketplace uses incorrect MIME type, otherwise
                    // Spring refuses to process it, even if its valid JSON
                            supportedMediaTypes.add(new org.springframework.http.MediaType("application","octet-stream", Charset.forName("UTF-8")));

                    ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(supportedMediaTypes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return template;
        }
}
