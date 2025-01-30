package com.filezip.processor.config;

import com.filezip.processor.batch.VideoItemWriter;
import com.filezip.processor.event.VideoFrameEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class StepVideoFrameConfig {

    private final VideoItemWriter videoItemWriter;

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step", jobRepository)
                .<String, String>chunk(1, platformTransactionManager)
                .reader(customReader())
                .writer(videoItemWriter)
                .build();
    }


    public KafkaItemReader<VideoFrameEvent, String> customReader() {

        Properties consumerProperties = new Properties();

        consumerProperties.put("bootstrap.servers", "localhost:9092");
        consumerProperties.put("group.id", "file-zip-consumer");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.JsonDeserializer");
        return new KafkaItemReader<>(consumerProperties, "send-framezip-processor-topic", 0, 1, 2);
    }
}
