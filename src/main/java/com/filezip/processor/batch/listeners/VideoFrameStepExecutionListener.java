package com.filezip.processor.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

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

        var message = stepExecution.getJobParameters().getParameters().get("message");

        if (message != null) {
            crearFiles((String) message.getValue());
        }
        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private void crearFiles(String message) {
        try {
            var fileId = message.replaceAll("videos/|\\.mp4", "");

            Path videoTemp = Path.of(String.format("src/main/resources/temp/%s.mp4", fileId));
            Path imagesTemp = Path.of(String.format("src/main/resources/images/%s", fileId));

            Files.deleteIfExists(videoTemp);
            deleteDirectory(imagesTemp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file); // Apaga o arquivo
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir); // Apaga o diretório depois de apagar os arquivos
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
