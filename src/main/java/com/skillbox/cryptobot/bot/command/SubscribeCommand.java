package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {
    private final CryptoCurrencyService service;
    private final SubscriberRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        Double subscriptionValue = getSubscriptionValue(arguments);

        if (subscriptionValue != null) {
            repository.updateSubscriptionValue(message.getChatId(), subscriptionValue);
            try {
                answer.setText("Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD\n" +
                        "Новая подписка создана на стоимость " + subscriptionValue + " USD");
            } catch (Exception e) {
                log.error("Error occurred while executing getBitcoinPrice method", e);
            }
        }else {
            answer.setText("Некорректный формат аргумента. Проверьте указанную стоимость.");
        }
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }

    private Double getSubscriptionValue(String[] arguments) {
        if (arguments.length != 1) {
            return null;
        }
        Optional<String> subscriptionArgument = Optional.ofNullable(arguments[0]);
        if (subscriptionArgument.isPresent()) {
            try {
                return Double.parseDouble(arguments[0]);
            } catch (Exception e) {
                log.error("Invalid argument format", e);
                return null;
            }
        }
        return null;
    }
}