<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="批次号" prop="batchNo">
        <el-input v-model="queryParams.batchNo" placeholder="请输入内部批次号" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="物料类型" prop="materialType">
        <el-select v-model="queryParams.materialType" placeholder="请选择物料类型" clearable style="width: 220px">
          <el-option v-for="item in materialTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="批次状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择批次状态" clearable style="width: 220px">
          <el-option v-for="item in batchStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:material:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:material:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:material:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:material:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="batchList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="内部批次号" align="center" prop="batchNo" min-width="150" />
      <el-table-column label="供应商" align="center" prop="supplierName" min-width="160" show-overflow-tooltip />
      <el-table-column label="关联款式" align="center" prop="styleName" min-width="160" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.styleName || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物料类型" align="center" prop="materialType" width="100">
        <template #default="scope">
          <span>{{ formatOptionLabel(materialTypeOptions, scope.row.materialType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物料名称" align="center" prop="materialName" min-width="150" show-overflow-tooltip />
      <el-table-column label="缸号" align="center" prop="vatNo" width="120" />
      <el-table-column label="克重" align="center" prop="gramWeight" width="100" />
      <el-table-column label="检测日期" align="center" prop="inspectDate" width="120" />
      <el-table-column label="状态" align="center" prop="status" width="110">
        <template #default="scope">
          <el-tag :type="getBatchStatusType(scope.row.status)">{{ formatOptionLabel(batchStatusOptions, scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:material:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:material:remove']">删除</el-button>
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
      <el-form ref="batchRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="内部批次号" prop="batchNo">
              <el-input v-model="form.batchNo" placeholder="留空则系统自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="form.supplierId" filterable placeholder="请选择供应商" style="width: 100%">
                <el-option v-for="item in supplierOptions" :key="item.supplierId" :label="item.supplierName" :value="item.supplierId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联款式" prop="styleId">
              <el-select v-model="form.styleId" filterable clearable placeholder="请选择关联款式" style="width: 100%">
                <el-option v-for="item in styleOptions" :key="item.styleId" :label="item.styleName" :value="item.styleId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料类型" prop="materialType">
              <el-radio-group v-model="form.materialType">
                <el-radio v-for="item in materialTypeOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="form.materialName" placeholder="如 校服主面料 / 拉链 / 反光条" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缸号" prop="vatNo">
              <el-input v-model="form.vatNo" placeholder="请输入缸号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="克重(g/m2)" prop="gramWeight">
              <el-input-number v-model="form.gramWeight" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="门幅(cm)" prop="widthValue">
              <el-input-number v-model="form.widthValue" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库数量" prop="inboundQuantity">
              <el-input-number v-model="form.inboundQuantity" :min="0" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数量单位" prop="quantityUnit">
              <el-input v-model="form.quantityUnit" placeholder="米 / 公斤 / 个" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="颜色" prop="colorName">
              <el-input v-model="form.colorName" placeholder="请输入颜色" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测日期" prop="inspectDate">
              <el-date-picker v-model="form.inspectDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择检测日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测报告编号" prop="reportNo">
              <el-input v-model="form.reportNo" placeholder="请输入检测报告编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批次状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择批次状态" style="width: 100%">
                <el-option v-for="item in batchStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="面料成分" prop="fabricComposition">
              <el-input v-model="form.fabricComposition" placeholder="如 聚酯纤维100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检测报告PDF" prop="reportFileUrl">
              <file-upload v-model="form.reportFileUrl" :limit="1" :file-type="['pdf']" />
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

<script setup lang="ts" name="UniformMaterialBatch">
import { addMaterialBatch, delMaterialBatch, getMaterialBatch, listMaterialBatch, updateMaterialBatch } from "@/api/uniform/material";
import { listSupplierOptions } from "@/api/uniform/supplier";
import { listStyleOptions } from "@/api/uniform/style";
import type { UniformMaterialBatch, UniformMaterialBatchQueryParams, UniformSupplier, UniformStyle } from "@/types";

const { proxy } = getCurrentInstance()!;

const materialTypeOptions = [
  { label: "主面料", value: "1" },
  { label: "辅料", value: "2" },
];

const batchStatusOptions = [
  { label: "待投产", value: "0" },
  { label: "生产中", value: "1" },
  { label: "已用完", value: "2" },
  { label: "锁定", value: "3" },
];

const batchList = ref<UniformMaterialBatch[]>([]);
const supplierOptions = ref<UniformSupplier[]>([]);
const styleOptions = ref<UniformStyle[]>([]);
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
  form: {} as UniformMaterialBatch,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    batchNo: undefined,
    materialName: undefined,
    materialType: undefined,
    status: undefined,
  } as UniformMaterialBatchQueryParams,
  rules: {
    supplierId: [{ required: true, message: "供应商不能为空", trigger: "change" }],
    materialType: [{ required: true, message: "物料类型不能为空", trigger: "change" }],
    materialName: [{ required: true, message: "物料名称不能为空", trigger: "blur" }],
    status: [{ required: true, message: "批次状态不能为空", trigger: "change" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listMaterialBatch(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      batchList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadOptions() {
  listSupplierOptions().then((response) => {
    supplierOptions.value = response.data || [];
  });
  listStyleOptions().then((response) => {
    styleOptions.value = response.data || [];
  });
}

function reset() {
  form.value = {
    batchId: undefined,
    batchNo: undefined,
    supplierId: undefined,
    styleId: undefined,
    materialType: "1",
    materialName: undefined,
    vatNo: undefined,
    gramWeight: undefined,
    fabricComposition: undefined,
    colorName: undefined,
    widthValue: undefined,
    inboundQuantity: undefined,
    quantityUnit: "米",
    reportNo: undefined,
    reportFileUrl: undefined,
    inspectDate: undefined,
    status: "0",
    remark: undefined,
  };
  proxy.resetForm("batchRef");
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

function handleSelectionChange(selection: UniformMaterialBatch[]) {
  ids.value = selection.map((item) => item.batchId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增原料批次";
}

function handleUpdate(row?: UniformMaterialBatch) {
  reset();
  const batchId = row?.batchId || ids.value[0];
  getMaterialBatch(batchId).then((response) => {
    form.value = response.data!;
    open.value = true;
    title.value = "修改原料批次";
  });
}

function submitForm() {
  proxy.$refs["batchRef"].validate((valid: boolean) => {
    if (!valid) {
      return;
    }
    const request = form.value.batchId ? updateMaterialBatch(form.value) : addMaterialBatch(form.value);
    request.then(() => {
      proxy.$modal.msgSuccess(form.value.batchId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformMaterialBatch) {
  const batchIds = row?.batchId || ids.value;
  proxy.$modal.confirm(`是否确认删除原料批次编号为 "${batchIds}" 的数据项？`).then(() => {
    return delMaterialBatch(batchIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/material/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `material_batch_${Date.now()}.xlsx`);
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

function getBatchStatusType(status?: string) {
  if (status === "3") return "danger";
  if (status === "2") return "info";
  if (status === "1") return "warning";
  return "success";
}

onMounted(() => {
  getList();
  loadOptions();
});
</script>
