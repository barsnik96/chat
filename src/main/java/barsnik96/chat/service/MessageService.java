package barsnik96.chat.service;

import barsnik96.chat.repository.MessageRepository;
import barsnik96.chat.model.Chat;
import barsnik96.chat.model.Message;
import barsnik96.chat.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mono<Message> findById(String messageId) {
        return messageRepository.findById(messageId);
    }

    public Flux<Message> findAllMessages(String chatId) {
        return messageRepository.findAllByChatId(chatId);
    }

    public Flux<Message> findAllMessagesPaginated(String chatId, String start, String chunk) {
        int startDoc = Integer.parseInt(start);
        int chunkSize = Integer.parseInt(chunk);

        return messageRepository.findAllByChatId(chatId, PageRequest.of(startDoc, chunkSize));
    }

    public Mono<Message> saveMessage(Message message, String chatId) {
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);

        return SecurityUtils.getPrincipal()
                .doOnNext(principal -> message.setSender(principal.getUser()))
                .flatMap(principal -> messageRepository.save(message));
    }

    @PreAuthorize("@webSecurity.isMessageSender(authentication, #message)")
    public Mono<Void> deleteMessage(Message message) {
        return messageRepository.delete(message);
    }
}
