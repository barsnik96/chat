package barsnik96.chat.repository;

import barsnik96.chat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, String> {

    Flux<Message> findAllByChatId(String chatId);

    Flux<Message> findAllByChatId(String chatId, Pageable pageable);
}
