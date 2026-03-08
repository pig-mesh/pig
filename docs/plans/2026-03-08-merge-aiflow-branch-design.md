# aiflow-params-refactor-backend 分支合并到 dev 分支设计方案

## 背景

将 `aiflow-params-refactor-backend` 分支自 2026-01-01 至 2026-03-08 的非 AI 功能合并到 `dev` 分支。

## 核心约束

**排除所有 AI 相关代码**，具体排除路径：
- `pigx-aigc/` (AI 模块)
- `pigx-knowledge/` (旧 AI 模块)
- `pigx-common/pigx-common-milvus/` (Milvus 向量库)
- `pigx-common/pigx-common-seekdb/` (SeekDB 向量库)
- `pigx-common/pigx-common-mcp/` (MCP 协议)
- `pigx-common/pigx-common-neo4j/` (Neo4j 图库)
- `db/9pigxx_ai.sql` (AI 专属 SQL)
- `db/ai_skills.sql` (AI 技能 SQL)

**保留合并**的模块：`pigx-common-sse` (通用 SSE 模块)

## 提交分类汇总

| 类别 | 数量 | 处理方式 |
|------|------|---------|
| 可直接 cherry-pick | 45 | 按时间顺序逐个合并 |
| 跨模块需部分合并 | 22 | cherry-pick --no-commit + 排除 AI 文件 |
| 完全排除 | 45 | 不合并 |

## 合并策略：三阶段 Cherry-pick

### 阶段一：直接 cherry-pick 纯非 AI 提交 (45 个)

按时间从旧到新依次 cherry-pick：

```
7b7aab886  fix(ai-prompt): 跳过外部 AI 注册短信验证  (注意：虽然涉及 ai 但文件在 upms)
dce71af84  fix(ai-register-user): 跳过外部 AI 注册短信验证
462335246  feat(i18n): set default locale to China for message sources
de04594c4  feat(pigx-upms-biz): 增强区域树查询功能
9db9ff1d1  feat(pigx-upms-api): 为 SysDictItem 添加 listClass 字段
2f112a4a3  fix: 修复 sys_dict_item 表数据中新增的 NULL 字段
339062b24  fix(pigx-common-datasource): 修正动态数据源查询中的 del_flag 参数值
7711b26b9  fix(pigx-common-datasource): 修正动态数据源查询中的 del_flag 参数值
eed66c0aa  fix(pigx-common-security): 修复用户密码修改时间处理逻辑
f6abe9852  feat(upms): 重构连接器导入功能，支持钉钉和企业微信Excel导入
b3b392a05  fix(pigx-common-security): 添加token过期错误提示
27895cb73  fix(pigx-common-bom): 更新MyBatis-Plus版本至3.5.16
e2ccea22b  fix(pigx-common-core): #7229 登录时租户上下文菜单查询错误
7b4f672c4  fix(pigx-common-bom): 更新依赖版本
a5f2a28fd  fix: 修复当不同租户appid重复时导致的mpservice初始化失败
792bd6c44  feat(pigx-mp-platform): 重构 WxMpInitConfigRunner 类
ae6e80d55  fix: 更新 spring-boot 版本至 3.5.10，修正 spring-boot-admin 版本至 3.5.7
cca898141  feat(pigx-knowledge): 移除知识库路由配置 (仅改 db/2pigxx.sql)
63a1bc0fd  fix(pigx-upms-biz): 修复租户用户移除时的租户切换问题
553a5fd9d  feat(pigx-boot): 在 Dockerfile 中添加中文字体支持
45eeadf0d  feat(pigx-upms-api): 新增站内消息/公告发送功能
d95348df5  feat(pigx-common-core): 增强Web配置支持异步任务处理
c28670b68  feat(pigx-flow-biz): 条件表达式新增角色和岗位选择器条件处理功能
2f8c6e37c  feat(pigx-flow-api): 新增流程状态事件发布订阅机制
7e55cdf39  feat(pigx-common-core): 优化WebCommonConfiguration配置类
00494dd23  feat(pigx-flow-api): 新增流程状态事件注册与校验功能
096b092c8  feat(WebCommonConfiguration): 更新异步任务执行器配置
6947407d3  refactor(pigx-common-core): 重命名异步任务执行器方法
d1a334aa3  fix(codegen): 修复生成模板导出参数的queryForm污染
b2f8b60f0  refactor(pigx-upms-biz): 简化文件上传逻辑
65c0842a4  refactor: 升级 Spring Boot 版本至 3.5.11
babe57f1e  feat(pigx-flow-api): 为 BizFlowBaseDto 添加 ExcelIgnore 注解
9d9d3294e  feat(pigx-boot): 添加可选模块依赖支持
4f237a926  feat: 优化文件上传配置，统一使用 undertow 服务器设置
0737e2943  feat(pigx-flow-biz): 扩展抄送任务支持角色和岗位类型
f2903a89f  feat(pigx-codegen): 优化数据源密码更新逻辑
21c83813f  feat(pigx-flow-biz): 优化流程创建逻辑
628c50b2a  feat(pigx-flow-api): 新增延时器节点支持
8553a1f5c  feat(pigx-flow-biz): 实现触发器节点功能
07ad3ff51  feat(pigx-flow-biz): 实现触发器节点功能
480ae63a8  feat(pigx-flow-biz): 新增流程打印模板功能
29ea56eee  feat(pigx-boot): 优化模块依赖配置 1.12.1
7d1cac266  feat(pigx-flow-biz): 优化任务服务参数传递
b61f13d61  feat(pigx-flow-biz): 增加触发器异常终止处理
```

### 阶段二：处理跨模块提交 (22 个)

对每个提交执行：
```bash
git cherry-pick --no-commit <hash>
git reset HEAD -- pigx-aigc/ pigx-knowledge/ \
  pigx-common/pigx-common-milvus/ pigx-common/pigx-common-seekdb/ \
  pigx-common/pigx-common-mcp/ pigx-common/pigx-common-neo4j/ \
  db/9pigxx_ai.sql db/ai_skills.sql
git checkout -- pigx-aigc/ pigx-knowledge/ \
  pigx-common/pigx-common-milvus/ pigx-common/pigx-common-seekdb/ \
  pigx-common/pigx-common-mcp/ pigx-common/pigx-common-neo4j/ \
  db/9pigxx_ai.sql db/ai_skills.sql
git commit -m "原始提交信息"
```

#### 高风险提交 (手动处理)

以下提交非 AI 部分极少，建议手动 checkout 单文件而非 cherry-pick：

1. **`b63758c9f`** feat(aigc): 添加 AIGC 服务常量
   - 仅需: `pigx-common/pigx-common-core/.../ServiceNameConstants.java`

2. **`051f319ff`** refactor(aigc): rename pigx-knowledge to pigx-aigc
   - 仅需: `docs/plans/` 文档和少量构建配置

3. **`60b4aca0f`** feat: Enhance tenant context handling
   - 仅需: `db/0pigxx_boot.sql`, `pigx-boot/pom.xml`

4. **`799188b31`** chore: upgrade version to 5.12.0
   - 全量 pom.xml 版本升级，需手动排除 AI 模块 pom

#### 普通跨模块提交

以下提交大部分变更在 AI 模块中，非 AI 部分主要是 `db/0pigxx_boot.sql`：

| Commit | 描述 | 需保留的非 AI 文件 |
|--------|------|-------------------|
| `05486d814` | 重构MCP聊天规则 | `db/0pigxx_boot.sql` |
| `19b3e3ba3` | PaddleOCR 异步解析 | `SysTenantServiceImpl.java` |
| `021139e55` | AI Skills 数据库表 | `db/0pigxx_boot.sql` |
| `a1225ed2c` | 调整AI菜单结构 | `db/0pigxx_boot.sql` |
| `6d240904a` | embedding 调用记录 | `db/0pigxx_boot.sql` |
| `eb3a334cb` | PgVector 依赖升级 | `pom.xml` (根) |
| `4f45c986d` | 移除模型代码 | `db/0pigxx_boot.sql` |
| `a25144a79` | AI代码评审功能 | `db/0pigxx_boot.sql` |
| `2bb41b75f` | 向量库默认配置 | `.gitignore`, `db/0pigxx_boot.sql`, `application-dev.yml` |
| `32544acd0` | SeekDB 重构 | `db/0pigxx_boot.sql`, `application-dev.yml` |
| `2e965fd55` | seekdb 混合搜索 | `db/0pigxx_boot.sql`, `pigx-common-bom/pom.xml` |
| `c9842d0c9` | AI海报默认模板 | `db/0pigxx_boot.sql` |
| `01a501165` | 海报二维码上传 | `db/0pigxx_boot.sql` |
| `8aaacc73b` | 信息图生成功能 | `db/0pigxx_boot.sql` |
| `40b9e3df1` | 视觉分析更新 | `db/0pigxx_boot.sql` |
| `d34cfbd1b` | knowledge 拆分 | 需确认非 AI 文件 |
| `5647dc701` | 智能巡检 | 需确认非 AI 文件 |
| `0a091418b` | 文档解析配置 | 需确认非 AI 文件 |

### 阶段三：验证和清理

1. **编译验证**
   ```bash
   mvn -T 1C clean compile -DskipTests -pl '!pigx-aigc'
   ```

2. **依赖检查**
   - 确认 pom.xml 无对 pigx-aigc 的引用
   - 确认无 import 引用 `com.pig4cloud.pigx.aigc` 包

3. **数据库脚本审查**
   - `db/0pigxx_boot.sql` 中 AI 相关菜单项需人工确认

## 涉及的模块和主要功能

### 非 AI 功能总览 (合并目标)

| 模块 | 主要功能 |
|------|---------|
| **pigx-flow** | 触发器节点、延时器节点、打印模板、流程状态事件、条件表达式增强 |
| **pigx-upms** | 站内消息/公告、连接器导入(钉钉/企微)、区域树增强、SysDictItem listClass |
| **pigx-common-core** | WebCommonConfiguration、异步任务执行器、MessageSource、SecurityConstants |
| **pigx-common-bom** | MyBatis-Plus 3.5.16、Spring Boot 3.5.11 升级 |
| **pigx-common-security** | token 过期提示、密码修改时间修复 |
| **pigx-common-datasource** | del_flag 参数修复 |
| **pigx-common-sse** | Redis 分发配置优化 |
| **pigx-visual/codegen** | 数据源密码更新、模板导出修复、AI扩展Controller |
| **pigx-boot** | Dockerfile 字体支持、模块依赖优化 |
| **pigx-app-server** | App 功能增强 |
| **pigx-mp-platform** | 微信公众号配置重构 |
| **pigx-auth/gateway/register** | 认证和网关更新 |
| **db/** | 非 AI 数据库表结构更新 |
| **根 pom.xml** | Spring Boot 3.5.11、版本管理更新 |

## 风险和注意事项

1. **db/0pigxx_boot.sql 混合内容**: 该文件同时包含 AI 和非 AI 表的变更，需人工审查每次合并
2. **版本升级提交 (799188b31)**: 涉及几乎所有 pom.xml，需逐一排除 AI 模块
3. **编译依赖**: 某些非 AI 代码可能间接引用了 AI 模块的类型，需编译验证
4. **pigx-boot 模块**: 作为单体模式入口，其 pom.xml 中可能引用了 AI 模块依赖
