package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// таблица представленных на ТП лотов
@Entity
@Data
@NoArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long listingId;
    private Long assetId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId")
    private SteamItem steamItem;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "lot_id")
    private List<LotSticker> lotStickerList = new ArrayList<>();

    private Boolean completeness;
    private Boolean profitability;
    private Double profit;
    private Double convertedPrice; // цена - нормированная
    private Double convertedFee; // комиссия - нормированная
    private Double realPrice;
    private Instant priceCalculatingDate;
    private Instant parseDate;
    private String stickersAsString;

}
