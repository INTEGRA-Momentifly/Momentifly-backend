package integra.momentifly.service;

import integra.momentifly.dto.ObjectiveResponse;
import integra.momentifly.dto.WriteObjectiveRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Objective;
import integra.momentifly.repository.ObjectiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;

    public ObjectiveResponse getAllObjectivesForUser(UUID userId) {
        return ObjectiveResponse.from(objectiveRepository.findByUserId(userId));
    }

    public ObjectiveResponse addObjective(WriteObjectiveRequest writeObjectiveRequest) {
        //        ONCE THERE IS A USER CLASS AND MANY-TO-ONE IS IMPLEMENTED:
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFounException("User not found"));

        if (objectiveRepository.existsByUserIdAndType(writeObjectiveRequest.userId(), writeObjectiveRequest.macroType()))
            throw new InvalidDataException("An objective of this type already exists for this user.");

        var objective = new Objective();
        objective.setUserId(writeObjectiveRequest.userId());
        objective.setType(writeObjectiveRequest.macroType());
        objective.setValue(writeObjectiveRequest.value());
        objectiveRepository.save(objective);
        return getAllObjectivesForUser(writeObjectiveRequest.userId());
    }

    public ObjectiveResponse updateObjective(WriteObjectiveRequest writeObjectiveRequest) throws NotFoundException {
        Objective objective = objectiveRepository.findByUserIdAndType(writeObjectiveRequest.userId(), writeObjectiveRequest.macroType())
                .orElseThrow(() -> new NotFoundException("Objective not found"));

        objective.setValue(writeObjectiveRequest.value());
        objectiveRepository.save(objective);
        return getAllObjectivesForUser(writeObjectiveRequest.userId());
    }
}