package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.dto.AddCollectionRequest;
import com.epam.training.gen.ai.dto.EmbeddingsRequest;
import com.epam.training.gen.ai.service.VectorService;
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

    private VectorService vectorService;

    @PutMapping("/collections/{collectionName}")
    public ResponseEntity<String> createCollection(@PathVariable String collectionName,
                                                   @RequestBody AddCollectionRequest request) {
        Collections.CollectionOperationResponse response;
        try {
            response = vectorService.createCollection(collectionName, request);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(String.format("Failed to create collection. Reason: %s", e.getCause()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(String.format("Collection created with response: {%s}", response));
    }

    @DeleteMapping("/collections/{collectionName}")
    public ResponseEntity<String> deleteCollection(@PathVariable String collectionName) {
        Collections.CollectionOperationResponse response;
        try {
            response = vectorService.deleteCollection(collectionName);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(String.format("Failed to delete collection. Reason: %s", e.getCause()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(String.format("Collection deleted with response: {%s}", response));
    }


    @PostMapping("/embeddings")
    public List<EmbeddingItem> getEmbeddings(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        return vectorService.getEmbeddings(prompt);
    }

    @PostMapping("/collections/{collectionName}/points")
    public void processEmbeddingsAndSave(@PathVariable String collectionName, @RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        try {
            vectorService.processAndSaveText(collectionName, prompt);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/collections/{collectionName}/points/query")
    public List<Points.ScoredPoint> searchEmbeddings(@PathVariable String collectionName, @RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        try {
            return vectorService.search(collectionName, prompt);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
