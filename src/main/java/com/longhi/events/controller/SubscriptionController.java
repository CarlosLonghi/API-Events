package com.longhi.events.controller;

import com.longhi.events.dto.SubscriptionResponse;
import com.longhi.events.exception.EventNotFoundException;
import com.longhi.events.dto.ErrorMessage;
import com.longhi.events.exception.SubscriptionConflictException;
import com.longhi.events.exception.UserIndicatorNotFoundException;
import com.longhi.events.model.User;
import com.longhi.events.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({"/api/v1/subscription/{prettyName}", "/api/v1/subscription/{prettyName}/{userIndicatorId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName,
                                                @RequestBody User subscriber,
                                                @PathVariable(required = false) Integer userIndicatorId
    ) {
        try {
            SubscriptionResponse response = subscriptionService.createNewSubscription(prettyName, subscriber, userIndicatorId);
            if (response != null) {
                return ResponseEntity.ok(response);
            }
        } catch (EventNotFoundException | UserIndicatorNotFoundException exception) {
            return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
        } catch (SubscriptionConflictException exception) {
            return ResponseEntity.status(409).body(new ErrorMessage(exception.getMessage()));
        }
        return ResponseEntity.badRequest().build();
    }
}
