package com.filezip.processor.adapters.inbound.storage;

import com.filezip.processor.application.config.ResourcesTempProperties;
import com.filezip.processor.application.ports.out.VideoFrameRepositoryPort;
import com.filezip.processor.batch.item.VideoProcessItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DownloadVideoStorageTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private ResourcesTempProperties resourcesTempProperties;

    @Mock
    private VideoFrameRepositoryPort videoFrameRepositoryPort;

    @InjectMocks
    private DownloadVideoStorage downloadVideoStorage;

    @Test
    public void testDownloadVideo_Success() throws IOException {
        VideoProcessItem videoProcessItem = VideoProcessItem.builder()
                .zipId("123")
                .fileName("test.mp4")
                .duration(2.0)
                .build();
        List<VideoProcessItem> videos = List.of(videoProcessItem);
        ResponseInputStream<GetObjectResponse> s3Object = mock(ResponseInputStream.class);

        when(resourcesTempProperties.getVideo()).thenReturn("src/main/resources/temp");
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(s3Object);

        downloadVideoStorage.downloadVideo(videos);
        verify(videoFrameRepositoryPort, timeout(1)).updateStatusProcessor(any(), any());
    }


    @Test
    public void testDownloadVideo_EmptyVideos() {
        List<VideoProcessItem> videos = List.of();

        downloadVideoStorage.downloadVideo(videos);

        verifyNoInteractions(s3Client, videoFrameRepositoryPort, resourcesTempProperties);
    }
}