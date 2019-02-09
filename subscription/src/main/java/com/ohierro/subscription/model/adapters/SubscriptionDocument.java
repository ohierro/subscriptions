package com.ohierro.subscription.model.adapters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Setter @Getter
@Builder
@Document(collection = "subscriptions")
class SubscriptionDocument {
    @Id
    private String id;
    private String email;
    private String firstName;
    private String gender;
    private LocalDate dateOfBirth;
    private Boolean consent;
    private String newsletterId;
}

