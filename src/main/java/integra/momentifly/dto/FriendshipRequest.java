package integra.momentifly.dto;

import integra.momentifly.model.Status;
import integra.momentifly.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipRequest {

    @NotNull
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID friendId;

    private LocalDateTime friendshipDate;

    private Status status;
}
