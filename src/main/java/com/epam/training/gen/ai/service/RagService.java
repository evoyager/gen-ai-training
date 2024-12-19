package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.rag.Assistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Slf4j
@Service
public class RagService {

    //TODO: use real apiKey from EPAM instead of demo
    //    @Value("${client-openai-key}")
    String apiKey = "demo";

    @Value("${client-openai-deployment-name}") String model;

    @Value("${client-openai-endpoint}") String endpoint;

    public String chatViaRag(String prompt) {
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(
                "./src/main/resources/documents"
        );
        //TODO: use real embedding store like Qdrant instead of in-memory
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(GPT_4_O_MINI)
                //TODO: use ai-proxy.lab.epam.com as base URL
//                .baseUrl(endpoint)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
        return assistant.chat(prompt);
    }
}
