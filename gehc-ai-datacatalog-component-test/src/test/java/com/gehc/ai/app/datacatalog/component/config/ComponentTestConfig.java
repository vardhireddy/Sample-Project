package com.gehc.ai.app.datacatalog.component.config;

/**
 * Created by sowjanyanaidu on 5/15/17.
 */


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("component")
@ComponentScan(basePackages = "com.gehc.ai.app.dc.component")
public class ComponentTestConfig {
}