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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
}
