package barsnik96.chat.model;

import barsnik96.chat.model.serializer.DocumentIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Message extends BaseId {

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToOne
    @NotNull
    @JsonSerialize(using = DocumentIdSerializer.class)
    private Chat chat;

    @OneToOne
    @NotNull
    @JsonSerialize(using = DocumentIdSerializer.class)
    private User sender;

    @NotBlank
    private String content;
}
