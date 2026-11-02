package integra.momentifly.dto;

import integra.momentifly.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipResponse {

  private UUID id;
  private UUID userId;
  private String userName;
  private UUID friendId;
  private String friendName;
  private LocalDateTime friendshipDate;
  private Status status;
}
