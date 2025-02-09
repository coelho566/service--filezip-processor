package com.filezip.processor.adapters.outbound.repository;


import com.filezip.processor.application.core.exception.ResourceNotFoundException;
import com.filezip.processor.application.core.repository.VideoFrameRepository;
import com.filezip.processor.application.core.repository.document.VideoDocument;
import com.filezip.processor.application.ports.out.VideoFrameRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFrameRepositoryAdapter implements VideoFrameRepositoryPort {


    private final VideoFrameRepository repository;

    @Override
    public void updateStatusProcessor(String videoId, String status) {
        repository.findById(videoId)
                .map(videoFrame -> {
                    log.info("Updating status of video frame by videoId: {} status: {}", videoId, status);
                    videoFrame.setProcessorStatus(status);
                    return repository.save(videoFrame);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Video with ID " + videoId + " not found"));
    }

    @Override
    public VideoDocument getVideoById(String videoId) {

        log.info("Get Video By User Id CorrelationId: {}", videoId);
        var videoDocument = repository.findById(videoId);
        return videoDocument
                .orElseThrow(() -> new ResourceNotFoundException("Video document not found for videoId: " + videoId));

    }
}
