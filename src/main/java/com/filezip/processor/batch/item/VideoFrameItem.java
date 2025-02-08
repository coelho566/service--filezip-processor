package com.filezip.processor.batch.item;

import lombok.Data;

import java.util.List;

@Data
public class VideoFrameItem {

    private String correlationId;
    private List<VideoProcessItem> videos;
}

