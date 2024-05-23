package net.smc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram-enabled}")
    private Boolean telegramEnabled;

    private String botToken = "-1";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getBotUsername() {
        return "TestBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // literally nothing
    }

    public void sendMsg(String text) {
        if (telegramEnabled) {
            text = text.replaceAll("\\|", "").replaceAll("_lot", "");
            Long chatId = -1L; // Ввести id юзера в телеграме
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(chatId.toString()); // Определение в какой чат отправить ответ
            // Можно сделать определение, на какое сообщение отвечать
            sendMessage.setText(text);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
