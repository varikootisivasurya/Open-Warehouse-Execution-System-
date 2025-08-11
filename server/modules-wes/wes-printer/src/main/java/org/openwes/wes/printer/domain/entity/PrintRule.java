package org.openwes.wes.printer.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.openwes.common.utils.base.UpdateUserPO;
import org.openwes.common.utils.id.IdGenerator;
import org.openwes.wes.api.print.constants.ModuleEnum;
import org.openwes.wes.api.print.constants.PrintNodeEnum;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@Table(name = "p_print_rule",
        indexes = {
                @Index(unique = true, name = "uk_print_rule_code", columnList = "ruleCode,deleteTime")
        })
@Filter(name = "filterDeleted", condition = "deleted = false")
public class PrintRule extends UpdateUserPO {

    @Id
    @GeneratedValue(generator = "databaseIdGenerator")
    @GenericGenerator(name = "databaseIdGenerator", type = IdGenerator.class)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(128) default '' comment '规则名称'")
    private String ruleName;

    @Column(nullable = false, columnDefinition = "varchar(64) comment '规则编码'")
    private String ruleCode;

    @Column(columnDefinition = "json comment '货主编码'")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> ownerCodes;

    @Column(columnDefinition = "json comment '销售平台'")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> salesPlatforms;

    @Column(columnDefinition = "json comment '承运商编码'")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> carrierCodes;

    @Column(columnDefinition = "json comment '入库单类型'")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> inboundOrderTypes;

    @Column(columnDefinition = "json comment '出库单类型'")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> outboundOrderTypes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(64) comment '模块'")
    private ModuleEnum module;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(64) comment '打印节点'")
    private PrintNodeEnum printNode;

    @Column(nullable = false, columnDefinition = "int default 1 comment '打印份数'")
    private Integer printCount = 1;

    @Column(nullable = false, columnDefinition = "varchar(255) default '' comment '模板编码'")
    private String templateCode;

    @Column(columnDefinition = "text comment 'sql script that query target args for the template'")
    private String sqlScript;

    private boolean deleted;
    @Column(nullable = false, columnDefinition = "bigint default 0 comment '删除时间'")
    private Long deleteTime = 0L;

    private boolean enabled;

    public void delete() {
        if (this.enabled) {
            throw new IllegalStateException("enabled rule can not delete");
        }
        this.deleted = true;
        this.deleteTime = System.currentTimeMillis();
    }
}
