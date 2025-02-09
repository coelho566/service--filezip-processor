package com.filezip.processor.batch.config;

import com.filezip.processor.batch.listeners.VideoFrameItemJobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .listener(new VideoFrameItemJobExecutionListener())
                .build();
    }

//    @Bean
//    public Flow myFlow(Step step1, Step step2) {
//        return new FlowBuilder<SimpleFlow>("myFlow")
//                .start(step1)
//                .next(step2)
//                .end();
//    }
}
