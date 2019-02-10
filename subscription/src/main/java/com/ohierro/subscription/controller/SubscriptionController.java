package com.ohierro.subscription.controller;

import com.ohierro.subscription.model.SubscriptionManager;
import com.ohierro.subscription.model.ports.Subscription;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(path = "subscription")
@Api()
public class SubscriptionController {

    private SubscriptionManager manager;

    SubscriptionController(SubscriptionManager manager) {
        this.manager = manager;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody SubscriptionRequest request) {
        try {
            request.validate();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(ErrorResponse.builder()
                    .message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }


        String id = manager.save(Subscription.builder()
                .email(request.email)
                .newsletterId(request.newsletterId)
                .consent(request.consent)
                .dateOfBirth(LocalDate.parse(request.dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .firstName(request.firstName)
                .gender(request.gender)
                .build());

        return new ResponseEntity(SubscriptionResponse.builder().id(id).build(), HttpStatus.OK);
    }
}
