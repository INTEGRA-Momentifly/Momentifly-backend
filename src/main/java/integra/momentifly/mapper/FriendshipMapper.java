package integra.momentifly.mapper;

import integra.momentifly.dto.FriendshipRequest;
import integra.momentifly.dto.FriendshipResponse;
import integra.momentifly.model.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "user.username", target = "userName")
  @Mapping(source = "friend.id", target = "friendId")
  @Mapping(source = "friend.username", target = "friendName")
  FriendshipResponse toDto(Friendship friendship);

  @Mapping(target = "id", ignore = true)
  Friendship fromDto(FriendshipRequest friendshipRequest);
}
