package com.filezip.processor.adapters.outbound;

import com.filezip.processor.application.ports.out.SendEventSqsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEventSqsAdapter implements SendEventSqsPort {

    @Value("${aws.sqs.email.url}")
    private String url;
    private final SqsClient sqsClient;


    @Override
    public void sendEventEmail(String fileId) {
        log.info("Send email fileId: {}", fileId);
        var sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(url)
                .messageBody(fileId)
                .build();
        sqsClient.sendMessage(sendMsgRequest);
    }
}
