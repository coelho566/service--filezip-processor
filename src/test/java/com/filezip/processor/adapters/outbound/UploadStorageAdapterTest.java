package com.filezip.processor.adapters.outbound;

import com.filezip.processor.application.config.ResourcesTempProperties;
import com.filezip.processor.application.core.exception.BusinessException;
import com.filezip.processor.application.core.exception.RetryProcessException;
import com.filezip.processor.batch.item.VideoProcessItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadStorageAdapterTest {

    @InjectMocks
    private UploadStorageAdapter uploadStorageAdapter;
    @Mock
    private S3Client s3Client;
    @Mock
    private ResourcesTempProperties resourcesTempProperties;


    @Test
    void testExtractFrameVideo_Exception() {
        VideoProcessItem processItem = mock(VideoProcessItem.class);
        when(processItem.getZipId()).thenReturn("testZipId");
        when(resourcesTempProperties.getVideo()).thenReturn("/invalid/path");

        assertThrows(RetryProcessException.class, () -> {
            uploadStorageAdapter.extractFrameVideo(processItem);
        });
    }

    @Test
    void testUploadZipStorage() throws IOException {
        VideoProcessItem processItem = mock(VideoProcessItem.class);
        when(processItem.getZipId()).thenReturn("testZipId");
        when(resourcesTempProperties.getImages()).thenReturn("/tmp/images");

        Path sourcePath = Paths.get("/tmp/images/testZipId");
        Files.createDirectories(sourcePath);
        Files.createFile(Paths.get("/tmp/images/testZipId/testFile.txt"));

        uploadStorageAdapter.uploadZipStorage(processItem);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        Files.deleteIfExists(Paths.get("/tmp/images/testZipId/testFile.txt"));
        Files.deleteIfExists(Paths.get("/tmp/images/testZipId"));
    }

    @Test
    void testUploadZipStorage_Exception() throws IOException {
        VideoProcessItem processItem = mock(VideoProcessItem.class);
        when(processItem.getZipId()).thenReturn("testZipId");
        when(resourcesTempProperties.getImages()).thenReturn("/invalid/path");

        assertThrows(BusinessException.class, () -> {
            uploadStorageAdapter.uploadZipStorage(processItem);
        });
    }
}