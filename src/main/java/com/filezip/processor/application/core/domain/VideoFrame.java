package com.filezip.processor.application.core.domain;

import lombok.Data;

@Data
public class VideoFrame {

    private String id;
    private String name;
    private String userId;
    private String userName;
    private String userEmail;
    private String directory;
    private String correlationId;
    private Double intervalFrame;
    private String videoPath;
    private String keyBucket;
    private String processorStatus;
}
