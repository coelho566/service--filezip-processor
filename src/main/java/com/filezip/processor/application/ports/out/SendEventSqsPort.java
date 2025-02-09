package com.filezip.processor.application.ports.out;

public interface SendEventSqsPort {

    void sendEventEmail(String fileId);
}
