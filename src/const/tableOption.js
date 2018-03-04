export default {
    border: true,
    index: true,
    selection: true,
    dic: ['GRADE', 'SEX'],
    column: [
        {
            label: "用户名",
            prop: "username",
            width: "150",
            dataDetail: val => {
                return `<span class="el-tag el-tag--success">${val}</span>`;
            },
            rules: [{ required: true, message: "请输入用户名", trigger: "blur" }]
        },
        {
            label: "姓名",
            prop: "name"
        },
        {
            label: "类型",
            prop: "type",
            dataDetail: val => {
                return `<span class="el-tag">${val}</span>`;
            },
            type: "select",
            dicData: 'GRADE'
        },
        {
            label: "权限",
            prop: "grade",
            type: "checkbox",
            dicData: 'GRADE'
        },
        {
            label: "地址",
            prop: "address",
            width: "300",
            span: 24,
            overHidden: true
        }
    ]
};