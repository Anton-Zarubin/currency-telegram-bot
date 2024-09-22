package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscriber;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SubscriberRepositoryTest {
    private final long CURRENT_TIME = System.currentTimeMillis();

    @Autowired
    private SubscriberRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.merge(new Subscriber(UUID.randomUUID(), 1234567890L, 1_000_200., CURRENT_TIME));
        entityManager.merge(new Subscriber(UUID.randomUUID(), 1234567899L, 1_010_000., 0L));
    }

    @Test
    public void findByUserId() {
        Subscriber subscriber = repository.findByUserId(1234567890L);
        assertNotNull(subscriber);
        assertEquals(1_000_200., subscriber.getSubscriptionValue());
        assertEquals(CURRENT_TIME, subscriber.getLastNotificationTime());
    }

    @Test
    public void updateSubscriptionValue() {
        repository.updateSubscriptionValue(1234567890L, 1_000_220.);
        Subscriber subscriber = repository.findByUserId(1234567890L);
        entityManager.refresh(subscriber);
        assertEquals(1_000_220., subscriber.getSubscriptionValue());
    }

    @Test
    public void findUsersToNotify() {
        List<Subscriber> testNotificationList = repository.findUsersToNotify(1_000_000., CURRENT_TIME);
        assertEquals(1, testNotificationList.size());
        assertEquals(1234567899L, testNotificationList.get(0).getUserId());
    }
}