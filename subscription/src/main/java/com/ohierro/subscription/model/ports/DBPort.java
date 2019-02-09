package com.ohierro.subscription.model.ports;

import java.util.List;

public interface DBPort {
    String save(Subscription subscription);

    Subscription update(String id, Subscription subscription);

    Subscription findOne(String id);

    List<Subscription> findByNewsletter(String newsletterId, boolean onlyWithConsent);

    List<Subscription> findSubscriptionByEmail(String email, boolean onlyWithConsent);
}
