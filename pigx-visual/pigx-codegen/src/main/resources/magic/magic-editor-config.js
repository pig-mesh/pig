var MAGIC_EDITOR_CONFIG = {
    title: '接口低代码设计平台',
    theme: 'dark',
    defaultExpand: true,
    checkUpdate: false,
    jdbcDrivers: ['com.mysql.cj.jdbc.Driver', 'com.mysql.jdbc.Driver', 'oracle.jdbc.driver.OracleDriver', 'org.postgresql.Driver', 'com.microsoft.sqlserver.jdbc.SQLServerDriver'],
    blockClose: true,   // 是否阻止离开页面
    autoSave: true,
    decorationTimeout: 10000,
    logMaxRows: Infinity,
    editorFontFamily: 'JetBrainsMono, Consolas, "Courier New",monospace, 微软雅黑',
    editorFontSize: 14,
    fontLigatures: true,
    header: {
        skin: true, document: true, repo: true, qqGroup: true
    },
    request: {
        beforeSend: function (config) {
            let tenant_id = new URL(window.location.href).searchParams.get("tenant_id");
            config.headers["TENANT-ID"] = tenant_id === null ? 1 : tenant_id

            let access_token = new URL(window.location.href).searchParams.get("access_token");
            if (access_token !== null) {
                config.headers["Authorization"] = `Bearer ${access_token}`
            }
            return config;
        }, onError: function (err) {
            return Promise.reject(err)
        }
    },
    response: {
        onSuccess: function (resp) {
            if (resp.data.code === -1) {
                window.location.href = '/'
            }
            return resp;
        }, onError: function (err) {
            console.log('请求失败', err)
            return Promise.reject(err)
        },
    },
    themes: {
        editor: {
            base: 'vs-dark',
            rules: [{foreground: 'A9B7C6'}, {
                token: 'keywords',
                foreground: 'CC7832',
                fontStyle: 'bold'
            }, {token: 'keyword', foreground: 'CC7832', fontStyle: 'bold'}, {
                token: 'number',
                foreground: '6897BB'
            }, {token: 'string', foreground: '6A8759', fontStyle: 'bold'}, {
                token: 'string.sql',
                foreground: '6A8759'
            }, {token: 'tag.sql', foreground: 'E8BF6A'}, {
                token: 'attribute.name.sql',
                foreground: 'BABABA'
            }, {token: 'attribute.value.sql', foreground: '6A8759'}, {
                token: 'predefined.sql',
                foreground: 'A9B7C6',
                fontStyle: 'italic'
            }, {token: 'predefined.magicscript', foreground: 'A9B7C6', fontStyle: 'italic'}, {
                token: 'key',
                foreground: '9876AA'
            }, {token: 'string.key.json', foreground: '9876AA'}, {
                token: 'string.value.json',
                foreground: '6A8759'
            }, {token: 'keyword.json', foreground: '6897BB'}, {
                token: 'operator.sql',
                foreground: 'CC7832',
                fontStyle: 'bold'
            }, {token: 'string.invalid', foreground: '008000', background: 'FFCCCC'}, {
                token: 'string.escape.invalid',
                foreground: '008000',
                background: 'FFCCCC'
            }, {token: 'string.escape', foreground: '000080', fontStyle: 'bold'}, {
                token: 'comment',
                foreground: '808080',
                fontStyle: 'italic'
            }, {token: 'comment.doc', foreground: '629755', fontStyle: 'italic'}, {
                token: 'comment.todo',
                foreground: 'A8C023',
                fontStyle: 'italic'
            }, {token: 'string.escape', foreground: 'CC7832'}],
            colors: {
                'editor.background': '#2B2B2B', 'editorLineNumber.foreground': '#999999',	//行号的颜色
                'editorGutter.background': '#313335',	//行号背景色
                'editor.lineHighlightBackground': '#323232',	//光标所在行的颜色
                'dropdown.background': '#3C3F41',	//右键菜单
                'dropdown.foreground': '#BBBBBB',	//右键菜单文字颜色
                'list.activeSelectionBackground': '#4B6EAF',	//右键菜单悬浮背景色
                'list.activeSelectionForeground': '#FFFFFF',	//右键菜单悬浮文字颜色
                'editorSuggestWidget.selectedBackground': '#113A5C' //代码提示选中行的背景色
            }
        }, styles: {
            'main-background-color': '#3C3F41', // 主要背景色
            'main-border-color': '#323232', // 主要边框色
            'main-color': '#bbb',   // 主要文字颜色
            'main-selected-background-color': '#323232',    // 主要选中背景色
            'main-hover-background-color': '#353739',   // 主要悬浮背景色
            'main-hover-icon-background-color': '#4C5052',  // 主要悬浮图标背景色
            'main-selected-color': '#fff',  // 主要选中文字颜色
            'main-icon-color': '#AFB1B3',   // 主要图标颜色

            'header-title-color': '#bbb',   // 顶部名字颜色
            'header-version-color': '#999', // 顶部版本号颜色
            'header-default-color': '#AFB1B3',  // 顶部其它文字颜色

            'empty-background-color': '#282828',    // 中间空的背景颜色
            'empty-key-color': '#489DF6',   // 中间空的快捷键文字颜色
            'empty-color': '#A0A0A0',   // 中间空的文字颜色

            'button-hover-background-color': '#365880', //  按钮悬浮背景颜色
            'button-hover-border-color': '#43688C', //  按钮悬浮边框颜色
            'button-background-color': '#4C5052',   // 按钮背景颜色
            'button-border-color': '#5E6060',   // 按钮边框颜色
            'button-disabled-color': '#5a5a5a', //  按钮禁用时的颜色

            'navbar-body-background-color': '#3C3F41',  // 导航条内容背景颜色
            'navbar-body-border-color': '#555555',  //导航条内边框颜色
            'resource-label-color': '#bbb',     // 资源树形菜单label颜色
            'resource-span-color': '#787878',   // 资源树形菜单span颜色

            'tree-hover-background-color': '#0d293e',   // 树形菜单悬浮背景色
            'tree-icon-color': '#aeb9c0',   //  树形菜单图标颜色

            'table-border-color': '#646464',    // 表格边框颜色

            'input-border-color': '#646464',    // input边框颜色
            'input-foucs-color': '#3D6185',     // input focus边框颜色
            'input-background-color': '#45494A',    // input背景颜色

            'select-background-color': '#3C3F41',   // select背景颜色
            'select-hover-background-color': '#3C3F41', // select悬浮背景色
            'select-option-background-color': '#3C3F41',    // select选项背景色
            'select-option-hover-background-color': '#4B6EAF',  // select选项悬浮背景色
            'select-option-border-color': '#808080',    // select选项边框色

            // 数据类型颜色
            'data-type-default-color': '#a9b7c6',
            'data-type-string-color': '#6a8759',
            'data-type-integer-color': '#6897bb',
            'data-type-byte-color': '#6897bb',
            'data-type-long-color': '#6897bb',
            'data-type-float-color': '#6897bb',
            'data-type-double-color': '#6897bb',
            'data-type-short-color': '#6897bb',
            'data-type-number-color': '#6897bb',
            'data-type-boolean-color': '#cc7832',
            'data-type-class-color': '#9876aa',
            'data-type-key-color': '#FF8E8E',


            'run-log-background-color': '#2b2b2b',  // 运行日志背景颜色
            // 日志级别颜色
            'log-level-info': '#ABC023',
            'log-level-error': '#CC666E',
            'log-level-debug': '#299999',
            'log-level-warn': 'unset',
            'log-level-trace': '#5394EC',
            'log-color-cyan': '#009191',
            'log-color-link': '#287BDE',

            'todo-color': '#A8C023',

            'debug-line-background-color': '#2D6099',   // 调试时，断点行背景颜色
            'breakpoints-background-color': '#C75450',  // 断点圆圈背景颜色
            'breakpoint-line-background-color': '#3a2323',  // 断点所在行的背景颜色

            'select-inputable-background-color': '#45494a', // select输入框背景颜色
            'select-inputable-border': 'transparent',

            'tab-selected-background-color': '#4E5254', // tab 选中时的背景颜色

            'message-em-color': '#68dd9a',  // 消息 em 颜色

            'checkbox-background-color': '#43494A',
            'checkbox-border-color': '#6B6B6B',
            'checkbox-text-color': '#bbb',
            'checkbox-selected-background-color': '#43494A',
            'checkbox-selected-border-color': '#6B6B6B',

            'toolbox-list-label-color': '#bbb',
            'toolbox-list-span-color': '#787878',
            'toolbox-border-color': '#323232',
            'toolbox-list-hover-background': '#0D293E',
            'toolbox-border-right-color': '#555555',
            'footer-border-color': '#323232',
            'tab-bar-border-color': '#323232',
            'dialog-border-color': '#282828',
            'dialog-shadow-color': '#151515',
            'table-col-border-color': '#333638',
            'table-row-border-color': '#333638',
            'table-hover-background': '#4B6EAF',
            'debug-line-background': '#2D6099',
            'breakpoints-background': '#C75450',
            'breakpoint-line-background': '#3a2323',
            'table-even-background': '#414547',
            'button-disabled-background': '#5A5A5A',
            'toolbox-list-header-icon-color': '#AFB1B3',
            'log-error-color': '#CC666E',
            'text-string-color': '#6A8759',
            'text-number-color': '#6897BB',
            'text-boolean-color': '#CC7832',
            'text-property-color': '#9876aa',
            'text-key-color': '#9876aa',
            'suggest-hover-background': '#113A5C',
            'suggest-hover-color': '#fff',
            'statusbar-em-color': '#68dd9a',
        }
    }
}
