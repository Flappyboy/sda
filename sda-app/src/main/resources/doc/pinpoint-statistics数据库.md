# pinpoint-statistics数据库

**ALGORITHMS：算法表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 算法表id
name | varchar | 算法名称
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）

**ALGORITHMS_PARAM：算法参数表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 算法参数表id
algorithmsId | varchar | 算法表id
param | varchar | 算法参数
order | int | 参数次序
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）

**APP：项目表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 项目表id
name | varchar | 项目名称
path | varchar | 项目路径
nodeNumber | int | 结点个数
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）


**CLASS_NODE：类结点表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 类结点表id
name | varchar | 类名称
key | int | 类结点键
appId | varchar | 项目id
type | int | 类型（0-类，1-接口）
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）


**DYNAMIC_ANALYSIS_INFO：动态分析信息表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 动态分析表id
appId | varchar | 项目id
startTine | datetime | 动态分析开始时间
endTime | datetime | 动态分析结束时间
type | int | 类型（0-类，1-接口）
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）


**DYNAMIC_CALL_INFO：动态调用信息表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 动态调用信息表id
caller | varchar | 调用结点id
callee | varchar | 被调用结点id
count | int | 调用次数
dynamicAnalysisInfoId | varchar | 动态分析表id
isInclude | int | 是否在静态调用中被包含（0
type | int | 类型（0-类，1-接口）
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）

**METHOD_NODE：方法结点表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 方法结点id
name | varchar | 方法名称
classId | varchar | 类结点id
key | int | 方法结点键
appId | varchar | 项目id
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）

**PARTITION_DETAIL：划分细节表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 划分细节表id
nodeId | varchar | 结点id
patitionResultId | varchar | 划分结果表id
type | int | 类型（0-类，1-接口
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）

**PARTITION_RESULT：划分结果表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 划分结果id
desc | varchar | 描述
name | varchar | 名称
algorithmsId | varchar | 算法id
dynamicAnalysisInfoId | varchar | 动态分析id
appId | varchar | 项目id
order | int | 结果排序
type | int | 类型（0-类，1-接口
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）

**STATIC_CALL_INFO：静态调用信息表**

参数 | 类型 | 说明
---|--- |---
id | varchar | 静态信息id
caller | varchar | 调用结点id
callee | varchar | 被调用结点id
count | int | 调用次数
appId | varchar | 项目id
type | int | 类型（0-类，1-接口
createdAt | datetime | 创建时间
updatedAt | datetime | 修改时间
flag | int | 是否可用（0-不可用，1-可用）




