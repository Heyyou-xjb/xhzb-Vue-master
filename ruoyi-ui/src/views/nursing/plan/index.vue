<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入护理计划名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in nursing_plan_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['nursing:plan:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['nursing:plan:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['nursing:plan:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['nursing:plan:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" :index="indexMethod" width="60" align="center" />
      <el-table-column label="名称" align="center" prop="planName" />
      <el-table-column label="排序号" align="center" prop="sortNo" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'">
            {{ scope.row.status == 1 ? "启用" : "禁用" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['nursing:plan:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['nursing:plan:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新建或修改护理计划对话框 -->
    <el-dialog :title="title" v-model="open" width="900px" append-to-body>
      <el-form ref="planRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="护理计划名称" prop="planName">
          <el-input
            v-model="form.planName"
            placeholder="请输入护理计划名称"
            maxlength="10"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in nursing_plan_status"
              :key="dict.value"
              :value="parseInt(dict.value)"
            >{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sortNo">
          <el-input-number v-model="form.sortNo" controls-position="right" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="护理项目">
          <el-table :data="form.projectPlanList" border size="small" style="width: 100%">
            <el-table-column label="护理项目名称" align="center" width="180">
              <template #default="scope">
                <el-select
                  v-model="scope.row.projectId"
                  placeholder="请选择"
                  filterable
                  style="width: 100%"
                >
                  <el-option
                    v-for="project in projectOptions"
                    :key="project.id"
                    :label="project.name"
                    :value="project.id"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="期望服务时间" align="center" width="160">
              <template #default="scope">
                <el-time-picker
                  v-model="scope.row.executeTime"
                  placeholder="请选择"
                  format="HH:mm"
                  value-format="HH:mm:ss"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column label="执行周期" align="center" width="140">
              <template #default="scope">
                <el-select v-model="scope.row.executeCycle" placeholder="请选择" style="width: 100%">
                  <el-option
                    v-for="cycle in executeCycleOptions"
                    :key="cycle.value"
                    :label="cycle.label"
                    :value="cycle.value"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="执行频次(次)" align="center" width="130">
              <template #default="scope">
                <el-input-number
                  v-model="scope.row.executeFrequency"
                  controls-position="right"
                  :min="1"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="130">
              <template #default="scope">
                <el-button link type="primary" icon="Plus" @click="handleAddItem(scope.$index)">添加</el-button>
                <el-button link type="danger" icon="Delete" @click="handleRemoveItem(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Plan">
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/nursing/plan"
import { listProject } from "@/api/nursing/project"
import { listProject_plan, addProject_plan, updateProject_plan, delProject_plan } from "@/api/nursing/project_plan"

const { proxy } = getCurrentInstance()
const { nursing_plan_status } = useDict('nursing_plan_status')

/** 执行周期：0 天 1 周 2月 */
const executeCycleOptions = [
  { value: 0, label: "每日" },
  { value: 1, label: "每周" },
  { value: 2, label: "每月" }
]

const planList = ref([])
// 护理项目下拉数据
const projectOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    planName: undefined,
    status: undefined,
  },
  rules: {
    planName: [
      { required: true, message: "护理计划名称不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    sortNo: [
      { required: true, message: "排序不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询护理计划列表 */
function getList() {
  loading.value = true
  listPlan(queryParams.value).then(response => {
    planList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 查询护理项目下拉数据 */
function getProjectOptions() {
  listProject({ pageNum: 1, pageSize: 500 }).then(response => {
    projectOptions.value = response.rows || []
  })
}

/** 序号列（按当前分页连续编号） */
function indexMethod(index) {
  return (queryParams.value.pageNum - 1) * queryParams.value.pageSize + index + 1
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    planName: null,
    status: 1,
    sortNo: 0,
    projectPlanList: [newPlanItem()],
    createTime: null,
    updateTime: null,
    createBy: null,
    updateBy: null,
    remark: null
  }
  proxy.resetForm("planRef")
}

/** 新增一条护理项目行（执行周期默认每日） */
function newPlanItem() {
  return {
    id: null,
    projectId: null,
    executeTime: "",
    executeCycle: 0,
    executeFrequency: null,
    remark: null
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "新建护理计划"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getPlan(_id).then(response => {
    form.value = { ...response.data, projectPlanList: [] }
    open.value = true
    title.value = "修改护理计划"
    // 回显护理项目
    return listProject_plan({ planId: _id, pageNum: 1, pageSize: 500 })
  }).then(response => {
    const items = (response.rows || []).map(item => ({
      id: item.id,
      projectId: item.projectId,
      executeTime: item.executeTime,
      executeCycle: item.executeCycle,
      executeFrequency: item.executeFrequency,
      remark: item.remark
    })).sort((a, b) => (a.id || 0) - (b.id || 0))
    form.value.projectPlanList = items.length ? items : [newPlanItem()]
  })
}

/** 添加护理项目行 */
function handleAddItem(index) {
  form.value.projectPlanList.splice(index + 1, 0, newPlanItem())
}

/** 删除护理项目行（至少保留一行） */
function handleRemoveItem(index) {
  form.value.projectPlanList.splice(index, 1)
  if (!form.value.projectPlanList.length) {
    form.value.projectPlanList.push(newPlanItem())
  }
}

/** 校验护理项目行 */
function validatePlanItems() {
  const items = form.value.projectPlanList || []
  if (!items.length) {
    proxy.$modal.msgError("请至少添加一条护理项目")
    return null
  }
  const projectIdSet = new Set()
  for (const item of items) {
    if (!item.projectId) {
      proxy.$modal.msgError("请选择护理项目名称")
      return null
    }
    if (projectIdSet.has(item.projectId)) {
      proxy.$modal.msgError("护理项目名称不能重复")
      return null
    }
    projectIdSet.add(item.projectId)
    if (!item.executeTime) {
      proxy.$modal.msgError("请选择期望服务时间")
      return null
    }
    if (item.executeCycle === null || item.executeCycle === undefined || item.executeCycle === "") {
      proxy.$modal.msgError("请选择执行周期")
      return null
    }
    if (item.executeFrequency === null || item.executeFrequency === undefined || item.executeFrequency === "") {
      proxy.$modal.msgError("请输入执行频次")
      return null
    }
  }
  return items
}

/**
 * 兼容新增接口未返回主键的情况：按名称（唯一）反查新建的护理计划编号
 */
function resolveNewPlanId() {
  return listPlan({ planName: form.value.planName, pageNum: 1, pageSize: 10 }).then(response => {
    const row = (response.rows || []).find(item => item.planName === form.value.planName)
    if (!row) {
      return Promise.reject(new Error("未获取到新建的护理计划编号"))
    }
    return row.id
  })
}

/** 保存护理项目关联（先删除已移除的，再新增/修改保留的） */
function savePlanItems(planId) {
  const items = form.value.projectPlanList || []
  return listProject_plan({ planId: planId, pageNum: 1, pageSize: 500 }).then(async response => {
    const exists = response.rows || []
    for (const item of exists) {
      if (!items.some(row => row.id === item.id)) {
        await delProject_plan(item.id)
      }
    }
    // 按行顺序依次保存，保证再次打开时护理项目的展示顺序与录入顺序一致
    for (const item of items) {
      const data = {
        planId: planId,
        projectId: item.projectId,
        executeTime: item.executeTime,
        executeCycle: item.executeCycle,
        executeFrequency: item.executeFrequency,
        remark: item.remark
      }
      if (item.id) {
        await updateProject_plan({ ...data, id: item.id })
      } else {
        await addProject_plan(data)
      }
    }
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["planRef"].validate(valid => {
    if (!valid) {
      return
    }
    const items = validatePlanItems()
    if (!items) {
      return
    }
    if (form.value.id != null) {
      updatePlan(form.value).then(() => {
        return savePlanItems(form.value.id)
      }).then(() => {
        proxy.$modal.msgSuccess("修改成功")
        open.value = false
        getList()
      })
    } else {
      addPlan(form.value).then(response => {
        // 后端返回新主键时直接使用，未返回时按名称反查
        return (response && response.data) ? response.data : resolveNewPlanId()
      }).then(planId => {
        return savePlanItems(planId)
      }).then(() => {
        proxy.$modal.msgSuccess("新增成功")
        open.value = false
        getList()
      }).catch(error => {
        proxy.$modal.msgError(error && error.message ? error.message : "保存失败")
      })
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除护理计划编号为"' + _ids + '"的数据项？').then(function() {
    return delPlan(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('nursing/plan/export', {
    ...queryParams.value
  }, `plan_${new Date().getTime()}.xlsx`)
}

getList()
getProjectOptions()
</script>
