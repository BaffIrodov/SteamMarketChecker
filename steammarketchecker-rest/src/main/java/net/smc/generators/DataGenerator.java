package net.smc.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.smc.entities.DefaultChild;
import net.smc.entities.DefaultParent;
import net.smc.repositories.DefaultParentRepository;
import net.smc.repositories.DefaultChildRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataGenerator {

    private final DefaultParentRepository defaultParentRepository;
    private final DefaultChildRepository defaultChildRepository;

    @Value("${generators.default-parent}")
    private Boolean generatorDefaultParentEnable;

    @Value("${generators.default-child}")
    private Boolean generatorDefaultChildEnable;

    private int defaultParentCount = 15;
    private int defaultChildCount = 5;

    @PostConstruct
    public void generateData() {
        generateDefaultParents();
        generateDefaultChildren();
    }

    public void generateDefaultParents() {
        if (generatorDefaultParentEnable) {
            for (int i = 0; i < defaultParentCount; i++) {
                defaultParentRepository.save(new DefaultParent("generatedName_" + (i + 1)));
            }
        }
    }

    public void generateDefaultChildren() {
        if (generatorDefaultChildEnable) {
            List<DefaultParent> defaultParents = defaultParentRepository.findAll();
            for (DefaultParent defaultParent : defaultParents) {
                List<DefaultChild> defaultChildrenForSave = new ArrayList<>();
                for (int i = 0; i < defaultChildCount; i++) {
                    DefaultChild defaultChild = new DefaultChild(defaultParent.getId(), "generatedName_" + (i + 1));
                    defaultChildrenForSave.add(defaultChild);
                }
                defaultChildRepository.saveAll(defaultChildrenForSave);
            }
        }
    }
}
