package com.filezip.processor.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "resources.temp")
public class ResourcesTempProperties {

    private String video;
    private String images;
    private String zip;
}
