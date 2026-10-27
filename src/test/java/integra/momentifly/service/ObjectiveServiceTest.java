package integra.momentifly.service;

import integra.momentifly.dto.ObjectiveResponse;
import integra.momentifly.dto.WriteObjectiveRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.MacroType;
import integra.momentifly.model.Objective;
import integra.momentifly.repository.ObjectiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {

    @Mock
    private ObjectiveRepository objectiveRepository;

    @InjectMocks
    private ObjectiveService objectiveService;

    private Objective objective(UUID userId, MacroType type, int value) {
        Objective objective = new Objective();
        objective.setUserId(userId);
        objective.setType(type);
        objective.setValue(value);
        return objective;
    }

    @Test
    void getAllObjectivesForUser_mapsEachTypeToItsField() {
        UUID userId = UUID.randomUUID();
        when(objectiveRepository.findByUserId(userId)).thenReturn(List.of(
                objective(userId, MacroType.CALORIES, 2000),
                objective(userId, MacroType.PROTEIN, 150),
                objective(userId, MacroType.CARBOHYDRATES, 100),
                objective(userId, MacroType.FATS, 50)));

        ObjectiveResponse response = objectiveService.getAllObjectivesForUser(userId);

        assertEquals(2000, response.calories());
        assertEquals(150, response.protein());
        assertEquals(100, response.carbohydrates());
        assertEquals(50, response.fats());
    }

    @Test
    void getAllObjectivesForUser_leavesTypesThatAreNotSetAsNull() {
        UUID userId = UUID.randomUUID();
        when(objectiveRepository.findByUserId(userId))
                .thenReturn(List.of(objective(userId, MacroType.CALORIES, 2000)));

        ObjectiveResponse response = objectiveService.getAllObjectivesForUser(userId);

        assertEquals(2000, response.calories());
        assertNull(response.protein());
        assertNull(response.carbohydrates());
        assertNull(response.fats());
    }

    @Test
    void getAllObjectivesForUser_withNoObjectives_returnsAllNull() {
        UUID userId = UUID.randomUUID();
        when(objectiveRepository.findByUserId(userId)).thenReturn(List.of());

        ObjectiveResponse response = objectiveService.getAllObjectivesForUser(userId);

        assertNull(response.calories());
        assertNull(response.protein());
        assertNull(response.carbohydrates());
        assertNull(response.fats());
    }

    @Test
    void addObjective_savesTheObjective_andReturnsTheUsersObjectives() {
        UUID userId = UUID.randomUUID();
        when(objectiveRepository.existsByUserIdAndType(userId, MacroType.CALORIES)).thenReturn(false);
        when(objectiveRepository.findByUserId(userId))
                .thenReturn(List.of(objective(userId, MacroType.CALORIES, 2000)));

        ObjectiveResponse response = objectiveService.addObjective(
                new WriteObjectiveRequest(userId, MacroType.CALORIES, 2000));

        ArgumentCaptor<Objective> captor = ArgumentCaptor.forClass(Objective.class);
        verify(objectiveRepository).save(captor.capture());
        assertEquals(userId, captor.getValue().getUserId());
        assertEquals(MacroType.CALORIES, captor.getValue().getType());
        assertEquals(2000, captor.getValue().getValue());
        assertEquals(2000, response.calories());
    }

    @Test
    void addObjective_whenTheUserAlreadyHasThatType_isRejected() {
        UUID userId = UUID.randomUUID();
        when(objectiveRepository.existsByUserIdAndType(userId, MacroType.CALORIES)).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> objectiveService.addObjective(
                new WriteObjectiveRequest(userId, MacroType.CALORIES, 2000)));

        verify(objectiveRepository, never()).save(any());
    }

    @Test
    void updateObjective_changesOnlyTheValue_andReturnsTheUsersObjectives() {
        UUID userId = UUID.randomUUID();
        Objective existing = objective(userId, MacroType.PROTEIN, 100);
        when(objectiveRepository.findByUserIdAndType(userId, MacroType.PROTEIN)).thenReturn(Optional.of(existing));
        when(objectiveRepository.findByUserId(userId)).thenReturn(List.of(existing));

        ObjectiveResponse response = objectiveService.updateObjective(
                new WriteObjectiveRequest(userId, MacroType.PROTEIN, 150));

        verify(objectiveRepository).save(existing);
        assertEquals(150, existing.getValue());
        assertEquals(MacroType.PROTEIN, existing.getType());   // the type is never changed
        assertEquals(150, response.protein());
        assertNull(response.calories());
    }

    @Test
    void updateObjective_whenTheUserHasNoObjectiveOfThatType_throwsNotFound() {
        UUID userId = UUID.randomUUID();
        when(objectiveRepository.findByUserIdAndType(userId, MacroType.FATS)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> objectiveService.updateObjective(
                new WriteObjectiveRequest(userId, MacroType.FATS, 50)));

        verify(objectiveRepository, never()).save(any());
    }
}