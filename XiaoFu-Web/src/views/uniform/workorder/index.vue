<template>
  <div class="app-container">
    <el-alert
      title="工序打卡会自动驱动工单状态流转，质检和赋码模块会继续基于这里的工单进行联动。"
      type="info"
      :closable="false"
      class="tips-alert"
    />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="queryParams.workOrderNo" placeholder="请输入工单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="客户订单号" prop="sourceOrderNo">
        <el-input v-model="queryParams.sourceOrderNo" placeholder="请输入客户订单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学校/客户" prop="schoolName">
        <el-input v-model="queryParams.schoolName" placeholder="请输入学校或客户名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="工单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="item in orderStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="质检状态" prop="qaStatus">
        <el-select v-model="queryParams.qaStatus" placeholder="请选择质检状态" clearable style="width: 220px">
          <el-option v-for="item in qaStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="赋码状态" prop="traceStatus">
        <el-select v-model="queryParams.traceStatus" placeholder="请选择赋码状态" clearable style="width: 220px">
          <el-option v-for="item in traceStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" style="width: 310px">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:workorder:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:workorder:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:workorder:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:workorder:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="workOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="150" />
      <el-table-column label="客户订单号" align="center" prop="sourceOrderNo" min-width="130" show-overflow-tooltip />
      <el-table-column label="学校/客户" align="center" prop="schoolName" min-width="160" show-overflow-tooltip />
      <el-table-column label="款式" align="center" prop="styleName" min-width="150" show-overflow-tooltip />
      <el-table-column label="原料批次" align="center" prop="materialBatchNo" min-width="140" />
      <el-table-column label="计划数量" align="center" prop="plannedQuantity" width="90" />
      <el-table-column label="完工数量" align="center" prop="completedQuantity" width="90" />
      <el-table-column label="工单状态" align="center" prop="orderStatus" width="110">
        <template #default="scope">
          <el-tag :type="getOrderStatusType(scope.row.orderStatus)">{{ formatOptionLabel(orderStatusOptions, scope.row.orderStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="质检状态" align="center" prop="qaStatus" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.qaStatus === '2' ? 'danger' : scope.row.qaStatus === '1' ? 'success' : 'info'">
            {{ formatOptionLabel(qaStatusOptions, scope.row.qaStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="赋码状态" align="center" prop="traceStatus" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.traceStatus === '1' ? 'success' : 'info'">{{ formatOptionLabel(traceStatusOptions, scope.row.traceStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="溯源码数" align="center" prop="traceGeneratedCount" width="90" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:workorder:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:workorder:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" v-model="open" width="1180px" append-to-body>
      <el-form ref="workOrderRef" :model="form" :rules="rules" label-width="110px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="工单基础" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="工单号" prop="workOrderNo">
                  <el-input v-model="form.workOrderNo" placeholder="留空则系统自动生成" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="客户订单号" prop="sourceOrderNo">
                  <el-input v-model="form.sourceOrderNo" placeholder="请输入客户订单号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="学校/客户" prop="schoolName">
                  <el-input v-model="form.schoolName" placeholder="请输入学校或客户名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="款式" prop="styleId">
                  <el-select v-model="form.styleId" filterable placeholder="请选择款式" style="width: 100%" @change="handleStyleChange">
                    <el-option v-for="item in styleOptions" :key="item.styleId" :label="item.styleName" :value="item.styleId" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="原料批次" prop="materialBatchId">
                  <el-select v-model="form.materialBatchId" filterable placeholder="请选择原料批次" style="width: 100%">
                    <el-option
                      v-for="item in materialBatchOptions"
                      :key="item.batchId"
                      :label="`${item.batchNo} / ${item.materialName}`"
                      :value="item.batchId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="赋码模式" prop="codeMode">
                  <el-radio-group v-model="form.codeMode">
                    <el-radio v-for="item in codeModeOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划数量" prop="plannedQuantity">
                  <el-input-number v-model="form.plannedQuantity" :min="1" :precision="0" controls-position="right" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划开工日" prop="plannedStartDate">
                  <el-date-picker v-model="form.plannedStartDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划完工日" prop="plannedEndDate">
                  <el-date-picker v-model="form.plannedEndDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="工序打卡" name="process">
            <div class="table-toolbar">
              <el-button type="primary" plain icon="Plus" @click="addProcessRow">新增工序打卡</el-button>
              <span class="toolbar-tip">建议按裁剪、缝纫、熨烫顺序录入，系统会自动推进工单状态。</span>
            </div>
            <el-table :data="form.processList" border class="dialog-table">
              <el-table-column label="工序" min-width="110">
                <template #default="scope">
                  <el-select v-model="scope.row.processType" placeholder="请选择工序">
                    <el-option v-for="item in processTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="责任人" min-width="120">
                <template #default="scope">
                  <el-input v-model="scope.row.operatorName" placeholder="请输入责任人" />
                </template>
              </el-table-column>
              <el-table-column label="质检员" min-width="120">
                <template #default="scope">
                  <el-input v-model="scope.row.inspectorName" placeholder="请输入质检员" />
                </template>
              </el-table-column>
              <el-table-column label="设备来源" min-width="110">
                <template #default="scope">
                  <el-select v-model="scope.row.deviceType" placeholder="请选择设备">
                    <el-option v-for="item in deviceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="开始时间" min-width="170">
                <template #default="scope">
                  <el-date-picker
                    v-model="scope.row.startTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="选择开始时间"
                  />
                </template>
              </el-table-column>
              <el-table-column label="结束时间" min-width="170">
                <template #default="scope">
                  <el-date-picker
                    v-model="scope.row.endTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="选择结束时间"
                  />
                </template>
              </el-table-column>
              <el-table-column label="合格数量" min-width="100">
                <template #default="scope">
                  <el-input-number v-model="scope.row.passQuantity" :min="0" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="不良数量" min-width="100">
                <template #default="scope">
                  <el-input-number v-model="scope.row.defectiveQuantity" :min="0" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="备注" min-width="160">
                <template #default="scope">
                  <el-input v-model="scope.row.remark" placeholder="如 针距复检通过" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="90" fixed="right">
                <template #default="scope">
                  <el-button link type="danger" icon="Delete" @click="removeProcessRow(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
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

<script setup lang="ts" name="UniformWorkOrder">
import { addWorkOrder, delWorkOrder, getWorkOrder, listWorkOrder, updateWorkOrder } from "@/api/uniform/workorder";
import { listStyleOptions } from "@/api/uniform/style";
import { listMaterialBatchOptions } from "@/api/uniform/material";
import type { UniformMaterialBatch, UniformProcessRecord, UniformStyle, UniformWorkOrder, UniformWorkOrderQueryParams } from "@/types";

const { proxy } = getCurrentInstance()!;

const orderStatusOptions = [
  { label: "待开工", value: "0" },
  { label: "裁剪中", value: "1" },
  { label: "缝纫中", value: "2" },
  { label: "熨烫中", value: "3" },
  { label: "待质检", value: "4" },
  { label: "质检合格", value: "5" },
  { label: "已赋码", value: "6" },
  { label: "质检拦截", value: "8" },
];

const qaStatusOptions = [
  { label: "待检", value: "0" },
  { label: "合格", value: "1" },
  { label: "不合格", value: "2" },
];

const traceStatusOptions = [
  { label: "未生成", value: "0" },
  { label: "已生成", value: "1" },
];

const codeModeOptions = [
  { label: "一物一码", value: "1" },
  { label: "一批一码", value: "2" },
];

const processTypeOptions = [
  { label: "裁剪", value: "cutting" },
  { label: "缝纫", value: "sewing" },
  { label: "熨烫", value: "ironing" },
];

const deviceTypeOptions = [
  { label: "平板", value: "tablet" },
  { label: "扫码枪", value: "scanner" },
  { label: "手工录入", value: "manual" },
];

const workOrderList = ref<UniformWorkOrder[]>([]);
const styleOptions = ref<UniformStyle[]>([]);
const materialBatchOptions = ref<UniformMaterialBatch[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const open = ref(false);
const title = ref("");
const activeTab = ref("basic");
const ids = ref<number[]>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<string[]>([]);

const data = reactive({
  form: {} as UniformWorkOrder,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workOrderNo: undefined,
    sourceOrderNo: undefined,
    schoolName: undefined,
    orderStatus: undefined,
    qaStatus: undefined,
    traceStatus: undefined,
  } as UniformWorkOrderQueryParams,
  rules: {
    styleId: [{ required: true, message: "款式不能为空", trigger: "change" }],
    materialBatchId: [{ required: true, message: "原料批次不能为空", trigger: "change" }],
    plannedQuantity: [{ required: true, message: "计划数量不能为空", trigger: "blur" }],
    codeMode: [{ required: true, message: "赋码模式不能为空", trigger: "change" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listWorkOrder(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      workOrderList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadOptions() {
  listStyleOptions().then((response) => {
    styleOptions.value = response.data || [];
  });
  listMaterialBatchOptions().then((response) => {
    materialBatchOptions.value = response.data || [];
  });
}

function createDefaultProcessRow(): UniformProcessRecord {
  return {
    processType: "cutting",
    operatorName: undefined,
    inspectorName: undefined,
    deviceType: "manual",
    startTime: undefined,
    endTime: undefined,
    passQuantity: undefined,
    defectiveQuantity: 0,
    remark: undefined,
  };
}

function reset() {
  form.value = {
    workOrderId: undefined,
    workOrderNo: undefined,
    sourceOrderNo: undefined,
    schoolName: undefined,
    styleId: undefined,
    materialBatchId: undefined,
    plannedQuantity: 1,
    codeMode: "1",
    plannedStartDate: undefined,
    plannedEndDate: undefined,
    remark: undefined,
    processList: [],
  };
  activeTab.value = "basic";
  proxy.resetForm("workOrderRef");
}

function cancel() {
  open.value = false;
  reset();
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleSelectionChange(selection: UniformWorkOrder[]) {
  ids.value = selection.map((item) => item.workOrderId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增生产工单";
}

function handleUpdate(row?: UniformWorkOrder) {
  reset();
  const workOrderId = row?.workOrderId || ids.value[0];
  getWorkOrder(workOrderId).then((response) => {
    form.value = {
      ...response.data!,
      processList: response.data?.processList || [],
    };
    open.value = true;
    title.value = "修改生产工单";
  });
}

function addProcessRow() {
  form.value.processList = form.value.processList || [];
  form.value.processList.push(createDefaultProcessRow());
}

function removeProcessRow(index: number) {
  form.value.processList?.splice(index, 1);
}

function normalizeProcessList(processList: UniformProcessRecord[] = []) {
  return processList
    .filter((item) => item.processType || item.operatorName || item.inspectorName)
    .map((item) => ({
      ...item,
      deviceType: item.deviceType || "manual",
    }));
}

function submitForm() {
  proxy.$refs["workOrderRef"].validate((valid: boolean) => {
    if (!valid) {
      activeTab.value = "basic";
      return;
    }
    const payload: UniformWorkOrder = {
      ...form.value,
      processList: normalizeProcessList(form.value.processList),
    };
    const request = payload.workOrderId ? updateWorkOrder(payload) : addWorkOrder(payload);
    request.then(() => {
      proxy.$modal.msgSuccess(payload.workOrderId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformWorkOrder) {
  const workOrderIds = row?.workOrderId || ids.value;
  proxy.$modal.confirm(`是否确认删除生产工单编号为 "${workOrderIds}" 的数据项？`).then(() => {
    return delWorkOrder(workOrderIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/workorder/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `workorder_${Date.now()}.xlsx`);
}

function handleStyleChange(styleId?: number) {
  const targetStyle = styleOptions.value.find((item) => item.styleId === styleId);
  if (targetStyle?.codeMode) {
    form.value.codeMode = targetStyle.codeMode;
  }
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

function getOrderStatusType(status?: string) {
  if (status === "8") return "danger";
  if (status === "6") return "success";
  if (status === "5") return "success";
  if (status === "4") return "warning";
  if (status === "3" || status === "2" || status === "1") return "warning";
  return "info";
}

onMounted(() => {
  getList();
  loadOptions();
});
</script>

<style scoped lang="scss">
.tips-alert {
  margin-bottom: 16px;
}

.table-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.toolbar-tip {
  color: #909399;
  font-size: 13px;
}

.dialog-table :deep(.el-input-number),
.dialog-table :deep(.el-select),
.dialog-table :deep(.el-date-editor) {
  width: 100%;
}
</style>
