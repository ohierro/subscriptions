package com.ohierro.subscription.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/subscription")
@Api(value = "/subscription", description = "API to subscribe to a specific newsletter")
@Slf4j
public class SubscriptionController {

    @Value("${app.subscription.url}")
    private String SUBCRIPTION_SERVICE_URL;

    @PostMapping("/")
    @ApiOperation(value = "Subscribe to specific newsletter")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Subscription is created correctly", response = SubscriptionResponse.class),
            @ApiResponse(code = 400, message = "Subscription is not correctly send. Some mandatory fields are missing", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse.class)
    })
    public ResponseEntity subcribe(@RequestBody SubscriptionRequest subscription) throws IOException {
        RestTemplate rest = new RestTemplate();

        try {
            SubscriptionResponse response = rest.postForObject(SUBCRIPTION_SERVICE_URL, subscription, SubscriptionResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HttpClientErrorException.BadRequest e) {
            ObjectMapper mapper = new ObjectMapper();

            ErrorResponse result = mapper.readValue(e.getResponseBodyAsString(),
                    ErrorResponse.class);

            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (RestClientException e) {
            log.error("Error calling subscription service", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

