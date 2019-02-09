package com.ohierro.subscription.controller;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
//@Builder
@ApiModel(description = "Error detailed information")
public class ErrorResponse {
    String message;
}
