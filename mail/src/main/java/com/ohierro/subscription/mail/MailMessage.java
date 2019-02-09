package com.ohierro.subscription.mail;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MailMessage {
    private String from;
    private String to;
    private String subject;
    private String message;
}
