package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.mockito.Mockito.*;

public class UnsubscribeCommandTest {
    private final SubscriberRepository repository = mock(SubscriberRepository.class);
    UnsubscribeCommand unsubscribeCommand = new UnsubscribeCommand(repository);

    Message message = mock(Message.class);
    AbsSender absSender = mock(AbsSender.class);

    @Test
    @DisplayName("Test /unsubscribe command")
    public void testUnsubscribeCommandProcessing() {
        long chatId = 1234567890L;
        when(message.getChatId()).thenReturn(chatId);
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(chatId);
        subscriber.setSubscriptionValue(60000.);
        when(repository.findByUserId(chatId)).thenReturn(subscriber);
        unsubscribeCommand.processMessage(absSender, message, null);
        verify(repository, times(1)).findByUserId(chatId);
        verify(repository, times(1)).updateSubscriptionValue(chatId,null);
    }

    @Test
    @DisplayName("Test /unsubscribe command if no active subscription")
    public void testUnsubscribeCommandProcessingIfNoSubscription() {
        long chatId = 1234567890L;
        when(message.getChatId()).thenReturn(chatId);
        Subscriber subscriber = new Subscriber();
        subscriber.setUserId(chatId);
        when(repository.findByUserId(chatId)).thenReturn(subscriber);
        unsubscribeCommand.processMessage(absSender, message, null);
        verify(repository, times(1)).findByUserId(chatId);
        verify(repository, never()).updateSubscriptionValue(chatId,null);
    }
}