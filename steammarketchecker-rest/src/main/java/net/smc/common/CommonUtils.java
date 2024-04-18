package net.smc.common;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CommonUtils {

    public void waiter(int timeoutInMS) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeoutInMS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public String defaultStringConverter(String unconvertedString) {
        return unconvertedString.replaceAll("™", "%E2%84%A2").replaceAll(" ", "%20").replaceAll("\\|", "%7C")
                .replaceAll("\\(", "%28").replaceAll("\\)", "%29");
    }

    public String connectAndGetJsonAsString(String url) { //если отваливается сервак или сеть у компа, то делаем повторный запрос
        UserAgent userAgent = new UserAgent();
        String result = "";
        try {
            result = Jsoup.connect(url).userAgent(userAgent.getUserAgentChrome())
                    .ignoreContentType(true).execute().body();
        } catch (IOException exception) {
            System.out.println("IOException в запросе по адресу: " + url);
        }
        return result;
    }

    private static String getRandomProxyHost() {
        Random random = new Random();
        List<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < 4; i++) randomNumbers.add(random.nextInt(1, 255));
        List<String> randomNumbersToString = new ArrayList<>();
        for (Integer number : randomNumbers) {
            randomNumbersToString.add(number.toString());
        }
        return String.join(".", randomNumbersToString);
    }

    private static String getRandomProxyPort() {
        Random random = new Random();
        Integer res = random.nextInt(1000, 2000);
        return res.toString();
    }
}