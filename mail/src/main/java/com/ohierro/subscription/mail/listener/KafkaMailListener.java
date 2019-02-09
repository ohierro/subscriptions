package com.ohierro.subscription.mail.listener;

import com.ohierro.subscription.mail.MailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class KafkaMailListener {

    @Value("${kafka.topic.receiver}")
    private static String MAIL_TOPIC = "";

    private JavaMailSender sender;

    @Autowired
    public KafkaMailListener(JavaMailSender sender) {
        this.sender = sender;
    }


    @KafkaListener(id = "mail-consumer", topics = "mail")
    public void receive(MailMessage message) throws MessagingException {
        log.info("Sending mail " + message);

        MimeMessage mail = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);

        helper.setTo(message.getTo());
        helper.setText(message.getMessage());
        helper.setSubject(message.getSubject());

        sender.send(mail);
    }
}
