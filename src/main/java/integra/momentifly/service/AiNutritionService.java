package integra.momentifly.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import integra.momentifly.dto.AINutritionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Service
public class AiNutritionService {
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${ai.nutrition.response.key:dummy-test-key}")
  private String apiKey;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public AINutritionResponse estimateNutrients(String description){
    OpenAIClient client = OpenAIOkHttpClient.builder()
        .apiKey(apiKey)
        .build();

    String prompt = String.format(
        "Estimează macronutrienții în grame și caloriile pentru: '%s'. Răspunde strict în format JSON cu cheile: proteins, carbs, fats, calories.",
        description
    );

    try {
      ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
          .model("gpt-4o-mini")
          .addUserMessage(prompt)
          .build();

      ChatCompletion completion = client.chat().completions().create(params);

      String jsonContent = completion.choices().getFirst().message().content().orElse("{}");

      jsonContent = jsonContent.replace("```json", "").replace("```", "").trim();

      return objectMapper.readValue(jsonContent, AINutritionResponse.class);
    } catch(Exception e){
      throw new RuntimeException("Eroare la estimarea nutrienților prin OpenAI: " + e.getMessage(), e);
    }
  }
}
