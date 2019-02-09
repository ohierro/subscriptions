package com.ohierro.subscription.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
class SubscriptionRequest {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @ApiModelProperty(value = "Email", required = true)
    String email;

    @ApiModelProperty(value = "First name")
    String firstName;

    @ApiModelProperty(value = "Gender", allowableValues = "[male, female]")
    String gender;

    @ApiModelProperty(value = "Date of birth in dd/mm/yyyy format", required = true)
    String dateOfBirth;

    @ApiModelProperty(value = "Consent", required = true)
    Boolean consent;

    @ApiModelProperty(value = "ID of the newsletter campaign", required = true)
    String newsletterId;

    public void validate() {
        try {
            LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("date should be in dd/mm/yyyy format");
        }


        checkNullOrEmpty("email", email);
        checkNullOrEmpty("date of birth", dateOfBirth);
        checkNullOrEmpty("newsletter ID", newsletterId);

        if (gender != null) {
            if (!"male".equals(gender) && !"female".equals(gender)) {
                throw new IllegalArgumentException("gender only can take value 'male' or 'female'");
            }
        }

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);

        if (!matcher.find()) {
            throw new IllegalArgumentException("invalid email");
        }
    }

    private void checkNullOrEmpty(String field, String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(field + " is a mandatory field");
        }
    }
}
