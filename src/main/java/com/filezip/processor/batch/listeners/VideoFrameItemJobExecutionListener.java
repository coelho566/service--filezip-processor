package com.filezip.processor.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VideoFrameItemJobExecutionListener implements JobExecutionListener {

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("Iniciando o Job: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long endTime = System.currentTimeMillis();
        log.info("Job {} finalizado com status: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
        log.info("Job {} executado em {} ms", jobExecution.getJobInstance().getJobName(), (endTime - startTime));
    }
}
