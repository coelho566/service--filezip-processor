package com.filezip.processor.batch.processor;

import com.filezip.processor.application.ports.out.UploadStoragePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VideoItemProcessorTest {

    @InjectMocks
    private VideoItemProcessor videoItemProcessor;
    @Mock
    private UploadStoragePort uploadStoragePort;

    @Test
    void process_sucess() {

        videoItemProcessor.process(any());
        verify(uploadStoragePort, times(1)).extractFrameVideo(any());
    }
}