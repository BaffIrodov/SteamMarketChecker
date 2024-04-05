package net.smc.common;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
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

//    public Document reliableConnectAndGetDocument(String url) {
//        return reliableConnectAndGetDocument(url, webDriver);
//    }

    public Document reliableConnectAndGetDocument(String url) { //если отваливается сервак или сеть у компа, то делаем повторный запрос
        Document doc = null;
        UserAgent userAgent = new UserAgent();
        for (int i = 0; i < 7; i++) {
            try {
                System.setProperty("http.proxyHost", getRandomProxyHost());

//                Connection.Response wow = Jsoup.connect("https://www.hltv.org/results").userAgent(userAgent.getUserAgentChrome()).execute();
//                Map<String, String> cookies = wow.cookies();
//                Map<String, String> headers = wow.headers();

//                Map<String, String> cookies = new HashMap<>();
//                cookies.put("_ga", "GA1.2.587819420.1598369288");
//                cookies.put("statsTablePadding", "medium");
//                cookies.put("cf_clearance", "2MCKo3oOWGTNmqbp0k5XXG0KDq3sVH3.e8RPt3dh27Q-1660503120-0-150");
//                cookies.put("MatchFilter", "{\"active\":false,\"live\":false,\"stars\":1,\"lan\":false,\"teams\":[]}");
//                cookies.put("CookieConsent", "{stamp:'COZvc8ROe7zINee/aAAoqH5+iqnLfwd3GliXAhbsiQ1NUFAbWs0VjQ==',necessary:true,preferences:true,statistics:true,marketing:true,ver:1,utc:1662408903974,region:'ru'}");
//                cookies.put("__gads", "ID=16018358cb683875-224e4dc244ce0049:T=1654202566:S=ALNI_MZ85XbvXtz9Jt-gFayXKrGWz2MzFA");
//                cookies.put("_au_1d", "AU1D-0100-001666798171-UTBBNS1G-WTKI");
//                cookies.put("_au_last_seen_pixels", "eyJhcG4iOjE2NjY3OTgxNzEsInR0ZCI6MTY2Njc5ODE3MSwicHViIjoxNjY2Nzk4MTcxLCJhZHgiOjE2NjY3OTgxNzEsImdvbyI6MTY2Njc5ODE3MSwicHBudCI6MTY2Njc5ODE3MSwibWVkaWFtYXRoIjoxNjY2Nzk4MTcxLCJzbWFydCI6MTY2Njc5ODE3MSwiYWRvIjoxNjY2Nzk4MTcxLCJvcGVueCI6MTY2Njc5ODE3MSwidW5ydWx5IjoxNjY2ODA2NzcxLCJzb24iOjE2NjY4MDY3NzEsImJlZXMiOjE2NjY4MDY3NzEsInJ1YiI6MTY2NjgwNjc3MSwiaW1wciI6MTY2NjgwNjc3MSwidGFib29sYSI6MTY2NjgwNjc3MX0=; cto_bundle=VxJC2183bjNZQjRiTGIySTYyTWVFcDhPVVFIUlVhbm1NeHZVU2o3bnU2YyUyRjIzd3k4aVU2SXpUZUlHSXUyWnAzSURLNXIxOEJBVWtibGxuTnFmJTJGMlVsTXdiSGljWXhmOHVjZHlBTGVRJTJCYW5uNGFqSmcwOE1XMjQxbExPSnRKY2hSZk13VQ");
//                cookies.put("cto_bidid", "YKIH-V91b3ZFbFpXTE9JTmZFNFlRT2txejdWYmVXZ3BwSWM4RU8yemkxdzZTTXRoZlklMkJxVzlUaXk3d29LTFNzV0d2TXVkU3JsdFZiOXRhdHRZc0FzWGxZdlN3JTNEJTNE");
//                cookies.put("showAdvancedScoreboard", "false");
//                cookies.put("__gpi", "UID=000007126f35e16d:T=1654202566:RT=1674791790:S=ALNI_Mb5UBDi__m_JiXUMT8u52zV_kz4Eg");
//                cookies.put("cf_zaraz_google-analytics_v4_8565", "true");
//                cookies.put("google-analytics_v4_8565__ga4", "1fa38ea3-0807-400c-afb7-0d13959c868c");
//                cookies.put("_lr_geo_location", "RU");
//                cookies.put("google-analytics_v4_8565__ga4sid", "904839035");
//                cookies.put("google-analytics_v4_8565__session_counter", "110");
//                cookies.put("__cf_bm", "saD.7ok8fL2fVDh3fTCPQfXLYprtp1GcyX5Gfqcilh4-1681155294-0-AT7eDiDS1ZMFfdMSdPZ8rbKy4J+0DVl4TKx7yJ6ESqdMqbAm4MBGDTPn/bnDMWMXcYgE9xEEcDeoP9pR4MEfACrYecdLI1MEU7BseRpdcVDgtCHWWxlcRPk1zCgoHVk60opEmv3NpzzVBUAO6AbAvlYdqXmSo7jnIxuihANjA3Z6");
//                cookies.put("outbrain_cid_fetch", "true");
//                cookies.put("google-analytics_v4_8565__engagementPaused", "1681155706393");
//                cookies.put("google-analytics_v4_8565__engagementStart", "1681155706866");
//                cookies.put("google-analytics_v4_8565__counter", "859");
//                cookies.put("google-analytics_v4_8565__let", "1681155706866");

//                cookies.put()


//                driver.get(url);
//                doc = Jsoup.parse(driver.getPageSource());
                String bodyAsString = Jsoup.connect(url).userAgent(userAgent.getUserAgentChrome())
                        .ignoreContentType(true).execute().body();
                Gson gson = new Gson();
                String wow = gson.fromJson(bodyAsString, String.class);
            } catch (RuntimeException | IOException exception) {
                if (i >= 4) {
//                    errorsService.saveError(exception, url);
                }
                System.out.println("IOException в запросе по адресу: " + url);
            }
            if (doc != null && doc.connection().response().statusCode() == 200) {
                break;
            } else {
                System.out.println("Коннект статус код не равен 200");
                waiter(1000 * i + 1); //делаем запросы с увеличивающимся таймаутом. Покрываем 15 секунд (11.06.22)
                if (i >= 4) {
                    System.out.println("Врубаю долгое ожидание");
                    waiter(180 * 1000); //на три минуты передышка, бан должен пройти
                }
            }
        }
        return doc;
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

    public static String standardIdParsingBySlice(String strBeforeId, String processedString) {
        return (processedString.replaceAll(".*" + strBeforeId, "").replaceAll("/.*", ""));
    }

    public static String standardIdParsingByPlace(Integer idPosition, String processedString) {
        String[] splittedString = processedString.split("/");
        return splittedString[idPosition];
    }
}


https://steamcommunity.com/market/priceoverview/?appid=730&currency=3&market_hash_name=StatTrak™%20M4A1-S%20%7C%20Hyper%20Beast%20(Minimal%20Wear)


        http://steamcommunity.com/market/pricehistory/?country=DE&currency=3&appid=440&market_hash_name=Specialized%20Killstreak%20Brass%20Beast


        https://steamcommunity.com/market/listings/730/AWP%20%7C%20Fever%20Dream%20%28Factory%20New%29/render/?query=&start=10&count=10&country=RU&language=russian&currency=5 ---- вот это правда!


        "4860020405071017516": {
        "listingid": "4860020405071017516",
        "price": 130349,
        "fee": 19551,
        "publisher_fee_app": 730,
        "publisher_fee_percent": "0.100000001490116119",
        "currencyid": 2005,
        "steam_fee": 6517,
        "publisher_fee": 13034,
        "converted_price": 130349,
        "converted_fee": 19551,
        "converted_currencyid": 2005,
        "converted_steam_fee": 6517,
        "converted_publisher_fee": 13034,
        "converted_price_per_unit": 130349,
        "converted_fee_per_unit": 19551,
        "converted_steam_fee_per_unit": 6517,
        "converted_publisher_fee_per_unit": 13034,
        "asset": {
        "currency": 0,
        "appid": 730,
        "contextid": "2",
        "id": "36748568048",
        "amount": "1",
        "market_actions": [
        {
        "link": "steam:\/\/rungame\/730\/76561202255233023\/+csgo_econ_action_preview%20M%listingid%A%assetid%D5062758769216870025",
        "name": "Осмотреть в игре…"
        }
        ]
        }
        },

        "4869028237732679807": {
        "listingid": "4869028237732679807",
        "price": 1309,
        "fee": 195,
        "publisher_fee_app": 730,
        "publisher_fee_percent": "0.100000001490116119",
        "currencyid": 2003,
        "steam_fee": 65,
        "publisher_fee": 130,
        "converted_price": 130933,
        "converted_fee": 19639,
        "converted_currencyid": 2005,
        "converted_steam_fee": 6546,
        "converted_publisher_fee": 13093,
        "converted_price_per_unit": 130933,
        "converted_fee_per_unit": 19639,
        "converted_steam_fee_per_unit": 6546,
        "converted_publisher_fee_per_unit": 13093,
        "asset": {
        "currency": 0,
        "appid": 730,
        "contextid": "2",
        "id": "30067991847",
        "amount": "1",
        "market_actions": [
        {
        "link": "steam:\/\/rungame\/730\/76561202255233023\/+csgo_econ_action_preview%20M%listingid%A%assetid%D14018136423453315213",
        "name": "Осмотреть в игре…"
        }
        ]
        }
        },
        "7109567244062181078": {
        "listingid": "7109567244062181078",
        "price": 1300,
        "fee": 195,
        "publisher_fee_app": 730,
        "publisher_fee_percent": "0.100000001490116119",
        "currencyid": 2003,
        "steam_fee": 65,
        "publisher_fee": 130,
        "converted_price": 130033,
        "converted_fee": 19504,
        "converted_currencyid": 2005,
        "converted_steam_fee": 6501,
        "converted_publisher_fee": 13003,
        "converted_price_per_unit": 130033,
        "converted_fee_per_unit": 19504,
        "converted_steam_fee_per_unit": 6501,
        "converted_publisher_fee_per_unit": 13003,
        "asset": {
        "currency": 0,
        "appid": 730,
        "contextid": "2",
        "id": "14896698431",
        "amount": "1",
        "market_actions": [
        {
        "link": "steam:\/\/rungame\/730\/76561202255233023\/+csgo_econ_action_preview%20M%listingid%A%assetid%D7639301691980652205",
        "name": "Осмотреть в игре…"
        }
        ]
        }
        },

