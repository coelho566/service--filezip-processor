package com.filezip.processor.application.core.usecase.impl;

import com.filezip.processor.application.core.domain.VideoFrame;
import com.filezip.processor.application.core.usecase.ProcessorVideoUseCase;
import com.filezip.processor.application.ports.in.DownloadVideoStoragePort;
import com.filezip.processor.application.ports.out.UploadStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessorVideoUseCaseImpl implements ProcessorVideoUseCase {

    private final DownloadVideoStoragePort downloadVideoStoragePort;
    private final UploadStoragePort uploadStoragePort;


    @Override
    public void process(VideoFrame videoFrame) {

//         downloadVideoStoragePort.downloadVideo(videoFrame.getId());
//         uploadStoragePort.extractFrameVideo(videoFrame);
//         uploadStoragePort.uploadZipStorage(videoFrame);

    }
}
