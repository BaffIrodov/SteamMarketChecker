package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.smc.dto.DefaultParentDto;
import net.smc.entities.DefaultParent;
import net.smc.readers.DefaultParentReader;
import net.smc.repositories.DefaultParentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class DefaultParentService {

    private final DefaultParentReader defaultParentReader;
    private final DefaultParentRepository defaultParentRepository;

    public List<DefaultParentDto> getAllDefaultParents(boolean showArchive) {
        return defaultParentReader.getAllDefaultParents(showArchive);
    }

    @GetMapping("{id}")
    public DefaultParentDto getDefaultParent(@PathVariable("id") Long id) {
        return defaultParentReader.getDefaultParentById(id);
    }

    @PostMapping("create")
    @Transactional
    public DefaultParentDto createDefaultParent(@RequestBody DefaultParentDto defaultParentDto) {
        DefaultParent defaultParent = new DefaultParent(defaultParentDto);
        defaultParent = this.defaultParentRepository.save(defaultParent);
//        this.messageService.createMessagesForAllUsers(parent.getId(),
//                "Новый заказ №" + parent.getNumber());
//        this.messageService.createTelegramMessagesForAllUsers("Новый заказ №" + parent.getNumber());
        return defaultParentReader.getDefaultParentById(defaultParent.getId());
    }

    @PutMapping("{id}/update")
    @Transactional
    public DefaultParentDto updateDefaultParent(@PathVariable Long id, @RequestBody DefaultParentDto defaultParentDto) {
        DefaultParent defaultParent = this.defaultParentRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Дефолт-родитель не найден!");
                }
        );
        defaultParent.update(defaultParentDto);
        return defaultParentReader.getDefaultParentById(defaultParent.getId());
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveDefaultParent(@PathVariable Long id) {
        DefaultParent defaultParent = this.defaultParentRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Дефолт-родитель не найден!");
                }
        );
        defaultParent.archive();
    }

}