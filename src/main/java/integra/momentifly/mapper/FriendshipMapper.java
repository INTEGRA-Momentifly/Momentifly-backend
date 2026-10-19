package integra.momentifly.mapper;

import integra.momentifly.dto.FriendshipRequest;
import integra.momentifly.dto.FriendshipResponse;
import integra.momentifly.model.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {
    FriendshipResponse toDto(Friendship friendship);

    @Mapping(target = "id", ignore = true)
    Friendship fromDto(FriendshipRequest friendshipRequest);
}
