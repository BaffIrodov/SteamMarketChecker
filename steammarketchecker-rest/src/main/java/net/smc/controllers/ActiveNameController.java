package net.smc.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.dto.ActiveNameDto;
import net.smc.services.ActiveNameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("active-names")
@RequiredArgsConstructor
public class ActiveNameController {

    private final ActiveNameService activeNameService;

    @GetMapping("all")
    public List<ActiveNameDto> getAllActiveNames(@RequestParam boolean showArchive) {
        return activeNameService.getAllActiveNames(showArchive);
    }

    @PostMapping("create")
    @Transactional
    public List<ActiveNameDto> createActiveName(@RequestBody ActiveNameDto activeNameDto) {
        return this.activeNameService.createActiveName(activeNameDto);
    }

    @PutMapping("{id}/update")
    @Transactional
    public List<ActiveNameDto> updateActiveName(@PathVariable Long id, @RequestBody ActiveNameDto activeNameDto) {
        return this.activeNameService.updateActiveName(id, activeNameDto);
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveActiveName(@PathVariable Long id) {
        this.activeNameService.archiveActiveName(id);
    }

    @GetMapping("{id}/force-update")
    @Transactional
    public void forceUpdateActiveName(@PathVariable Long id) {
        this.activeNameService.forceUpdateActiveName(id);
    }

}
