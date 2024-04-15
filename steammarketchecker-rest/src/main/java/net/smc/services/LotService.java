package net.smc.services;

import com.querydsl.core.group.GroupBy;
import lombok.RequiredArgsConstructor;
import net.smc.dto.LotDto;
import net.smc.dto.SteamItemDto;
import net.smc.entities.Lot;
import net.smc.readers.LotReader;
import net.smc.repositories.LotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {
    private final LotReader lotReader;
    private final LotRepository lotRepository;

    public List<LotDto> getAllLots(Boolean onlyActual, Boolean onlyCompleteness, Boolean onlyProfitability) {
        // todo надо маппиться нормально - пока и так сойдет
        // todo либо можно не получать всё сразу, а только по открытию диалога делать такой запрос
        List<LotDto> allLots = lotReader.getAllLots(onlyActual, onlyCompleteness, onlyProfitability);
        Map<Long, LotDto> mapLotDtoById = allLots.stream().collect(Collectors.toMap(LotDto::getId, e -> e));
        List<Lot> lots = lotRepository.findAllById(allLots.stream().map(LotDto::getId).toList());
        lots.forEach(lotEntity -> {
            List<SteamItemDto> stickerDtoList = new ArrayList<>();
            lotEntity.getLotStickerList().forEach(lotSticker -> {
                stickerDtoList.add(new SteamItemDto(lotSticker.getSteamSticker()));
            });
            mapLotDtoById.get(lotEntity.getId()).setStickers(stickerDtoList);
        });
        return allLots;
    }

    // todo здесь нужен шедулер, который будет проверять completeness
}
