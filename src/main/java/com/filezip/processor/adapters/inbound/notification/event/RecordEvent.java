package com.filezip.processor.adapters.inbound.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordEvent {
    public String eventVersion;
    public String eventSource;
    public String awsRegion;
    public String eventName;
    public S3Event s3;
}
