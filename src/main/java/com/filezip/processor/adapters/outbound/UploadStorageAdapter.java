package com.filezip.processor.adapters.outbound;

import com.filezip.processor.application.config.ResourcesTempProperties;
import com.filezip.processor.application.core.exception.BusinessException;
import com.filezip.processor.application.core.exception.RetryProcessException;
import com.filezip.processor.application.ports.out.UploadStoragePort;
import com.filezip.processor.application.utils.PathResourcesUtils;
import com.filezip.processor.batch.item.VideoProcessItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadStorageAdapter implements UploadStoragePort {

    private final S3Client s3Client;
    private final ResourcesTempProperties resourcesTempProperties;


    @Override
    @SuppressWarnings("resource")
    public void extractFrameVideo(VideoProcessItem processItem) {
        try {
            log.info("Init processing extract frame: {}", processItem.getZipId());
            var grabber = new FFmpegFrameGrabber(String.format("%s/%s.mp4", resourcesTempProperties.getVideo(), processItem.getZipId()));
            grabber.start();
            // Obter a duração do vídeo em segundos
            double durationInSeconds = (double) grabber.getLengthInTime() / avutil.AV_TIME_BASE;

            var converter = new Java2DFrameConverter();
            for (double currentTime = 0; currentTime < durationInSeconds; currentTime += processItem.getDuration()) {
                log.info("Processando frame: {}", currentTime);

                // Pular para o tempo desejado
                grabber.setTimestamp((long) (currentTime * avutil.AV_TIME_BASE));
                var frame = grabber.grabImage();

                if (frame != null) {

                    var correlationId = processItem.getZipId();
                    var outputFolder = resourcesTempProperties.getImages();
                    PathResourcesUtils.getPath(String.format("%s/%s", outputFolder, correlationId));

                    var image = converter.getBufferedImage(frame);
                    var outputPath = String.format("%s/%s/frame_at_%s.jpg", outputFolder, correlationId, currentTime);
                    var output = new File(outputPath);
                    ImageIO.write(image, "jpg", output);
                }
            }

            grabber.stop();
        } catch (Exception e) {
            log.error("Error transforming video in frame image");
            throw new RetryProcessException("Retry processing extract frame video");
        }
    }

    @Override
    public void uploadZipStorage(VideoProcessItem processItem) {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            log.info("Init processing zip storage: {}", processItem.getZipId());
            var sourcePath = Paths.get(String.format("%s/%s", resourcesTempProperties.getImages(), processItem.getZipId()));
            Files.walk(sourcePath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (Exception e) {
                            log.error("Error processing zip storage for video frame: {}", processItem.getZipId());
                            throw new RetryProcessException("Retry processing zip storage for video frame: " + processItem.getZipId());
                        }
                    });

            zipOut.finish();
            byte[] zipBytes = byteArrayOutputStream.toByteArray();

            try {
                var keyBucket = String.format("zip/%s.zip", processItem.getZipId());
                var putObjectRequest = PutObjectRequest.builder()
                        .bucket("filezip-bucket-service")
                        .key(keyBucket)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(zipBytes));
                log.info("Finish processing zip storage for video frame: {}", processItem.getZipId());
            } catch (S3Exception e) {
                log.error("Error seeding zip storage for video frame: {}", processItem.getZipId());
                throw new RetryProcessException("Retry seeding zip storage for video frame: " + processItem.getZipId());
            }
        } catch (IOException e) {
            log.error("Error processing file zip storage for video frame: {}", processItem.getZipId(), e);
            throw new BusinessException(e.getMessage());
        }
    }
}
