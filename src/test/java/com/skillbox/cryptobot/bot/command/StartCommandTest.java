package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import org.junit.jupiter.api.*;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StartCommandTest {
    private final SubscriberRepository repository = mock(SubscriberRepository.class);
    StartCommand startCommand = new StartCommand(repository);

    @Test
    @DisplayName("Test /start command")
    public void testStartCommandProcessing() {
        long chatId = 1234567890L;
        Message message = mock(Message.class);
        AbsSender absSender = mock(AbsSender.class);
        when(message.getChatId()).thenReturn(chatId);
        when(repository.findByUserId(chatId)).thenReturn(null);
        startCommand.processMessage(absSender, message, null);
        verify(repository, times(1)).findByUserId(chatId);
        verify(repository, times(1)).save(any(Subscriber.class));
    }
}