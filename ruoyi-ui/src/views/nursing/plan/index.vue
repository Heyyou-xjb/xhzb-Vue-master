<template>
  <div class="app-container">
    <div class="searchBox">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="名称" prop="planName">
          <el-input
            v-model="queryParams.planName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择" clearable>
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
    </div>

    <el-row :gutter="10" class="newBtn mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['nursing:plan:add']"
        >新增护理计划</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="planList">
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="名称" align="center" prop="planName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'">
            {{ scope.row.status == 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="280" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            class="font-bt"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['nursing:plan:edit']"
          >修改</el-button>
          <el-button
            link
            type="primary"
            icon="Delete"
            class="btn-dl"
            @click="handleDelete(scope.row)"
            v-hasPermi="['nursing:plan:remove']"
          >删除</el-button>
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleLook(scope.row)"
          >查看</el-button>
          <el-button
            link
            type="primary"
            :icon="scope.row.status == 1 ? 'Lock' : 'Unlock'"
            :class="scope.row.status == 1 ? 'btn-dl' : 'font-bt'"
            @click="handleEnable(scope.row)"
            v-hasPermi="['nursing:plan:edit']"
          >{{ scope.row.status == 1 ? '禁用' : '启用' }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getNursingPalnList"
    />

    <!-- 添加或修改护理计划对话框 -->
    <el-dialog :title="title" v-model="dialogVisible" width="840" @close="cancel()">
      <el-form ref="planRef" :model="formData" :rules="rules" label-width="120px">
        <el-row gutter="10">
          <el-col :span="24" class="elcolFlex">
            <el-form-item label="护理计划名称：" prop="planName">
              <el-input
                v-model="formData.planName"
                maxlength="10"
                show-word-limit
                type="text"
                clearable
                placeholder="请输入"
              ></el-input>
            </el-form-item>
            <el-form-item label="状态：" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio
                  v-for="dict in nursing_plan_status"
                  :key="dict.value"
                  :value="dict.value"
                  :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="排序：" prop="sortNo">
              <el-input-number
                v-model="formData.sortNo"
                :min="1"
                :precision="0"
                :step="1"
                placeholder="请输入"
                @blur="textBlurNo"
                @change="textBlurNo"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="护理项目：" prop="projectPlans">
          <div class="info family">
            <div class="tableTop">
              <el-button type="primary" plain size="small" icon="Plus" @click="handleRowAdd">添加护理项目</el-button>
            </div>
            <div class="tableHead">
              <div>护理项目名称</div>
              <div>期望服务时间</div>
              <div>执行周期</div>
              <div>执行频次(次)</div>
              <div>操作</div>
            </div>
            <div class="tableBody">
              <div class="tableColumn" v-for="(item, index) in planItems" :key="index">
                <div class="column">
                  <el-select v-model="item.projectId" placeholder="请选择" clearable style="width: 100%">
                    <el-option
                      v-for="option in projectOptions"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                    ></el-option>
                  </el-select>
                </div>
                <div class="column">
                  <el-time-picker
                    v-model="item.executeTime"
                    format="HH:mm"
                    value-format="HH:mm"
                    style="width: 100%"
                    placeholder="请选择时间"
                    clearable
                  ></el-time-picker>
                </div>
                <div class="column">
                  <el-select v-model="item.executeCycle" placeholder="请选择" style="width: 100%">
                    <el-option
                      v-for="cycle in executeCycleOptions"
                      :key="cycle.value"
                      :label="cycle.label"
                      :value="cycle.value"
                    ></el-option>
                  </el-select>
                </div>
                <div class="column">
                  <el-input-number
                    v-model="item.executeFrequency"
                    :controls="false"
                    :max="7"
                    :min="1"
                    :precision="0"
                    :step="1"
                  />
                </div>
                <div class="column">
                  <el-icon v-if="planItems.length > 1" @click="handleRowDel(item, index)" class="delect"><Minus /></el-icon>
                  <el-icon @click="handleRowAdd" class="add"><Plus /></el-icon>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel()">取消</el-button>
      </div>
    </el-dialog>

    <!-- 查看护理计划 -->
    <DialogLook
      ref="lookRef"
      :visible="lookVisible"
      :base-data="lookData"
      :palnData="projectOptions"
      @handle-close="handleLookClose"
    ></DialogLook>
  </div>
</template>

<script setup name="Plan">
import { Plus, Minus } from '@element-plus/icons-vue'
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/nursing/plan"
import { getProjectAll } from "@/api/nursing/project"
import DialogLook from './look.vue'

const { proxy } = getCurrentInstance()
const { nursing_plan_status } = useDict('nursing_plan_status')

const queryRef = ref()
const planList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const title = ref('')
const dialogVisible = ref(false)
const lookVisible = ref(false)
const lookRef = ref()
const lookData = ref({})
/** 护理项目下拉选项（接口返回的 label/value 结构） */
const projectOptions = ref([])
/** 护理项目子表数据 */
const planItems = ref([])
const maxSort = ref(99999999)

const formData = ref({
  status: '1',
  sortNo: 1,
})

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  planName: null,
  status: null,
})

const rules = ref({
  planName: [
    { required: true, message: '护理计划名称为空，请输入护理计划名称', trigger: 'blur' },
  ],
  status: [
    { required: true, message: '状态为空，请选择状态', trigger: 'change' },
  ],
  sortNo: [
    { required: true, message: '排序为空，请选择排序', trigger: 'change' },
  ],
})

/** 执行周期选项：展示给用户的是 每日/每周/每月，提交给后端分别是 0/1/2 */
const executeCycleOptions = [
  { label: '每日', value: 0 },
  { label: '每周', value: 1 },
  { label: '每月', value: 2 },
]

/** 禁用状态值：取字典中的「禁用」值，字典未配置时回退为 0 */
const disabledStatus = computed(() => {
  const item = (nursing_plan_status.value || []).find(dict => dict.label === '禁用')
  return item ? Number(item.value) : 0
})

const getDefaultExecuteTime = () => '08:00'

/** 护理项目子表默认行 */
function initPlanItem() {
  return {
    id: null,
    projectId: undefined,
    executeTime: getDefaultExecuteTime(),
    executeCycle: 1,
    executeFrequency: 1,
  }
}

/** 查询所有护理项目，作为护理项目下拉列表的数据来源 */
function getAllProjectList() {
  getProjectAll().then(response => {
    projectOptions.value = response.data || []
  })
}

/** 添加护理项目（文字按钮与行内 + 号共用） */
function handleRowAdd() {
  getAllProjectList()
  planItems.value.push(initPlanItem())
}

/** 删除某一行护理项目 */
function handleRowDel(item, index) {
  if (planItems.value.length <= 1) {
    return
  }
  planItems.value.splice(index, 1)
}

/** 查询护理计划列表 */
function getNursingPalnList() {
  loading.value = true
  listPlan(queryParams.value).then(response => {
    planList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  dialogVisible.value = false
  reset()
}

/** 表单重置 */
function reset() {
  formData.value = {
    planName: null,
    status: '1',
    sortNo: 1,
  }
  planItems.value = [initPlanItem()]
  proxy.resetForm('planRef')
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getNursingPalnList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  getAllProjectList()
  dialogVisible.value = true
  title.value = '添加护理计划'
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  getPlan(row.id).then(response => {
    formData.value = response.data
    formData.value.status = String(formData.value.status)
    // 回显护理项目：项目id统一转成字符串，与下拉选项的 value 类型保持一致
    const projectPlans = response.data.projectPlans || []
    planItems.value = projectPlans.length
      ? projectPlans.map(item => ({
          id: item.id,
          projectId: item.projectId != null ? String(item.projectId) : undefined,
          executeTime: item.executeTime || getDefaultExecuteTime(),
          executeCycle: item.executeCycle != null ? Number(item.executeCycle) : 1,
          executeFrequency: item.executeFrequency != null ? Number(item.executeFrequency) : 1,
          remark: item.remark,
        }))
      : [initPlanItem()]
    dialogVisible.value = true
    title.value = '修改护理计划'
  })
  getAllProjectList()
}

/** 查看按钮操作 */
function handleLook(row) {
  getPlan(row.id).then(response => {
    lookData.value = response.data
    lookVisible.value = true
  })
  getAllProjectList()
}

/** 查看弹窗关闭 */
function handleLookClose() {
  lookVisible.value = false
}

/** 校验护理项目子表（必须选择项目，且不能重复） */
function validatePlanItems() {
  if (planItems.value.some(item => !item.projectId)) {
    proxy.$modal.msgError('请选择护理项目')
    return false
  }
  const projectIds = planItems.value.map(item => item.projectId)
  if (new Set(projectIds).size !== projectIds.length) {
    proxy.$modal.msgError('请勿选择重复的护理项目')
    return false
  }
  return true
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs['planRef'].validate(valid => {
    if (!valid) {
      return
    }
    if (!validatePlanItems()) {
      return
    }
    const submitData = {
      ...formData.value,
      projectPlans: planItems.value.map(item => ({
        id: item.id,
        projectId: item.projectId,
        executeTime: item.executeTime,
        executeCycle: item.executeCycle,
        executeFrequency: item.executeFrequency,
        remark: item.remark,
      })),
    }
    if (submitData.id != null) {
      updatePlan(submitData).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        cancel()
        getNursingPalnList()
      })
    } else {
      addPlan(submitData).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        cancel()
        getNursingPalnList()
      })
    }
  })
}

/** 启用/禁用按钮操作（复用修改接口完成状态值修改） */
async function handleEnable(row) {
  const status = row.status == 1 ? disabledStatus.value : 1
  const info = status == 1 ? '启用' : '禁用'
  try {
    if (await proxy.$modal.confirm(`此操作将${info}${row.planName}，是否继续？`)) {
      await updatePlan({ id: row.id, status: status })
      getNursingPalnList()
      proxy.$modal.msgSuccess(`${info}成功`)
    }
  } catch (error) {
    console.error('操作失败，请重试或联系管理员。')
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm('此操作将删除该' + row.planName + '，是否继续？').then(function() {
    return delPlan(row.id)
  }).then(() => {
    getNursingPalnList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

/** 排序号限制 */
function textBlurNo() {
  const sortNo = formData.value.sortNo
  if (sortNo <= 1) {
    formData.value.sortNo = 1
  }
  if (sortNo > maxSort.value) {
    formData.value.sortNo = maxSort.value
  }
}

getNursingPalnList()
</script>

<style src="./index.scss" lang="scss"></style>
