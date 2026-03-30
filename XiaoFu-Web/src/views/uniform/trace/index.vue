<template>
  <div class="app-container">
    <el-alert
      title="只有最新质检结果为合格的工单才能生成溯源码；一批一码默认生成 1 条，一物一码默认按工单计划数量生成。"
      type="success"
      :closable="false"
      class="tips-alert"
    />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="queryParams.workOrderNo" placeholder="请输入工单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="溯源码" prop="traceCode">
        <el-input v-model="queryParams.traceCode" placeholder="请输入溯源码" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="赋码模式" prop="codeMode">
        <el-select v-model="queryParams.codeMode" placeholder="请选择赋码模式" clearable style="width: 220px">
          <el-option v-for="item in codeModeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="打印状态" prop="printStatus">
        <el-select v-model="queryParams.printStatus" placeholder="请选择打印状态" clearable style="width: 220px">
          <el-option v-for="item in printStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="Plus" @click="handleGenerate" v-hasPermi="['uniform:trace:add']">生成溯源码</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:trace:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:trace:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="traceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="150" />
      <el-table-column label="款式" align="center" prop="styleName" min-width="140" show-overflow-tooltip />
      <el-table-column label="原料批次" align="center" prop="batchNo" min-width="140" />
      <el-table-column label="赋码模式" align="center" prop="codeMode" width="100">
        <template #default="scope">
          <span>{{ formatOptionLabel(codeModeOptions, scope.row.codeMode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="序号" align="center" prop="serialNo" width="80" />
      <el-table-column label="溯源码" align="center" prop="traceCode" min-width="220" show-overflow-tooltip />
      <el-table-column label="二维码内容" align="center" prop="qrContent" min-width="260" show-overflow-tooltip />
      <el-table-column label="打印状态" align="center" prop="printStatus" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.printStatus === '1' ? 'success' : 'info'">{{ formatOptionLabel(printStatusOptions, scope.row.printStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="traceStatus" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.traceStatus === '0' ? 'success' : 'danger'">{{ formatOptionLabel(traceStatusOptions, scope.row.traceStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="170" />
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="success" @click="handlePortalPreview(scope.row)">微站预览</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:trace:remove']">删除</el-button>
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

    <el-dialog title="生成溯源码" v-model="generateOpen" width="620px" append-to-body>
      <el-form ref="generateRef" :model="generateForm" :rules="generateRules" label-width="110px">
        <el-form-item label="选择工单" prop="workOrderId">
          <el-select v-model="generateForm.workOrderId" filterable placeholder="请选择已完成质检的工单" style="width: 100%">
            <el-option v-for="item in workOrderOptions" :key="item.workOrderId" :label="`${item.workOrderNo} / ${item.styleName} / ${item.materialBatchNo || '-'}`" :value="item.workOrderId" />
          </el-select>
        </el-form-item>
        <el-form-item label="生成数量" prop="generateCount">
          <el-input-number v-model="generateForm.generateCount" :min="1" :precision="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-alert title="一批一码场景下后端会自动按 1 条生成；一物一码场景下可手动调整生成数量。" type="info" :closable="false" />
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitGenerate">确 定</el-button>
          <el-button @click="generateOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="UniformTrace">
import { delTraceCode, generateTraceCode, listTraceCode } from "@/api/uniform/trace";
import { listWorkOrderOptions } from "@/api/uniform/workorder";
import type { UniformTraceCode, UniformTraceCodeQueryParams, UniformTraceGenerateRequest, UniformWorkOrder } from "@/types";

const { proxy } = getCurrentInstance()!;

const codeModeOptions = [
  { label: "一物一码", value: "1" },
  { label: "一批一码", value: "2" },
];

const printStatusOptions = [
  { label: "未打印", value: "0" },
  { label: "已打印", value: "1" },
];

const traceStatusOptions = [
  { label: "有效", value: "0" },
  { label: "作废", value: "1" },
];

const traceList = ref<UniformTraceCode[]>([]);
const workOrderOptions = ref<UniformWorkOrder[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const generateOpen = ref(false);
const ids = ref<number[]>([]);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<string[]>([]);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workOrderNo: undefined,
    traceCode: undefined,
    codeMode: undefined,
    printStatus: undefined,
    traceStatus: undefined,
  } as UniformTraceCodeQueryParams,
  generateForm: {
    workOrderId: undefined,
    generateCount: 1,
  } as UniformTraceGenerateRequest,
  generateRules: {
    workOrderId: [{ required: true, message: "工单不能为空", trigger: "change" }],
    generateCount: [{ required: true, message: "生成数量不能为空", trigger: "blur" }],
  },
});

const { queryParams, generateForm, generateRules } = toRefs(data);

function getList() {
  loading.value = true;
  listTraceCode(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      traceList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadWorkOrders() {
  listWorkOrderOptions().then((response) => {
    workOrderOptions.value = (response.data || []).filter((item) => item.qaStatus === "1" && item.traceStatus !== "1");
  });
}

function resetGenerateForm() {
  generateForm.value = {
    workOrderId: undefined,
    generateCount: 1,
  };
  proxy.resetForm("generateRef");
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

function handleSelectionChange(selection: UniformTraceCode[]) {
  ids.value = selection.map((item) => item.traceId!);
  multiple.value = !selection.length;
}

function handleGenerate() {
  resetGenerateForm();
  generateOpen.value = true;
  loadWorkOrders();
}

function submitGenerate() {
  proxy.$refs["generateRef"].validate((valid: boolean) => {
    if (!valid) {
      return;
    }
    generateTraceCode(generateForm.value).then(() => {
      proxy.$modal.msgSuccess("溯源码生成成功");
      generateOpen.value = false;
      getList();
      loadWorkOrders();
    });
  });
}

function handleDelete(row?: UniformTraceCode) {
  const traceIds = row?.traceId || ids.value;
  proxy.$modal.confirm(`是否确认删除溯源码编号为 "${traceIds}" 的数据项？`).then(() => {
    return delTraceCode(traceIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
    loadWorkOrders();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/trace/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `trace_code_${Date.now()}.xlsx`);
}

function handlePortalPreview(row: UniformTraceCode) {
  if (!row.traceCode) {
    return;
  }
  window.open(`/portal/trace/${encodeURIComponent(row.traceCode)}`, "_blank");
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

onMounted(() => {
  getList();
  loadWorkOrders();
});
</script>

<style scoped lang="scss">
.tips-alert {
  margin-bottom: 16px;
}
</style>
