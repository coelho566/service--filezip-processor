package com.filezip.processor.batch.item;

import lombok.Getter;

@Getter
public enum ProcessorStatusItem {

    RECEIVED,
    STARTING_PROCESS,
    PROCESSING,
    PROCESSED,
    ERROR
}
