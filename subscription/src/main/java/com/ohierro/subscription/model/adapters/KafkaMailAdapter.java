package com.ohierro.subscription.model.adapters;

import com.ohierro.subscription.mail.MailMessage;
import com.ohierro.subscription.model.ports.MailPort;
import com.ohierro.subscription.model.ports.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMailAdapter implements MailPort {

    KafkaTemplate<String, MailMessage> template;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.subject}")
    private String subject;

    @Value("${app.mail.message}")
    private String message;

    @Autowired
    public KafkaMailAdapter(KafkaTemplate<String, MailMessage> template) {
        this.template = template;
    }

    @Override
    public void sendEmail(Subscription subscription) {
        log.info("Sending email", subscription);

        String receiverName = subscription.getFirstName() == null ? "anonymous" : subscription.getFirstName();

        template.send("mail", MailMessage.builder()
                .from(from)
                .to(subscription.getEmail())
                .subject(subject.replace("%name", receiverName))
                .message(message.replace("%newsletter", subscription.getNewsletterId()))
                .build());
    }
}
