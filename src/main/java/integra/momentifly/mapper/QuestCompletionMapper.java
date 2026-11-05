package integra.momentifly.mapper;

import integra.momentifly.dto.QuestCompletionRequest;
import integra.momentifly.dto.QuestCompletionResponse;
import integra.momentifly.model.QuestCompletion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestCompletionMapper {
  QuestCompletionResponse toDto(QuestCompletion questCompletion);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "completionDate", ignore = true)
  @Mapping(target = "completionStatus", ignore = true)
  QuestCompletion fromDto(QuestCompletionRequest questCompletionRequest);
}
