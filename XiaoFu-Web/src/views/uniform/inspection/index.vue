<template>
  <div class="app-container">
    <el-alert
      title="系统会按 GB/T 31888-2015 自动判定并拦截不合格工单：甲醛≤75、PH 4.0-8.5、无异味、芳香胺合格、色牢度≥3、起球率≥3。"
      type="warning"
      :closable="false"
      class="tips-alert"
    />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="质检单号" prop="inspectionNo">
        <el-input v-model="queryParams.inspectionNo" placeholder="请输入质检单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="queryParams.workOrderNo" placeholder="请输入工单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="质检结果" prop="result">
        <el-select v-model="queryParams.result" placeholder="请选择质检结果" clearable style="width: 220px">
          <el-option v-for="item in resultOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="拦截状态" prop="interceptStatus">
        <el-select v-model="queryParams.interceptStatus" placeholder="请选择拦截状态" clearable style="width: 220px">
          <el-option v-for="item in interceptOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="质检时间" style="width: 310px">
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:inspection:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:inspection:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:inspection:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:inspection:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="质检单号" align="center" prop="inspectionNo" min-width="150" />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="150" />
      <el-table-column label="款式" align="center" prop="styleName" min-width="140" show-overflow-tooltip />
      <el-table-column label="原料批次" align="center" prop="batchNo" min-width="140" />
      <el-table-column label="甲醛" align="center" prop="formaldehyde" width="90" />
      <el-table-column label="PH" align="center" prop="phValue" width="80" />
      <el-table-column label="色牢度" align="center" prop="colorFastness" width="90" />
      <el-table-column label="起球率" align="center" prop="pillingGrade" width="90" />
      <el-table-column label="质检结果" align="center" prop="result" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.result === '0' ? 'success' : 'danger'">{{ formatOptionLabel(resultOptions, scope.row.result) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="拦截状态" align="center" prop="interceptStatus" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.interceptStatus === '1' ? 'danger' : 'success'">{{ formatOptionLabel(interceptOptions, scope.row.interceptStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="不合格原因" align="center" prop="failReason" min-width="220" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.failReason || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="质检时间" align="center" prop="inspectTime" width="170" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:inspection:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:inspection:remove']">删除</el-button>
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

    <el-dialog :title="title" v-model="open" width="1100px" append-to-body>
      <el-form ref="inspectionRef" :model="form" :rules="rules" label-width="115px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="质检单号" prop="inspectionNo">
              <el-input v-model="form.inspectionNo" placeholder="留空则系统自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联工单" prop="workOrderId">
              <el-select v-model="form.workOrderId" filterable placeholder="请选择工单" style="width: 100%">
                <el-option v-for="item in workOrderOptions" :key="item.workOrderId" :label="`${item.workOrderNo} / ${item.styleName}`" :value="item.workOrderId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行标准" prop="gbStandard">
              <el-input v-model="form.gbStandard" placeholder="默认 GB/T 31888-2015" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="抽检数量" prop="sampleQuantity">
              <el-input-number v-model="form.sampleQuantity" :min="1" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="甲醛含量" prop="formaldehyde">
              <el-input-number v-model="form.formaldehyde" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="PH值" prop="phValue">
              <el-input-number v-model="form.phValue" :min="0" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="色牢度" prop="colorFastness">
              <el-input-number v-model="form.colorFastness" :min="0" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="起球率" prop="pillingGrade">
              <el-input-number v-model="form.pillingGrade" :min="0" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="异味判定" prop="odorResult">
              <el-radio-group v-model="form.odorResult">
                <el-radio v-for="item in odorOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="芳香胺判定" prop="amineResult">
              <el-radio-group v-model="form.amineResult">
                <el-radio v-for="item in amineOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质检员" prop="inspectorName">
              <el-input v-model="form.inspectorName" placeholder="请输入质检员姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质检时间" prop="inspectTime">
              <el-date-picker
                v-model="form.inspectTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择质检时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="质检报告PDF" prop="reportFileUrl">
              <file-upload v-model="form.reportFileUrl" :limit="1" :file-type="['pdf']" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="结论说明" prop="conclusion">
              <el-input v-model="form.conclusion" type="textarea" :rows="2" placeholder="可留空，系统会自动补默认结论" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
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

<script setup lang="ts" name="UniformInspection">
import { addInspection, delInspection, getInspection, listInspection, updateInspection } from "@/api/uniform/inspection";
import { listWorkOrderOptions } from "@/api/uniform/workorder";
import type { UniformQualityInspection, UniformQualityInspectionQueryParams, UniformWorkOrder } from "@/types";

const { proxy } = getCurrentInstance()!;

const resultOptions = [
  { label: "合格", value: "0" },
  { label: "不合格", value: "1" },
];

const interceptOptions = [
  { label: "放行", value: "0" },
  { label: "拦截", value: "1" },
];

const odorOptions = [
  { label: "无异味", value: "0" },
  { label: "有异味", value: "1" },
];

const amineOptions = [
  { label: "合格", value: "0" },
  { label: "不合格", value: "1" },
];

const inspectionList = ref<UniformQualityInspection[]>([]);
const workOrderOptions = ref<UniformWorkOrder[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const open = ref(false);
const title = ref("");
const ids = ref<number[]>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<string[]>([]);

const data = reactive({
  form: {} as UniformQualityInspection,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    inspectionNo: undefined,
    workOrderNo: undefined,
    result: undefined,
    interceptStatus: undefined,
  } as UniformQualityInspectionQueryParams,
  rules: {
    workOrderId: [{ required: true, message: "关联工单不能为空", trigger: "change" }],
    gbStandard: [{ required: true, message: "执行标准不能为空", trigger: "blur" }],
    sampleQuantity: [{ required: true, message: "抽检数量不能为空", trigger: "blur" }],
    odorResult: [{ required: true, message: "异味判定不能为空", trigger: "change" }],
    amineResult: [{ required: true, message: "芳香胺判定不能为空", trigger: "change" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listInspection(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      inspectionList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadWorkOrders() {
  listWorkOrderOptions().then((response) => {
    workOrderOptions.value = response.data || [];
  });
}

function reset() {
  form.value = {
    inspectionId: undefined,
    inspectionNo: undefined,
    workOrderId: undefined,
    gbStandard: "GB/T 31888-2015",
    sampleQuantity: 1,
    formaldehyde: undefined,
    phValue: undefined,
    odorResult: "0",
    amineResult: "0",
    colorFastness: undefined,
    pillingGrade: undefined,
    inspectorName: undefined,
    inspectTime: undefined,
    reportFileUrl: undefined,
    conclusion: undefined,
    remark: undefined,
  };
  proxy.resetForm("inspectionRef");
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

function handleSelectionChange(selection: UniformQualityInspection[]) {
  ids.value = selection.map((item) => item.inspectionId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增成品质检";
}

function handleUpdate(row?: UniformQualityInspection) {
  reset();
  const inspectionId = row?.inspectionId || ids.value[0];
  getInspection(inspectionId).then((response) => {
    form.value = response.data!;
    open.value = true;
    title.value = "修改成品质检";
  });
}

function submitForm() {
  proxy.$refs["inspectionRef"].validate((valid: boolean) => {
    if (!valid) {
      return;
    }
    const request = form.value.inspectionId ? updateInspection(form.value) : addInspection(form.value);
    request.then(() => {
      proxy.$modal.msgSuccess(form.value.inspectionId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformQualityInspection) {
  const inspectionIds = row?.inspectionId || ids.value;
  proxy.$modal.confirm(`是否确认删除成品质检编号为 "${inspectionIds}" 的数据项？`).then(() => {
    return delInspection(inspectionIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/inspection/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `inspection_${Date.now()}.xlsx`);
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
