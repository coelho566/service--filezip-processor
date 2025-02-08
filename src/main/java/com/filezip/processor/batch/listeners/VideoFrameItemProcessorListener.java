package com.filezip.processor.batch.listeners;

import com.filezip.processor.adapters.outbound.repository.VideoFrameRepositoryAdapter;
import com.filezip.processor.batch.item.ProcessorStatusItem;
import com.filezip.processor.batch.item.VideoProcessItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFrameItemProcessorListener implements ItemProcessListener<VideoProcessItem, VideoProcessItem> {

    private final VideoFrameRepositoryAdapter videoFrameRepositoryAdapter;

    @Override
    public void beforeProcess(VideoProcessItem item) {
        log.info("Processando item: {}", item);
        videoFrameRepositoryAdapter.updateStatusProcessor(item.getZipId(), ProcessorStatusItem.PROCESSING.name());
    }

    @Override
    public void afterProcess(VideoProcessItem item, VideoProcessItem result) {
        log.info("Item processado: {} -> Resultado: {}", item, result);
        videoFrameRepositoryAdapter.updateStatusProcessor(item.getZipId(), ProcessorStatusItem.PROCESSED.name());
    }

    @Override
    public void onProcessError(VideoProcessItem item, Exception e) {
        log.info("Erro ao processar item: {} - Erro: {}", item.getZipId(), e.getMessage());
        videoFrameRepositoryAdapter.updateStatusProcessor(item.getZipId(), ProcessorStatusItem.ERROR.name());
    }

}
