const DIC = {
  vaild: [{
    label: '否',
    value: '0'
  },
    {
      label: '是',
      value: '1'
    }
  ]
}
export const tableOption = {
  "border": true,
  "index": true,
  "stripe": true,
  "menuAlign": "center",
  "align": "center",
  "editBtn": false,
  "delBtn": false,
  "dic": [],
  "column": [{
    width: 150,
    label: "编号",
    prop: "clientId",
    align: 'center',
    sortable: true,
    rules: [{
      required: true,
      message: "请输入clientId",
      trigger: "blur"
    }]
  }, {
    width: 300,
    label: "密钥",
    prop: "clientSecret",
    align: 'center',
    sortable: true,
    rules: [{
      required: true,
      message: "请输入clientSecret",
      trigger: "blur"
    }]
  }, {
    label: "域",
    prop: "scope",
    align: 'center',
    width: 150,
    rules: [{
      required: true,
      message: "请输入scope",
      trigger: "blur"
    }]
  }, {
    label: "授权模式",
    prop: "authorizedGrantTypes",
    align: 'center',
    width: 150,
    hide: true,
    rules: [{
      required: true,
      message: "请输入授权模式",
      trigger: "blur"
    }]
  }, {
    label: "回调地址",
    prop: "webServerRedirectUri",
    align: 'center',
    width: 150,
    hide: true,
  }, {
    label: "权限",
    prop: "authorities",
    align: 'center',
    width: 150,
    hide: true,
  }, {
    label: "请求令牌",
    prop: "accessTokenValidity",
    align: 'center',
    width: 150,
    hide: true,
  }, {
    label: "刷新令牌",
    prop: "refreshTokenValidity",
    align: 'center',
    width: 150,
    hide: true,
  }, {
    label: "扩展信息",
    prop: "additionalInformation",
    align: 'center',
    width: 150,
    hide: true,
  }, {
    label: "自动放行",
    prop: "autoapprove",
    align: 'center',
    type: 'radio',
    dicData:DIC.vaild,
    width: 150,
    rules: [{
      required: true,
      message: "请选择是否放行",
      trigger: "blur"
    }]
  },{
    label: "资源ID",
    prop: "resourceIds",
    align: 'center',
    width: 150,
  }]
}
