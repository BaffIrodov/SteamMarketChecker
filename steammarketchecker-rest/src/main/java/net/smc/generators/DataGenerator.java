package net.smc.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.smc.entities.*;
import net.smc.enums.ParseType;
import net.smc.enums.SteamItemType;
import net.smc.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataGenerator {

    private final DefaultParentRepository defaultParentRepository;
    private final DefaultChildRepository defaultChildRepository;
    private final ActiveNameRepository activeNameRepository;
    private final ParseQueueRepository parseQueueRepository;
    private final LotRepository lotRepository;
    private final LotStickerRepository lotStickerRepository;
    private final SteamItemRepository steamItemRepository;

    @Value("${truncate-table.active-name}")
    private Boolean truncateActiveNameTable;

    @Value("${truncate-table.parse-queue}")
    private Boolean truncateParseQueueTable;

    @Value("${truncate-table.lot}")
    private Boolean truncateLotTable;

    @Value("${truncate-table.lot-sticker}")
    private Boolean truncateLotStickerTable;

    @Value("${truncate-table.steam-item}")
    private Boolean truncateSteamItemTable;

    @Value("${generators.active-name}")
    private Boolean generatorActiveNameEnable;

    @Value("${generators.parse-queue}")
    private Boolean generatorParseQueueEnable;

    @Value("${generators.lot-with-steam-items}")
    private Boolean generatorLotWithSteamItemsEnable;

    private int defaultParentCount = 15;
    private int defaultChildCount = 5;
    private int activeNameCount = 5;
    private int parseQueueCount = 5;
    private int lotCount = 5;
    private int stickerCount = 5;
    private int skinCount = 5;

    @PostConstruct
    public void generateData() {
        truncateTables();
        generateActiveNames();
        generateParseQueues();
        generateLotWithSteamItems();
    }

    public void generateActiveNames() {
        if (generatorActiveNameEnable && activeNameRepository.findAll().size() == 0) {
            for (int i = 0; i < activeNameCount; i++) {
                activeNameRepository.save(
                        new ActiveName("generatedName_" + (i + 1), (i+1)*10, (i+5)*10)
                );
            }
        }
    }

    public void generateParseQueues() {
        if (generatorParseQueueEnable && parseQueueRepository.findAll().size() == 0) {
            for (int i = 0; i < parseQueueCount; i++) {
                parseQueueRepository.save(new ParseQueue(i, ParseType.LOT, "generatedParseTarget_" + (i + 1), "generatedParseUrl_" + (i + 1)));
                parseQueueRepository.save(new ParseQueue(i, ParseType.SKIN, "generatedParseTarget_" + (i + 1), "generatedParseUrl_" + (i + 1)));
                parseQueueRepository.save(new ParseQueue(i, ParseType.STICKER, "generatedParseTarget_" + (i + 1), "generatedParseUrl_" + (i + 1)));
            }
        }
    }

    public void generateLotWithSteamItems() {
        Random random = new Random();
        if (generatorLotWithSteamItemsEnable && lotRepository.findAll().size() == 0
                && lotStickerRepository.findAll().size() == 0 && steamItemRepository.findAll().size() == 0) {
            List<Lot> lotList = new ArrayList<>();
            List<SteamItem> skinList = new ArrayList<>();
            List<SteamItem> stickerList = new ArrayList<>();
            for (int i = 0; i < lotCount; i++) {
                lotList.add(new Lot((double) (i+1), (double) (i+1), (double) (i+1), (double) (i+1)));
            }
            lotRepository.saveAllAndFlush(lotList);
            for (int i = 0; i < skinCount; i++) {
                skinList.add(new SteamItem("generatedSkinName_" + (i + 1), (double) (i+1)*10, (double) (i+1)*10, SteamItemType.SKIN));
            }
            for (int i = 0; i < stickerCount; i++) {
                stickerList.add(new SteamItem("generatedStickerName_" + (i + 1), (double) (i+1)*10, (double) (i+1)*10, SteamItemType.STICKER));
            }
            steamItemRepository.saveAllAndFlush(skinList);
            steamItemRepository.saveAllAndFlush(stickerList);
            for (Lot lot : lotList) {
                List<LotSticker> lotStickerList = new ArrayList<>();
                int randStickerCount = random.nextInt(1, 4);
                int randStickerIndex = random.nextInt(0, stickerCount-1);
                int randSkinIndex = random.nextInt(0, skinCount-1);
                for (int i = 0; i < randStickerCount; i++) {
                    lotStickerList.add(new LotSticker(lot.getId(), stickerList.get(randStickerIndex)));
                }
                lot.setSteamItem(skinList.get(randSkinIndex));
                lot.setLotStickerList(lotStickerList);
                lotRepository.saveAllAndFlush(lotList);
                lotStickerRepository.saveAllAndFlush(lotStickerList);
            }
        }
    }

    public void truncateTables() {
        if (truncateActiveNameTable) activeNameRepository.deleteAll();
        if (truncateParseQueueTable) parseQueueRepository.deleteAll();
        if (truncateLotTable) lotRepository.deleteAll();
        if (truncateLotStickerTable) lotStickerRepository.deleteAll();
        if (truncateSteamItemTable) steamItemRepository.deleteAll();
    }
}
