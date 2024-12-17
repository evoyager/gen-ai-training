package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.AddCollectionRequest;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class VectorService {
    private final QdrantClient qdrantClient;

    public Collections.CollectionOperationResponse createCollection(String collectionName, AddCollectionRequest request) throws ExecutionException, InterruptedException {
        return qdrantClient.createCollectionAsync(collectionName, VectorParams.newBuilder().setDistance(Distance.Cosine).setSize(request.getSize()).build()).get();
    }
}
