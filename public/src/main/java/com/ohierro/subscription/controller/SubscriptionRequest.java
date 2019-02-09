package com.ohierro.subscription.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


@ApiModel(description = "Data needed to create a new subscription to a newsletter")
@Data
class SubscriptionRequest {

    @ApiModelProperty(value = "Email", required = true)
    String email;

    @ApiModelProperty(value = "First name")
    String firstName;

    @ApiModelProperty(value = "Gender", allowableValues = "male, female")
    String gender;

    @ApiModelProperty(value = "Date of birth in dd/mm/yyyy format", required = true)
    String dateOfBirth;

    @ApiModelProperty(value = "Consent", required = true)
    Boolean consent;

    @ApiModelProperty(value = "ID of the newsletter campaign", required = true)
    String newsletterId;
}
