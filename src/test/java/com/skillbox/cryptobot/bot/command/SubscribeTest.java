package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class SubscribeTest {
    private final SubscriberRepository repository = mock(SubscriberRepository.class);
    private final CryptoCurrencyService service = mock(CryptoCurrencyService.class);
    SubscribeCommand subscribeCommand = new SubscribeCommand(service, repository);

    Message message = mock(Message.class);
    AbsSender absSender = mock(AbsSender.class);

    @Test
    @DisplayName("Test /subscribe command with correct argument")
    public void testSubscribeCommandProcessing() throws IOException {
        long chatId = 1234567890L;
        String[] args = {"60000."};
        when(message.getChatId()).thenReturn(chatId);
        subscribeCommand.processMessage(absSender, message, args);
        verify(repository, times(1)).updateSubscriptionValue(chatId ,Double.parseDouble(args[0]));
        verify(service,times(1)).getBitcoinPrice();
    }

    @Test
    @DisplayName("Test /subscribe command with invalid argument")
    public void testSubscribeCommandProcessingWhenInvalidArgument() throws IOException {
        long chatId = 1234567890L;
        String[] args = {"abc"};
        when(message.getChatId()).thenReturn(chatId);
        subscribeCommand.processMessage(absSender, message, args);
        verify(repository, never()).updateSubscriptionValue(anyLong() ,anyDouble());
        verify(service,never()).getBitcoinPrice();
    }
}