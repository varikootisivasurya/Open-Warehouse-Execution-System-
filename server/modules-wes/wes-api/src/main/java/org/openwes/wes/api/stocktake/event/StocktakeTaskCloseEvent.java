package org.openwes.wes.api.stocktake.event;

import org.openwes.domain.event.api.DomainEvent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StocktakeTaskCloseEvent extends DomainEvent {

    @NotNull
    private Long stocktakeTaskId;

    @NotNull
    private Long stocktakeOrderId;
}
