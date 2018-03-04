export default {
    dic: ['GRADE', 'SEX'],
    column: [
        {
            label: "用户名",
            prop: "username",
            rules: [{ required: true, message: "请输入用户名", trigger: "blur" }]
        },
        {
            label: "姓名",
            prop: "name"
        },
        {
            label: "类型",
            prop: "type",
            type: "select",
            dicData: 'GRADE'
        },
        {
            label: "性别",
            prop: "sex",
            type: "radio",
            dicData: 'SEX'
        },
        {
            label: "权限",
            prop: "grade",
            span: 24,
            type: "checkbox",
            dicData: 'GRADE'
        },
        {
            label: "地址",
            disabled: true,
            span: 24,
            prop: "address",
        }
    ]
};