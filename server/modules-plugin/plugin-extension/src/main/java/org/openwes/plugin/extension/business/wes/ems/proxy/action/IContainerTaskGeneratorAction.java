package org.openwes.plugin.extension.business.wes.ems.proxy.action;

import org.openwes.wes.api.ems.proxy.dto.ContainerTaskDTO;
import org.openwes.wes.api.ems.proxy.dto.CreateContainerTaskDTO;

import java.util.Collections;
import java.util.List;

public interface IContainerTaskGeneratorAction {

    default List<ContainerTaskDTO> generateContainerTasks(List<CreateContainerTaskDTO> createContainerTaskDTOs) {
        return Collections.emptyList();
    }

}
