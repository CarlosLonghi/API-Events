package com.longhi.events.repository;

import com.longhi.events.dto.SubscriptionRanking;
import com.longhi.events.model.Event;
import com.longhi.events.model.Subscription;
import com.longhi.events.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);

    @Query(value = "SELECT count(subscription_number) AS quantidade, indication_user_id, user_name " +
            "FROM db_events.tbl_subscription INNER JOIN db_events.tbl_user " +
            "ON tbl_subscription.indication_user_id = tbl_user.user_id " +
            "WHERE indication_user_id IS NOT NULL " +
            "AND event_id = :eventId " +
            "GROUP BY indication_user_id " +
            "ORDER BY quantidade DESC", nativeQuery = true)
    public List<SubscriptionRanking> generateRanking(@Param("eventId") Integer eventId);
}
