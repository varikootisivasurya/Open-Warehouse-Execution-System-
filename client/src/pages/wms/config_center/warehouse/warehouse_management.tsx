import schema2component from "@/utils/schema2component"
import {
    api_warehouse_add,
    api_warehouse_get,
    api_warehouse_update
} from "@/pages/wms/config_center/constants/api_constant"
import {
    create_update_columns,
    true_false_options
} from "@/utils/commonContants"
import {
    city,
    country,
    district,
    province
} from "@/pages/wms/constants/select_search_api_contant"
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
        label: "table.warehouseCode",
        type: "input-text",
        name: "warehouseCode",
        maxLength: 64,
        required: true,
        disabledOn: "data.mode === 'edit'" // 修改时禁用
    },
    {
        label: "table.warehouseName",
        type: "input-text",
        name: "warehouseName",
        maxLength: 128,
        required: true
    },
    {
        label: "table.warehouseType",
        type: "select",
        name: "warehouseType",
        source: "${dictionary.WarehouseType}",
        required: true
    },
    {
        label: "table.warehouseProperties",
        type: "select",
        name: "warehouseAttrType",
        source: "${dictionary.WarehouseAttrType}",
        required: true
    },
    {
        label: "table.warehouseLevel",
        type: "select",
        name: "warehouseLevel",
        source: "${dictionary.WarehouseLevel}",
        required: true
    },
    {
        label: "table.warehouseLables",
        type: "select",
        name: "warehouseLabel",
        source: "${dictionary.WarehouseLabel}",
        maxLength: 64
    },
    {
        label: "table.mainBusiness",
        type: "select",
        name: "businessType",
        source: "${dictionary.WarehouseBusinessType}",
        required: true
    },
    {
        label: "table.warehouseStructure",
        type: "select",
        name: "structureType",
        source: "${dictionary.WarehouseStructureType}",
        required: true
    },
    {
        label: "table.warehouseArea",
        type: "input-number",
        name: "area",
        minimum: 0
    },
    {
        label: "table.warehouseFloorHeight",
        type: "input-number",
        name: "height",
        minimum: 0
    },
    {
        label: "table.virtualWarehouse",
        type: "switch",
        name: "virtualWarehouse",
        value: false
    },

    // 地址信息
    {
        label: "table.country",
        type: "select",
        name: "addressDTO.country",
        source: country
    },
    {
        label: "table.province",
        type: "select",
        name: "addressDTO.province",
        initFetchOn: "data.addressDTO.country",
        source: {
            ...province,
            url: province.url + "&country-op=eq&country=${addressDTO.country}"
        }
    },
    {
        label: "table.city",
        type: "select",
        name: "addressDTO.city",
        initFetchOn: "data.addressDTO.province",
        source: {
            ...city,
            url:
                city.url +
                "&country-op=eq&country=${addressDTO.country}&province-op=eq&province=${addressDTO.province}"
        }
    },
    {
        label: "table.district/county",
        type: "select",
        name: "addressDTO.district",
        initFetchOn: "data.addressDTO.city",
        source: {
            ...district,
            url:
                district.url +
                "&country-op=eq&country=${addressDTO.country}&province-op=eq&province=${addressDTO.province}&city-op=eq&city=${addressDTO.city}"
        }
    },
    {
        label: "table.fullAddress",
        type: "input-text",
        name: "addressDTO.address"
    },

    // 联系方式
    {
        label: "table.contact",
        type: "input-text",
        name: "contactorDTO.name"
    },
    {
        label: "table.phoneNumber",
        type: "input-text",
        name: "contactorDTO.tel",
        validations: {
            isTelNumber: true
        }
    },
    {
        label: "table.email",
        type: "input-text",
        name: "contactorDTO.mail",
        validations: {
            isEmail: true
        }
    },
    {
        label: "table.fax",
        type: "input-text",
        name: "contactorDTO.fax",
        validations: {
            isTelNumber: true
        }
    }
]

const add = {
    type: "button",
    actionType: "drawer",
    icon: "fa fa-plus",
    label: "button.add",
    target: "role",
    drawer: {
        title: "button.add",
        closeOnEsc: true,
        body: {
            type: "form",
            api: api_warehouse_add,
            body: form,
            data: { mode: "add" }
        }
    },
    reload: "role"
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
        name: "warehouseCode",
        label: "table.warehouseCode",
        searchable: true
    },
    {
        name: "warehouseName",
        label: "table.warehouseName",
        searchable: true
    },
    {
        name: "warehouseType",
        label: "table.warehouseType",
        type: "mapping",
        source: "${dictionary.WarehouseType}",
        searchable: {
            type: "select",
            source: "${dictionary.WarehouseType}"
        }
    },
    {
        name: "warehouseAttrType",
        label: "table.warehouseProperties",
        type: "mapping",
        source: "${dictionary.WarehouseAttrType}",
        searchable: {
            type: "select",
            source: "${dictionary.WarehouseAttrType}"
        }
    },
    {
        name: "warehouseLevel",
        label: "table.warehouseLevel",
        type: "mapping",
        source: "${dictionary.WarehouseLevel}",
        searchable: {
            type: "select",
            source: "${dictionary.WarehouseLevel}"
        }
    },
    {
        name: "warehouseLabel",
        label: "table.warehouseLables",
        type: "mapping",
        source: "${dictionary.WarehouseLabel}",
        searchable: {
            type: "select",
            source: "${dictionary.WarehouseLabel}"
        }
    },
    {
        name: "businessType",
        label: "table.mainBusiness",
        type: "mapping",
        source: "${dictionary.WarehouseBusinessType}",
        searchable: {
            type: "select",
            source: "${dictionary.WarehouseBusinessType}"
        }
    },
    {
        name: "structureType",
        label: "table.warehouseStructure",
        type: "mapping",
        source: "${dictionary.WarehouseStructureType}",
        searchable: {
            type: "select",
            source: "${dictionary.WarehouseStructureType}"
        }
    },
    {
        name: "virtualWarehouse",
        label: "table.virtualWarehouse",
        type: "mapping",
        map: true_false_options
    },
    {
        name: "country",
        label: "table.country"
    },
    {
        name: "province",
        label: "table.province"
    },
    {
        name: "city",
        label: "table.city"
    },
    {
        name: "district",
        label: "table.district/county"
    },
    {
        name: "name",
        label: "table.contact"
    },
    {
        name: "tel",
        label: "table.phoneNumber"
    },
    {
        name: "mail",
        label: "table.email"
    },
    {
        name: "fax",
        label: "table.fax"
    },
    ...create_update_columns
]

const searchIdentity = "MWarehouseMainData"
const showColumns = columns

const schema = {
    type: "page",
    title: "warehouseManagement.title",
    toolbar: [],
    data: {
        dictionary: "${ls:dictionary}"
    },
    body: [
        {
            type: "crud",
            syncLocation: false,
            name: "role",
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
                    width: 150,
                    buttons: [
                        {
                            label: "button.modify",
                            type: "button",
                            actionType: "drawer",
                            drawer: {
                                title: "button.modify",
                                closeOnEsc: true,
                                closeOnOutside: true,
                                body: {
                                    type: "form",
                                    initApi: api_warehouse_get,
                                    api: api_warehouse_update,
                                    body: form
                                }
                            },
                            reload: "role"
                        }
                    ],
                    toggled: true
                }
            ]
        }
    ]
}

export default schema2component(schema)
