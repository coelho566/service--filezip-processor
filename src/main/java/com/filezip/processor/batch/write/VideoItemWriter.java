package com.filezip.processor.batch.write;

import com.filezip.processor.application.ports.out.UploadStoragePort;
import com.filezip.processor.batch.item.VideoProcessItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoItemWriter implements ItemWriter<VideoProcessItem> {

    private final UploadStoragePort uploadStoragePort;

    @Override
    public void write(Chunk<? extends VideoProcessItem> chunk) {
        chunk.getItems().forEach(uploadStoragePort::uploadZipStorage);
    }
}
