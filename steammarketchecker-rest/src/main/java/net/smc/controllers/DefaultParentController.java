package net.smc.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.dto.DefaultParentDto;
import net.smc.services.DefaultParentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("default-parent")
@RequiredArgsConstructor
public class DefaultParentController {
    private final DefaultParentService defaultParentService;

    @GetMapping("all")
    public List<DefaultParentDto> getAllDefaultParents(@RequestParam boolean showArchive) {
        return defaultParentService.getAllDefaultParents(showArchive);
    }

    @GetMapping("{id}")
    public DefaultParentDto getDefaultParent(@PathVariable("id") Long id) {
        return defaultParentService.getDefaultParent(id);
    }

    @PostMapping("create")
    @Transactional
    public DefaultParentDto createDefaultParent(@RequestBody DefaultParentDto defaultParentDto) {
        return this.defaultParentService.createDefaultParent(defaultParentDto);
    }

    @PutMapping("{id}/update")
    @Transactional
    public DefaultParentDto updateDefaultParent(@PathVariable Long id, @RequestBody DefaultParentDto defaultParentDto) {
        return this.defaultParentService.updateDefaultParent(id, defaultParentDto);
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveDefaultParent(@PathVariable Long id) {
        this.defaultParentService.archiveDefaultParent(id);
    }
}
