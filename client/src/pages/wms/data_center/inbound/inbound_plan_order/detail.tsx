const detailColumns = [
    {
        name: "inboundPlanOrderId",
        label: "入库通知单ID",
        hidden: true
    },
    {
        name: "batchAttributes",
        label: "table.batchAttributes"
    },
    {
        name: "boxNo",
        label: "table.lpnNumber"
    },
    {
        name: "brand",
        label: "table.brand"
    },
    {
        name: "color",
        label: "table.color"
    },
    {
        name: "qtyAbnormal",
        label: "skuArea.qtyAbnormal"
    },
    {
        name: "qtyAccepted",
        label: "table.acceptanceQuantity"
    },
    {
        name: "qtyRestocked",
        label: "table.plannedQuantity"
    },
    {
        name: "qtyUnreceived",
        label: "table.unreceivedQuantity"
    },
    {
        name: "responsibleParty",
        label: "table.responsibleParty"
    },
    {
        name: "size",
        label: "table.size"
    },
    {
        name: "skuCode",
        label: "table.skuCode"
    },
    {
        name: "skuName",
        label: "table.skuName"
    },
    {
        name: "ownerCode",
        label: "table.productOwner"
    },
    {
        name: "style",
        label: "table.style"
    }
]

export const detailDialog = {
    title: "table.inboundPlanDetails",
    actions: [],
    closeOnEsc: true,
    closeOnOutside: true,
    size: "xl",
    body: [
        {
            type: "crud",
            syncLocation: false,
            name: "inboundPlanOrderDetailTable",
            api: {
                method: "POST",
                url: "/search/search?page=${page}&perPage=${perPage}&inboundPlanOrderId=${id}&inboundPlanOrderId-op=eq",
                dataType: "application/json"
            },
            defaultParams: {
                searchIdentity: "WInboundPlanOrderDetail",
                showColumns: detailColumns
            },
            footerToolbar: ["switch-per-page", "statistics", "pagination"],
            columns: detailColumns
        }
    ]
}
