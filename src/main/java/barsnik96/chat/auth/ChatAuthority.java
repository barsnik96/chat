package barsnik96.chat.auth;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@EqualsAndHashCode
public class ChatAuthority implements GrantedAuthority {

    private String authority;

    public ChatAuthority(String authority) {
        this.authority = "CHAT_" + authority + "_MOD";
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
