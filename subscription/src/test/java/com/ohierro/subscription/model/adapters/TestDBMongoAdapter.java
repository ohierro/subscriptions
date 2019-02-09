package com.ohierro.subscription.model.adapters;

import com.ohierro.subscription.model.ports.Subscription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDBMongoAdapter {

    @Autowired
    private DBMongoAdapter mongoAdapter;

    @Autowired
    private MongoOperations db;

    @Before
    public void setUp() {
        db.dropCollection("subscriptions");
    }

    @Test
    public void testSave() {
        String id = mongoAdapter.save(Subscription.builder()
                .email("fake@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("testID")
                .build());

        assertNotNull(id);
        assertFalse(id.isEmpty());

        List<SubscriptionDocument> documents = db.find(new Query(where("")), SubscriptionDocument.class);
        assertEquals(1, documents.size());
    }

    @Test
    public void testFindByNewsletter() {
        db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("firstNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake2@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("secondNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake3@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(false)
                .newsletterId("firstNewsletter")
                .build());

        List<Subscription> subscriptions = mongoAdapter.findByNewsletter("firstNewsletter", false);

        assertEquals(2, subscriptions.size());
        assertTrue(subscriptions.stream().filter(s -> s.getEmail().equals("fake1@email.com")).findFirst().isPresent());
        assertTrue(subscriptions.stream().filter(s -> s.getEmail().equals("fake3@email.com")).findFirst().isPresent());
    }

    @Test
    public void testFindByNewsletterWithConsent() {
        db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("firstNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake2@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(false)
                .newsletterId("firstNewsletter")
                .build());

        List<Subscription> subscriptions = mongoAdapter.findByNewsletter("firstNewsletter", true);

        assertEquals(1, subscriptions.size());
        assertTrue(subscriptions.stream().filter(s -> s.getEmail().equals("fake1@email.com")).findFirst().isPresent());
    }

    @Test
    public void testFindByEmail() {
        db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("firstNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(false)
                .newsletterId("firstNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake2@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(false)
                .newsletterId("firstNewsletter")
                .build());

        List<Subscription> subscriptions = mongoAdapter.findSubscriptionByEmail("fake1@email.com", false);

        assertEquals(2, subscriptions.size());
        assertEquals(2, subscriptions.stream().filter(s -> s.getEmail().equals("fake1@email.com")).count());
    }

    @Test
    public void testFindByEmailWithConsent() {
        db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("firstNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(false)
                .newsletterId("firstNewsletter")
                .build());
        db.insert(SubscriptionDocument.builder()
                .email("fake2@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(false)
                .newsletterId("firstNewsletter")
                .build());

        List<Subscription> subscriptions = mongoAdapter.findSubscriptionByEmail("fake1@email.com", true);

        assertEquals(1, subscriptions.size());
        assertEquals(1, subscriptions.stream().filter(s -> s.getEmail().equals("fake1@email.com")).count());
    }

    @Test
    public void testUpdate() {
        SubscriptionDocument document = db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("firstNewsletter")
                .build());


        Subscription subscription = mongoAdapter.update(document.getId(), Subscription.builder()
            .email("fake1@email.com")
            .dateOfBirth(LocalDate.of(1981, 10, 9))
            .consent(false)
            .newsletterId("anotherNewsletter")
            .build());

        assertFalse(subscription.getConsent());
        assertEquals("anotherNewsletter", subscription.getNewsletterId());
    }

    @Test
    public void testFindById() {
        SubscriptionDocument document = db.insert(SubscriptionDocument.builder()
                .email("fake1@email.com")
                .dateOfBirth(LocalDate.of(1981, 10, 9))
                .consent(true)
                .newsletterId("firstNewsletter")
                .build());

        Subscription subscription = mongoAdapter.findOne(document.getId());

        assertEquals("fake1@email.com", subscription.getEmail());
        assertEquals(LocalDate.of(1981, 10, 9), subscription.getDateOfBirth());
        assertTrue(subscription.getConsent());
        assertEquals("firstNewsletter", subscription.getNewsletterId());
    }
}
