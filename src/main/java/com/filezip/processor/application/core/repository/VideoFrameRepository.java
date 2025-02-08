package com.filezip.processor.application.core.repository;

import com.filezip.processor.application.core.repository.document.VideoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoFrameRepository extends MongoRepository<VideoDocument, String> {

    List<VideoDocument> findByUserIdAndCorrelationId(String userId, String correlationId);
}
