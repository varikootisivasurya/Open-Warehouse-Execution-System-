import schema2component from "@/utils/schema2component"
import {
    create_update_columns,
    enable_options,
    true_false_options
} from "@/utils/commonContants"
import { owner_code } from "@/pages/wms/constants/select_search_api_contant"
import {
    api_batch_attribute_add,
    api_batch_attribute_get,
    api_batch_attribute_update
} from "@/pages/wms/config_center/constants/api_constant"
import { api_crud_search } from "@/pages/constantApi"

const form = [
    {
        type: "hidden",
        name: "id"
    },
    {
        type: "hidden",
        name: "version"
    },
    {
        label: "table.ruleEncoding",
        type: "input-text",
        name: "code",
        required: true
    },
    {
        label: "table.ruleName",
        type: "input-text",
        name: "name",
        required: true
    },
    {
        type: "select",
        name: "ownerCode",
        label: "table.productOwner",
        source: owner_code
    },
    {
        label: "table.SKUCategories",
        type: "select",
        name: "skuFirstCategory",
        source: "${dictionary.SkuFirstCategory}"
    },
    {
        name: "enable",
        type: "switch",
        label: "table.whetherEnabled"
    },
    {
        type: "input-table",
        name: "batchAttributeFieldConfigs",
        addable: true,
        editable: true,
        removable: true,
        showFooterAddBtn: false,
        value: [
            {
                fieldCode: "inboundDate",
                fieldName: "table.inboundDate"
            },
            {
                fieldCode: "productDate",
                fieldName: "table.dateOfManufacture"
            },
            {
                fieldCode: "expiredDate",
                fieldName: "table.ExpirationDate"
            }
        ],
        columns: [
            {
                name: "fieldCode",
                label: "table.batchAttributeCode",
                type: "input-text",
                required: true
            },
            {
                name: "fieldName",
                label: "table.batchAttributes",
                type: "input-text"
            },
            {
                name: "required",
                label: "table.required",
                type: "switch",
                options: true_false_options
            },
            {
                name: "keyAttribute",
                label: "table.keyAttributes",
                type: "switch",
                options: true_false_options
            },
            {
                name: "exactMatch",
                label: "table.preciseMatch",
                type: "switch",
                options: true_false_options
            },
            {
                name: "enable",
                label: "table.enable",
                type: "switch",
                options: enable_options
            }
        ]
    }
]

const add = {
    type: "button",
    actionType: "dialog",
    icon: "fa fa-plus",
    label: "button.add",
    dialog: {
        title: "button.add",
        size: "lg",
        closeOnEsc: true,
        body: {
            type: "form",
            api: api_batch_attribute_add,
            body: form
        }
    }
}

const columns = [
    {
        name: "id",
        label: "ID",
        hidden: true
    },
    {
        name: "version",
        label: "Version",
        hidden: true
    },
    {
        name: "code",
        label: "table.ruleEncoding",
        searchable: true
    },
    {
        name: "name",
        label: "table.ruleName",
        searchable: true
    },
    {
        name: "ownerCode",
        label: "table.productOwner",
        type: "mapping",
        source: owner_code,
        searchable: {
            type: "select",
            source: owner_code
        }
    },
    {
        name: "skuFirstCategory",
        label: "table.SKUCategories",
        type: "mapping",
        source: "${dictionary.SkuFirstCategory}",
        searchable: {
            type: "select",
            source: "${dictionary.SkuFirstCategory}"
        }
    },
    {
        name: "enable",
        label: "table.enable",
        type: "mapping",
        map: enable_options
    },
    ...create_update_columns
]

const searchIdentity = "MBatchAttributeConfig"
const showColumns = columns

const schema = {
    type: "page",
    title: "batchManagement.title",
    toolbar: [],
    data: {
        dictionary: "${ls:dictionary}"
    },
    body: [
        {
            type: "crud",
            syncLocation: false,
            name: "batchAttributeTable",
            api: api_crud_search,
            defaultParams: {
                searchIdentity: searchIdentity,
                showColumns: showColumns,
                searchObject: {
                    orderBy: "update_time desc"
                }
            },
            autoFillHeight: true,
            autoGenerateFilter: {
                columnsNum: 3,
                showBtnToolbar: true
            },
            headerToolbar: [add],
            footerToolbar: ["switch-per-page", "statistics", "pagination"],
            columns: [
                ...columns,
                {
                    type: "operation",
                    label: "table.operation",
                    width: 100,
                    buttons: [
                        {
                            label: "button.modify",
                            type: "button",
                            actionType: "dialog",
                            dialog: {
                                title: "button.modify",
                                size: "lg",
                                closeOnEsc: true,
                                closeOnOutside: true,
                                body: {
                                    type: "form",
                                    initApi: api_batch_attribute_get,
                                    api: api_batch_attribute_update,
                                    controls: form
                                }
                            }
                        }
                    ],
                    toggled: true
                }
            ]
        }
    ]
}

export default schema2component(schema)
