package com.filezip.processor.adapters.inbound.notification.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RootEvent {

    @JsonProperty("Records")
    public List<RecordEvent> records;
}
