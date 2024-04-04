package net.smc.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// таблица для связи лота с его стикерами
@Entity
@Data
@NoArgsConstructor
public class LotSticker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lot_id")
    private Long lotId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stickerId")
    private Sticker sticker;

}
