package com.filezip.processor.batch.write;

import com.filezip.processor.application.ports.out.UploadStoragePort;
import com.filezip.processor.batch.item.VideoProcessItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VideoItemWriterTest {

    @InjectMocks
    private VideoItemWriter videoItemWriter;

    @Mock
    private UploadStoragePort uploadStoragePort;


    @Test
    void testWrite() {
        // Criação de dados de teste
        var videoProcessItem = VideoProcessItem.builder()
                .zipId("123")
                .fileName("test.mp4")
                .duration(2.0)
                .build();
        var videoProcessItem1 = VideoProcessItem.builder()
                .zipId("1234")
                .fileName("test2.mp4")
                .duration(2.0)
                .build();
        Chunk<VideoProcessItem> chunk = new Chunk<>(List.of(videoProcessItem, videoProcessItem1));

        videoItemWriter.write(chunk);

        verify(uploadStoragePort, times(1)).uploadZipStorage(videoProcessItem);
        verify(uploadStoragePort, times(1)).uploadZipStorage(videoProcessItem1);
    }
}