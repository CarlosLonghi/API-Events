package com.longhi.events.repository;

import com.longhi.events.model.Event;
import com.longhi.events.model.Subscription;
import com.longhi.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);
}
