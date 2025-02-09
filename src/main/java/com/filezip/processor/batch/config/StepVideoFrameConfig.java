package com.filezip.processor.batch.config;

import com.filezip.processor.adapters.outbound.repository.VideoFrameRepositoryAdapter;
import com.filezip.processor.application.core.exception.RetryProcessException;
import com.filezip.processor.application.ports.in.DownloadVideoStoragePort;
import com.filezip.processor.batch.item.VideoProcessItem;
import com.filezip.processor.batch.listeners.VideoFrameItemProcessorListener;
import com.filezip.processor.batch.listeners.VideoFrameStepExecutionListener;
import com.filezip.processor.batch.processor.VideoItemProcessor;
import com.filezip.processor.batch.write.VideoItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StepVideoFrameConfig {

    private final VideoItemWriter videoItemWriter;
    private final VideoItemProcessor videoItemProcessor;
    private final DownloadVideoStoragePort downloadVideoStorage;
    private final VideoFrameRepositoryAdapter videoFrameRepositoryAdapter;
    private final VideoFrameItemProcessorListener videoFrameItemProcessorListener;
    private final VideoFrameStepExecutionListener videoFrameStepExecutionListener;


    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step", jobRepository)
                .<VideoProcessItem, VideoProcessItem>chunk(10, platformTransactionManager)
                .reader(itemReader(""))
                .processor(videoItemProcessor)
                .writer(videoItemWriter)
                .faultTolerant()
                .retry(RetryProcessException.class) // Especifica que queremos retry para RuntimeException
                .retryLimit(3)
                .listener(videoFrameItemProcessorListener)
                .listener(videoFrameStepExecutionListener)
                .build();
    }


    @Bean
    @StepScope
    public ItemReader<VideoProcessItem> itemReader(@Value("#{jobParameters['message']}") String message) {

        if (message != null && !message.isEmpty()) {
            var fileId = message.replaceAll("videos/|\\.mp4", "");

            var videoById = videoFrameRepositoryAdapter.getVideoById(fileId);
            var videoProcessItem = VideoProcessItem.builder()
                    .zipId(fileId)
                    .fileName(videoById.getName())
                    .duration(videoById.getIntervalFrame())
                    .build();

            downloadVideoStorage.downloadVideo(List.of(videoProcessItem));
            return new ListItemReader<>(List.of(videoProcessItem));
        } else {
            return new ListItemReader<>(Collections.emptyList());
        }
    }

}
