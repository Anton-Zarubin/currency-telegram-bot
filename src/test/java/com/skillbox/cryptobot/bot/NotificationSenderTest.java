package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class NotificationSenderTest {
    private final CryptoBot bot = mock(CryptoBot.class);
    private final CryptoCurrencyService service =  mock(CryptoCurrencyService.class);
    private final SubscriberRepository repository = mock(SubscriberRepository.class);
    NotificationSender notificationSender = new NotificationSender(bot, service, repository);

    @Test
    @DisplayName("Test sending notifications")
    public void testSendNotifications() throws IOException, TelegramApiException {
        when(service.getBitcoinPrice()).thenReturn(60304.);
        Subscriber subscriber1 = new Subscriber(UUID.randomUUID(), 1234567890L, 60370., 0L);
        Subscriber subscriber2 = new Subscriber(UUID.randomUUID(), 1234567899L, 60400., 0L);
        List<Subscriber> usersToNotify = new ArrayList<>();
        usersToNotify.add(subscriber1);
        usersToNotify.add(subscriber2);
        when(repository.findUsersToNotify(anyDouble() ,anyLong())).thenReturn(usersToNotify);
        notificationSender.sendNotifications();
        verify(bot, times(2)).execute(any(SendMessage.class));
        verify(repository, times(1)).saveAll(usersToNotify);
    }
}