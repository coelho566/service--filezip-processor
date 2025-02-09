package com.filezip.processor.batch.listeners;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoFrameItemJobExecutionListenerTest {

    @InjectMocks
    private VideoFrameItemJobExecutionListener listener;

    @Mock
    private JobExecution jobExecution;

    @Mock
    private JobInstance jobInstance;


    @Test
    public void testBeforeJob() {

        jobExecution.setJobInstance(jobInstance);

        when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        when(jobInstance.getJobName()).thenReturn("jobName");

        listener.beforeJob(jobExecution);
        verify(jobExecution, times(1)).getJobInstance();
        System.out.println("BeforeJob: Iniciando o Job: testJob");
    }

    @Test
    public void testAfterJob() {

        jobExecution.setJobInstance(jobInstance);
        when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        when(jobInstance.getJobName()).thenReturn("jobName");

        listener.afterJob(jobExecution);
        verify(jobExecution, times(2)).getJobInstance();
        verify(jobExecution, times(1)).getStatus();
    }
}