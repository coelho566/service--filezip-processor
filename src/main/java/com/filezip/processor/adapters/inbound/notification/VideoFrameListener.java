package com.filezip.processor.adapters.inbound.notification;

import com.filezip.processor.adapters.inbound.notification.event.RootEvent;
import com.filezip.processor.application.utils.JsonUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VideoFrameListener {

    private final Job job;
    private final JobLauncher jobLauncher;
    private final JsonUtils jsonUtils;

    private final Cache<String, Boolean> processedIdsCache;

    public VideoFrameListener(Job job, JobLauncher jobLauncher, JsonUtils jsonUtils) {
        this.job = job;
        this.jobLauncher = jobLauncher;
        this.jsonUtils = jsonUtils;
        this.processedIdsCache =  Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // Expira após 10 minutos
                .maximumSize(1000)  // Máximo de 1000 entradas
                .build();
    }

    @SqsListener("${aws.sqs.email.name}")
    public void handleS3Event(String message) throws IOException {

        log.info("Received S3 event: {}", message);

        var event = jsonUtils.jsonToObject(message, RootEvent.class);
        if (event != null && event.records != null) {

            event.records.stream()
                    .filter(Objects::nonNull)
                    .forEach(record -> {
                        try {
                            if (processedIdsCache.getIfPresent(record.getS3().getObject().getKey()) == null) {
                                processedIdsCache.put(record.getS3().getObject().getKey(), Boolean.TRUE);
                                JobParameters jobParameters = new JobParametersBuilder()
                                        .addString("message", record.getS3().getObject().getKey())
                                        .addLong("time", System.currentTimeMillis())
                                        .toJobParameters();
                                jobLauncher.run(job, jobParameters);
                            }
                        } catch (JobExecutionException e) {
                            log.error("Job execution error", e);
                        }
                    });
        }
    }
}
