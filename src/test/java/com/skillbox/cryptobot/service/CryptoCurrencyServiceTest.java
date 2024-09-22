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
    public void updateBitcoinPrice() throws IOException {
        when(client.getBitcoinPrice()).thenReturn(60304.);
        assertEquals(60304., service.updateBitcoinPrice());
    }

    @Test
    public void updateBitcoinPrice_whenBinanceApiUnavailableAndNullPrice() throws IOException {
        when(client.getBitcoinPrice()).thenThrow(new IOException());
        assertThrows(RuntimeException.class, ()-> service.updateBitcoinPrice());
    }

    @Test
    public void updateBitcoinPrice_whenBinanceApiUnavailableAndNotNullPrice() throws IOException {
        when(client.getBitcoinPrice()).thenReturn(60304.);
        service.updateBitcoinPrice();
        when(client.getBitcoinPrice()).thenThrow(new IOException());
        assertEquals(60304., service.updateBitcoinPrice());
    }
}