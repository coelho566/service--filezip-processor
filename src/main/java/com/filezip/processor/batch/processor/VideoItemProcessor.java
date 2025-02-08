package com.filezip.processor.batch.processor;

import com.filezip.processor.application.ports.out.UploadStoragePort;
import com.filezip.processor.batch.item.VideoProcessItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoItemProcessor implements ItemProcessor<VideoProcessItem, VideoProcessItem> {

    private final UploadStoragePort uploadStoragePort;

    @Override
    public VideoProcessItem process(VideoProcessItem videoProcessItem) {
        uploadStoragePort.extractFrameVideo(videoProcessItem);
        return videoProcessItem;
    }
}
