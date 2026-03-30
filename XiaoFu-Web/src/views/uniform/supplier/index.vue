<template>
  <div class="app-container">
    <el-row :gutter="16" class="summary-row">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never" class="summary-card total-card">
          <el-statistic title="供应商总数" :value="alertSummary.totalCount || 0" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never" class="summary-card healthy-card">
          <el-statistic title="状态正常" :value="alertSummary.normalCount || 0" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never" class="summary-card warning-card">
          <el-statistic title="30天内到期" :value="alertSummary.warningCount || 0" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card shadow="never" class="summary-card danger-card">
          <el-statistic title="已过期" :value="alertSummary.expiredCount || 0" />
        </el-card>
      </el-col>
    </el-row>

    <el-alert
      title="证照到期预警可直接结合定时任务使用，任务调用目标为 uniformSupplierTask.scanCertificateAlerts"
      type="info"
      :closable="false"
      class="tips-alert"
    />

    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="供应商编码" prop="supplierCode">
        <el-input
          v-model="queryParams.supplierCode"
          placeholder="请输入供应商编码"
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商名称" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入供应商名称"
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商类型" prop="supplierType">
        <el-select v-model="queryParams.supplierType" placeholder="请选择类型" clearable style="width: 220px">
          <el-option v-for="item in supplierTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警状态" prop="alertStatus">
        <el-select v-model="queryParams.alertStatus" placeholder="请选择预警状态" clearable style="width: 220px">
          <el-option v-for="item in alertStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商状态" prop="status">
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:supplier:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:supplier:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:supplier:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:supplier:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="supplierList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编码" align="center" prop="supplierCode" min-width="120" />
      <el-table-column label="名称" align="center" prop="supplierName" min-width="180" show-overflow-tooltip />
      <el-table-column label="类型" align="center" prop="supplierType" width="120">
        <template #default="scope">
          <span>{{ formatOptionLabel(supplierTypeOptions, scope.row.supplierType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系人" align="center" prop="contactPerson" width="100" />
      <el-table-column label="联系电话" align="center" prop="contactPhone" width="130" />
      <el-table-column label="最近到期日" align="center" prop="nearestExpireDate" width="120" />
      <el-table-column label="预警证照" align="center" prop="alertCertificates" min-width="180" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.alertCertificates || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警状态" align="center" prop="alertStatus" width="120">
        <template #default="scope">
          <el-tag :type="getAlertTagType(scope.row.alertStatus)">{{ formatOptionLabel(alertStatusOptions, scope.row.alertStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">{{ formatOptionLabel(statusOptions, scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:supplier:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:supplier:remove']">删除</el-button>
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
      <el-form ref="supplierRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商编码" prop="supplierCode">
              <el-input v-model="form.supplierCode" placeholder="如 FAB-001" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="supplierName">
              <el-input v-model="form.supplierName" placeholder="请输入供应商名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商类型" prop="supplierType">
              <el-select v-model="form.supplierType" placeholder="请选择供应商类型" style="width: 100%">
                <el-option v-for="item in supplierTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警提前天数" prop="warningDays">
              <el-input-number v-model="form.warningDays" :min="1" :max="365" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入联系地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">ISO9001 资质</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="证书编号" prop="isoCertNo">
              <el-input v-model="form.isoCertNo" placeholder="请输入证书编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="到期日期" prop="isoExpireDate">
              <el-date-picker v-model="form.isoExpireDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证书附件" prop="isoCertFileUrl">
              <file-upload v-model="form.isoCertFileUrl" :limit="1" :file-type="['pdf']" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">环保认证</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="认证编号" prop="envCertNo">
              <el-input v-model="form.envCertNo" placeholder="请输入认证编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="到期日期" prop="envExpireDate">
              <el-date-picker v-model="form.envExpireDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="认证附件" prop="envCertFileUrl">
              <file-upload v-model="form.envCertFileUrl" :limit="1" :file-type="['pdf']" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">面料质检报告</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="报告编号" prop="qualityReportNo">
              <el-input v-model="form.qualityReportNo" placeholder="请输入报告编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="到期日期" prop="qualityReportExpireDate">
              <el-date-picker
                v-model="form.qualityReportExpireDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="报告附件" prop="qualityReportFileUrl">
              <file-upload v-model="form.qualityReportFileUrl" :limit="1" :file-type="['pdf']" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="item in statusOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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

<script setup lang="ts" name="UniformSupplier">
import { addSupplier, delSupplier, getSupplier, getSupplierAlertSummary, listSupplier, updateSupplier } from "@/api/uniform/supplier";
import type { UniformSupplier, UniformSupplierAlertSummary, UniformSupplierQueryParams } from "@/types";

const { proxy } = getCurrentInstance()!;

const supplierTypeOptions = [
  { label: "面料供应商", value: "fabric" },
  { label: "辅料供应商", value: "accessory" },
  { label: "加工厂", value: "processing" },
  { label: "物流商", value: "logistics" },
  { label: "其他", value: "other" },
];

const alertStatusOptions = [
  { label: "正常", value: "0" },
  { label: "30天内到期", value: "1" },
  { label: "已过期", value: "2" },
];

const statusOptions = [
  { label: "正常", value: "0" },
  { label: "停用", value: "1" },
];

const supplierList = ref<UniformSupplier[]>([]);
const alertSummary = ref<UniformSupplierAlertSummary>({});
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
  form: {} as UniformSupplier,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    supplierCode: undefined,
    supplierName: undefined,
    supplierType: undefined,
    alertStatus: undefined,
    status: undefined,
  } as UniformSupplierQueryParams,
  rules: {
    supplierCode: [{ required: true, message: "供应商编码不能为空", trigger: "blur" }],
    supplierName: [{ required: true, message: "供应商名称不能为空", trigger: "blur" }],
    supplierType: [{ required: true, message: "供应商类型不能为空", trigger: "change" }],
    warningDays: [{ required: true, message: "预警提前天数不能为空", trigger: "blur" }],
    status: [{ required: true, message: "供应商状态不能为空", trigger: "change" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listSupplier(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      supplierList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
  getSummary();
}

function getSummary() {
  getSupplierAlertSummary().then((response) => {
    alertSummary.value = response.data || {};
  });
}

function cancel() {
  open.value = false;
  reset();
}

function reset() {
  form.value = {
    supplierId: undefined,
    supplierCode: undefined,
    supplierName: undefined,
    supplierType: "fabric",
    contactPerson: undefined,
    contactPhone: undefined,
    address: undefined,
    isoCertNo: undefined,
    isoExpireDate: undefined,
    isoCertFileUrl: undefined,
    envCertNo: undefined,
    envExpireDate: undefined,
    envCertFileUrl: undefined,
    qualityReportNo: undefined,
    qualityReportExpireDate: undefined,
    qualityReportFileUrl: undefined,
    warningDays: 30,
    status: "0",
    remark: undefined,
  };
  proxy.resetForm("supplierRef");
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

function handleSelectionChange(selection: UniformSupplier[]) {
  ids.value = selection.map((item) => item.supplierId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增供应商";
}

function handleUpdate(row?: UniformSupplier) {
  reset();
  const supplierId = row?.supplierId || ids.value[0];
  getSupplier(supplierId).then((response) => {
    form.value = response.data!;
    open.value = true;
    title.value = "修改供应商";
  });
}

function submitForm() {
  proxy.$refs["supplierRef"].validate((valid: boolean) => {
    if (!valid) {
      return;
    }
    const request = form.value.supplierId ? updateSupplier(form.value) : addSupplier(form.value);
    request.then(() => {
      proxy.$modal.msgSuccess(form.value.supplierId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformSupplier) {
  const supplierIds = row?.supplierId || ids.value;
  proxy.$modal.confirm(`是否确认删除供应商编号为 "${supplierIds}" 的数据项？`).then(() => {
    return delSupplier(supplierIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download(
    "uniform/supplier/export",
    proxy.addDateRange({ ...queryParams.value }, dateRange.value),
    `supplier_${new Date().getTime()}.xlsx`,
  );
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

function getAlertTagType(status?: string) {
  if (status === "2") {
    return "danger";
  }
  if (status === "1") {
    return "warning";
  }
  return "success";
}

onMounted(() => {
  getList();
});
</script>

<style scoped lang="scss">
.summary-row {
  margin-bottom: 16px;
}

.summary-card {
  border: 0;
  border-left: 4px solid transparent;
}

.total-card {
  border-left-color: #409eff;
}

.healthy-card {
  border-left-color: #67c23a;
}

.warning-card {
  border-left-color: #e6a23c;
}

.danger-card {
  border-left-color: #f56c6c;
}

.tips-alert {
  margin-bottom: 16px;
}
</style>
