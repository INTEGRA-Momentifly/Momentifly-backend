package integra.momentifly.service;

import integra.momentifly.dto.ObjectiveResponse;
import integra.momentifly.dto.WriteObjectiveRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.MacroType;
import integra.momentifly.model.Objective;
import integra.momentifly.model.User;
import integra.momentifly.repository.ObjectiveRepository;
import integra.momentifly.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {

    @Mock
    private ObjectiveRepository objectiveRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ObjectiveService objectiveService;

    private User userNamed(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }

    private Objective objective(User user, MacroType type, int value) {
        Objective objective = new Objective();
        objective.setUser(user);
        objective.setType(type);
        objective.setValue(value);
        return objective;
    }

    // ---------- getObjectives ----------

    @Test
    void getObjectives_mapsEachTypeToItsField() {
        User user = userNamed("paul");
        when(objectiveRepository.findByUser_Username("paul")).thenReturn(List.of(
                objective(user, MacroType.CALORIES, 2000),
                objective(user, MacroType.PROTEIN, 150),
                objective(user, MacroType.CARBOHYDRATES, 100),
                objective(user, MacroType.FATS, 50)));

        ObjectiveResponse response = objectiveService.getObjectives("paul");

        assertEquals(2000, response.calories());
        assertEquals(150, response.protein());
        assertEquals(100, response.carbohydrates());
        assertEquals(50, response.fats());
    }

    @Test
    void getObjectives_leavesTypesThatAreNotSetAsNull() {
        User user = userNamed("paul");
        when(objectiveRepository.findByUser_Username("paul"))
                .thenReturn(List.of(objective(user, MacroType.CALORIES, 2000)));

        ObjectiveResponse response = objectiveService.getObjectives("paul");

        assertEquals(2000, response.calories());
        assertNull(response.protein());
        assertNull(response.carbohydrates());
        assertNull(response.fats());
    }

    @Test
    void getObjectives_withNoObjectives_returnsAllNull() {
        when(objectiveRepository.findByUser_Username("paul")).thenReturn(List.of());

        ObjectiveResponse response = objectiveService.getObjectives("paul");

        assertNull(response.calories());
        assertNull(response.protein());
        assertNull(response.carbohydrates());
        assertNull(response.fats());
    }

    // ---------- addObjective ----------

    @Test
    void addObjective_savesTheObjectiveForTheUser_andReturnsTheUpdatedObjectives() {
        User user = userNamed("paul");
        when(objectiveRepository.existsByUser_UsernameAndType("paul", MacroType.CALORIES)).thenReturn(false);
        when(userRepository.findByUsername("paul")).thenReturn(Optional.of(user));
        when(objectiveRepository.findByUser_Username("paul"))
                .thenReturn(List.of(objective(user, MacroType.CALORIES, 2000)));

        ObjectiveResponse response = objectiveService.addObjective("paul",
                new WriteObjectiveRequest(MacroType.CALORIES, 2000));

        ArgumentCaptor<Objective> captor = ArgumentCaptor.forClass(Objective.class);
        verify(objectiveRepository).save(captor.capture());
        assertSame(user, captor.getValue().getUser());
        assertEquals(MacroType.CALORIES, captor.getValue().getType());
        assertEquals(2000, captor.getValue().getValue());
        assertEquals(2000, response.calories());
    }

    @Test
    void addObjective_whenTheTypeAlreadyExists_isRejected() {
        when(objectiveRepository.existsByUser_UsernameAndType("paul", MacroType.CALORIES)).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> objectiveService.addObjective("paul",
                new WriteObjectiveRequest(MacroType.CALORIES, 2000)));

        verify(objectiveRepository, never()).save(any());
    }

    @Test
    void addObjective_forUnknownUser_throwsNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> objectiveService.addObjective("ghost",
                new WriteObjectiveRequest(MacroType.CALORIES, 2000)));

        verify(objectiveRepository, never()).save(any());
    }

    // ---------- updateObjective ----------

    @Test
    void updateObjective_changesTheValue_andReturnsTheUpdatedObjectives() {
        User user = userNamed("paul");
        Objective existing = objective(user, MacroType.CALORIES, 1800);
        when(objectiveRepository.findByUser_UsernameAndType("paul", MacroType.CALORIES))
                .thenReturn(Optional.of(existing));
        when(objectiveRepository.findByUser_Username("paul")).thenReturn(List.of(existing));

        ObjectiveResponse response = objectiveService.updateObjective("paul",
                new WriteObjectiveRequest(MacroType.CALORIES, 2200));

        verify(objectiveRepository).save(existing);
        assertEquals(2200, existing.getValue());
        assertEquals(2200, response.calories());
    }

    @Test
    void updateObjective_whenNotSetYet_throwsNotFound() {
        when(objectiveRepository.findByUser_UsernameAndType("paul", MacroType.FATS))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> objectiveService.updateObjective("paul",
                new WriteObjectiveRequest(MacroType.FATS, 50)));

        verify(objectiveRepository, never()).save(any());
    }
}
