package barsnik96.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Chat extends BaseId {

    private String name;

    @OneToMany
    @NotNull
    private Set<User> members;
}
