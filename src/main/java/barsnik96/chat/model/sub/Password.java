package barsnik96.chat.model.sub;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class Password {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
