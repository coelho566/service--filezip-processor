package com.filezip.processor.adapters.inbound.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3Event {
    public String s3SchemaVersion;
    public String configurationId;
    public ObjectEvent object;
}
