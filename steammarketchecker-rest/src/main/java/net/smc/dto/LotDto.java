package net.smc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.smc.entities.Lot;
import net.smc.entities.LotSticker;
import net.smc.entities.SteamItem;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotDto {
    private Long id;

    private Long listingId;
    private Long assetId;
    private String lotParseTarget;

    private SteamItemDto skin;

    private List<SteamItemDto> stickers = new ArrayList<>();

    private boolean completeness;
    private boolean profitability;
    private boolean actual;
    private Double profit;
    private Double convertedPrice; // цена - нормированная
    private Double convertedFee; // комиссия - нормированная
    private Double realPrice;
    private Instant priceCalculatingDate;
    private Instant parseDate;
    private String stickersAsString;
    private Integer positionInListing;

    public LotDto(Lot lot) {
        this.id = lot.getId();
        this.listingId = lot.getListingId();
        this.assetId = lot.getAssetId();
        this.lotParseTarget = lot.getLotParseTarget();
        this.skin = new SteamItemDto(lot.getSteamItem());
        lot.getLotStickerList().forEach(lotSticker -> {
            this.stickers.add(new SteamItemDto(lotSticker.getSteamSticker()));
        });
        this.completeness = lot.isCompleteness();
        this.profitability = lot.isProfitability();
        this.actual = lot.isActual();
        this.profit = lot.getProfit();
        this.convertedPrice = lot.getConvertedPrice();
        this.convertedFee = lot.getConvertedFee();
        this.realPrice = lot.getRealPrice();
        this.priceCalculatingDate = lot.getPriceCalculatingDate();
        this.parseDate = lot.getParseDate();
        this.stickersAsString = lot.getStickersAsString();
        this.positionInListing = lot.getPositionInListing();
    }
}
