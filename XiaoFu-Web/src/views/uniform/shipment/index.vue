<template>
  <div class="app-container">
    <el-alert
      title="班级发运单绑定已赋码工单，装箱明细会自动汇总总件数和装箱进度，并随发货/签收时间推进履约状态。"
      type="success"
      :closable="false"
      class="tips-alert"
    />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="发运单号" prop="shipmentNo">
        <el-input v-model="queryParams.shipmentNo" placeholder="请输入发运单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="queryParams.workOrderNo" placeholder="请输入工单号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学校" prop="schoolName">
        <el-input v-model="queryParams.schoolName" placeholder="请输入学校名称" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="班级" prop="className">
        <el-input v-model="queryParams.className" placeholder="请输入班级名称" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="shipmentStatus">
        <el-select v-model="queryParams.shipmentStatus" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="item in shipmentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:shipment:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:shipment:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:shipment:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:shipment:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="shipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="发运单号" align="center" prop="shipmentNo" min-width="150" />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="140" />
      <el-table-column label="款式" align="center" prop="styleName" min-width="140" show-overflow-tooltip />
      <el-table-column label="学校/班级" align="center" min-width="220" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.schoolName }} / {{ scope.row.gradeName || "-" }}{{ scope.row.className }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总件数" align="center" prop="totalQuantity" width="90" />
      <el-table-column label="已装箱" align="center" prop="packedQuantity" width="90" />
      <el-table-column label="状态" align="center" prop="shipmentStatus" width="100">
        <template #default="scope">
          <el-tag :type="getShipmentStatusType(scope.row.shipmentStatus)">{{ formatOptionLabel(shipmentStatusOptions, scope.row.shipmentStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="物流单号" align="center" prop="logisticsNo" min-width="150" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.logisticsNo || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="dispatchTime" width="170" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:shipment:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:shipment:remove']">删除</el-button>
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
      <el-form ref="shipmentRef" :model="form" :rules="rules" label-width="110px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基础信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="发运单号" prop="shipmentNo">
                  <el-input v-model="form.shipmentNo" placeholder="留空则系统自动生成" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="关联工单" prop="workOrderId">
                  <el-select v-model="form.workOrderId" filterable placeholder="请选择已赋码工单" style="width: 100%">
                    <el-option
                      v-for="item in workOrderOptions"
                      :key="item.workOrderId"
                      :label="`${item.workOrderNo} / ${item.styleName} / 溯源码${item.traceGeneratedCount || 0}`"
                      :value="item.workOrderId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="学校" prop="schoolName">
                  <el-input v-model="form.schoolName" placeholder="请输入学校名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="校区" prop="campusName">
                  <el-input v-model="form.campusName" placeholder="请输入校区名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="年级" prop="gradeName">
                  <el-input v-model="form.gradeName" placeholder="例如 2026级初一" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="班级" prop="className">
                  <el-input v-model="form.className" placeholder="例如 3班" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="物流公司" prop="logisticsCompany">
                  <el-input v-model="form.logisticsCompany" placeholder="例如 顺丰 / 京东物流" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="物流单号" prop="logisticsNo">
                  <el-input v-model="form.logisticsNo" placeholder="请输入物流单号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发货时间" prop="dispatchTime">
                  <el-date-picker
                    v-model="form.dispatchTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="请选择发货时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="签收时间" prop="signTime">
                  <el-date-picker
                    v-model="form.signTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="请选择签收时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="装箱明细" name="package">
            <div class="table-toolbar">
              <el-button type="primary" plain icon="Plus" @click="addPackageRow">新增箱单</el-button>
              <span class="toolbar-tip">数量大于 0 的箱单才会保存，系统会自动汇总总件数和装箱件数。</span>
            </div>
            <el-table :data="form.packageList" border class="dialog-table">
              <el-table-column label="箱号" min-width="130">
                <template #default="scope">
                  <el-input v-model="scope.row.packageNo" placeholder="留空自动生成" />
                </template>
              </el-table-column>
              <el-table-column label="性别" min-width="100">
                <template #default="scope">
                  <el-select v-model="scope.row.gender" placeholder="请选择">
                    <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="尺码" min-width="100">
                <template #default="scope">
                  <el-input v-model="scope.row.sizeCode" placeholder="如 150 / 160" />
                </template>
              </el-table-column>
              <el-table-column label="数量" min-width="100">
                <template #default="scope">
                  <el-input-number v-model="scope.row.quantity" :min="0" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="状态" min-width="110">
                <template #default="scope">
                  <el-select v-model="scope.row.scanStatus" placeholder="请选择状态">
                    <el-option v-for="item in scanStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="箱贴" min-width="180">
                <template #default="scope">
                  <el-input v-model="scope.row.boxLabel" placeholder="留空由后端自动生成" />
                </template>
              </el-table-column>
              <el-table-column label="起始溯源码" min-width="180">
                <template #default="scope">
                  <el-input v-model="scope.row.traceCodeStart" placeholder="可选" />
                </template>
              </el-table-column>
              <el-table-column label="结束溯源码" min-width="180">
                <template #default="scope">
                  <el-input v-model="scope.row.traceCodeEnd" placeholder="可选" />
                </template>
              </el-table-column>
              <el-table-column label="备注" min-width="150">
                <template #default="scope">
                  <el-input v-model="scope.row.remark" placeholder="请输入备注" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="90" fixed="right">
                <template #default="scope">
                  <el-button link type="danger" icon="Delete" @click="removePackageRow(scope.$index)">删除</el-button>
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

<script setup lang="ts" name="UniformShipmentOrder">
import { addShipmentOrder, delShipmentOrder, getShipmentOrder, listShipmentOrder, updateShipmentOrder } from "@/api/uniform/shipment";
import { listWorkOrderOptions } from "@/api/uniform/workorder";
import type { UniformShipmentOrder, UniformShipmentOrderQueryParams, UniformShipmentPackage, UniformWorkOrder } from "@/types";

const { proxy } = getCurrentInstance()!;

const shipmentStatusOptions = [
  { label: "待分拣", value: "0" },
  { label: "分拣中", value: "1" },
  { label: "待发货", value: "2" },
  { label: "已发货", value: "3" },
  { label: "已签收", value: "4" },
];

const scanStatusOptions = [
  { label: "待装箱", value: "0" },
  { label: "已装箱", value: "1" },
  { label: "已出库", value: "2" },
];

const genderOptions = [
  { label: "男装", value: "male" },
  { label: "女装", value: "female" },
  { label: "中性", value: "unisex" },
];

const shipmentList = ref<UniformShipmentOrder[]>([]);
const workOrderOptions = ref<UniformWorkOrder[]>([]);
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
  form: {} as UniformShipmentOrder,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    shipmentNo: undefined,
    workOrderNo: undefined,
    schoolName: undefined,
    className: undefined,
    shipmentStatus: undefined,
  } as UniformShipmentOrderQueryParams,
  rules: {
    workOrderId: [{ required: true, message: "工单不能为空", trigger: "change" }],
    schoolName: [{ required: true, message: "学校不能为空", trigger: "blur" }],
    className: [{ required: true, message: "班级不能为空", trigger: "blur" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listShipmentOrder(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      shipmentList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadWorkOrders() {
  listWorkOrderOptions().then((response) => {
    workOrderOptions.value = (response.data || []).filter((item) => item.traceStatus === "1");
  });
}

function createDefaultPackageRow(): UniformShipmentPackage {
  return {
    packageNo: undefined,
    gender: "unisex",
    sizeCode: undefined,
    quantity: 0,
    scanStatus: "0",
    boxLabel: undefined,
    traceCodeStart: undefined,
    traceCodeEnd: undefined,
    remark: undefined,
  };
}

function reset() {
  form.value = {
    shipmentId: undefined,
    shipmentNo: undefined,
    workOrderId: undefined,
    schoolName: undefined,
    campusName: undefined,
    gradeName: undefined,
    className: undefined,
    logisticsCompany: undefined,
    logisticsNo: undefined,
    dispatchTime: undefined,
    signTime: undefined,
    packageList: [],
    remark: undefined,
  };
  activeTab.value = "basic";
  proxy.resetForm("shipmentRef");
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

function handleSelectionChange(selection: UniformShipmentOrder[]) {
  ids.value = selection.map((item) => item.shipmentId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增班级分拣发运";
}

function handleUpdate(row?: UniformShipmentOrder) {
  reset();
  const shipmentId = row?.shipmentId || ids.value[0];
  getShipmentOrder(shipmentId).then((response) => {
    form.value = {
      ...response.data!,
      packageList: response.data?.packageList || [],
    };
    open.value = true;
    title.value = "修改班级分拣发运";
  });
}

function addPackageRow() {
  form.value.packageList = form.value.packageList || [];
  form.value.packageList.push(createDefaultPackageRow());
}

function removePackageRow(index: number) {
  form.value.packageList?.splice(index, 1);
}

function normalizePackageList(packageList: UniformShipmentPackage[] = []) {
  return packageList.filter((item) => (item.quantity || 0) > 0);
}

function submitForm() {
  proxy.$refs["shipmentRef"].validate((valid: boolean) => {
    if (!valid) {
      activeTab.value = "basic";
      return;
    }
    const payload: UniformShipmentOrder = {
      ...form.value,
      packageList: normalizePackageList(form.value.packageList),
    };
    const request = payload.shipmentId ? updateShipmentOrder(payload) : addShipmentOrder(payload);
    request.then(() => {
      proxy.$modal.msgSuccess(payload.shipmentId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformShipmentOrder) {
  const shipmentIds = row?.shipmentId || ids.value;
  proxy.$modal.confirm(`是否确认删除班级分拣发运编号为 "${shipmentIds}" 的数据项？`).then(() => {
    return delShipmentOrder(shipmentIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/shipment/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `shipment_order_${Date.now()}.xlsx`);
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

function getShipmentStatusType(status?: string) {
  if (status === "4") return "success";
  if (status === "3") return "success";
  if (status === "2") return "warning";
  if (status === "1") return "info";
  return "";
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
.dialog-table :deep(.el-input) {
  width: 100%;
}
</style>
