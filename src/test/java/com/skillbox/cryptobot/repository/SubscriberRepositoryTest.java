package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscriber;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubscriberRepositoryTest {
    private final long CURRENT_TIME = System.currentTimeMillis();

    private Subscriber subscriber1;
    private Subscriber subscriber2;

    @Autowired
    private SubscriberRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        subscriber1 = new Subscriber(UUID.randomUUID(), 1234567890L, 1_000_200., CURRENT_TIME);
        subscriber2 = new Subscriber(UUID.randomUUID(), 1234567899L, 1_010_000., 0L);
        repository.save(subscriber1);
        repository.save(subscriber2);
    }

    @AfterEach
    public void tearDown() {
        repository.delete(subscriber1);
        repository.delete(subscriber2);
    }

    @Test
    @DisplayName("Test findByUserId method")
    public void testFindByUserId() {
        Subscriber subscriber = repository.findByUserId(1234567890L);
        assertNotNull(subscriber);
        assertEquals(1_000_200., subscriber.getSubscriptionValue());
        assertEquals(CURRENT_TIME, subscriber.getLastNotificationTime());
    }

    @Test
    @DisplayName("Test updateSubscriptionValue method")
    public void testUpdateSubscriptionValue() {
        repository.updateSubscriptionValue(1234567890L, 1_000_220.);
        Double subscriptionValue = jdbcTemplate.queryForObject("SELECT subscription_value FROM subscribers WHERE telegram_user_id = ?",
                Double.class, 1234567890L);
        assertEquals(1_000_220., subscriptionValue);
    }

    @Test
    @DisplayName("Test findUsersToNotify method")
    public void testFindUsersToNotify() {
        List<Subscriber> testNotificationList = repository.findUsersToNotify(1_000_000., CURRENT_TIME);
        assertEquals(1, testNotificationList.size());
        assertEquals(1234567899L, testNotificationList.get(0).getUserId());
    }
}