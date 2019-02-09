package com.ohierro.subscription.model;

import com.ohierro.subscription.model.ports.DBPort;
import com.ohierro.subscription.model.ports.MailPort;
import com.ohierro.subscription.model.ports.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SubscriptionManager {

    private DBPort dbPort;

    private MailPort mailPort;

    @Autowired
    public SubscriptionManager(DBPort dbPort, MailPort mailPort) {
        this.dbPort = dbPort;
        this.mailPort = mailPort;
    }

    public String save(Subscription subscription) {
        String id = dbPort.save(subscription);

        mailPort.sendEmail(subscription);

        return id;
    }

    public List<Subscription> findByNewsletter(String newsletterId) {
        return findByNewsletter(newsletterId, true);
    }

    public List<Subscription> findByNewsletter(String newsletterId, boolean onlyWithConsent) {
        return dbPort.findByNewsletter(newsletterId, onlyWithConsent);
    }

    public List<Subscription> findSubscriptionByEmail(String email) {
        return findSubscriptionByEmail(email, true);
    }

    public List<Subscription> findSubscriptionByEmail(String email, boolean onlyWithConsent) {
        return dbPort.findSubscriptionByEmail(email, onlyWithConsent);
    }
}
