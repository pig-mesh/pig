# GitHub Action 模板初始化设计

## 背景

当前仓库以 `com.pig4cloud:pig` 作为默认根坐标，源码、模块名、配置文件、Docker 相关内容都默认使用 `pig` 作为项目标识。目标是提供一个可手动触发的 GitHub Action，根据用户输入的 `groupId` 和 `artifactId`，生成一份已经完成项目初始化替换的 zip 模板产物，而不是直接修改当前仓库。

## 目标

- 提供一个 `workflow_dispatch` 触发的 GitHub Action
- 输入参数为 `groupId` 和 `artifactId`
- 生成一个不回写仓库的项目副本
- 将项目副本中的根坐标、模块名、包名、文件路径、配置项中的 `pig` 前缀尽量替换为新项目命名
- 输出 `${artifactId}.zip` 作为 workflow artifact

## 非目标

- 不自动提交任何模板初始化后的代码回当前分支
- 不尝试重写第三方依赖坐标、第三方组织名或框架固定命名
- 不引入额外 Java 程序或 Maven 插件作为模板生成器

## 输入与命名规则

### Action 输入

- `groupId`: 目标 Maven 组织标识，例如 `com.demo`
- `artifactId`: 目标项目标识，例如 `my-app`

### 派生值

- `new_group_id = groupId`
- `new_artifact_id = artifactId`
- `new_package_suffix = artifactId` 全小写并移除所有中划线
- `new_base_package = groupId + "." + new_package_suffix`

示例：

- 输入：`groupId=com.demo`，`artifactId=my-app`
- 输出基础包：`com.demo.myapp`

### 替换基准

当前模板中的默认值按以下固定常量处理：

- 旧根坐标：`com.pig4cloud:pig`
- 旧基础包：`com.pig4cloud.pig`
- 旧项目前缀：`pig`

## 推荐方案

采用“GitHub Action 调用仓库内脚本”的实现方式。

### 理由

- 替换需求同时包含文本替换和结构性改名，单纯堆叠 shell 命令在 workflow 中可维护性差
- 核心逻辑集中到仓库脚本中，便于本地调试和后续扩展
- workflow 只负责参数收集、环境准备、调用脚本和上传产物，职责清晰

## 方案结构

### Workflow

新增 workflow 文件：

- `.github/workflows/template-init.yml`

触发方式：

- `workflow_dispatch`

输入参数：

- `groupId`
- `artifactId`

执行步骤：

1. checkout 仓库
2. 校验输入参数
3. 调用仓库脚本在临时目录生成项目副本
4. 打包生成 `${artifactId}.zip`
5. 上传 artifact

### 生成脚本

新增脚本文件：

- `scripts/generate-template.sh`

职责：

- 计算 `new_base_package`
- 将仓库复制到临时目录
- 排除 `.git`、`target`、`node_modules` 等无关目录
- 对白名单文本文件做内容替换
- 对模块目录和 Java 包路径目录做结构改名
- 生成 zip 包

## 替换范围与顺序

替换按固定顺序执行，避免路径与内容互相打架。

### 1. 结构改名

先处理目录和文件路径。

- 根输出目录命名为 `${artifactId}`
- 模块目录前缀替换：
  - `pig-gateway -> ${artifactId}-gateway`
  - `pig-auth -> ${artifactId}-auth`
  - `pig-upms -> ${artifactId}-upms`
  - `pig-visual -> ${artifactId}-visual`
  - `pig-common-* -> ${artifactId}-common-*`
  - `pig-upms-* -> ${artifactId}-upms-*`
- Java 源码目录中的基础包路径从 `com/pig4cloud/pig` 重写为 `${groupId路径}/${artifactId去横线}`

说明：

- `artifactId` 中的中划线保留在模块名、目录名、服务名、压缩包文件名中
- 仅在 Java 包名维度使用“全小写并去中划线”的归一化规则

### 2. 精确文本替换

对以下内容进行精确替换：

- Maven `groupId`
- Maven `artifactId`
- Maven 父子模块依赖引用
- Java `package` 与 `import`
- XML、YAML、Properties 中显式出现的基础包
- 配置文件中显式使用的 `pig-*` 服务名、配置名、镜像名、容器名、Nacos dataId

### 3. 前缀型替换

对模板中明确表达项目命名的 `pig` 前缀进行统一前缀替换：

- `pig-xxx -> ${artifactId}-xxx`
- 独立出现且语义明确表示项目名的 `pig -> ${artifactId}`

这一层只作用于白名单文本文件，不处理二进制文件。

### 4. 打包输出

- 生成目录最终名称为 `${artifactId}`
- 打包为 `${artifactId}.zip`
- 上传为 GitHub Actions artifact

## 文件白名单与排除规则

### 允许替换的文件类型

- `pom.xml`
- `.java`
- `.kt`
- `.xml`
- `.yml`
- `.yaml`
- `.properties`
- `.md`
- `Dockerfile`
- `.dockerfile`
- `.sql`
- `.sh`
- `.json`

### 排除目录

- `.git`
- `.github`
- `.idea`
- `target`
- `node_modules`
- `dist`

说明：

- `.github` 目录建议从产物中排除，避免模板使用者拿到和当前仓库强耦合的 CI 配置
- 如果后续希望模板也继承标准 CI，可再单独放开

## 风险控制

### 输入校验

脚本在执行前校验：

- `groupId` 不允许为空
- `artifactId` 不允许为空
- `artifactId` 必须符合模块命名规则，只允许小写字母、数字和中划线
- 归一化后的包名后缀不能为空

### 替换安全边界

以下内容不应被误改：

- 第三方依赖坐标
- 第三方组织名，例如 `org.springframework`、`com.alibaba`
- 许可证文本中的第三方声明
- 非白名单文件和二进制文件

### 失败策略

- 使用 `set -euo pipefail`
- 目标目录改名冲突时直接失败
- 压缩包未生成时 workflow 失败
- 任一核心替换步骤异常则停止执行，避免产出半成品

## 数据流

1. 用户在 GitHub Actions 页面手动触发 workflow
2. workflow 读取 `groupId`、`artifactId`
3. workflow 调用 `scripts/generate-template.sh`
4. 脚本复制模板仓库到临时目录
5. 脚本计算目标基础包并完成结构改名与文本替换
6. 脚本输出 `${artifactId}.zip`
7. workflow 上传 artifact，供用户下载

## 验收标准

输入：

- `groupId=com.demo`
- `artifactId=my-app`

期望结果：

- 生成 artifact：`my-app.zip`
- 根项目坐标变为 `com.demo:my-app`
- Java 基础包变为 `com.demo.myapp`
- 目录和模块名变为 `my-app-*`
- `package`、`import`、源码目录保持一致
- `docker-compose.yml`、`Dockerfile`、配置文件、README、SQL 中与项目模板强相关的 `pig` 前缀尽量替换为 `my-app`
- 当前仓库工作树不产生模板初始化后的代码改动

## 测试建议

- 本地脚本 dry run 测试一组不带中划线的 `artifactId`
- 本地脚本 dry run 测试一组带中划线的 `artifactId`
- 检查生成目录中的以下关键点：
  - 根 `pom.xml`
  - 至少一个 Java 应用启动类
  - 至少一个子模块 `pom.xml`
  - `docker-compose.yml`
  - `db/*.sql`
- 解压 zip 后执行一次关键文本抽样检查，确认旧基础包和核心旧模块名前缀已被替换

## 开放问题

- 当前仓库存在少量 `pigx-*` 痕迹和冲突标记，这些内容不属于本次命名设计目标，实施时需要避免误替换或将其纳入排除规则
- `.github` 是否进入模板产物当前默认排除，如后续需要保留，应单独定义哪些 workflow 可复用
