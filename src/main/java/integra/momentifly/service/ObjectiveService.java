package integra.momentifly.service;

import integra.momentifly.dto.ObjectiveResponse;
import integra.momentifly.dto.WriteObjectiveRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Objective;
import integra.momentifly.model.User;
import integra.momentifly.repository.ObjectiveRepository;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final UserRepository userRepository;

    public ObjectiveResponse getObjectives(String username) {
        return ObjectiveResponse.from(objectiveRepository.findByUser_Username(username));
    }

    public ObjectiveResponse addObjective(String username, WriteObjectiveRequest request) {
        if (objectiveRepository.existsByUser_UsernameAndType(username, request.type())) {
          throw new InvalidDataException("An objective of type " + request.type() + " already exists");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Objective objective = new Objective();
        objective.setUser(user);
        objective.setType(request.type());
        objective.setValue(request.value());
        objectiveRepository.save(objective);

        return getObjectives(username);
    }

    public ObjectiveResponse updateObjective(String username, WriteObjectiveRequest request) {
        Objective objective = objectiveRepository.findByUser_UsernameAndType(username, request.type())
                .orElseThrow(() -> new NotFoundException("No " + request.type() + " objective set yet"));

        objective.setValue(request.value());
        objectiveRepository.save(objective);

        return getObjectives(username);
    }
}
