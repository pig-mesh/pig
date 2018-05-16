const cc = require('conventional-changelog')
const fs = require('fs')
const ph = require('path')
const inquirer = require('inquirer')
const curVersion = require('../package.json').version
const filePath = './src/views';
const tempPath = './tools/temp';
const validate = require('./util/validate')
const release = async () => {
  console.log(`Current version: ${curVersion}`)
  const {
    name
  } = await inquirer.prompt([{
    name: 'name',
    message: `请输入模板名称?`,
    type: 'input',
    validate: function (input) {
      var done = this.async();
      if (validate.validatenull(input)) {
        done('模板名称不能为空');
        return;
      }
      done(null, true);
    }
  }])

  const {
    path
  } = await inquirer.prompt([{
    name: 'path',
    message: `请输入接口路径(/admin/user)?`,
    type: 'input',
    validate: function (input) {
      var done = this.async();
      if (validate.validatenull(input)) {
        done('接口路径不能为空');
        return;
      }
      done(null, true);
    }
  }])

  const {
    dic
  } = await inquirer.prompt([{
    name: 'dic',
    message: `请输入字典集合(用,隔开)?`,
    type: 'input'
  }])

  const {
    id
  } = await inquirer.prompt([{
    name: 'id',
    default: 'id',
    message: `请输入主键ID(默认为id)?`,
    type: 'input',
  }])

  let content = [{
      title: 'api.js',
      list: ['path'],
      value: '',
    },
    {
      title: 'index.vue',
      list: ['name', 'id'],
      value: '',
    },
    {
      title: 'option.js',
      list: ['dic'],
      value: '',
    }
  ]


  const mkdirPath = `${filePath}/${name}`;
  if (!fs.existsSync(mkdirPath)) {
    fs.mkdirSync(mkdirPath);
  }

  content.forEach(ele => {
    ele.value = fs.readFileSync(`${tempPath}/${ele.title}`, "utf-8");
    ele.list.forEach(param => {
      ele.value = ele.value.replace(new RegExp(`{{${param}}}`, "ig"), eval(param));
    })
    fs.createWriteStream(`${mkdirPath}/${ele.title}`);
    fs.writeFileSync(`${mkdirPath}/${ele.title}`, ele.value);
  })

  console.log('模板生成完成');



}
release().catch(err => {
  console.error(err)
  process.exit(1)
})
