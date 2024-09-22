package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationSender {
    private final CryptoBot bot;
    private final CryptoCurrencyService service;
    private final SubscriberRepository repository;
    @Value("${time-values.notification-frequency-in-millis}")
    long notificationDelay;

    @Scheduled(fixedDelayString = "${time-values.course-update-frequency-in-minutes}", timeUnit = TimeUnit.MINUTES)
    public void sendNotifications() {
        double price = service.updateBitcoinPrice();
        log.info("Bitcoin price received: {}", price);
        long currentTime = System.currentTimeMillis();

        List<Subscriber> usersToNotify = repository.findUsersToNotify(price,
                (currentTime - notificationDelay));
        usersToNotify.forEach(u -> {
            try {
                bot.execute(new SendMessage(String.valueOf(u.getUserId()),
                        "Пора покупать. Стоимость биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD"));
            } catch (Exception e) {
                log.error("Error occurred during notification", e);
            }
            u.setLastNotificationTime(currentTime);
        });
        repository.saveAll(usersToNotify);
    }
}