package barsnik96.chat.service;

import barsnik96.chat.repository.ChatRepository;
import barsnik96.chat.model.Chat;
import barsnik96.chat.model.Message;
import barsnik96.chat.model.User;
import barsnik96.chat.util.CollectionUtils;
import barsnik96.chat.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ReactiveMongoOperations mongoOperations;

    @Autowired
    public ChatService(ChatRepository chatRepository, ReactiveMongoOperations mongoOperations) {
        this.chatRepository = chatRepository;
        this.mongoOperations = mongoOperations;
    }

    public Flux<Chat> findAllChatsForCurrentUser() {
        return SecurityUtils.getPrincipal()
                .flatMapMany(principal -> chatRepository.findAllByMembers(principal.getId()));
    }

    @PostAuthorize("@webSecurity.addChatAuthority(authentication, #chat)")
    public Mono<Chat> saveChat(Chat chat) {
        Set<User> members = CollectionUtils.emptySetIfNull(chat.getMembers());

        return SecurityUtils.getPrincipal()
                .doOnNext(principal -> members.add(principal.getUser()))
                .doOnNext(principal -> chat.setMembers(members))
                .flatMap(principal -> chatRepository.save(chat));
    }

    @PreAuthorize("@webSecurity.hasChatAuthority(authentication, #chatId) " +
            "and @webSecurity.removeChatAuthority(authentication, #chatId)")
    public Mono<Void> deleteChat(String chatId) {
        return chatRepository.deleteById(chatId);
    }

    public Flux<Chat> streamNewChats() {
        ChangeStreamOptions options = ChangeStreamOptions.builder()
                .filter(newAggregation(match(where("operationType").is("insert"))))
                .build();

        return mongoOperations
                .changeStream("chat", options, Chat.class)
                .map(ChangeStreamEvent::getBody)
                .zipWith(SecurityUtils.getPrincipal())
                .filter(tuple -> tuple.getT1().getMembers().contains(tuple.getT2().getUser()))
                .map(Tuple2::getT1);
    }

    public Flux<Message> streamNewMessages() {
        ChangeStreamOptions options = ChangeStreamOptions.builder()
                .filter(newAggregation(match(where("operationType").is("insert"))))
                .build();

        return mongoOperations
                .changeStream("message", options, Message.class)
                .map(ChangeStreamEvent::getBody)
                .zipWith(findAllChatsForCurrentUser().collectList())
                .filter(tuple -> tuple.getT2().contains(tuple.getT1().getChat()))
                .map(Tuple2::getT1);
    }

    public Flux<User> findAllMembers(String chatId) {
        return chatRepository
                .findById(chatId)
                .flatMapMany(chat -> Flux.fromIterable(chat.getMembers()));
    }

    public Mono<Chat> saveMember(String chatId, User member) {
        return chatRepository
                .findById(chatId)
                .doOnNext(chat -> chat.getMembers().add(member))
                .flatMap(chatRepository::save);
    }

    @PreAuthorize("@webSecurity.hasChatAuthority(authentication, #chatId) or #userId == principal.id")
    public Mono<Void> deleteMember(String chatId, String userId) {
        return chatRepository
                .findById(chatId)
                .filter(chat -> chat.getMembers().removeIf(member -> member.getId().equals(userId)))
                .flatMap(chatRepository::save)
                .then();
    }
}
