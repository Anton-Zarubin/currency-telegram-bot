package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.mockito.Mockito.*;

public class GetSubscriptionCommandTest {
    private final SubscriberRepository repository = mock(SubscriberRepository.class);
    GetSubscriptionCommand getSubscriptionCommand = new GetSubscriptionCommand(repository);

    @Test
    @DisplayName("Test /get_subscription command")
    public void testGetSubscriptionCommandProcessing() {
        long chatId = 1234567890L;
        Message message = mock(Message.class);
        AbsSender absSender = mock(AbsSender.class);
        when(message.getChatId()).thenReturn(chatId);
        Subscriber subscriber = mock(Subscriber.class);
        when(repository.findByUserId(chatId)).thenReturn(subscriber);
        getSubscriptionCommand.processMessage(absSender, message, null);
        verify(repository, times(1)).findByUserId(chatId);
        verify(subscriber, times(1)).getSubscriptionValue();
    }
}