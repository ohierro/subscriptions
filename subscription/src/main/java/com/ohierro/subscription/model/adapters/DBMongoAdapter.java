package com.ohierro.subscription.model.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohierro.subscription.model.ports.DBPort;
import com.ohierro.subscription.model.ports.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class DBMongoAdapter implements DBPort {

    private MongoOperations db;
    private ObjectMapper mapper;

    @Autowired
    public DBMongoAdapter(MongoOperations db, ObjectMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public String save(Subscription subscription) {
        SubscriptionDocument document = SubscriptionDocument.builder()
                .email(subscription.getEmail())
                .firstName(subscription.getFirstName())
                .gender(subscription.getGender())
                .dateOfBirth(subscription.getDateOfBirth())
                .newsletterId(subscription.getNewsletterId())
                .consent(subscription.getConsent())
                .build();

        document = db.save(document);
        return document.getId();
    }

    @Override
    public Subscription update(String id, Subscription subscription) {
        SubscriptionDocument document = db.findById(id, SubscriptionDocument.class);

        document.setEmail(subscription.getEmail());
        document.setNewsletterId(subscription.getNewsletterId());
        document.setConsent(subscription.getConsent());
        document.setDateOfBirth(subscription.getDateOfBirth());
        document.setFirstName(subscription.getFirstName());
        document.setGender(subscription.getGender());

        return convert(db.save(document));
    }

    @Override
    public Subscription findOne(String id) {
        return convert(db.findById(id, SubscriptionDocument.class));
    }

    @Override
    public List<Subscription> findByNewsletter(String newsletterId, boolean onlyWithConsent) {
        Criteria criteria = Criteria.where("newsletterId").is(newsletterId);

        if (onlyWithConsent) {
            criteria.and("consent").is(true);
        }

        return db.find(query(criteria), SubscriptionDocument.class)
                .stream()
                .map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findSubscriptionByEmail(String email, boolean onlyWithConsent) {
        Criteria criteria = Criteria.where("email").is(email);

        if (onlyWithConsent) {
            criteria.and("consent").is(true);
        }

        return db.find(query(criteria), SubscriptionDocument.class)
                .stream()
                .map(this::convert).collect(Collectors.toList());
    }

    private Subscription convert(SubscriptionDocument document) {
        return mapper.convertValue(document, Subscription.class);
    }
}
