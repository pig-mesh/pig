# 合并 aiflow-params-refactor-backend 非 AI 代码到 dev 分支 — 实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 将 `aiflow-params-refactor-backend` 分支 2026-01-01 至 2026-03-08 期间的非 AI 功能代码合并到 `dev` 分支

**Architecture:** 三阶段 cherry-pick 策略。阶段一批量合并 45 个纯非 AI 提交；阶段二对 22 个跨模块提交进行选择性文件提取；阶段三编译验证和清理。

**Tech Stack:** Git, Maven, Spring Boot 3.5.11

---

## AI 排除路径清单 (全局引用)

以下路径在所有 cherry-pick 操作中必须排除：

```
pigx-aigc/
pigx-knowledge/
pigx-common/pigx-common-milvus/
pigx-common/pigx-common-seekdb/
pigx-common/pigx-common-mcp/
pigx-common/pigx-common-neo4j/
db/9pigxx_ai.sql
db/ai_skills.sql
```

排除命令模板（后续简称 `EXCLUDE_PATHS`）：
```bash
EXCLUDE_PATHS="pigx-aigc/ pigx-knowledge/ pigx-common/pigx-common-milvus/ pigx-common/pigx-common-seekdb/ pigx-common/pigx-common-mcp/ pigx-common/pigx-common-neo4j/ db/9pigxx_ai.sql db/ai_skills.sql"
```

---

## Task 1: 创建工作分支并备份

**Files:**
- 无文件修改，仅 Git 操作

**Step 1: 确认当前在 dev 分支**

```bash
cd /Users/lengleng/work/dev1227/pigx
git branch --show-current
```
Expected: `dev`

**Step 2: 确保工作区干净**

```bash
git status
```
Expected: `working tree clean`

**Step 3: 创建备份分支**

```bash
git branch dev-backup-before-merge
```

**Step 4: 创建合并工作分支**

```bash
git checkout -b merge/aiflow-non-ai-to-dev
```

**Step 5: 验证**

```bash
git branch --show-current
```
Expected: `merge/aiflow-non-ai-to-dev`

---

## Task 2: 阶段一 — 批量 Cherry-pick 纯非 AI 提交 (第 1 批: 最早期提交)

**Files:**
- 涉及 pigx-upms, pigx-common-datasource, pigx-common-security, pigx-common-bom 等模块

按时间正序（从旧到新）执行 cherry-pick。如遇冲突，手动解决后 `git add` 并 `git cherry-pick --continue`。

**Step 1: Cherry-pick 第 1-15 个提交**

逐个执行，每个成功后继续下一个：

```bash
# 1. fix(ai-prompt): 优化提示词模板保存报错 (文件在 upms，非 AI)
git cherry-pick 7b7aab886

# 2. fix(ai-register-user): 跳过外部 AI 注册短信验证
git cherry-pick dce71af84

# 3. feat(i18n): set default locale to China
git cherry-pick 462335246

# 4. feat(pigx-upms-biz): 增强区域树查询功能
git cherry-pick de04594c4

# 5. feat(pigx-upms-api): SysDictItem 添加 listClass 字段
git cherry-pick 9db9ff1d1

# 6. fix: 修复 sys_dict_item 表数据中新增的 NULL 字段
git cherry-pick 2f112a4a3

# 7. fix(pigx-common-datasource): 修正 del_flag 参数值
git cherry-pick 339062b24

# 8. fix(pigx-common-datasource): 修正 del_flag 参数值 (第二个)
git cherry-pick 7711b26b9

# 9. fix(pigx-common-security): 修复用户密码修改时间处理逻辑
git cherry-pick eed66c0aa

# 10. feat(upms): 重构连接器导入功能，钉钉/企微
git cherry-pick f6abe9852

# 11. fix(pigx-common-security): 添加token过期错误提示
git cherry-pick b3b392a05

# 12. fix(pigx-common-bom): 更新MyBatis-Plus至3.5.16
git cherry-pick 27895cb73

# 13. fix(pigx-common-core): #7229 租户上下文菜单查询错误
git cherry-pick e2ccea22b

# 14. fix(pigx-common-bom): 更新依赖版本
git cherry-pick 7b4f672c4

# 15. fix: 修复租户appid重复导致mpservice初始化失败
git cherry-pick a5f2a28fd
```

**Step 2: 验证 cherry-pick 状态**

```bash
git log --oneline -15
```
Expected: 看到 15 个新提交

---

## Task 3: 阶段一 — 批量 Cherry-pick (第 2 批: 中期提交)

**Step 1: Cherry-pick 第 16-30 个提交**

```bash
# 16. feat(pigx-mp-platform): 重构 WxMpInitConfigRunner
git cherry-pick 792bd6c44

# 17. fix: 更新 spring-boot 版本至 3.5.10
git cherry-pick ae6e80d55

# 18. feat(pigx-knowledge): 移除知识库路由配置 (仅改 db/2pigxx.sql)
git cherry-pick cca898141

# 19. fix(pigx-upms-biz): 修复租户用户移除时的租户切换问题
git cherry-pick 63a1bc0fd

# 20. feat(pigx-boot): Dockerfile 添加中文字体支持
git cherry-pick 553a5fd9d

# 21. feat(pigx-upms-api): 新增站内消息/公告发送功能
git cherry-pick 45eeadf0d

# 22. feat(pigx-common-core): 增强Web配置支持异步任务处理
git cherry-pick d95348df5

# 23. feat(pigx-flow-biz): 条件表达式新增角色和岗位选择器
git cherry-pick c28670b68

# 24. feat(pigx-flow-api): 新增流程状态事件发布订阅机制
git cherry-pick 2f8c6e37c

# 25. feat(pigx-common-core): 优化WebCommonConfiguration配置类
git cherry-pick 7e55cdf39

# 26. feat(pigx-flow-api): 新增流程状态事件注册与校验功能
git cherry-pick 00494dd23

# 27. feat(WebCommonConfiguration): 更新异步任务执行器配置
git cherry-pick 096b092c8

# 28. refactor(pigx-common-core): 重命名异步任务执行器方法
git cherry-pick 6947407d3

# 29. fix(codegen): 修复生成模板导出参数的queryForm污染
git cherry-pick d1a334aa3

# 30. refactor(pigx-upms-biz): 简化文件上传逻辑
git cherry-pick b2f8b60f0
```

**Step 2: 验证**

```bash
git log --oneline -30
```

---

## Task 4: 阶段一 — 批量 Cherry-pick (第 3 批: 后期提交)

**Step 1: Cherry-pick 第 31-45 个提交**

```bash
# 31. refactor: 升级 Spring Boot 版本至 3.5.11
git cherry-pick 65c0842a4

# 32. feat(pigx-flow-api): BizFlowBaseDto 添加 ExcelIgnore 注解
git cherry-pick babe57f1e

# 33. feat(pigx-boot): 添加可选模块依赖支持
git cherry-pick 9d9d3294e

# 34. feat: 优化文件上传配置，统一使用 undertow
git cherry-pick 4f237a926

# 35. feat(pigx-flow-biz): 扩展抄送任务支持角色和岗位
git cherry-pick 0737e2943

# 36. feat(pigx-codegen): 优化数据源密码更新逻辑
git cherry-pick f2903a89f

# 37. feat(pigx-flow-biz): 优化流程创建逻辑
git cherry-pick 21c83813f

# 38. feat(pigx-flow-api): 新增延时器节点支持
git cherry-pick 628c50b2a

# 39. feat(pigx-flow-biz): 实现触发器节点功能
git cherry-pick 8553a1f5c

# 40. feat(pigx-flow-biz): 实现触发器节点功能 (续)
git cherry-pick 07ad3ff51

# 41. feat(pigx-flow-biz): 新增流程打印模板功能
git cherry-pick 480ae63a8

# 42. feat(pigx-boot): 优化模块依赖配置 1.12.1
git cherry-pick 29ea56eee

# 43. feat(pigx-flow-biz): 优化任务服务参数传递
git cherry-pick 7d1cac266

# 44. feat(pigx-flow-biz): 增加触发器异常终止处理
git cherry-pick b61f13d61
```

**Step 2: 验证阶段一完成**

```bash
git log --oneline HEAD~45..HEAD
```
Expected: 约 44-45 个新提交（可能有一两个因冲突合并）

---

## Task 5: 阶段二 — 跨模块提交 · 版本升级 (799188b31)

这是最大的跨模块提交 — 全量版本升级到 5.12.0，涉及几乎所有模块的 pom.xml。

**Files:**
- 修改: 约 50 个 pom.xml 文件
- 修改: `docker-compose.yml`
- 修改: `pigx-boot/src/main/resources/application-dev.yml`

**Step 1: Cherry-pick 但不提交**

```bash
git cherry-pick --no-commit 799188b31
```

**Step 2: 排除 AI 模块文件**

```bash
# 撤销 AI 模块的 pom.xml 变更
git restore --staged --worktree -- pigx-aigc/ || true
git restore --staged --worktree -- pigx-common/pigx-common-milvus/pom.xml || true
git restore --staged --worktree -- pigx-common/pigx-common-seekdb/pom.xml || true
git restore --staged --worktree -- pigx-common/pigx-common-mcp/pom.xml || true
git restore --staged --worktree -- pigx-common/pigx-common-neo4j/pom.xml || true
git restore --staged --worktree -- db/9pigxx_ai.sql || true
```

**Step 3: 检查暂存区，确认只剩非 AI 文件**

```bash
git diff --cached --name-only | grep -E '(aigc|knowledge|milvus|seekdb|mcp|neo4j|9pigxx_ai)' || echo "OK: 无 AI 文件"
```
Expected: `OK: 无 AI 文件`

**Step 4: 提交**

```bash
git commit -m "$(cat <<'EOF'
chore: upgrade version to 5.12.0 (non-AI modules only)

Cherry-picked from 799188b31, excluding pigx-aigc and AI-related common modules.
EOF
)"
```

---

## Task 6: 阶段二 — 跨模块提交 · ServiceNameConstants (b63758c9f)

这个提交改了约 200 个 AI 文件，但非 AI 部分仅 `ServiceNameConstants.java`。

**Files:**
- 修改: `pigx-common/pigx-common-core/src/main/java/com/pig4cloud/pigx/common/core/constant/ServiceNameConstants.java`

**Step 1: 直接从源分支提取单个文件**

```bash
git checkout aiflow-params-refactor-backend -- pigx-common/pigx-common-core/src/main/java/com/pig4cloud/pigx/common/core/constant/ServiceNameConstants.java
```

**Step 2: 检查 diff 确认变更合理**

```bash
git diff --cached -- pigx-common/pigx-common-core/src/main/java/com/pig4cloud/pigx/common/core/constant/ServiceNameConstants.java
```

**Step 3: 确认没有引入对 aigc 包的依赖**

```bash
git diff --cached | grep -i 'aigc\|knowledge' || echo "OK: 无 AI 引用"
```

**Step 4: 提交**

```bash
git commit -m "$(cat <<'EOF'
feat(pigx-common-core): 更新 ServiceNameConstants 添加 AIGC 服务常量

从 b63758c9f 提取非 AI 文件部分。
EOF
)"
```

---

## Task 7: 阶段二 — 跨模块提交 · 租户上下文增强 (60b4aca0f)

非 AI 部分仅 `db/0pigxx_boot.sql` 和 `pigx-boot/pom.xml`。

**Files:**
- 修改: `pigx-boot/pom.xml`

**Step 1: Cherry-pick 但不提交**

```bash
git cherry-pick --no-commit 60b4aca0f
```

**Step 2: 排除 AI 文件**

```bash
git restore --staged --worktree -- pigx-aigc/ || true
git restore --staged --worktree -- db/9pigxx_ai.sql || true
```

**Step 3: 审查 db/0pigxx_boot.sql 变更**

```bash
git diff --cached -- db/0pigxx_boot.sql | head -100
```

人工审查：确认 SQL 变更中不含纯 AI 功能表（如 `ai_flow`, `ai_model`, `ai_dataset` 等），仅保留通用业务表变更。如果 SQL 中混有 AI 表定义，需手动编辑移除。

**Step 4: 检查无 AI 残留**

```bash
git diff --cached --name-only | grep -E '(aigc|knowledge|milvus|seekdb|mcp|neo4j|9pigxx_ai)' || echo "OK"
```

**Step 5: 提交**

```bash
git commit -m "$(cat <<'EOF'
feat: 增强租户上下文处理 (非 AI 部分)

从 60b4aca0f 提取 db/0pigxx_boot.sql 和 pigx-boot/pom.xml 变更。
EOF
)"
```

---

## Task 8: 阶段二 — 跨模块提交 · PgVector 依赖升级 (eb3a334cb)

非 AI 部分仅根 `pom.xml`。

**Files:**
- 修改: `pom.xml`

**Step 1: 直接从源分支提取**

```bash
git checkout aiflow-params-refactor-backend -- pom.xml
```

**Step 2: 审查根 pom.xml 变更**

```bash
git diff --cached -- pom.xml
```

确认：是否有引入 AI 专属依赖（如 langchain4j, spring-ai 等），如有需手动移除。

**Step 3: 提交**

```bash
git commit -m "$(cat <<'EOF'
refactor: 更新根 pom.xml 依赖版本

从 eb3a334cb 提取根 pom.xml 变更。
EOF
)"
```

---

## Task 9: 阶段二 — 跨模块提交 · PaddleOCR 中的 upms 变更 (19b3e3ba3)

非 AI 部分仅 `SysTenantServiceImpl.java`。

**Files:**
- 修改: `pigx-upms/pigx-upms-biz/src/main/java/.../SysTenantServiceImpl.java`

**Step 1: 提取单文件**

```bash
git checkout aiflow-params-refactor-backend -- pigx-upms/pigx-upms-biz/src/main/java/com/pig4cloud/pigx/admin/service/impl/SysTenantServiceImpl.java
```

**Step 2: 审查变更**

```bash
git diff --cached -- pigx-upms/
```

**Step 3: 提交**

```bash
git commit -m "$(cat <<'EOF'
feat(pigx-upms-biz): 优化 SysTenantServiceImpl 租户处理逻辑

从 19b3e3ba3 提取非 AI 部分。
EOF
)"
```

---

## Task 10: 阶段二 — 跨模块提交 · seekdb 混合搜索中的 BOM 变更 (2e965fd55)

非 AI 部分：`pigx-common/pigx-common-bom/pom.xml` 和 `pigx-common/pom.xml`。

**Files:**
- 修改: `pigx-common/pigx-common-bom/pom.xml`
- 修改: `pigx-common/pom.xml`

**Step 1: 提取文件**

```bash
git checkout aiflow-params-refactor-backend -- pigx-common/pigx-common-bom/pom.xml pigx-common/pom.xml
```

**Step 2: 审查变更**

```bash
git diff --cached -- pigx-common/pigx-common-bom/pom.xml pigx-common/pom.xml
```

确认：`pigx-common/pom.xml` 中是否新增了 AI 相关子模块引用（如 `<module>pigx-common-milvus</module>` 等），如有需手动移除。

**Step 3: 提交**

```bash
git commit -m "$(cat <<'EOF'
refactor(pigx-common): 更新 BOM 依赖版本

从 2e965fd55 提取非 AI 部分。
EOF
)"
```

---

## Task 11: 阶段二 — 跨模块提交 · 向量库默认配置中的通用文件 (2bb41b75f)

非 AI 部分：`.gitignore`、`pigx-boot/src/main/resources/application-dev.yml`。

**Files:**
- 修改: `.gitignore`
- 修改: `pigx-boot/src/main/resources/application-dev.yml`

**Step 1: 提取文件**

```bash
git checkout aiflow-params-refactor-backend -- .gitignore
git checkout aiflow-params-refactor-backend -- pigx-boot/src/main/resources/application-dev.yml
```

**Step 2: 审查变更**

```bash
git diff --cached -- .gitignore pigx-boot/src/main/resources/application-dev.yml
```

确认 `application-dev.yml` 中无 AI 专属配置（如 `langchain4j`, `spring-ai`, `milvus` 配置项等），如有需移除。

**Step 3: 提交**

```bash
git commit -m "$(cat <<'EOF'
chore: 更新 .gitignore 和 boot 开发配置

从 2bb41b75f 提取非 AI 部分。
EOF
)"
```

---

## Task 12: 阶段二 — 跨模块提交 · AI Skills 数据库表 (021139e55)

非 AI 部分仅 `db/0pigxx_boot.sql`（菜单/权限数据）。

**Files:**
- 修改: `db/0pigxx_boot.sql`

**Step 1: Cherry-pick 但不提交**

```bash
git cherry-pick --no-commit 021139e55
```

**Step 2: 排除 AI 文件**

```bash
git restore --staged --worktree -- db/9pigxx_ai.sql || true
git restore --staged --worktree -- db/ai_skills.sql || true
```

**Step 3: 审查 SQL 变更**

```bash
git diff --cached -- db/0pigxx_boot.sql | head -60
```

人工审查：确认是否只是菜单权限数据，是否需要保留。

**Step 4: 提交（或放弃）**

如果 SQL 变更合理：
```bash
git commit -m "$(cat <<'EOF'
feat: 新增 AI Skills 菜单权限数据 (db/0pigxx_boot.sql)

从 021139e55 提取非 AI SQL 部分。
EOF
)"
```

如果 SQL 变更全是 AI 相关的，放弃：
```bash
git restore --staged --worktree -- .
```

---

## Task 13: 阶段二 — 批量处理剩余 db/0pigxx_boot.sql 跨模块提交

以下提交的非 AI 部分都只有 `db/0pigxx_boot.sql`，逐一审查决定是否保留。

**提交列表：**
- `05486d814` 重构MCP聊天规则
- `a1225ed2c` 调整AI菜单结构
- `6d240904a` embedding 调用记录
- `4f45c986d` 移除模型代码
- `a25144a79` AI代码评审功能
- `c9842d0c9` AI海报默认模板
- `01a501165` 海报二维码上传
- `8aaacc73b` 信息图生成功能
- `40b9e3df1` 视觉分析更新
- `32544acd0` SeekDB 重构
- `5647dc701` 智能巡检 (`db/0pigxx_boot.sql` + `db/6pigxx_config.sql`)
- `0a091418b` 文档解析配置

**Step 1: 对每个提交执行统一流程**

对列表中的每个 `<HASH>`：

```bash
# a) cherry-pick 不提交
git cherry-pick --no-commit <HASH>

# b) 排除所有 AI 文件
git restore --staged --worktree -- pigx-aigc/ pigx-knowledge/ \
  pigx-common/pigx-common-milvus/ pigx-common/pigx-common-seekdb/ \
  pigx-common/pigx-common-mcp/ pigx-common/pigx-common-neo4j/ \
  db/9pigxx_ai.sql db/ai_skills.sql 2>/dev/null || true

# c) 检查剩余变更
git diff --cached --stat

# d) 如果有合理的非 AI 变更，提交
git commit -m "从 <HASH> 提取非 AI 数据库变更"

# e) 如果无有效变更或全是 AI 相关
git restore --staged --worktree -- .
```

**Step 2: 审查决策原则**

- `db/0pigxx_boot.sql` 中的 `sys_menu` 插入如果是 AI 菜单项 → 排除
- `db/0pigxx_boot.sql` 中的通用业务表变更 → 保留
- `db/6pigxx_config.sql` 变更 → 保留

---

## Task 14: 阶段二 — 跨模块提交 · knowledge 拆分和重命名 (d34cfbd1b, 051f319ff)

这两个大型重构提交几乎全是 AI 文件，非 AI 部分极少。

**Step 1: d34cfbd1b — 仅提取 BOM pom.xml**

```bash
git checkout aiflow-params-refactor-backend -- pigx-common/pigx-common-bom/pom.xml
git diff --cached -- pigx-common/pigx-common-bom/pom.xml
```

如果变更已在 Task 10 中包含（来自 2e965fd55），则跳过：
```bash
git restore --staged -- pigx-common/pigx-common-bom/pom.xml
```

**Step 2: 051f319ff — 无需提取**

该提交的 `docs/plans/` 文件是旧的设计文档，可忽略。所有其他文件都是 pigx-aigc 重命名，全部排除。

---

## Task 15: 阶段三 — 编译验证

**Step 1: 执行编译（排除 pigx-aigc）**

```bash
cd /Users/lengleng/work/dev1227/pigx
mvn -T 1C clean compile -DskipTests -pl '!pigx-aigc/pigx-aigc-api,!pigx-aigc/pigx-aigc-biz'
```

Expected: `BUILD SUCCESS`

如果编译失败，分析错误：
- 如果是缺少 AI 模块依赖 → 检查 pom.xml 是否误引入了 pigx-aigc 依赖
- 如果是 import 错误 → 移除对 `com.pig4cloud.pigx.aigc` 包的引用

**Step 2: 如果编译失败，修复后重新编译**

```bash
# 查找是否有对 aigc 包的引用
grep -r "com.pig4cloud.pigx.aigc" --include="*.java" \
  pigx-upms/ pigx-flow/ pigx-boot/ pigx-common/ pigx-visual/ pigx-app-server/ pigx-auth/ pigx-gateway/
```

Expected: 无匹配（或仅在排除模块中）

---

## Task 16: 阶段三 — 依赖和配置检查

**Step 1: 检查根 pom.xml 模块列表**

```bash
grep '<module>' pom.xml
```

确认不会强制编译 pigx-aigc（`pigx-aigc` 应在 `<modules>` 列表中，但不影响排除编译）。

**Step 2: 检查 pigx-boot pom.xml 依赖**

```bash
grep -A2 'pigx-aigc\|pigx-knowledge' pigx-boot/pom.xml
```

如果存在对 pigx-aigc 的依赖且 `<optional>true</optional>` 或有 profile 控制 → OK
如果是硬依赖 → 需确认 pigx-boot 是否能在不加载 AI 模块的情况下启动

**Step 3: 检查 pigx-common pom.xml 子模块列表**

```bash
grep '<module>' pigx-common/pom.xml
```

确认 AI 相关子模块（milvus, seekdb, mcp, neo4j）在列表中的存在不影响编译。

---

## Task 17: 阶段三 — 最终审查和合并到 dev

**Step 1: 查看完整变更统计**

```bash
git log --oneline merge/aiflow-non-ai-to-dev ^dev | wc -l
git diff dev...merge/aiflow-non-ai-to-dev --stat | tail -5
```

**Step 2: 确认无 AI 模块文件被合并**

```bash
git diff dev...merge/aiflow-non-ai-to-dev --name-only | grep -E '(pigx-aigc/|pigx-knowledge/|pigx-common-milvus/|pigx-common-seekdb/|pigx-common-mcp/|pigx-common-neo4j/|db/9pigxx_ai|db/ai_skills)' || echo "OK: 无 AI 文件被合并"
```
Expected: `OK: 无 AI 文件被合并`

**Step 3: 合并到 dev 分支**

```bash
git checkout dev
git merge merge/aiflow-non-ai-to-dev --no-ff -m "$(cat <<'EOF'
merge: 合并 aiflow-params-refactor-backend 非 AI 功能到 dev

合并范围: 2026-01-01 至 2026-03-08
排除模块: pigx-aigc, pigx-common-milvus/seekdb/mcp/neo4j
包含功能:
- pigx-flow: 触发器、延时器、打印模板、流程状态事件
- pigx-upms: 站内消息、连接器导入、区域树增强
- pigx-common: BOM升级、core优化、security修复
- pigx-visual/codegen: 数据源密码、模板导出修复
- pigx-boot: Dockerfile字体支持、模块依赖
- pigx-app-server: App功能增强
- 版本升级: Spring Boot 3.5.11, MyBatis-Plus 3.5.16
EOF
)"
```

**Step 4: 最终编译验证**

```bash
mvn -T 1C clean compile -DskipTests -pl '!pigx-aigc/pigx-aigc-api,!pigx-aigc/pigx-aigc-biz'
```
Expected: `BUILD SUCCESS`

**Step 5: 提交完成，保留备份分支**

```bash
echo "合并完成。备份分支: dev-backup-before-merge"
echo "如需回滚: git checkout dev && git reset --hard dev-backup-before-merge"
```

---

## 冲突处理指南

cherry-pick 过程中遇到冲突时：

1. `git status` 查看冲突文件
2. 如果冲突文件在 AI 排除路径中 → `git restore --staged --worktree -- <file>`
3. 如果冲突文件在非 AI 模块中 → 手动编辑解决冲突，选择源分支（aiflow）的版本
4. 解决后 `git add <file>` 然后 `git cherry-pick --continue`
5. 如果某个提交冲突太多无法合理解决 → `git cherry-pick --abort` 并跳过

## 回滚方案

如果整个合并过程出现严重问题：

```bash
git checkout dev
git reset --hard dev-backup-before-merge
git branch -D merge/aiflow-non-ai-to-dev
```
