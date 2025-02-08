package com.filezip.processor.application.ports.out;



import com.filezip.processor.application.core.repository.document.VideoDocument;

import java.util.List;

public interface VideoFrameRepositoryPort {

    void updateStatusProcessor(String videoId, String status);

    VideoDocument getVideoById(String userId);
}
