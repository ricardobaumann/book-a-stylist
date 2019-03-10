package com.github.ricardobaumann.bookastylist.projections;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SlotCountProjection {
    private final Integer slotNumber;
    private final Long count;
}
