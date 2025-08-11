package org.openwes.wes.api.outbound.event;

import org.openwes.domain.event.api.DomainEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OutboundPlanOrderCompleteEvent extends DomainEvent {
    private List<Long> outboundPlanOrderIds;
}
