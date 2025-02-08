package com.filezip.processor.adapters.inbound.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectEvent {
    public String key;
    public int size;
    public String eTag;
    public String sequencer;
}
