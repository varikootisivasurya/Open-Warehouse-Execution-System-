package org.openwes.api.platform.application.service.handler.request.wms;

import org.openwes.api.platform.api.constants.ApiTypeEnum;
import org.openwes.api.platform.api.dto.request.RequestHandleContext;
import org.openwes.api.platform.application.service.handler.AbstractRequestHandler;
import org.openwes.wes.api.task.dto.TransferContainerReleaseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReleaseTransferContainerHandler extends AbstractRequestHandler {

    @Override
    public String getApiType() {
        return ApiTypeEnum.TRANSFER_CONTAINER_RELEASE.name();
    }

    @Override
    public void invoke(RequestHandleContext context) {
        List<TransferContainerReleaseDTO> releaseDTOS = getTargetList(context, TransferContainerReleaseDTO.class);
        coreClientService.transferContainerRelease(releaseDTOS);
    }
}
