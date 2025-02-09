package com.filezip.processor.application.ports.out;


import com.filezip.processor.application.core.repository.document.VideoDocument;

public interface VideoFrameRepositoryPort {

    void updateStatusProcessor(String videoId, String status);

    VideoDocument getVideoById(String userId);
}
