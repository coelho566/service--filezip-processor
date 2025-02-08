package com.filezip.processor.adapters.inbound.storage;

import com.filezip.processor.application.config.ResourcesTempProperties;
import com.filezip.processor.application.ports.in.DownloadVideoStoragePort;
import com.filezip.processor.application.ports.out.VideoFrameRepositoryPort;
import com.filezip.processor.application.utils.PathResourcesUtils;
import com.filezip.processor.batch.item.ProcessorStatusItem;
import com.filezip.processor.batch.item.VideoProcessItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadVideoStorage implements DownloadVideoStoragePort {

    private final S3Client s3Client;
    private final ResourcesTempProperties resourcesTempProperties;
    private final VideoFrameRepositoryPort videoFrameRepositoryPort;

    @Override
    public void downloadVideo(List<VideoProcessItem> videos) {

        videos.forEach(videoFrameItem -> {
            var fileName = videoFrameItem.getZipId();
            log.info("Downloading video file {}", fileName);

            videoFrameRepositoryPort.updateStatusProcessor(fileName, ProcessorStatusItem.STARTING_PROCESS.name());

            PathResourcesUtils.getPath(resourcesTempProperties.getVideo());

            var getObjectRequest = GetObjectRequest.builder()
                    .bucket("filezip-bucket-service")
                    .key(String.format("videos/%s.mp4", fileName))
                    .build();

            var resourceTempVideo = String.format("%s/%s.mp4", resourcesTempProperties.getVideo(), fileName);
            try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
                 FileOutputStream outputStream = new FileOutputStream(resourceTempVideo)) {

                log.info("Create video file temporary {}", fileName);
                s3Object.transferTo(outputStream);

            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
