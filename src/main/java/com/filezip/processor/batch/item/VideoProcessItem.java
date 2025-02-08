package com.filezip.processor.batch.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoProcessItem {

    private String zipId;
    private String fileName;
    private Double duration;
}
