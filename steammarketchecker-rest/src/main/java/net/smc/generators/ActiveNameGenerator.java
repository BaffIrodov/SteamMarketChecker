package net.smc.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.smc.entities.ActiveName;
import net.smc.repositories.ActiveNameRepository;
import net.smc.repositories.LotRepository;
import net.smc.repositories.LotStickerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ActiveNameGenerator {

    @Value("${default-parse-period.active-name}")
    private Integer defaultActiveNameParsePeriod;

    private final ActiveNameRepository activeNameRepository;
    private final LotRepository lotRepository;
    private final LotStickerRepository lotStickerRepository;

    private final List<String> exteriorList = List.of(
            "(Factory New)",
            "(Minimal Wear)",
            "(Field-Tested)",
            "(Well-Worn)",
            "(Battle-Scarred)"
    );

    private final List<String> activeNames = List.of(
//            "FAMAS | Pulse (Factory New)",
//            "FAMAS | Pulse (Minimal Wear)",
//            "FAMAS | Pulse (Field-Tested)",
//            "FAMAS | Pulse (Well-Worn)"
//            "AWP | Electric Hive (Field-Tested)"


            // это дорогие, их нужно по десятке фигачить
//            "AWP | Electric Hive (Factory New)",
//            "Desert Eagle | Oxide Blaze (Factory New)",
//            "AWP | Fever Dream (Factory New)",
//            "AWP | Electric Hive (Well-Worn)",
//            "M4A4 | X-Ray (Minimal Wear)",
//            "AWP | Corticera (Factory New)",
//            "AK-47 | Blue Laminate (Minimal Wear)",
//            "AWP | Electric Hive (Field-Tested)"


            // это дешевые, можно по 100
//            "StatTrak™ AK-47 | Blue Laminate (Factory New)",
//            "AK-47 | Blue Laminate (Factory New)",
//            "StatTrak™ AK-47 | Blue Laminate (Minimal Wear)",
//            "AK-47 | Blue Laminate (Minimal Wear)",
//            "StatTrak™ AWP | Fever Dream (Factory New)",
//            "AWP | Fever Dream (Factory New)",
//            "StatTrak™ AWP | Fever Dream (Minimal Wear)",
//            "AWP | Fever Dream (Minimal Wear)",
//            "M4A4 | Radiation Hazard (Factory New)",
//            "M4A4 | Radiation Hazard (Minimal Wear)",
//            "M4A4 | Radiation Hazard (Field-Tested)",
//            "M4A4 | Radiation Hazard (Well-Worn)",
//            "AWP | Sun in Leo (Factory New)",
//            "AWP | Sun in Leo (Minimal Wear)",
//            "AWP | Sun in Leo (Field-Tested)",
//            "AWP | Sun in Leo (Well-Worn)",

//            "StatTrak™ M4A4 | Faded Zebra (Factory New)",
//            "M4A4 | Faded Zebra (Factory New)",
//            "StatTrak™ M4A4 | Faded Zebra (Minimal Wear)",
//            "M4A4 | Faded Zebra (Minimal Wear)",
//            "StatTrak™ M4A4 | Faded Zebra (Field-Tested)",
//            "M4A4 | Faded Zebra (Field-Tested)",
//            "StatTrak™ M4A4 | Faded Zebra (Well-Worn)",
//            "M4A4 | Faded Zebra (Well-Worn)",
//            "StatTrak™ M4A4 | Faded Zebra (Battle-Scarred)",
//            "M4A4 | Faded Zebra (Battle-Scarred)",
//
//            // Прямо с завода - очень редкие, их может не быть на ТП
//            "StatTrak™ AK-47 | Red Laminate (Minimal Wear)",
//            "AK-47 | Red Laminate (Minimal Wear)",
//            "StatTrak™ AK-47 | Red Laminate (Field-Tested)",
//            "AK-47 | Red Laminate (Field-Tested)",
//            "StatTrak™ AK-47 | Red Laminate (Well-Worn)",
//            "AK-47 | Red Laminate (Well-Worn)",
//            "StatTrak™ AK-47 | Red Laminate (Battle-Scarred)",
//            "AK-47 | Red Laminate (Battle-Scarred)",
//
//            // Есть только MW и FT
//            "StatTrak™ FAMAS | Doomkitty (Minimal Wear)",
//            "FAMAS | Doomkitty (Minimal Wear)",
//            "StatTrak™ FAMAS | Doomkitty (Field-Tested)",
//            "FAMAS | Doomkitty (Field-Tested)",
//
//            // Всё ок, но скинов в целом не много, они могут не парситься в какой-то момент
//            "StatTrak™ AK-47 | Jaguar (Factory New)",
//            "AK-47 | Jaguar (Factory New)",
//            "StatTrak™ AK-47 | Jaguar (Minimal Wear)",
//            "AK-47 | Jaguar (Minimal Wear)",
//            "StatTrak™ AK-47 | Jaguar (Field-Tested)",
//            "AK-47 | Jaguar (Field-Tested)",
//            "StatTrak™ AK-47 | Jaguar (Well-Worn)",
//            "AK-47 | Jaguar (Well-Worn)",
//            "StatTrak™ AK-47 | Jaguar (Battle-Scarred)",
//            "AK-47 | Jaguar (Battle-Scarred)",
//
//            // Всё ок, но статтрек прямо с завода может исчезнуть
//            "StatTrak™ MAC-10 | Ultraviolet (Factory New)",
//            "MAC-10 | Ultraviolet (Factory New)",
//            "StatTrak™ MAC-10 | Ultraviolet (Minimal Wear)",
//            "MAC-10 | Ultraviolet (Minimal Wear)",
//            "StatTrak™ MAC-10 | Ultraviolet (Field-Tested)",
//            "MAC-10 | Ultraviolet (Field-Tested)",
//            "StatTrak™ MAC-10 | Ultraviolet (Well-Worn)",
//            "MAC-10 | Ultraviolet (Well-Worn)",
//            "StatTrak™ MAC-10 | Ultraviolet (Battle-Scarred)",
//            "MAC-10 | Ultraviolet (Battle-Scarred)",
//
//            // WW и BS просто нету
//            "StatTrak™ USP-S | Blood Tiger (Factory New)",
//            "USP-S | Blood Tiger (Factory New)",
//            "StatTrak™ USP-S | Blood Tiger (Minimal Wear)",
//            "USP-S | Blood Tiger (Minimal Wear)",
//            "StatTrak™ USP-S | Blood Tiger (Field-Tested)",
//            "USP-S | Blood Tiger (Field-Tested)",
//
//            // WW и BS просто нету
//            "StatTrak™ M4A1-S | Blood Tiger (Factory New)",
//            "M4A1-S | Blood Tiger (Factory New)",
//            "StatTrak™ M4A1-S | Blood Tiger (Minimal Wear)",
//            "M4A1-S | Blood Tiger (Minimal Wear)",
//            "StatTrak™ M4A1-S | Blood Tiger (Field-Tested)",
//            "M4A1-S | Blood Tiger (Field-Tested)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ Desert Eagle | Blue Ply (Factory New)",
//            "Desert Eagle | Blue Ply (Factory New)",
//            "StatTrak™ Desert Eagle | Blue Ply (Minimal Wear)",
//            "Desert Eagle | Blue Ply (Minimal Wear)",
//            "StatTrak™ Desert Eagle | Blue Ply (Field-Tested)",
//            "Desert Eagle | Blue Ply (Field-Tested)",
//            "StatTrak™ Desert Eagle | Blue Ply (Well-Worn)",
//            "Desert Eagle | Blue Ply (Well-Worn)",
//            "StatTrak™ Desert Eagle | Blue Ply (Battle-Scarred)",
//            "Desert Eagle | Blue Ply (Battle-Scarred)",
//
//            // WW и BS просто нету
//            "StatTrak™ Desert Eagle | Conspiracy (Factory New)",
//            "Desert Eagle | Conspiracy (Factory New)",
//            "StatTrak™ Desert Eagle | Conspiracy (Minimal Wear)",
//            "Desert Eagle | Conspiracy (Minimal Wear)",
//            "StatTrak™ Desert Eagle | Conspiracy (Field-Tested)",
//            "Desert Eagle | Conspiracy (Field-Tested)",
//
//            // Всё ок, но статтрек прямо с завода может исчезнуть
//            "StatTrak™ Desert Eagle | Crimson Web (Factory New)",
//            "Desert Eagle | Crimson Web (Factory New)",
//            "StatTrak™ Desert Eagle | Crimson Web (Minimal Wear)",
//            "Desert Eagle | Crimson Web (Minimal Wear)",
//            "StatTrak™ Desert Eagle | Crimson Web (Field-Tested)",
//            "Desert Eagle | Crimson Web (Field-Tested)",
//            "StatTrak™ Desert Eagle | Crimson Web (Well-Worn)",
//            "Desert Eagle | Crimson Web (Well-Worn)",
//            "StatTrak™ Desert Eagle | Crimson Web (Battle-Scarred)",
//            "Desert Eagle | Crimson Web (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ AWP | Elite Build (Factory New)",
//            "AWP | Elite Build (Factory New)",
//            "StatTrak™ AWP | Elite Build (Minimal Wear)",
//            "AWP | Elite Build (Minimal Wear)",
//            "StatTrak™ AWP | Elite Build (Field-Tested)",
//            "AWP | Elite Build (Field-Tested)",
//            "StatTrak™ AWP | Elite Build (Well-Worn)",
//            "AWP | Elite Build (Well-Worn)",
//            "StatTrak™ AWP | Elite Build (Battle-Scarred)",
//            "AWP | Elite Build (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ M4A1-S | Nightmare (Factory New)",
//            "M4A1-S | Nightmare (Factory New)",
//            "StatTrak™ M4A1-S | Nightmare (Minimal Wear)",
//            "M4A1-S | Nightmare (Minimal Wear)",
//            "StatTrak™ M4A1-S | Nightmare (Field-Tested)",
//            "M4A1-S | Nightmare (Field-Tested)",
//            "StatTrak™ M4A1-S | Nightmare (Well-Worn)",
//            "M4A1-S | Nightmare (Well-Worn)",
//            "StatTrak™ M4A1-S | Nightmare (Battle-Scarred)",
//            "M4A1-S | Nightmare (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ M4A1-S | Basilisk (Factory New)",
//            "M4A1-S | Basilisk (Factory New)",
//            "StatTrak™ M4A1-S | Basilisk (Minimal Wear)",
//            "M4A1-S | Basilisk (Minimal Wear)",
//            "StatTrak™ M4A1-S | Basilisk (Field-Tested)",
//            "M4A1-S | Basilisk (Field-Tested)",
//            "StatTrak™ M4A1-S | Basilisk (Well-Worn)",
//            "M4A1-S | Basilisk (Well-Worn)",
//            "StatTrak™ M4A1-S | Basilisk (Battle-Scarred)",
//            "M4A1-S | Basilisk (Battle-Scarred)",
//
//            // Нет статтреков вообще, всё остальное ок
//            "M4A1-S | Nitro (Factory New)",
//            "M4A1-S | Nitro (Minimal Wear)",
//            "M4A1-S | Nitro (Field-Tested)",
//            "M4A1-S | Nitro (Well-Worn)",
//            "M4A1-S | Nitro (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ Desert Eagle | Oxide Blaze (Factory New)",
//            "Desert Eagle | Oxide Blaze (Factory New)",
//            "StatTrak™ Desert Eagle | Oxide Blaze (Minimal Wear)",
//            "Desert Eagle | Oxide Blaze (Minimal Wear)",
//            "StatTrak™ Desert Eagle | Oxide Blaze (Field-Tested)",
//            "Desert Eagle | Oxide Blaze (Field-Tested)",
//            "StatTrak™ Desert Eagle | Oxide Blaze (Well-Worn)",
//            "Desert Eagle | Oxide Blaze (Well-Worn)",
//            "StatTrak™ Desert Eagle | Oxide Blaze (Battle-Scarred)",
//            "Desert Eagle | Oxide Blaze (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ AK-47 | Elite Build (Factory New)",
//            "AK-47 | Elite Build (Factory New)",
//            "StatTrak™ AK-47 | Elite Build (Minimal Wear)",
//            "AK-47 | Elite Build (Minimal Wear)",
//            "StatTrak™ AK-47 | Elite Build (Field-Tested)",
//            "AK-47 | Elite Build (Field-Tested)",
//            "StatTrak™ AK-47 | Elite Build (Well-Worn)",
//            "AK-47 | Elite Build (Well-Worn)",
//            "StatTrak™ AK-47 | Elite Build (Battle-Scarred)",
//            "AK-47 | Elite Build (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ AK-47 | Frontside Misty (Factory New)",
//            "AK-47 | Frontside Misty (Factory New)",
//            "StatTrak™ AK-47 | Frontside Misty (Minimal Wear)",
//            "AK-47 | Frontside Misty (Minimal Wear)",
//            "StatTrak™ AK-47 | Frontside Misty (Field-Tested)",
//            "AK-47 | Frontside Misty (Field-Tested)",
//            "StatTrak™ AK-47 | Frontside Misty (Well-Worn)",
//            "AK-47 | Frontside Misty (Well-Worn)",
//            "StatTrak™ AK-47 | Frontside Misty (Battle-Scarred)",
//            "AK-47 | Frontside Misty (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ AWP | Hyper Beast (Factory New)",
//            "AWP | Hyper Beast (Factory New)",
//            "StatTrak™ AWP | Hyper Beast (Minimal Wear)",
//            "AWP | Hyper Beast (Minimal Wear)",
//            "StatTrak™ AWP | Hyper Beast (Field-Tested)",
//            "AWP | Hyper Beast (Field-Tested)",
//            "StatTrak™ AWP | Hyper Beast (Well-Worn)",
//            "AWP | Hyper Beast (Well-Worn)",
//            "StatTrak™ AWP | Hyper Beast (Battle-Scarred)",
//            "AWP | Hyper Beast (Battle-Scarred)",
//
//            // Всё ок, всех позиций много
//            "StatTrak™ AK-47 | Point Disarray (Factory New)",
//            "AK-47 | Point Disarray (Factory New)",
//            "StatTrak™ AK-47 | Point Disarray (Minimal Wear)",
//            "AK-47 | Point Disarray (Minimal Wear)",
//            "StatTrak™ AK-47 | Point Disarray (Field-Tested)",
//            "AK-47 | Point Disarray (Field-Tested)",
//            "StatTrak™ AK-47 | Point Disarray (Well-Worn)",
//            "AK-47 | Point Disarray (Well-Worn)",
//            "StatTrak™ AK-47 | Point Disarray (Battle-Scarred)",
//            "AK-47 | Point Disarray (Battle-Scarred)",
//
//            // Всё ок, но всех статтрек позиций немного
//            "StatTrak™ AK-47 | Neon Rider (Factory New)",
//            "AK-47 | Neon Rider (Factory New)",
//            "StatTrak™ AK-47 | Neon Rider (Minimal Wear)",
//            "AK-47 | Neon Rider (Minimal Wear)",
//            "StatTrak™ AK-47 | Neon Rider (Field-Tested)",
//            "AK-47 | Neon Rider (Field-Tested)",
//            "StatTrak™ AK-47 | Neon Rider (Well-Worn)",
//            "AK-47 | Neon Rider (Well-Worn)",
//            "StatTrak™ AK-47 | Neon Rider (Battle-Scarred)",
//            "AK-47 | Neon Rider (Battle-Scarred)",
//
//            // Нет FN вообще
//            "StatTrak™ AK-47 | Redline (Minimal Wear)",
//            "AK-47 | Redline (Minimal Wear)",
//            "StatTrak™ AK-47 | Redline (Field-Tested)",
//            "AK-47 | Redline (Field-Tested)",
//            "StatTrak™ AK-47 | Redline (Well-Worn)",
//            "AK-47 | Redline (Well-Worn)",
//            "StatTrak™ AK-47 | Redline (Battle-Scarred)",
//            "AK-47 | Redline (Battle-Scarred)",
//
//            // Нет статтрека вообще, FN - мало
//            "AK-47 | Jungle Spray (Factory New)",
//            "AK-47 | Jungle Spray (Minimal Wear)",
//            "AK-47 | Jungle Spray (Field-Tested)",
//            "AK-47 | Jungle Spray (Well-Worn)",
//            "AK-47 | Jungle Spray (Battle-Scarred)",
//
//            // Нет статтрека вообще, FN - мало
//            "AK-47 | Black Laminate (Factory New)",
//            "AK-47 | Black Laminate (Minimal Wear)",
//            "AK-47 | Black Laminate (Field-Tested)",
//            "AK-47 | Black Laminate (Well-Worn)",
//            "AK-47 | Black Laminate (Battle-Scarred)",
//
//            // Нет статтрека вообще, WW - мало, BS - нету
//            "AK-47 | Green Laminate (Factory New)",
//            "AK-47 | Green Laminate (Minimal Wear)",
//            "AK-47 | Green Laminate (Field-Tested)",
//            "AK-47 | Green Laminate (Well-Worn)",
//
//            // Всё ок, BS и WW позиций немного
//            "StatTrak™ AK-47 | Orbit Mk01 (Factory New)",
//            "AK-47 | Orbit Mk01 (Factory New)",
//            "StatTrak™ AK-47 | Orbit Mk01 (Minimal Wear)",
//            "AK-47 | Orbit Mk01 (Minimal Wear)",
//            "StatTrak™ AK-47 | Orbit Mk01 (Field-Tested)",
//            "AK-47 | Orbit Mk01 (Field-Tested)",
//            "StatTrak™ AK-47 | Orbit Mk01 (Well-Worn)",
//            "AK-47 | Orbit Mk01 (Well-Worn)",
//            "StatTrak™ AK-47 | Orbit Mk01 (Battle-Scarred)",
//            "AK-47 | Orbit Mk01 (Battle-Scarred)",
//
//            // Всё ок, WW позиций немного
//            "StatTrak™ AK-47 | Cartel (Factory New)",
//            "AK-47 | Cartel (Factory New)",
//            "StatTrak™ AK-47 | Cartel (Minimal Wear)",
//            "AK-47 | Cartel (Minimal Wear)",
//            "StatTrak™ AK-47 | Cartel (Field-Tested)",
//            "AK-47 | Cartel (Field-Tested)",
//            "StatTrak™ AK-47 | Cartel (Well-Worn)",
//            "AK-47 | Cartel (Well-Worn)",
//            "StatTrak™ AK-47 | Cartel (Battle-Scarred)",
//            "AK-47 | Cartel (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ AK-47 | Neon Revolution (Factory New)",
//            "AK-47 | Neon Revolution (Factory New)",
//            "StatTrak™ AK-47 | Neon Revolution (Minimal Wear)",
//            "AK-47 | Neon Revolution (Minimal Wear)",
//            "StatTrak™ AK-47 | Neon Revolution (Field-Tested)",
//            "AK-47 | Neon Revolution (Field-Tested)",
//            "StatTrak™ AK-47 | Neon Revolution (Well-Worn)",
//            "AK-47 | Neon Revolution (Well-Worn)",
//            "StatTrak™ AK-47 | Neon Revolution (Battle-Scarred)",
//            "AK-47 | Neon Revolution (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ AK-47 | Legion of Anubis (Factory New)",
//            "AK-47 | Legion of Anubis (Factory New)",
//            "StatTrak™ AK-47 | Legion of Anubis (Minimal Wear)",
//            "AK-47 | Legion of Anubis (Minimal Wear)",
//            "StatTrak™ AK-47 | Legion of Anubis (Field-Tested)",
//            "AK-47 | Legion of Anubis (Field-Tested)",
//            "StatTrak™ AK-47 | Legion of Anubis (Well-Worn)",
//            "AK-47 | Legion of Anubis (Well-Worn)",
//            "StatTrak™ AK-47 | Legion of Anubis (Battle-Scarred)",
//            "AK-47 | Legion of Anubis (Battle-Scarred)",
//
//            // Нет статтрека
//            "AK-47 | Baroque Purple (Factory New)",
//            "AK-47 | Baroque Purple (Minimal Wear)",
//            "AK-47 | Baroque Purple (Field-Tested)",
//            "AK-47 | Baroque Purple (Well-Worn)",
//            "AK-47 | Baroque Purple (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ AK-47 | Phantom Disruptor (Factory New)",
//            "AK-47 | Phantom Disruptor (Factory New)",
//            "StatTrak™ AK-47 | Phantom Disruptor (Minimal Wear)",
//            "AK-47 | Phantom Disruptor (Minimal Wear)",
//            "StatTrak™ AK-47 | Phantom Disruptor (Field-Tested)",
//            "AK-47 | Phantom Disruptor (Field-Tested)",
//            "StatTrak™ AK-47 | Phantom Disruptor (Well-Worn)",
//            "AK-47 | Phantom Disruptor (Well-Worn)",
//            "StatTrak™ AK-47 | Phantom Disruptor (Battle-Scarred)",
//            "AK-47 | Phantom Disruptor (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ AK-47 | Ice Coaled (Factory New)",
//            "AK-47 | Ice Coaled (Factory New)",
//            "StatTrak™ AK-47 | Ice Coaled (Minimal Wear)",
//            "AK-47 | Ice Coaled (Minimal Wear)",
//            "StatTrak™ AK-47 | Ice Coaled (Field-Tested)",
//            "AK-47 | Ice Coaled (Field-Tested)",
//            "StatTrak™ AK-47 | Ice Coaled (Well-Worn)",
//            "AK-47 | Ice Coaled (Well-Worn)",
//            "StatTrak™ AK-47 | Ice Coaled (Battle-Scarred)",
//            "AK-47 | Ice Coaled (Battle-Scarred)",
//
//            // Нет BS
//            "StatTrak™ AK-47 | Bloodsport (Factory New)",
//            "AK-47 | Bloodsport (Factory New)",
//            "StatTrak™ AK-47 | Bloodsport (Minimal Wear)",
//            "AK-47 | Bloodsport (Minimal Wear)",
//            "StatTrak™ AK-47 | Bloodsport (Field-Tested)",
//            "AK-47 | Bloodsport (Field-Tested)",
//            "StatTrak™ AK-47 | Bloodsport (Well-Worn)",
//            "AK-47 | Bloodsport (Well-Worn)",
//
//            // Всё ок
//            "StatTrak™ USP-S | Kill Confirmed (Factory New)",
//            "USP-S | Kill Confirmed (Factory New)",
//            "StatTrak™ USP-S | Kill Confirmed (Minimal Wear)",
//            "USP-S | Kill Confirmed (Minimal Wear)",
//            "StatTrak™ USP-S | Kill Confirmed (Field-Tested)",
//            "USP-S | Kill Confirmed (Field-Tested)",
//            "StatTrak™ USP-S | Kill Confirmed (Well-Worn)",
//            "USP-S | Kill Confirmed (Well-Worn)",
//            "StatTrak™ USP-S | Kill Confirmed (Battle-Scarred)",
//            "USP-S | Kill Confirmed (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ USP-S | Stainless (Factory New)",
//            "USP-S | Stainless (Factory New)",
//            "StatTrak™ USP-S | Stainless (Minimal Wear)",
//            "USP-S | Stainless (Minimal Wear)",
//            "StatTrak™ USP-S | Stainless (Field-Tested)",
//            "USP-S | Stainless (Field-Tested)",
//            "StatTrak™ USP-S | Stainless (Well-Worn)",
//            "USP-S | Stainless (Well-Worn)",
//            "StatTrak™ USP-S | Stainless (Battle-Scarred)",
//            "USP-S | Stainless (Battle-Scarred)",
//
//            // Есть только mw и ft
//            "StatTrak™ USP-S | Dark Water (Minimal Wear)",
//            "USP-S | Dark Water (Minimal Wear)",
//            "StatTrak™ USP-S | Dark Water (Field-Tested)",
//            "USP-S | Dark Water (Field-Tested)",
//
//            // Есть только MW и FT
//            "StatTrak™ M4A1-S | Dark Water (Minimal Wear)",
//            "M4A1-S | Dark Water (Minimal Wear)",
//            "StatTrak™ M4A1-S | Dark Water (Field-Tested)",
//            "M4A1-S | Dark Water (Field-Tested)",
//
//            // Всё ок, но FN мало
//            "StatTrak™ USP-S | Overgrowth (Factory New)",
//            "USP-S | Overgrowth (Factory New)",
//            "StatTrak™ USP-S | Overgrowth (Minimal Wear)",
//            "USP-S | Overgrowth (Minimal Wear)",
//            "StatTrak™ USP-S | Overgrowth (Field-Tested)",
//            "USP-S | Overgrowth (Field-Tested)",
//            "StatTrak™ USP-S | Overgrowth (Well-Worn)",
//            "USP-S | Overgrowth (Well-Worn)",
//            "StatTrak™ USP-S | Overgrowth (Battle-Scarred)",
//            "USP-S | Overgrowth (Battle-Scarred)",
//
//            // Нет статтрек
//            "USP-S | Royal Blue (Factory New)",
//            "USP-S | Royal Blue (Minimal Wear)",
//            "USP-S | Royal Blue (Field-Tested)",
//            "USP-S | Royal Blue (Well-Worn)",
//            "USP-S | Royal Blue (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ USP-S | Blueprint (Factory New)",
//            "USP-S | Blueprint (Factory New)",
//            "StatTrak™ USP-S | Blueprint (Minimal Wear)",
//            "USP-S | Blueprint (Minimal Wear)",
//            "StatTrak™ USP-S | Blueprint (Field-Tested)",
//            "USP-S | Blueprint (Field-Tested)",
//            "StatTrak™ USP-S | Blueprint (Well-Worn)",
//            "USP-S | Blueprint (Well-Worn)",
//            "StatTrak™ USP-S | Blueprint (Battle-Scarred)",
//            "USP-S | Blueprint (Battle-Scarred)",
//
//            // Нет статтрека, WW, BS
//            "USP-S | Pathfinder (Factory New)",
//            "USP-S | Pathfinder (Minimal Wear)",
//            "USP-S | Pathfinder (Field-Tested)",
//
//            // Всё ок
//            "StatTrak™ USP-S | Neo-Noir (Factory New)",
//            "USP-S | Neo-Noir (Factory New)",
//            "StatTrak™ USP-S | Neo-Noir (Minimal Wear)",
//            "USP-S | Neo-Noir (Minimal Wear)",
//            "StatTrak™ USP-S | Neo-Noir (Field-Tested)",
//            "USP-S | Neo-Noir (Field-Tested)",
//            "StatTrak™ USP-S | Neo-Noir (Well-Worn)",
//            "USP-S | Neo-Noir (Well-Worn)",
//            "StatTrak™ USP-S | Neo-Noir (Battle-Scarred)",
//            "USP-S | Neo-Noir (Battle-Scarred)",
//
//            // Нет WW, BS
//            "StatTrak™ USP-S | Serum (Factory New)",
//            "USP-S | Serum (Factory New)",
//            "StatTrak™ USP-S | Serum (Minimal Wear)",
//            "USP-S | Serum (Minimal Wear)",
//            "StatTrak™ USP-S | Serum (Field-Tested)",
//            "USP-S | Serum (Field-Tested)",
//
//            // Всё ок
//            "StatTrak™ USP-S | Monster Mashup (Factory New)",
//            "USP-S | Monster Mashup (Factory New)",
//            "StatTrak™ USP-S | Monster Mashup (Minimal Wear)",
//            "USP-S | Monster Mashup (Minimal Wear)",
//            "StatTrak™ USP-S | Monster Mashup (Field-Tested)",
//            "USP-S | Monster Mashup (Field-Tested)",
//            "StatTrak™ USP-S | Monster Mashup (Well-Worn)",
//            "USP-S | Monster Mashup (Well-Worn)",
//            "StatTrak™ USP-S | Monster Mashup (Battle-Scarred)",
//            "USP-S | Monster Mashup (Battle-Scarred)"

//            // Всё ок
//            "StatTrak™ AK-47 | Legion of Anubis (Factory New)",
//            "AK-47 | Legion of Anubis (Factory New)",
//            "StatTrak™ AK-47 | Legion of Anubis (Minimal Wear)",
//            "AK-47 | Legion of Anubis (Minimal Wear)",
//            "StatTrak™ AK-47 | Legion of Anubis (Field-Tested)",
//            "AK-47 | Legion of Anubis (Field-Tested)",
//            "StatTrak™ AK-47 | Legion of Anubis (Well-Worn)",
//            "AK-47 | Legion of Anubis (Well-Worn)",
//            "StatTrak™ AK-47 | Legion of Anubis (Battle-Scarred)",
//            "AK-47 | Legion of Anubis (Battle-Scarred)",
//
//            // Всё ок
//            "StatTrak™ AWP | Atheris (Factory New)",
//            "AWP | Atheris (Factory New)",
//            "StatTrak™ AWP | Atheris (Minimal Wear)",
//            "AWP | Atheris (Minimal Wear)",
//            "StatTrak™ AWP | Atheris (Field-Tested)",
//            "AWP | Atheris (Field-Tested)",
//            "StatTrak™ AWP | Atheris (Well-Worn)",
//            "AWP | Atheris (Well-Worn)",
//            "StatTrak™ AWP | Atheris (Battle-Scarred)",
//            "AWP | Atheris (Battle-Scarred)",
//
//            // WW и BS просто нету
//            "StatTrak™ Desert Eagle | Conspiracy (Factory New)",
//            "Desert Eagle | Conspiracy (Factory New)",
//            "StatTrak™ Desert Eagle | Conspiracy (Minimal Wear)",
//            "Desert Eagle | Conspiracy (Minimal Wear)",
//            "StatTrak™ Desert Eagle | Conspiracy (Field-Tested)",
//            "Desert Eagle | Conspiracy (Field-Tested)",
//
//            // Всё ок
//            "StatTrak™ M4A1-S | Briefing (Factory New)",
//            "M4A1-S | Briefing (Factory New)",
//            "StatTrak™ M4A1-S | Briefing (Minimal Wear)",
//            "M4A1-S | Briefing (Minimal Wear)",
//            "StatTrak™ M4A1-S | Briefing (Field-Tested)",
//            "M4A1-S | Briefing (Field-Tested)",
//            "StatTrak™ M4A1-S | Briefing (Well-Worn)",
//            "M4A1-S | Briefing (Well-Worn)",
//            "StatTrak™ M4A1-S | Briefing (Battle-Scarred)",
//            "M4A1-S | Briefing (Battle-Scarred)",
//
            // Всё ок
            "StatTrak™ USP-S | Cortex (Factory New)",
            "USP-S | Cortex (Factory New)",
            "StatTrak™ USP-S | Cortex (Minimal Wear)",
            "USP-S | Cortex (Minimal Wear)",
            "StatTrak™ USP-S | Cortex (Field-Tested)",
            "USP-S | Cortex (Field-Tested)",
            "StatTrak™ USP-S | Cortex (Well-Worn)",
            "USP-S | Cortex (Well-Worn)",
            "StatTrak™ USP-S | Cortex (Battle-Scarred)",
            "USP-S | Cortex (Battle-Scarred)",

            // Нет статтрек
            "Desert Eagle | Midnight Storm (Factory New)",
            "Desert Eagle | Midnight Storm (Minimal Wear)",
            "Desert Eagle | Midnight Storm (Field-Tested)",
            "Desert Eagle | Midnight Storm (Well-Worn)",
            "Desert Eagle | Midnight Storm (Battle-Scarred)"

    );

    private final List<String> activeNamesWithoutExterior = List.of(

            // это дешевые, можно по 100
//            "AK-47 | Blue Laminate"
//            "AK-47 | Slate"
//            "SG 553 | Dragon Tech"
    );

    @Value("${generators.real-active-name}")
    private Boolean generatorRealActiveNameEnable;

    @PostConstruct
    public void generateData() {
        generateRealActiveNames();
    }

    public void generateRealActiveNames() {
        if (generatorRealActiveNameEnable) {
            activeNameRepository.deleteAll();
            lotRepository.deleteAll();
            lotStickerRepository.deleteAll();
            log.warn("ActiveName tables (real data) filled");
            for (String activeName : activeNames) {
                activeNameRepository.save(
                        new ActiveName(activeName, 100, defaultActiveNameParsePeriod)
                );
            }
            // вот это может создать несуществующий айтем
//            for (String activeNameWithoutExterior : activeNamesWithoutExterior) {
//                for (String exterior : exteriorList) {
//                    activeNameRepository.save(
//                            new ActiveName(activeNameWithoutExterior + " " + exterior, 100, defaultActiveNameParsePeriod)
//                    );
//                }
//            }
        }
    }
}
