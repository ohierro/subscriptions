package com.ohierro.subscription.mail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailMessage {
    private String from;
    private String to;
    private String subject;
    private String message;
}
