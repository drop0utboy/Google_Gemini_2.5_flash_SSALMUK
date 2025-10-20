package com.example.demo.controller;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/ai")
@Slf4j
public class AiController {
    @Autowired
    private ChatModel chatModel;

    @PostMapping(
        value = "/chat",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE
    )

    public String postMethodName(@RequestParam("question") String question) {
        // LLM의 역할과 스타일을 정의
        SystemMessage systemMessage = SystemMessage.builder()
        // .text("ENTP 같은 말투로 답변할것")
        .text("")
        .build();

        // User Message 사용자 질문을 담음
        UserMessage userMessage = UserMessage.builder()
        .text(question)
        .build();

        // 모델과 파라미터를 설정
        // 디폴트 gemini 2.5 flash
        ChatOptions chatOptions = ChatOptions.builder()
        // .model("gemini-2.5-flash")
        // .maxTokens(100)
        // .temperature(0.0)
        .build();

        // Prompt: 메시지와 옵션을 결합합니다.
        Prompt prompt = Prompt.builder()
        .messages(systemMessage, userMessage)
        .chatOptions(chatOptions)
        .build();

        // ChatModel 호출 및 응답 처리
        ChatResponse chatResponse = chatModel.call(prompt);
        
        log.info("=================================================");
        log.info("chatResponse: {}", chatResponse.toString());
        log.info("=================================================");

        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        String answer = assistantMessage.getText();
        
        log.info("Answer: {}", answer);
        log.info("Total Tokens Used: {}", chatResponse.getMetadata().getUsage().getTotalTokens());

        return answer;
    }
    
}
