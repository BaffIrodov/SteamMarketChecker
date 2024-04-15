package net.smc.dto.dtofromjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseResultForSteamItem {
    private boolean connectSuccessful;
    private SteamItemFromJsonDto steamItemFromJsonDto;
}
