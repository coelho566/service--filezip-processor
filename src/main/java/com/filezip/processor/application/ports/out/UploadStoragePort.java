package com.filezip.processor.application.ports.out;

import com.filezip.processor.batch.item.VideoProcessItem;

public interface UploadStoragePort {

    void extractFrameVideo(VideoProcessItem processItem);

    void uploadZipStorage(VideoProcessItem processItem);
}
