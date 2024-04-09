package net.smc.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.dto.SteamItemDto;
import net.smc.enums.SteamItemType;
import net.smc.services.SteamItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("steam-items")
@RequiredArgsConstructor
public class SteamItemController {
    private final SteamItemService steamItemService;

    @GetMapping("all")
    public List<SteamItemDto> getAllActiveNames(@RequestParam SteamItemType steamItemType) {
        return steamItemService.getAllSteamItems(steamItemType);
    }

    @GetMapping("{id}/force-update")
    @Transactional
    public void forceUpdateActiveName(@PathVariable Long id) {
        this.steamItemService.forceUpdateSteamItem(id);
    }
}
