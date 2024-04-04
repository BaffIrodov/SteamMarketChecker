package net.smc.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.smc.dto.DefaultChildDto;
import net.smc.services.DefaultChildService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("default-child")
@RequiredArgsConstructor
public class DefaultChildController {
    private final DefaultChildService defaultChildService;

    @GetMapping("all")
    public List<DefaultChildDto> getAllDefaultChildren(@RequestParam boolean showArchive) {
        return defaultChildService.getAllDefaultChildren(showArchive);
    }

    @GetMapping("{id}")
    public List<DefaultChildDto> getDefaultChildrenByParentId(@PathVariable("id") Long id,
                                                              @RequestParam boolean showArchive) {
        return defaultChildService.getDefaultChildrenByParentId(id, showArchive);
    }

    @PostMapping("create")
    @Transactional
    public List<DefaultChildDto> createDefaultChild(@RequestBody DefaultChildDto defaultChildDto) {
        return this.defaultChildService.createDefaultChild(defaultChildDto);
    }

    @PutMapping("{id}/update")
    @Transactional
    public List<DefaultChildDto> updateDefaultChild(@PathVariable Long id, @RequestBody DefaultChildDto defaultChildDto) {
        return this.defaultChildService.updateDefaultChild(id, defaultChildDto);
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveDefaultChild(@PathVariable Long id) {
        this.defaultChildService.archiveDefaultChild(id);
    }
}
