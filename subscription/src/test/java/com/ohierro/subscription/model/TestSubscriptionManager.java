package com.ohierro.subscription.model;

import com.ohierro.subscription.model.ports.DBPort;
import com.ohierro.subscription.model.ports.MailPort;
import com.ohierro.subscription.model.ports.Subscription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestSubscriptionManager {

    private SubscriptionManager manager;

    @Mock
    private DBPort dbPort;

    @Mock
    private MailPort mailPort;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        manager = new SubscriptionManager(dbPort, mailPort);
    }

    @Test
    public void testCreateValidSubscription() {
        Subscription subscription = Subscription.builder()
        .email("fake@email.com")
        .firstName("john")
        .gender("female")
        .dateOfBirth(LocalDate.of(1981, 10, 9))
        .consent(true)
        .newsletterId("testID")
        .build();

        manager.save(subscription);

        verify(dbPort).save(any());
        verify(mailPort, times(1)).sendEmail(subscription);
    }

//    @Test
//    public void testCreateOnlyMandatorySubcription() {
//        manager.save(Subscription.builder()
//                .email("fake@email.com")
//                .dateOfBirth(LocalDate.of(1981, 10, 9))
//                .consent(true)
//                .newsletterId("testID")
//                .build());
//
//        verify(dbPort).save(any());
//
//        verify(mailPort, times(1)).sendEmail();
//    }

    @Test
    public void testFindByNewsletters() {
        when(dbPort.findByNewsletter(anyString(), anyBoolean())).thenReturn(new ArrayList<>());

        manager.findByNewsletter("testID");
        verify(dbPort).findByNewsletter(eq("testID"), eq(true));


        manager.findByNewsletter("testID", false);
        verify(dbPort).findByNewsletter(eq("testID"), eq(false));
    }
}
