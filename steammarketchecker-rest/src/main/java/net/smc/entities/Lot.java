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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "listingId"}) })
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

    public Lot(Double profit, Double convertedPrice, Double convertedFee, Double realPrice) {
        this.profit = profit;
        this.convertedPrice = convertedPrice;
        this.convertedFee = convertedFee;
        this.realPrice = realPrice;
        this.priceCalculatingDate = Instant.now();
    }

    // конструктор на основе дто тут не нужен - эта сущность никогда не создается из дто

}
