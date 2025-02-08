package com.filezip.processor.application.ports.in;

import com.filezip.processor.batch.item.VideoProcessItem;

import java.util.List;

public interface DownloadVideoStoragePort {

    void downloadVideo(List<VideoProcessItem> videos);
}
