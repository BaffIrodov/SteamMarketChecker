package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.common.CommonUtils;
import net.smc.dto.ActiveNameDto;
import net.smc.dto.ParseQueueDto;
import net.smc.entities.ActiveName;
import net.smc.entities.ParseQueue;
import net.smc.entities.SteamItem;
import net.smc.enums.ParseType;
import net.smc.readers.ActiveNameReader;
import net.smc.readers.ParseQueueReader;
import net.smc.readers.SteamItemReader;
import net.smc.repositories.ActiveNameRepository;
import net.smc.repositories.ParseQueueRepository;
import net.smc.repositories.SteamItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ActiveNameService {

    private final ActiveNameReader activeNameReader;
    private final ActiveNameRepository activeNameRepository;
    private final ParseQueueRepository parseQueueRepository;
    private final SteamItemRepository steamItemRepository;
    private final ParseQueueReader parseQueueReader;
    private final SteamItemReader steamItemReader;
    private final CommonUtils commonUtils;

    @Scheduled(fixedDelayString = "${scheduled.active-name}", initialDelay = 1000)
    public void parseActualActiveNamesByPeriod() {
        List<ActiveName> allActualActiveNames = activeNameRepository.findAllByArchive(false);
        List<ActiveName> allOutdatedActiveNames = new ArrayList<>();
        // Определяем, какие activeNames требуют обновления из-за просрочки или требования пользователя
        for (ActiveName activeName : allActualActiveNames) {
            long now = Instant.now().getEpochSecond();
            long parseDate = Optional.ofNullable(activeName.getLastParseDate()).orElse(Instant.MIN).getEpochSecond();
            if (now - parseDate > activeName.getParsePeriod() || activeName.isForceUpdate()) {
                allOutdatedActiveNames.add(activeName);
            }
        }
        // Для всех activeNames, которые требуют обновления, заводим задачу в очереди. И исправляем данные, из-за которых они outdated
        if (allOutdatedActiveNames.size() > 0) {
            List<String> allOutdatedSteamItemNames = allOutdatedActiveNames.stream().map(e -> e.getItemName() + "_skin").toList();
            List<ParseQueue> parseQueueList = new ArrayList<>();
            // Ищем все задачи в очереди на это наименование
            Map<String, ParseQueueDto> mapParseQueueByParseTargetForSkin = parseQueueReader.getMapQueueByTarget(
                    allOutdatedSteamItemNames, false);
            // Ищем все steamItem, уже существующие в базе, на это наименование
            // (если steamItem уже существует, то мы его обновляем по собственному шедулеру, а не по этому)
            List<String> steamItemNamesThatAlreadyExists = steamItemReader.getAllSteamItemNamesByNameList(allOutdatedSteamItemNames);

            Map<String, ParseQueueDto> mapParseQueueByParseTargetForLot = parseQueueReader.getMapQueueByTarget(
                    allOutdatedActiveNames.stream().map(e -> e.getItemName() + "_lot").toList(), false);
            for (ActiveName activeName : allOutdatedActiveNames) {
                // Завели задачу в очереди на скин
                // (задача заведется только если скина не существовало в базе и если задачи уже нет в очереди)
                if (mapParseQueueByParseTargetForSkin.get(activeName.getItemName() + "_skin") == null
                        && !steamItemNamesThatAlreadyExists.contains(activeName.getItemName() + "_skin")) {
                    activeName.processOutdatedActiveName(); // Исправили данные
                    parseQueueList.add(new ParseQueue(0, ParseType.SKIN, activeName.getItemName() + "_skin",
                            activeName.getParseItemCount(), commonUtils));
                }
                // Завели задачу в очереди на лот
                // (задача заведется, если задачи нет в очереди)
//                if (mapParseQueueByParseTargetForLot.get(activeName.getItemName() + "_lot") == null) {
//                    activeName.processOutdatedActiveName(); // Исправили данные
//                    parseQueueList.add(new ParseQueue(0, ParseType.LOT, activeName.getItemName() + "_lot",
//                            activeName.getParseItemCount(), commonUtils));
//                }
            }
            // todo и то же самое для лотов
            activeNameRepository.saveAll(allOutdatedActiveNames);
            parseQueueRepository.saveAll(parseQueueList);
        }
    }

    public List<ActiveNameDto> getAllActiveNames(boolean showArchive) {
        return activeNameReader.getAllActiveNames(showArchive);
    }

    @Transactional
    public List<ActiveNameDto> createActiveName(@RequestBody ActiveNameDto activeNameDto) {
        ActiveName activeName = new ActiveName(activeNameDto);
        activeName = this.activeNameRepository.save(activeName);
        return activeNameReader.getActiveNameByIds(List.of(activeName.getId()));
    }

    @Transactional
    public List<ActiveNameDto> updateActiveName(@PathVariable Long id, @RequestBody ActiveNameDto activeNameDto) {
        ActiveName activeName = this.activeNameRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("ActiveName не найден!");
                }
        );
        activeName.update(activeNameDto);
        return activeNameReader.getActiveNameByIds(List.of(activeName.getId()));
    }

    @Transactional
    public void archiveActiveName(@PathVariable Long id) {
        ActiveName activeName = this.activeNameRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Активная парсинг позиция не найдена!");
                }
        );
        activeName.archiveOrUnarchive();
    }

    @Transactional
    public void forceUpdateActiveName(@PathVariable Long id) {
        ActiveName activeName = this.activeNameRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Активная парсинг позиция не найдена!");
                }
        );
        activeName.forceUpdate();
    }
}
