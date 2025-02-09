package com.filezip.processor.batch.listeners;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoFrameStepExecutionListenerTest {

    @InjectMocks
    private VideoFrameStepExecutionListener listener;

    @Mock
    private StepExecution stepExecution;

    @BeforeEach
    void setUp() {
        listener = new VideoFrameStepExecutionListener();
    }

    @Test
    void testAfterStepWithMessage() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("message", "videos/test.mp4")
                .toJobParameters();
        when(stepExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        when(stepExecution.getJobParameters()).thenReturn(jobParameters);

        listener.afterStep(stepExecution);

        verify(stepExecution, times(1)).getJobParameters();
    }

    @Test
    void testAfterStepWithoutMessage() {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        when(stepExecution.getJobParameters()).thenReturn(jobParameters);

        ExitStatus status = listener.afterStep(stepExecution);

        verify(stepExecution, times(1)).getJobParameters();
    }
}