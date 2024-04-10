package net.smc.controllers;

import lombok.RequiredArgsConstructor;
import net.smc.dto.LotDto;
import net.smc.dto.SteamItemDto;
import net.smc.services.LotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("lots")
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;

    @GetMapping("all")
    public List<LotDto> getAllLots(@RequestParam Boolean onlyActual,
                                   @RequestParam Boolean onlyCompleteness,
                                   @RequestParam Boolean onlyProfitability) {
        return lotService.getAllLots(onlyActual, onlyCompleteness, onlyProfitability);
    }
}
