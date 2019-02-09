package com.ohierro.subscription.model.ports;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter @Getter
@ToString
public class Subscription {
    @NonNull
    String email;

    String firstName;

    String gender;

    @NonNull
    LocalDate dateOfBirth;
    @NonNull
    Boolean consent;
    @NonNull
    String newsletterId;
}
