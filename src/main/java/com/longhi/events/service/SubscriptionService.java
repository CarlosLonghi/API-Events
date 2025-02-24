package com.longhi.events.service;

import com.longhi.events.dto.SubscriptionResponse;
import com.longhi.events.exception.EventNotFoundException;
import com.longhi.events.exception.SubscriptionConflictException;
import com.longhi.events.exception.UserIndicatorNotFoundException;
import com.longhi.events.model.Event;
import com.longhi.events.model.Subscription;
import com.longhi.events.model.User;
import com.longhi.events.repository.EventRepository;
import com.longhi.events.repository.SubscriptionRepository;
import com.longhi.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createNewSubscription(String eventPrettyName, User user, Integer userIndicatorId) {
        // Recuperando evento pelo pretty name
        Event event = eventRepository.findByPrettyName(eventPrettyName);
        if (event == null) { // Caso o eventPrettyName estiver incorreto
            throw new EventNotFoundException("O evento '" + eventPrettyName + "' não existe");
        }
        User userRecovered = userRepository.findByEmail(user.getEmail());
        if (userRecovered == null) { // Caso for o primeiro evento do usuario
            userRecovered = userRepository.save(user);
        }

        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userRecovered);

        if (userIndicatorId != null) {
            User userIndicator = userRepository.findById(userIndicatorId).orElse(null);
            if (userIndicator == null) {
                throw new UserIndicatorNotFoundException("O usuário indicador não existe");
            }
            subscription.setIndication(userIndicator);
        }

        Subscription tempSubscription = subscriptionRepository.findByEventAndSubscriber(event, userRecovered);
        if (tempSubscription != null) { // Caso o usuario já estiver inscrito no evento
            throw new SubscriptionConflictException("Já existe a inscrição do email '" + userRecovered.getEmail() + "' para o evento '" + event.getTitle() + "'");
        }

        Subscription response = subscriptionRepository.save(subscription);
        return new SubscriptionResponse(response.getSubscriptionNumber(), "http://codecraft.com/subscription/" + response.getEvent().getPrettyName() + "/" + response.getSubscriber().getId());
    }
}
