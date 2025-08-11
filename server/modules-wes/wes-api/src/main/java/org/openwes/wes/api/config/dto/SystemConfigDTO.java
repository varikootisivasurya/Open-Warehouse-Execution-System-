package org.openwes.wes.api.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.openwes.wes.api.config.constants.TransferContainerReleaseMethodEnum;

import java.io.Serializable;

import static org.openwes.wes.api.config.constants.TransferContainerReleaseMethodEnum.AUTOMATED;

@Data
public class SystemConfigDTO implements Serializable {

    private Long id;
    private BasicConfigDTO basicConfig;
    private EmsConfigDTO emsConfig;
    private InboundConfigDTO inboundConfig;
    private OutboundConfigDTO outboundConfig;
    private OutboundAlgoConfigDTO outboundAlgoConfig;
    private StockConfigDTO stockConfig;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BasicConfigDTO implements Serializable {
        private TransferContainerReleaseMethodEnum transferContainerReleaseMethod = AUTOMATED;
        private int autoReleaseDelayTimeMin = 30;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmsConfigDTO implements Serializable {
        private boolean allowBatchCreateContainerTasks;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutboundConfigDTO implements Serializable {
        private boolean checkRepeatedCustomerOrderNo;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StockConfigDTO implements Serializable {

        private boolean stockAbnormalAutoCreateAdjustmentOrder;
        private boolean adjustmentOrderAutoCompleteAdjustment;

        private int zeroStockSavedDays = 7;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutboundAlgoConfigDTO implements Serializable {

        private boolean useLocalAlgorithm = true;

        // 算法超时时间
        private Integer cutoffTime;

        // 算法模式
        private String mode;

        private Double shareRackPoolMaxStationDistance;
        private Integer maxHitNum;
        private String orderDispatchStrategy;
        private Integer orderDispatchBalanceOffset;
        private String firstHitRackSide;
        private int maxOnTheWayRackNum;
        private String taskBalanceDimension;
        private String algoName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InboundConfigDTO implements Serializable {

        private boolean checkRepeatedCustomerOrderNo;
        private boolean checkRepeatedLpnCode;
    }

    private Long version;

}
