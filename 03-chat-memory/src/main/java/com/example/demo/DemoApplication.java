package com.example.demo;

import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder) {
		return args -> { // @formatter:off

			var chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();

			var sessionId = UUID.randomUUID().toString();

			ChatClient chatClient = chatClientBuilder
				.defaultAdvisors(MyLoggingAdvisor.builder().showConversationHistory(true).build())
			
				.defaultAdvisors(a -> a.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
										.param(ChatMemory.CONVERSATION_ID, sessionId))
				.build();
			
			System.out.println("Name introduction: " + chatClient.prompt("My name is Christian Tzolov").call().content());
			
			System.out.println("Asking for the name: " + chatClient.prompt("What is my name?").call().content());

		}; // @formatter:on
	}

}
