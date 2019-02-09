package com.ohierro.subscription.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Response to new subscription request. Contains ID of the created request")
public class SubscriptionResponse {
    @ApiModelProperty(value = "Identifier of created subscription")
    String id;
}
