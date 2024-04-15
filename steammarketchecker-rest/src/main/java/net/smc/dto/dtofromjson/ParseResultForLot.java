package net.smc.dto.dtofromjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseResultForLot {
    private boolean connectSuccessful;
    private List<LotFromJsonDto> lotWithStickersFromJsonDtoList;
}
