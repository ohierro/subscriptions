package com.ohierro.subscription.controller;

import com.ohierro.subscription.model.SubscriptionManager;
import com.ohierro.subscription.model.ports.Subscription;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/newsletter")
@Api()
public class NewsletterController {

    private SubscriptionManager manager;

    NewsletterController(SubscriptionManager manager) {
        this.manager = manager;
    }

    @GetMapping("/{id}/subscription")
    public ResponseEntity findSubscription(@PathVariable String id, @RequestParam(required = false) boolean onlyWithConsent) {
        List<Subscription> subscriptionList = manager.findByNewsletter(id, onlyWithConsent);

        if (subscriptionList.size() == 0) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(subscriptionList, HttpStatus.OK);
    }

}
