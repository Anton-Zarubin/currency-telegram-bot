package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class GetPriceCommandTest {
    private final CryptoCurrencyService service = mock(CryptoCurrencyService.class);
    GetPriceCommand getPriceCommand = new GetPriceCommand(service);

    @Test
    @DisplayName("Test /get_price command")
    public void testGetPriceCommandProcessing() throws IOException {
        long chatId = 1234567890L;
        Message message = mock(Message.class);
        AbsSender absSender = mock(AbsSender.class);
        when(message.getChatId()).thenReturn(chatId);
        getPriceCommand.processMessage(absSender, message, null);
        verify(service, times(1)).getBitcoinPrice();
    }
}