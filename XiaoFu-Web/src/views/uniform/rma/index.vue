<template>
  <div class="app-container">
    <el-alert
      title="退换货单会绑定到溯源码，便于追踪旧衣回收、补发和售后闭环。"
      type="warning"
      :closable="false"
      class="tips-alert"
    />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="RMA单号" prop="rmaNo">
        <el-input v-model="queryParams.rmaNo" placeholder="请输入RMA单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="溯源码" prop="traceCode">
        <el-input v-model="queryParams.traceCode" placeholder="请输入溯源码" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="申请类型" prop="requestType">
        <el-select v-model="queryParams.requestType" placeholder="请选择申请类型" clearable style="width: 220px">
          <el-option v-for="item in requestTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:rma:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:rma:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:rma:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:rma:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="rmaList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="RMA单号" align="center" prop="rmaNo" min-width="150" />
      <el-table-column label="溯源码" align="center" prop="traceCode" min-width="200" show-overflow-tooltip />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="150" />
      <el-table-column label="学生" align="center" prop="studentName" width="100" />
      <el-table-column label="学校/班级" align="center" min-width="180" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.schoolName || "-" }} / {{ scope.row.className || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请类型" align="center" prop="requestType" width="100">
        <template #default="scope">
          <span>{{ formatOptionLabel(requestTypeOptions, scope.row.requestType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="原因" align="center" prop="reasonType" width="120">
        <template #default="scope">
          <span>{{ formatOptionLabel(reasonTypeOptions, scope.row.reasonType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="原尺码" align="center" prop="oldSizeCode" width="90" />
      <el-table-column label="新尺码" align="center" prop="newSizeCode" width="90">
        <template #default="scope">
          <span>{{ scope.row.newSizeCode || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="110">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ formatOptionLabel(statusOptions, scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="applyTime" width="170" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:rma:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:rma:remove']">删除</el-button>
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

    <el-dialog :title="title" v-model="open" width="980px" append-to-body>
      <el-form ref="rmaRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="RMA单号" prop="rmaNo">
              <el-input v-model="form.rmaNo" placeholder="留空则系统自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联溯源码" prop="traceId">
              <el-select v-model="form.traceId" filterable placeholder="请选择溯源码" style="width: 100%" @change="handleTraceChange">
                <el-option
                  v-for="item in traceOptions"
                  :key="item.traceId"
                  :label="`${item.traceCode} / ${item.workOrderNo}`"
                  :value="item.traceId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="selectedTrace">
            <el-alert
              :title="`当前绑定：工单 ${selectedTrace.workOrderNo || '-'} / 款式 ${selectedTrace.styleName || '-'} / 批次 ${selectedTrace.batchNo || '-'}`"
              type="info"
              :closable="false"
            />
          </el-col>
          <el-col :span="12">
            <el-form-item label="学校" prop="schoolName">
              <el-input v-model="form.schoolName" placeholder="请输入学校名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="className">
              <el-input v-model="form.className" placeholder="请输入班级名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入学生姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请数量" prop="applyQuantity">
              <el-input-number v-model="form.applyQuantity" :min="1" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请类型" prop="requestType">
              <el-radio-group v-model="form.requestType">
                <el-radio v-for="item in requestTypeOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原因类型" prop="reasonType">
              <el-select v-model="form.reasonType" placeholder="请选择原因类型" style="width: 100%">
                <el-option v-for="item in reasonTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原尺码" prop="oldSizeCode">
              <el-input v-model="form.oldSizeCode" placeholder="请输入原尺码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="新尺码" prop="newSizeCode">
              <el-input v-model="form.newSizeCode" :disabled="form.requestType === '2'" placeholder="退货场景可留空" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请时间" prop="applyTime">
              <el-date-picker
                v-model="form.applyTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择申请时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="回收时间" prop="receiveTime">
              <el-date-picker
                v-model="form.receiveTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择旧衣回收时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="补发单号" prop="resendTrackingNo">
              <el-input v-model="form.resendTrackingNo" placeholder="换货场景填写补发物流单号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理结论" prop="conclusion">
              <el-input v-model="form.conclusion" type="textarea" :rows="2" placeholder="请输入处理结论" />
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

<script setup lang="ts" name="UniformRmaOrder">
import { addRmaOrder, delRmaOrder, getRmaOrder, listRmaOrder, updateRmaOrder } from "@/api/uniform/rma";
import { listTraceCodeOptions } from "@/api/uniform/trace";
import type { UniformRmaOrder, UniformRmaOrderQueryParams, UniformTraceCode } from "@/types";

const { proxy } = getCurrentInstance()!;

const requestTypeOptions = [
  { label: "换货", value: "1" },
  { label: "退货", value: "2" },
];

const reasonTypeOptions = [
  { label: "尺码偏小", value: "size_small" },
  { label: "尺码偏大", value: "size_large" },
  { label: "质量问题", value: "quality_issue" },
  { label: "其他", value: "other" },
];

const statusOptions = [
  { label: "待审核", value: "0" },
  { label: "待回收入库", value: "1" },
  { label: "待补发", value: "2" },
  { label: "已完成", value: "3" },
  { label: "已驳回", value: "8" },
];

const rmaList = ref<UniformRmaOrder[]>([]);
const traceOptions = ref<UniformTraceCode[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const open = ref(false);
const title = ref("");
const ids = ref<number[]>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<string[]>([]);
const selectedTrace = computed(() => traceOptions.value.find((item) => item.traceId === form.value.traceId));

const data = reactive({
  form: {} as UniformRmaOrder,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    rmaNo: undefined,
    traceCode: undefined,
    studentName: undefined,
    requestType: undefined,
    status: undefined,
  } as UniformRmaOrderQueryParams,
  rules: {
    traceId: [{ required: true, message: "溯源码不能为空", trigger: "change" }],
    studentName: [{ required: true, message: "学生姓名不能为空", trigger: "blur" }],
    requestType: [{ required: true, message: "申请类型不能为空", trigger: "change" }],
    reasonType: [{ required: true, message: "原因类型不能为空", trigger: "change" }],
    applyQuantity: [{ required: true, message: "申请数量不能为空", trigger: "blur" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listRmaOrder(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      rmaList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadTraceOptions() {
  listTraceCodeOptions().then((response) => {
    traceOptions.value = response.data || [];
  });
}

function reset() {
  form.value = {
    rmaId: undefined,
    rmaNo: undefined,
    traceId: undefined,
    schoolName: undefined,
    className: undefined,
    studentName: undefined,
    requestType: "1",
    reasonType: "size_small",
    oldSizeCode: undefined,
    newSizeCode: undefined,
    applyQuantity: 1,
    status: "0",
    applyTime: undefined,
    receiveTime: undefined,
    resendTrackingNo: undefined,
    conclusion: undefined,
    remark: undefined,
  };
  proxy.resetForm("rmaRef");
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

function handleSelectionChange(selection: UniformRmaOrder[]) {
  ids.value = selection.map((item) => item.rmaId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增退换货RMA";
}

function handleUpdate(row?: UniformRmaOrder) {
  reset();
  const rmaId = row?.rmaId || ids.value[0];
  getRmaOrder(rmaId).then((response) => {
    form.value = response.data!;
    open.value = true;
    title.value = "修改退换货RMA";
  });
}

function handleTraceChange() {
  if (form.value.requestType === "2") {
    form.value.newSizeCode = "";
  }
}

function submitForm() {
  proxy.$refs["rmaRef"].validate((valid: boolean) => {
    if (!valid) {
      return;
    }
    const payload: UniformRmaOrder = {
      ...form.value,
      newSizeCode: form.value.requestType === "2" ? "" : form.value.newSizeCode,
    };
    const request = payload.rmaId ? updateRmaOrder(payload) : addRmaOrder(payload);
    request.then(() => {
      proxy.$modal.msgSuccess(payload.rmaId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformRmaOrder) {
  const rmaIds = row?.rmaId || ids.value;
  proxy.$modal.confirm(`是否确认删除退换货RMA编号为 "${rmaIds}" 的数据项？`).then(() => {
    return delRmaOrder(rmaIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/rma/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `rma_order_${Date.now()}.xlsx`);
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

function getStatusType(status?: string) {
  if (status === "8") return "danger";
  if (status === "3") return "success";
  if (status === "2") return "warning";
  if (status === "1") return "info";
  return "";
}

onMounted(() => {
  getList();
  loadTraceOptions();
});
</script>

<style scoped lang="scss">
.tips-alert {
  margin-bottom: 16px;
}
</style>
