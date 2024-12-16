package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.dto.EmbeddingsRequest;
import com.epam.training.gen.ai.service.EmbeddingsService;
import io.qdrant.client.grpc.Points;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v5")
@AllArgsConstructor
public class EmbeddingsController {

    private EmbeddingsService embeddingsService;

    @PostMapping("/embeddings")
    public List<EmbeddingItem> getEmbeddings(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        return embeddingsService.getEmbeddings(prompt);
    }

    @PostMapping("/save-embeddings")
    public void processEmbeddingsAndSave(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        try {
            embeddingsService.processAndSaveText(prompt);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/search-embeddings")
    public List<Points.ScoredPoint> searchEmbeddings(@RequestBody EmbeddingsRequest request) {
        String prompt = request.getInput();
        try {
            return embeddingsService.search(prompt);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
