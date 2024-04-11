package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.dto.SteamItemDto;
import net.smc.entities.SteamItem;
import net.smc.enums.SteamItemType;
import net.smc.readers.SteamItemReader;
import net.smc.repositories.SteamItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SteamItemService {

    // todo тут нужен свой шедулер для обновления
    private final SteamItemReader steamItemReader;
    private final SteamItemRepository steamItemRepository;

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
