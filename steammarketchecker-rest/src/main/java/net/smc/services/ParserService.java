package net.smc.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.smc.common.CommonUtils;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParserService {
    private final CommonUtils commonUtils;

    @PostConstruct
    public void getAndSaveListings() {
        Document document = commonUtils.reliableConnectAndGetDocument(
                "https://steamcommunity.com/market/listings/730/AWP%20%7C%20Fever%20Dream%20%28Factory%20New%29/render/?query=&start=10&count=10&country=RU&language=russian&currency=5"
        );
        int i = 0;
    }

}
