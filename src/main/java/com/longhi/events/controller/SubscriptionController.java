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
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({"/api/v1/subscription/{eventPrettyName}", "/api/v1/subscription/{eventPrettyName}/{userIndicatorId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String eventPrettyName,
                                                @RequestBody User subscriber,
                                                @PathVariable(required = false) Integer userIndicatorId
    ) {
        try {
            SubscriptionResponse response = subscriptionService.createNewSubscription(eventPrettyName, subscriber, userIndicatorId);
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

    @GetMapping("/api/v1/subscription/{eventPrettyName}/ranking")
    public ResponseEntity<?> generateRankingByEvent(@PathVariable String eventPrettyName) {
        try {
            return ResponseEntity.ok(subscriptionService.getCompleteRanking(eventPrettyName).subList(0, 3));
        } catch (EventNotFoundException exception) {
            return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
        }
    }

    @GetMapping("/api/v1/subscription/{eventPrettyName}/ranking/{userIndicatorId}")
    public ResponseEntity<?> generateRankingByEventAndUser(@PathVariable String eventPrettyName,
                                                           @PathVariable Integer userIndicatorId
    ) {
        try {
            return ResponseEntity.ok(subscriptionService.getRankingByUser(eventPrettyName, userIndicatorId));
        } catch (Exception exception) {
            return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
        }
    }

}
