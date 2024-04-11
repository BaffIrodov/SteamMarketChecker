package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.common.CommonUtils;
import net.smc.dto.ActiveNameDto;
import net.smc.dto.ParseQueueDto;
import net.smc.entities.ActiveName;
import net.smc.entities.ParseQueue;
import net.smc.enums.ParseType;
import net.smc.readers.ActiveNameReader;
import net.smc.readers.ParseQueueReader;
import net.smc.repositories.ActiveNameRepository;
import net.smc.repositories.ParseQueueRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActiveNameService {

    private final ActiveNameReader activeNameReader;
    private final ActiveNameRepository activeNameRepository;
    private final ParseQueueRepository parseQueueRepository;
    private final ParseQueueReader parseQueueReader;
    private final CommonUtils commonUtils;

    @Scheduled(fixedDelayString = "${scheduled.active-name}", initialDelay = 1000)
    public void parseActiveNamesByPeriod() {
        List<ActiveName> allActiveNames = activeNameRepository.findAll();
        List<ActiveName> allOutdatedActiveNames = new ArrayList<>();
        for (ActiveName activeName : allActiveNames) {
            long now = Instant.now().getEpochSecond();
            long parseDate = Optional.ofNullable(activeName.getLastParseDate()).orElse(Instant.MIN).getEpochSecond();
            if (now - parseDate > activeName.getParsePeriod() || activeName.isForceUpdate()) {
                allOutdatedActiveNames.add(activeName);
            }
        }
        if (allOutdatedActiveNames.size() > 0) {
            List<ParseQueue> parseQueueList = new ArrayList<>();
            Map<String, ParseQueueDto> mapParseQueueByParseTarget = parseQueueReader.getMapQueueByTarget(
                    allOutdatedActiveNames.stream().map(ActiveName::getItemName).toList(), false);
            for (ActiveName activeName : allOutdatedActiveNames) {
                activeName.setLastParseDate(Instant.now());
                activeName.setForceUpdate(false);
                if (mapParseQueueByParseTarget.get(activeName.getItemName()) == null) {
                    parseQueueList.add(new ParseQueue(0, ParseType.SKIN, activeName.getItemName(),
                            activeName.getParseItemCount(), commonUtils));
                }
            }
            activeNameRepository.saveAll(allActiveNames);
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
