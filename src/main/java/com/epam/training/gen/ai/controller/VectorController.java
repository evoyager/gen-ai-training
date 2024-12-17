package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.dto.AddCollectionRequest;
import com.epam.training.gen.ai.dto.EmbeddingsRequest;
import com.epam.training.gen.ai.service.VectorService;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Points;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v5")
@AllArgsConstructor
public class VectorController {

    private SimpleVectorActions simpleVectorActions;
    private VectorService vectorService;

    @PutMapping("/vector/collections/{collection_name}")
    public ResponseEntity<String> createCollection(@PathVariable String collection_name,
                                                   @RequestBody AddCollectionRequest request) {
        Collections.CollectionOperationResponse response;
        try {
            response = vectorService.createCollection(collection_name, request);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(String.format("Failed to create collection. Reason: %s", e.getCause()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(String.format("Collection created with response: {%s}", response));
    }


    @PostMapping("/embeddings")
    public List<EmbeddingItem> getEmbeddings(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        return simpleVectorActions.getEmbeddings(prompt);
    }

    @PostMapping("/save-embeddings")
    public void processEmbeddingsAndSave(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        try {
            simpleVectorActions.processAndSaveText(prompt);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/search-embeddings")
    public List<Points.ScoredPoint> searchEmbeddings(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        try {
            return simpleVectorActions.search(prompt);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
