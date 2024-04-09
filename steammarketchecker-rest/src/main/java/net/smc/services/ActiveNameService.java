package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.dto.ActiveNameDto;
import net.smc.entities.ActiveName;
import net.smc.readers.ActiveNameReader;
import net.smc.repositories.ActiveNameRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActiveNameService {

    private final ActiveNameReader activeNameReader;
    private final ActiveNameRepository activeNameRepository;

    @Scheduled(fixedDelay = 1000)
    public void parseActiveNamesByPeriod() {
        List<ActiveName> allActiveNames = activeNameRepository.findAll();
        for (ActiveName activeName : allActiveNames) {
            long now = Instant.now().getEpochSecond();
            long parseDate = Optional.ofNullable(activeName.getLastParseDate()).orElse(Instant.MIN).getEpochSecond();
            if (now - parseDate > activeName.getParsePeriod() || activeName.isForceUpdate()) {
                activeName.setLastParseDate(Instant.now());
                activeName.setForceUpdate(false);
            }
        }
        activeNameRepository.saveAll(allActiveNames);
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
