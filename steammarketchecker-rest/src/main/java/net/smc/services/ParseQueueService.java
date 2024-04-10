package net.smc.services;

import lombok.RequiredArgsConstructor;
import net.smc.dto.ParseQueueDto;
import net.smc.readers.ParseQueueReader;
import net.smc.repositories.ParseQueueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParseQueueService {
    private final ParseQueueReader parseQueueReader;
    private final ParseQueueRepository parseQueueRepository;

    public List<ParseQueueDto> getAllParseQueues(Boolean archive) {
        return parseQueueReader.getAllParseQueues(archive);
    }
}
