package ru.griga.tickets.model.itinerary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;
import ru.griga.tickets.model.place.base.Place;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity
@EqualsAndHashCode
public class Itinerary {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;

    @StartNode
    private Place source;

    @EndNode
    private Place destination;

    // Цена в валюте currencyCode
    @EqualsAndHashCode.Exclude
    private BigDecimal cost;

    // Цена в стабильной валюте, которая скорее всего будет неизменна на время ttl
    private BigDecimal baseCost;

    private String currencyCode;

    // Компания-перевозчик или распостранитель билетов
    // Используем ID для реюза классов Carrier
    private String carrierCode;

    // Дата и время прилета / вылета
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime; // todo тут время лежит в UTC+0. Можно его на морду так и отдавать а там конвертить уже в пояс, или вообще забить

    // Дата и время получения билета
    // Исключаем из EAHC, т.к. можно найти один и тот же билет 2 разными пуллами
    @EqualsAndHashCode.Exclude
    private LocalDateTime foundAt;

    // Время жизни записи, после которого запись признаётся невалидной (секунды)
    @EqualsAndHashCode.Exclude
    private long ttl;

    /**
     * Устарел ли перелет (необходимо обновление)
     * @return true, если перелет неактуален и должен быть удален из базы;
     * в другом случае false
     */
    public boolean isObsolete() {
        return ChronoUnit.SECONDS.between(this.foundAt, LocalDateTime.now()) > this.ttl;
    }

}