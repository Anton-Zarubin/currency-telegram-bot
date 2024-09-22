package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscriber;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    @Query(value = "SELECT s FROM Subscriber s WHERE s.userId = :userId")
    Subscriber findByUserId(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE subscribers SET subscription_value = ?2 WHERE telegram_user_id = ?1", nativeQuery = true)
    void updateSubscriptionValue(Long userId, Double subscriptionValue);
}