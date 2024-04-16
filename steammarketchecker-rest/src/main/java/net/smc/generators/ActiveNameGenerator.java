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
//            "AWP | Electric Hive (Field-Tested)"


            // это дорогие, их нужно по десятке фигачить
//            "AWP | Electric Hive (Factory New)",
//            "Desert Eagle | Oxide Blaze (Factory New)",
//            "AWP | Fever Dream (Factory New)",
//            "AWP | Electric Hive (Well-Worn)",
//            "M4A4 | X-Ray (Minimal Wear)",
//            "AWP | Corticera (Factory New)",
//            "AK-47 | Blue Laminate (Minimal Wear)",
//            "AWP | Electric Hive (Field-Tested)"


            // это дешевые, можно по 50
//            "AK-47 | Slate (Field-Tested)"

            );

    private final List<String> activeNamesWithoutExterior = List.of(

            // это дешевые, можно по 50
            "AK-47 | Slate"
//            "SG 553 | Dragon Tech"
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
                        new ActiveName(activeName, 100, defaultActiveNameParsePeriod)
                );
            }
            for (String activeNameWithoutExterior : activeNamesWithoutExterior) {
                for (String exterior: exteriorList) {
                    activeNameRepository.save(
                            new ActiveName(activeNameWithoutExterior + " " + exterior, 100, defaultActiveNameParsePeriod)
                    );
                }
            }
        }
    }
}
