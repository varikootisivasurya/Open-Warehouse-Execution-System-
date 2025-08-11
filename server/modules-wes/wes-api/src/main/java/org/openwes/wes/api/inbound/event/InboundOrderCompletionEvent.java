package org.openwes.wes.api.inbound.event;

import org.openwes.domain.event.api.DomainEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class InboundOrderCompletionEvent extends DomainEvent {
    private Long inboundOrderId;
}
