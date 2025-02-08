package com.filezip.processor.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VideoFrameStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {

        StepExecutionListener.super.beforeStep(stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step status: {}", stepExecution.getStatus());
        log.info("Total de itens recebidos: {}", stepExecution.getReadCount());
        log.info("Total de itens processados: {}", stepExecution.getWriteCount());
        log.info("Total de itens com erro: {}", stepExecution.getProcessSkipCount());


        return StepExecutionListener.super.afterStep(stepExecution);
    }
}
