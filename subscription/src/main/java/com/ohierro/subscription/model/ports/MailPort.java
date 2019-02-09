package com.ohierro.subscription.model.ports;

public interface MailPort {
    void sendEmail(Subscription subscription);
}
