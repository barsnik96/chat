package barsnik96.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@ToString
@Entity
public class User extends BaseId {

    @NotBlank
    @Column(unique=true)
    private String username;

    @NotBlank
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    @JsonIgnore
    @Transient
    private List<GrantedAuthority> roles = new ArrayList<>();

    @JsonIgnore
    @Transient
    private List<GrantedAuthority> authorities = new ArrayList<>();

    private String status = "";

    public User() {
        addRole(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public void addRole(GrantedAuthority role) {
        roles.add(role);
    }

    public boolean removeRole(GrantedAuthority role) {
        return roles.remove(role);
    }

    public void addAuthority(GrantedAuthority authority) {
        authorities.add(authority);
    }

    public boolean removeAuthority(GrantedAuthority authority) {
        return authorities.remove(authority);
    }
}
