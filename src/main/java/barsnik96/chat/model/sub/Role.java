package barsnik96.chat.model.sub;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Role {

    @NotBlank
    @Pattern(regexp = "ROLE_\\w+")
    private String role;
}
