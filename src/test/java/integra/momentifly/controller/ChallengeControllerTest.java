package integra.momentifly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import integra.momentifly.dto.ChallengeResponse;
import integra.momentifly.dto.CreateChallengeRequest;
import integra.momentifly.exception.GlobalExceptionHandler;
import integra.momentifly.model.ChallengeStatus;
import integra.momentifly.service.ChallengeService;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ChallengeControllerTest {

  private static final String QUEST = "{\"description\":\"Drink water\",\"points\":10}";
  private static final String ONE_QUEST = "[" + QUEST + "]";

  @Mock
  private ChallengeService challengeService;

  @InjectMocks
  private ChallengeController challengeController;

  private MockMvc mockMvc;
  private final Principal principal = new UsernamePasswordAuthenticationToken("david", "password");

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(challengeController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  private ResultActions postBody(String json) throws Exception {
    return mockMvc.perform(post("/api/challenges")
        .principal(principal)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json));
  }

  private String challengeJson(String name, String durationDays, String quests) {
    return "{\"name\":" + name + ",\"durationDays\":" + durationDays + ",\"quests\":" + quests + "}";
  }

  @Test
  void createChallenge_ShouldReturn201AndChallenge() throws Exception {
    ChallengeResponse response = new ChallengeResponse(
        UUID.randomUUID(), "Healthy week", 7, LocalDate.now().plusDays(1),
        ChallengeStatus.UPCOMING, List.of());
    when(challengeService.createChallenge(eq("david"), any(CreateChallengeRequest.class)))
        .thenReturn(response);

    postBody(challengeJson("\"Healthy week\"", "7", ONE_QUEST))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Healthy week"))
        .andExpect(jsonPath("$.durationDays").value(7))
        .andExpect(jsonPath("$.status").value("UPCOMING"));
  }

  @Test
  void createChallenge_ShouldReturn400_WhenNameIsBlank() throws Exception {
    postBody(challengeJson("\"   \"", "7", ONE_QUEST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").value("Every challenge must have a name."));

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenNameIsMissing() throws Exception {
    postBody(challengeJson("null", "7", ONE_QUEST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").exists());

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenDurationIsMissing() throws Exception {
    postBody(challengeJson("\"Healthy week\"", "null", ONE_QUEST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.durationDays").exists());

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenThereAreNoQuests() throws Exception {
    postBody(challengeJson("\"Healthy week\"", "7", "[]"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.quests").exists());

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenThereAreMoreThanFiveQuests() throws Exception {
    String sixQuests = "[" + String.join(",", Collections.nCopies(6, QUEST)) + "]";

    postBody(challengeJson("\"Healthy week\"", "7", sixQuests))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.quests").exists());

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenQuestPointsAreNotPositive() throws Exception {
    String quest = "[{\"description\":\"Drink water\",\"points\":0}]";

    postBody(challengeJson("\"Healthy week\"", "7", quest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['quests[0].points']").exists());

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenQuestDescriptionIsBlank() throws Exception {
    String quest = "[{\"description\":\"\",\"points\":10}]";

    postBody(challengeJson("\"Healthy week\"", "7", quest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['quests[0].description']").exists());

    verifyNoInteractions(challengeService);
  }

  @Test
  void createChallenge_ShouldReturn400_WhenBodyIsMalformed() throws Exception {
    postBody("{\"name\": ")
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Malformed request body"));

    verifyNoInteractions(challengeService);
  }
}
