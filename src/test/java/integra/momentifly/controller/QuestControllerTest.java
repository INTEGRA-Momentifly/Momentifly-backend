package integra.momentifly.controller;

import integra.momentifly.model.Quest;
import integra.momentifly.service.QuestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestControllerTest {

    @Mock
    private QuestService questService;

    @InjectMocks
    private QuestController questController;

    @Test
    void createQuest_ShouldReturn200AndQuest() {
        Quest quest = new Quest(null, "Task", 15.0);
        Quest savedQuest = new Quest(UUID.randomUUID(), "Task", 15.0);
        when(questService.createQuest(any(Quest.class))).thenReturn(savedQuest);

        ResponseEntity<Quest> response = questController.createQuest(quest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedQuest, response.getBody());
    }

    @Test
    void getAllQuests_ShouldReturn200AndList() {
        Quest quest = new Quest(UUID.randomUUID(), "Task", 15.0);
        when(questService.getAllQuests()).thenReturn(List.of(quest));

        ResponseEntity<List<Quest>> response = questController.getAllQuests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getQuestById_ShouldReturn200_WhenExists() {
        UUID id = UUID.randomUUID();
        Quest quest = new Quest(id, "Task", 15.0);
        when(questService.getQuestById(id)).thenReturn(Optional.of(quest));

        ResponseEntity<Quest> response = questController.getQuestById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quest, response.getBody());
    }

    @Test
    void getQuestById_ShouldReturn404_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(questService.getQuestById(id)).thenReturn(Optional.empty());

        ResponseEntity<Quest> response = questController.getQuestById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateQuest_ShouldReturn200_WhenUpdated() {
        UUID id = UUID.randomUUID();
        Quest updateData = new Quest(null, "Updated", 20.0);
        Quest updatedQuest = new Quest(id, "Updated", 20.0);
        when(questService.updateQuest(eq(id), any(Quest.class))).thenReturn(updatedQuest);

        ResponseEntity<Quest> response = questController.updateQuest(id, updateData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedQuest, response.getBody());
    }

    @Test
    void deleteQuest_ShouldReturn204() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = questController.deleteQuest(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}