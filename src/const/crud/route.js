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
    "label": "编号",
    "prop": "id",
    align: 'center',
    sortable: true,
    hide: true
  }, {
    width: 150,
    "label": "服务名称",
    "prop": "serviceId",
    align: 'center',
    sortable: true,
    rules: [{
      required: true,
      message: "请输入服务名称",
      trigger: "blur"
    }]
  }, {
    "label": "匹配路径",
    "prop": "path",
    align: 'center',
    width: 150,
  }, {
    "label": "转发地址",
    "prop": "url",
    align: 'center',
    width: 150
  }, {
    "label": "去掉前缀",
    "prop": "stripPrefix",
    align: 'center',
    width: 150,
    type: 'radio',
    dicData: [{
      label: '否',
      value: false
    }, {
      label: '是',
      value: true
    }]
  }, {
    "label": "是否重试",
    "prop": "retryable",
    align: 'center',
    width: 150,
    type: 'radio',
    dicData: [{
      label: '否',
      value: false
    }, {
      label: '是',
      value: true
    }]
  },{
    "label": "是否启用",
    "prop": "enabled",
    align: 'center',
    width: 150,
    type: 'radio',
    dicData: [{
      label: '否',
      value: false
    }, {
      label: '是',
      value: true
    }]
  }, {
    "label": "敏感头",
    "prop": "sensitiveheadersList",
    align: 'center',
    width: 200,
    hide: true
  },{
    "label": "创建时间",
    "prop": "createTime",
    format: 'yyyy-MM-dd hh:mm:ss',
    align: 'center',
    width: 200,
    hide: true
  }]
}
