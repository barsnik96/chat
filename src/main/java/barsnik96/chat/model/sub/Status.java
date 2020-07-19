package barsnik96.chat.model.sub;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class Status {

    @NotBlank
    private String status;
}
