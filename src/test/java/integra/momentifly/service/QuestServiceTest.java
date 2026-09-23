package integra.momentifly.service;

import integra.momentifly.model.Quest;
import integra.momentifly.repository.QuestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {

    @Mock
    private QuestRepository questRepository;

    @InjectMocks
    private QuestService questService;

    @Test
    void createQuest_ShouldReturnSavedQuest() {
        Quest quest = new Quest(null, "Test Quest", 10.0);
        Quest savedQuest = new Quest(UUID.randomUUID(), "Test Quest", 10.0);

        when(questRepository.save(any(Quest.class))).thenReturn(savedQuest);

        Quest result = questService.createQuest(quest);

        assertNotNull(result.getId());
        assertEquals("Test Quest", result.getDescription());
        verify(questRepository, times(1)).save(quest);
    }

    @Test
    void getAllQuests_ShouldReturnList() {
        Quest quest = new Quest(UUID.randomUUID(), "Test", 10.0);
        when(questRepository.findAll()).thenReturn(List.of(quest));

        List<Quest> result = questService.getAllQuests();

        assertEquals(1, result.size());
        verify(questRepository, times(1)).findAll();
    }

    @Test
    void getQuestById_ShouldReturnQuest_WhenExists() {
        UUID id = UUID.randomUUID();
        Quest quest = new Quest(id, "Test", 10.0);

        when(questRepository.findById(id)).thenReturn(Optional.of(quest));

        Optional<Quest> result = questService.getQuestById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void updateQuest_ShouldUpdateAndReturnQuest_WhenExists() {
        UUID id = UUID.randomUUID();
        Quest existingQuest = new Quest(id, "Old", 10.0);
        Quest updateData = new Quest(null, "New", 20.0);

        when(questRepository.findById(id)).thenReturn(Optional.of(existingQuest));
        when(questRepository.save(any(Quest.class))).thenReturn(existingQuest);

        Quest result = questService.updateQuest(id, updateData);

        assertEquals("New", result.getDescription());
        assertEquals(20.0, result.getPoints());
    }

    @Test
    void updateQuest_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        Quest updateData = new Quest(null, "New", 20.0);

        when(questRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> questService.updateQuest(id, updateData));
    }

    @Test
    void deleteQuest_ShouldCallRepositoryDelete() {
        UUID id = UUID.randomUUID();
        questService.deleteQuest(id);
        verify(questRepository, times(1)).deleteById(id);
    }
}