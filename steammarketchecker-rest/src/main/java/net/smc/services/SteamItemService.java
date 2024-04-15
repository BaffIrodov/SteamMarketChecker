package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.common.CommonUtils;
import net.smc.dto.ParseQueueDto;
import net.smc.dto.SteamItemDto;
import net.smc.entities.ParseQueue;
import net.smc.entities.SteamItem;
import net.smc.enums.ParseType;
import net.smc.enums.SteamItemType;
import net.smc.readers.ParseQueueReader;
import net.smc.readers.SteamItemReader;
import net.smc.repositories.LotRepository;
import net.smc.repositories.ParseQueueRepository;
import net.smc.repositories.SteamItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SteamItemService {

    // todo тут нужен свой шедулер для обновления
    private final SteamItemReader steamItemReader;
    private final SteamItemRepository steamItemRepository;
    private final LotRepository lotRepository;
    private final ParseQueueRepository parseQueueRepository;
    private final ParseQueueReader parseQueueReader;
    private final CommonUtils commonUtils;

    @Scheduled(fixedDelayString = "${scheduled.steam-item}", initialDelay = 1000)
    public void parseActualSteamItemsByPeriod() {
        List<SteamItem> allActualSteamItems = steamItemRepository.findAll(); // все steamItem должны быть актуальными
        List<SteamItem> allOutdatedSteamItems = new ArrayList<>();
        // Определяем, какие steamItems требуют обновления из-за просрочки или требования пользователя
        for (SteamItem steamItem : allActualSteamItems) {
            long now = Instant.now().getEpochSecond();
            long parseDate = Optional.ofNullable(steamItem.getParseDate()).orElse(Instant.MIN).getEpochSecond();
            if (now - parseDate > steamItem.getParsePeriod() || steamItem.isForceUpdate()) {
                allOutdatedSteamItems.add(steamItem);
            }
        }
        // Для всех steamItems, которые требуют обновления, заводим задачу в очереди.
        // И исправляем данные, из-за которых они outdated
        if (allOutdatedSteamItems.size() > 0) {
            List<ParseQueue> parseQueueList = new ArrayList<>();
            Map<String, ParseQueueDto> mapParseQueueByParseTargetForSkin = parseQueueReader.getMapQueueByTarget(
                    allOutdatedSteamItems.stream().map(e -> e.getName()).toList(), false);
            for (SteamItem steamItem : allOutdatedSteamItems) {
                // Завели задачу в очереди на скин
                // (задача заведется, если задачи нет в очереди)
                if (mapParseQueueByParseTargetForSkin.get(steamItem.getName()) == null) {
                    steamItem.processOutdatedSteamItem(); // Исправили данные
                    parseQueueList.add(new ParseQueue(0, ParseType.valueOf(steamItem.getSteamItemType().toString()),
                            steamItem.getName(), 0, null, commonUtils));
                }
            }
            steamItemRepository.saveAll(allOutdatedSteamItems);
            parseQueueRepository.saveAll(parseQueueList);
        }
    }


    public List<SteamItemDto> getAllSteamItems(SteamItemType steamItemType) {
        return steamItemReader.getAllSteamItems(steamItemType);
    }

    @Transactional
    public void forceUpdateSteamItem(@PathVariable Long id) {
        SteamItem steamItem = this.steamItemRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Стим позиция не найдена!");
                }
        );
        steamItem.forceUpdate();
    }
}
