package com.filezip.processor.batch.listeners;

import com.filezip.processor.adapters.outbound.repository.VideoFrameRepositoryAdapter;
import com.filezip.processor.application.core.exception.RetryProcessException;
import com.filezip.processor.application.ports.out.SendEventSqsPort;
import com.filezip.processor.batch.item.ProcessorStatusItem;
import com.filezip.processor.batch.item.VideoProcessItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoFrameItemProcessorListenerTest {

    @Mock
    private VideoFrameRepositoryAdapter videoFrameRepositoryAdapter;

    @Mock
    private SendEventSqsPort sendEventSqsPort;

    @InjectMocks
    private VideoFrameItemProcessorListener listener;


    @Test
    public void testBeforeProcess() {
        VideoProcessItem item = VideoProcessItem.builder()
                .zipId("123")
                .fileName("test.mp4")
                .duration(2.0)
                .build();

        listener.beforeProcess(item);

        verify(videoFrameRepositoryAdapter).updateStatusProcessor("123", ProcessorStatusItem.PROCESSING.name());
    }

    @Test
    public void testAfterProcess() {
        VideoProcessItem videoProcessItem = VideoProcessItem.builder()
                .zipId("123")
                .fileName("test.mp4")
                .duration(2.0)
                .build();

        listener.afterProcess(videoProcessItem, videoProcessItem);

        verify(videoFrameRepositoryAdapter).updateStatusProcessor("123", ProcessorStatusItem.PROCESSED.name());
    }

    @Test
    public void testOnProcessError_RetryProcessException() {
        VideoProcessItem item = mock(VideoProcessItem.class);
        when(item.getZipId()).thenReturn("zip123");
        Exception e = new RetryProcessException("Error");

        listener.onProcessError(item, e);

        verify(videoFrameRepositoryAdapter).updateStatusProcessor("zip123", ProcessorStatusItem.RETENTIVE_PROCESS.name());
    }

    @Test
    public void testOnProcessError_OtherException() {
        VideoProcessItem item = mock(VideoProcessItem.class);
        when(item.getZipId()).thenReturn("zip123");
        Exception e = new Exception("Error");

        listener.onProcessError(item, e);

        verify(sendEventSqsPort).sendEventEmail("zip123");
        verify(videoFrameRepositoryAdapter).updateStatusProcessor("zip123", ProcessorStatusItem.ERROR.name());
    }
}