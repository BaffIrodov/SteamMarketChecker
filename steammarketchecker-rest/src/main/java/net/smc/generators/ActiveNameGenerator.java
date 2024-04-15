package net.smc.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.smc.entities.ActiveName;
import net.smc.repositories.ActiveNameRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ActiveNameGenerator {

    @Value("${default-parse-period.active-name}")
    private Integer defaultActiveNameParsePeriod;

    private final ActiveNameRepository activeNameRepository;

    private final List<String> exteriorList = List.of(
            "(Factory New)",
            "(Minimal Wear)",
            "(Field-Tested)",
            "(Well-Worn)",
            "(Battle-Scarred)"
    );

    private final List<String> activeNames = List.of(
//            "FAMAS | Pulse (Factory New)",
//            "FAMAS | Pulse (Minimal Wear)",
//            "FAMAS | Pulse (Field-Tested)",
//            "FAMAS | Pulse (Well-Worn)"
            "AWP | Electric Hive (Field-Tested)"
            );

    @Value("${generators.real-active-name}")
    private Boolean generatorRealActiveNameEnable;

    @PostConstruct
    public void generateData() {
        generateRealActiveNames();
    }

    public void generateRealActiveNames() {
        if (generatorRealActiveNameEnable) {
            activeNameRepository.deleteAll();
            log.warn("ActiveName tables (real data) filled");
            for (String activeName : activeNames) {
                activeNameRepository.save(
                        new ActiveName(activeName, 10, defaultActiveNameParsePeriod)
                );
            }
        }
    }
}
