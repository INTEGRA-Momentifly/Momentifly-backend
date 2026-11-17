package integra.momentifly.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import java.util.List;

@Service
public class EmailService {
  private final MailtrapClient mailtrapClient;
  private final String senderEmail;

  public EmailService( @Value("${mailtrap.api.token}") String token, @Value("${mailtrap.sandbox.inbox-id}") Long inboxId, @Value("${mailtrap.sender.email}") String senderEmail) {
    MailtrapConfig config = new MailtrapConfig.Builder()
        .token(token)
        .sandbox(true)
        .inboxId(inboxId)
        .build();

    this.mailtrapClient =
        MailtrapClientFactory.createMailtrapClient(config);

    this.senderEmail = senderEmail;
  }

  public void sendFriendRequestEmail(String recipientEmail, String senderUsername) {
    MailtrapMail mail = MailtrapMail.builder()
        .from(new Address(senderEmail, "Momentifly"))
        .to(List.of(new Address(recipientEmail)))
        .subject("You have a new friend request!")
        .text(
            "Hello!\n\n" +
                senderUsername +
                " would like to become your friend on Momentifly.\n\n" +
                "Log in to Momentifly to accept or reject the request.\n\n" +
                "http://localhost:3000\n\n" +
                "Best regards,\nMomentifly Team"
        )
        .category("Friend Request")
        .build();

    try {
      mailtrapClient.send(mail);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to send friend request email", e
      );
    }
  }
}
