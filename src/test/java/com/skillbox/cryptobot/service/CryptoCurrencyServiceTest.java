package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.client.BinanceClient;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CryptoCurrencyServiceTest {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client = mock(BinanceClient.class);
    CryptoCurrencyService service = new CryptoCurrencyService(client);

    @Test
    @DisplayName("Test for update Bitcoin price")
    public void testUpdateBitcoinPrice() throws IOException {
        when(client.getBitcoinPrice()).thenReturn(60304.);
        assertEquals(60304., service.updateBitcoinPrice());
    }

    @Test
    @DisplayName("Test for update Bitcoin price when Binance Api unavailable and price was null")
    public void testUpdateBitcoinPriceWhenBinanceApiUnavailableAndNullPrice() throws IOException {
        when(client.getBitcoinPrice()).thenThrow(new IOException());
        assertThrows(RuntimeException.class, ()-> service.updateBitcoinPrice());
    }

    @Test
    @DisplayName("Test for update Bitcoin price when Binance Api unavailable and price was not null")
    public void testUpdateBitcoinPriceWhenBinanceApiUnavailableAndNotNullPrice() throws IOException {
        when(client.getBitcoinPrice()).thenReturn(60304.);
        service.updateBitcoinPrice();
        when(client.getBitcoinPrice()).thenThrow(new IOException());
        assertEquals(60304., service.updateBitcoinPrice());
    }
}