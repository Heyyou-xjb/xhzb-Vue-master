<!-- 查看护理计划弹窗 -->
<template>
  <div class="dialogBox lookBox">
    <el-dialog
      v-model="formVisible"
      title="查看护理计划"
      :show-close="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="60%"
      @close="onClickCloseBtn"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" label-width="110px" label-position="right">
        <el-form-item label="护理计划名称：">
          <span>{{ formData.planName }}</span>
        </el-form-item>
        <el-form-item label="状态：">
          <span>{{ formData.status == 1 ? '启用' : '禁用' }}</span>
        </el-form-item>
        <el-form-item label="排序：">
          <span>{{ formData.sortNo }}</span>
        </el-form-item>
        <el-form-item label="护理项目：" class="lookPro">
          <div class="scrollTable dialogHeight">
            <el-table :data="formData.projectPlans || []" height="423" style="width: 100%">
              <el-table-column label="护理项目名称" min-width="120" show-overflow-tooltip>
                <template #default="{ row }">
                  {{ projectLabel(row.projectId) }}
                </template>
              </el-table-column>
              <el-table-column prop="executeTime" label="期望服务时间" min-width="120" show-overflow-tooltip />
              <el-table-column prop="executeCycle" label="执行周期" min-width="100">
                <template #default="{ row }">
                  <span v-if="row.executeCycle == 0">每日</span>
                  <span v-else-if="row.executeCycle == 1">每周</span>
                  <span v-else>每月</span>
                </template>
              </el-table-column>
              <el-table-column prop="executeFrequency" label="执行频次(次)" width="100" />
            </el-table>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup name="PlanLook">
import { watch } from 'vue'

const props = defineProps({
  // 弹层显示隐藏
  visible: {
    type: Boolean,
    default: false
  },
  // 计划详情数据
  baseData: {
    type: Object,
    default: () => ({})
  },
  // 护理项目下拉选项，用于把 projectId 转换成名称
  palnData: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['handleClose'])

const formVisible = ref(false)
const formData = ref({})
const formRef = ref()

watch(() => props.visible, (newVal) => {
  formVisible.value = newVal
})

watch(() => props.baseData, (newVal) => {
  formData.value = newVal || {}
}, { deep: true })

/** 根据项目id显示项目名称 */
function projectLabel(projectId) {
  const item = (props.palnData || []).find(option => String(option.value) === String(projectId))
  return item ? item.label : projectId
}

/** 关闭弹窗 */
function onClickCloseBtn() {
  formVisible.value = false
  emit('handleClose')
}
</script>

<style scoped lang="scss">
.scrollTable {
  width: 100%;
}

.dialogHeight {
  min-height: 423px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #333;
  font-weight: 500;
}

:deep(.el-table td) {
  color: #666;
}

.lookBox {
  :deep(.el-dialog__body) {
    padding-top: 15px;
  }

  .el-form {
    display: flex;
    flex-wrap: wrap;
    width: 100%;

    .el-form-item {
      width: 50%;
      margin-bottom: 10px;

      :deep(.el-form-item__label) {
        padding: 0;
      }

      &.lookPro {
        width: 100%;
      }
    }
  }
}
</style>
