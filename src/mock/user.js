import Mock from 'mockjs'
export const userInfo = {
    userInfo: {
        username: 'admin',
        name: 'avue',
    },
    roles: ['admin'],
    permission: [
        'sys_crud_btn_add',
        'sys_crud_btn_export',
        'sys_menu_btn_add',
        'sys_menu_btn_edit',
        'sys_menu_btn_del',
        'sys_role_btn1',
        'sys_role_btn2',
        'sys_role_btn3',
        'sys_role_btn4',
        'sys_role_btn5',
        'sys_role_btn6',
    ],//权限级别
}
let List = []
for (let i = 0; i < 5; i++) {
    List.push(Mock.mock({
        id: '@increment',
        name: Mock.mock('@cname'),
        username: Mock.mock('@last'),
        'type|0-1': 0,
        'sex|0-1': 0,
        grade: [0, 1],
        address: Mock.mock('@cparagraph(1, 3)'),
        check: [1, 3, 4]
    }))
}
export const tableData = {
    total: 11,
    pageSize: 10,
    tableData: List
};

