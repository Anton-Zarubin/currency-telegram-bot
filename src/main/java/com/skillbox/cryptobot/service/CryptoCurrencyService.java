package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.client.BinanceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class CryptoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;

    public CryptoCurrencyService(BinanceClient client) {
        this.client = client;
    }

    public double getBitcoinPrice() throws IOException {
        if (price.get() == null) {
            price.set(client.getBitcoinPrice());
        }
        return price.get();
    }

    public double updateBitcoinPrice () {
        try {
            price.set(client.getBitcoinPrice());
        } catch (IOException e) {
            log.error("Binance API unavailable");
            if (price.get() == null) {
                price.set(0.0);
                log.error("Bitcoin price unknown and is set to $0.0");
                throw new RuntimeException();
            } else {
                log.error("Bitcoin price was not updated, the value was left at {} $", price.get());
            }
        }
        return price.get();
    }
}
