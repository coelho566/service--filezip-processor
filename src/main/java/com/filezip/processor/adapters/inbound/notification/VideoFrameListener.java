package com.filezip.processor.adapters.inbound.notification;

import com.filezip.processor.adapters.inbound.notification.event.RootEvent;
import com.filezip.processor.application.utils.JsonUtils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoFrameListener {

    private final Job job;
    private final JobLauncher jobLauncher;
    private final JsonUtils jsonUtils;


    @SqsListener("${aws.sqs.queue-name}")
    public void handleS3Event(String message) throws IOException {

        log.info("Received S3 event: {}", message);

        var event = jsonUtils.jsonToObject(message, RootEvent.class);
        event.records.forEach(record -> {
            try {

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("message", record.getS3().getObject().getKey())
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();
                jobLauncher.run(job, jobParameters);
            } catch (JobExecutionException e) {

                e.printStackTrace();
            }
        });

    }
}
