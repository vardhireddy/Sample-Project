/*
 * DataCatalogConfigProvider.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Class DataCatalogConfigProvider.
 */
@Configuration
public class DataCatalogConfigProvider {

    /**
     * Cloud datacatalog registry config.
     *
     * @return the datacatalog registry config
     */
    @Bean
    @ConfigurationProperties ( prefix = "datacatalog.config" )
    public DataCatalogRegistryConfig dataCatalogRegistryConfig() {
        return new DataCatalogRegistryConfig();
    }
    
    /**
     * Cloud datacatalog dev config.
     *
     * @return the datacatalog dev config
     */
    @Bean
    @ConfigurationProperties ( prefix = "datacatalog.devConfig" )
    public DataCatalogDevConfig dataCatalogDevConfig() {
        return new DataCatalogDevConfig();
    }

}
