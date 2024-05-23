package net.smc;

import net.smc.services.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableScheduling
public class SteamMarketCheckerApplication {

    public static void main(String[] args) throws TelegramApiException {
        SpringApplication.run(SteamMarketCheckerApplication.class, args);
        if (false) { //todo заглушка для отключения регистрации бота, убрать при наличии актуального токена
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBotService());
        }
    }
}
