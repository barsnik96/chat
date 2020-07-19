package barsnik96.chat.auth;

import barsnik96.chat.model.Chat;
import barsnik96.chat.model.Message;
import barsnik96.chat.model.User;
import barsnik96.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {

    private final UserRepository userRepository;

    @Autowired
    public WebSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean hasChatAuthority(Authentication authentication, String chatId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        return user.getAuthorities().contains(new ChatAuthority(chatId));
    }

    public boolean addChatAuthority(Authentication authentication, Chat chat) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        user.addAuthority(new ChatAuthority(chat.getId()));
        userRepository.save(user).subscribe();

        return true;
    }

    public boolean removeChatAuthority(Authentication authentication, String chatId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        if (user.removeAuthority(new ChatAuthority(chatId))) {
            userRepository.save(user).subscribe();
        }

        return true;
    }

    public boolean isMessageSender(Authentication authentication, Message message) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        return user.getId().equals(message.getSender().getId());
    }
}