package integra.momentifly.controller;

import integra.momentifly.dto.ChallengeResponse;
import integra.momentifly.dto.CreateChallengeRequest;
import integra.momentifly.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<ChallengeResponse>createChallenge(Authentication authentication, @Valid @RequestBody CreateChallengeRequest request){
      ChallengeResponse response = challengeService.createChallenge(authentication.getName(), request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
