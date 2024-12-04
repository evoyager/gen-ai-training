package com.epam.training.gen.ai.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChatHistory {
    private final List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getRecentHistory(int limit) {
        int start = Math.max(0, messages.size() - limit);
        List<String> recentMessages = messages.subList(start, messages.size());
        return String.join(" ", recentMessages);
    }
}
