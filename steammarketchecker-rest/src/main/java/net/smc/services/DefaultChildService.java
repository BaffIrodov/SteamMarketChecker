package net.smc.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.smc.dto.DefaultChildDto;
import net.smc.entities.DefaultChild;
import net.smc.readers.DefaultChildReader;
import net.smc.repositories.DefaultChildRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class DefaultChildService {

    private final DefaultChildReader defaultChildReader;
    private final DefaultChildRepository defaultChildRepository;

    public List<DefaultChildDto> getAllDefaultChildren(boolean showArchive) {
        return defaultChildReader.getAllDefaultChildren(showArchive);
    }

    public List<DefaultChildDto> getDefaultChildrenByParentId(@PathVariable("id") Long parentId, boolean showArchive) {
        return defaultChildReader.getDefaultChildrenByParentId(parentId, showArchive);
    }

    @Transactional
    public List<DefaultChildDto> createDefaultChild(@RequestBody DefaultChildDto defaultChildDto) {
        DefaultChild defaultChild = new DefaultChild(defaultChildDto);
        defaultChild = this.defaultChildRepository.save(defaultChild);
//        this.messageService.createMessagesForAllUsers(parent.getId(),
//                "Новый заказ №" + parent.getNumber());
        return defaultChildReader.getDefaultChildrenByParentId(defaultChild.getId());
    }

    @Transactional
    public List<DefaultChildDto> updateDefaultChild(@PathVariable Long id, @RequestBody DefaultChildDto defaultChildDto) {
        DefaultChild defaultChild = this.defaultChildRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Дефолт-ребенок не найден!");
                }
        );
        defaultChild.update(defaultChildDto);
        return defaultChildReader.getDefaultChildrenByParentId(defaultChild.getId());
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveDefaultChild(@PathVariable Long id) {
        DefaultChild defaultChild = this.defaultChildRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Дефолт-ребенок не найден!");
                }
        );
        defaultChild.archive();
    }

}