package net.smc.controllers;

import lombok.RequiredArgsConstructor;
import net.smc.dto.ParseQueueDto;
import net.smc.services.ParseQueueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("parse-queues")
@RequiredArgsConstructor
public class ParseQueueController {
    private final ParseQueueService parseQueueService;

    @GetMapping("all")
    public List<ParseQueueDto> getAllParseQueues(@RequestParam Boolean archive) {
        return parseQueueService.getAllParseQueues(archive);
    }
}
