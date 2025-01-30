package com.filezip.processor.item;

import lombok.Data;

@Data
public class VideoFrameItem {

    private String id;
    private String name;
    private String userId;
    private String userName;
    private String userEmail;
    private String directory;
    private String correlationId;
    private Double intervalFrame;
    private String processorStatus;
}
