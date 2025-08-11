import schema2component from "@/utils/schema2component"
import { api_crud_search } from "@/pages/constantApi"
import {api_api_log_get} from "@/pages/api_platform/constants/api_constant";

const form = [
    {
        type: "hidden",
        name: "id"
    },
    {
        type: "input-text",
        name: "apiCode",
        label: "interfacePlatform.interfaceManagement.table.interfaceCode",
        readOnly: true
    },
    {
        type: "input-text",
        name: "costTime",
        label: "interfacePlatform.interfaceLogs.table.duration(ms)",
        readOnly: true
    },
    {
        type: "input-text",
        name: "retryCount",
        label: "interfacePlatform.interfaceLogs.table.numberOfRetries",
        readOnly: true
    },
    {
        type: "input-text",
        name: "status",
        label: "table.status",
        readOnly: true
    },
    {
        type: "textarea",
        name: "requestData",
        label: "interfacePlatform.interfaceLogs.from.requestPackets",
        maxRows: 10,
        readOnly: true
    },
    {
        type: "textarea",
        name: "responseData",
        label: "interfacePlatform.interfaceLogs.from.responsePackets",
        maxRows: 10,
        readOnly: true
    }
]

const columns = [
    {
        name: "id",
        label: "ID",
        hidden: true
    },
    {
        name: "apiCode",
        label: "interfacePlatform.interfaceManagement.table.interfaceCode",
        searchable: true
    },
    {
        name: "costTime",
        label: "interfacePlatform.interfaceLogs.table.duration(ms)"
    },
    {
        name: "retryCount",
        label: "interfacePlatform.interfaceLogs.table.numberOfRetries"
    },
    {
        name: "status",
        label: "table.status",
        type: "mapping",
        source: "${dictionary.ApiLogStatus}",
        searchable: {
            type: "select",
            source: "${dictionary.ApiLogStatus}"
        }
    },
    {
        label: "table.createdBy",
        name: "createUser"
    },
    {
        name: "createTime",
        label: "table.creationTime",
        tpl: "${createTime/1000|date:YYYY-MM-DD HH\\:mm\\:ss}",
        searchable: {
            type: "input-date-range",
            valueFormat: "x"
        }
    }
]

const searchIdentity = "AApiLog"
const showColumns = columns

const schema = {
    type: "page",
    title: "interfacePlatform.interfaceLogs.title",
    toolbar: [],
    data: {
        dictionary: "${ls:dictionary}"
    },
    body: [
        {
            type: "crud",
            syncLocation: false,
            name: "ApiLogTable",
            api: api_crud_search,
            defaultParams: {
                searchIdentity: searchIdentity,
                showColumns: showColumns,
                searchObject: {
                    orderBy: "create_time desc"
                }
            },
            autoFillHeight: true,
            autoGenerateFilter: {
                columnsNum: 3,
                showBtnToolbar: true
            },
            headerToolbar: ["reload"],
            footerToolbar: ["switch-per-page", "statistics", "pagination"],
            columns: [
                ...columns,
                {
                    type: "operation",
                    label: "table.operation",
                    width: 230,
                    buttons: [
                        {
                            label: "button.detail",
                            type: "button",
                            level: "link",
                            actionType: "dialog",
                            dialog: {
                                title: "button.detail",
                                size: "lg",
                                closeOnEsc: true,
                                closeOnOutside: true,
                                body: {
                                    type: "form",
                                    initApi: api_api_log_get,
                                    body: form
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
