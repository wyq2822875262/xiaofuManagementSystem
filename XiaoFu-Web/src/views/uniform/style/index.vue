<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="款式编码" prop="styleCode">
        <el-input
          v-model="queryParams.styleCode"
          placeholder="请输入款式编码"
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="款式名称" prop="styleName">
        <el-input
          v-model="queryParams.styleName"
          placeholder="请输入款式名称"
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="季节" prop="season">
        <el-select v-model="queryParams.season" placeholder="请选择季节" clearable style="width: 220px">
          <el-option v-for="item in seasonOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="styleType">
        <el-select v-model="queryParams.styleType" placeholder="请选择类型" clearable style="width: 220px">
          <el-option v-for="item in styleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="请选择性别" clearable style="width: 220px">
          <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:style:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:style:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:style:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:style:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="styleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="款式编码" align="center" prop="styleCode" min-width="120" />
      <el-table-column label="款式名称" align="center" prop="styleName" min-width="180" show-overflow-tooltip />
      <el-table-column label="季节" align="center" prop="season" width="100">
        <template #default="scope">
          <span>{{ formatOptionLabel(seasonOptions, scope.row.season) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="styleType" width="120">
        <template #default="scope">
          <span>{{ formatOptionLabel(styleTypeOptions, scope.row.styleType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="适用性别" align="center" prop="gender" width="100">
        <template #default="scope">
          <span>{{ formatOptionLabel(genderOptions, scope.row.gender) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="主面料" align="center" prop="fabricName" min-width="150" show-overflow-tooltip />
      <el-table-column label="尺码矩阵" align="center" prop="sizeSummary" min-width="220" show-overflow-tooltip />
      <el-table-column label="BOM数" align="center" prop="bomCount" width="90" />
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
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:style:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:style:remove']">删除</el-button>
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
      <el-form ref="styleRef" :model="form" :rules="rules" label-width="110px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基础信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="款式编码" prop="styleCode">
                  <el-input v-model="form.styleCode" placeholder="如 SUMMER-SPORT-01" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="款式名称" prop="styleName">
                  <el-input v-model="form.styleName" placeholder="请输入款式名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="季节" prop="season">
                  <el-select v-model="form.season" placeholder="请选择季节" style="width: 100%">
                    <el-option v-for="item in seasonOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="类型" prop="styleType">
                  <el-select v-model="form.styleType" placeholder="请选择类型" style="width: 100%">
                    <el-option v-for="item in styleTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="适用性别" prop="gender">
                  <el-select v-model="form.gender" placeholder="请选择适用性别" style="width: 100%">
                    <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
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
                <el-form-item label="主面料" prop="fabricName">
                  <el-input v-model="form.fabricName" placeholder="请输入主面料名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="面料成分" prop="fabricComposition">
                  <el-input v-model="form.fabricComposition" placeholder="如 聚酯纤维100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="支持特体" prop="specialBodySupport">
                  <el-radio-group v-model="form.specialBodySupport">
                    <el-radio v-for="item in yesNoOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="form.status">
                    <el-radio v-for="item in statusOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="洗护说明" prop="careInstructions">
                  <el-input v-model="form.careInstructions" type="textarea" :rows="3" placeholder="请输入洗护说明" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="尺码矩阵" name="size">
            <div class="table-toolbar">
              <el-button type="primary" plain icon="Plus" @click="addSizeRow">新增尺码</el-button>
              <span class="toolbar-tip">支持标准尺码与特体标记，供后续量体推荐算法直接复用。</span>
            </div>
            <el-table :data="form.sizeList" border class="dialog-table">
              <el-table-column label="尺码名称" min-width="120">
                <template #default="scope">
                  <el-input v-model="scope.row.sizeName" placeholder="如 150 / 150加肥" />
                </template>
              </el-table-column>
              <el-table-column label="尺码编码" min-width="100">
                <template #default="scope">
                  <el-input v-model="scope.row.sizeCode" placeholder="如 150" />
                </template>
              </el-table-column>
              <el-table-column label="特体类型" min-width="110">
                <template #default="scope">
                  <el-select v-model="scope.row.specialType" placeholder="请选择">
                    <el-option v-for="item in specialTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="身高下限(cm)" min-width="120">
                <template #default="scope">
                  <el-input-number v-model="scope.row.heightMin" :min="0" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="身高上限(cm)" min-width="120">
                <template #default="scope">
                  <el-input-number v-model="scope.row.heightMax" :min="0" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="体重下限(kg)" min-width="120">
                <template #default="scope">
                  <el-input-number v-model="scope.row.weightMin" :min="0" :precision="1" :step="0.5" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="体重上限(kg)" min-width="120">
                <template #default="scope">
                  <el-input-number v-model="scope.row.weightMax" :min="0" :precision="1" :step="0.5" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="排序" min-width="90">
                <template #default="scope">
                  <el-input-number v-model="scope.row.sortOrder" :min="1" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="90" fixed="right">
                <template #default="scope">
                  <el-button link type="danger" icon="Delete" @click="removeSizeRow(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="BOM物料" name="bom">
            <div class="table-toolbar">
              <el-button type="primary" plain icon="Plus" @click="addBomRow">新增物料</el-button>
              <span class="toolbar-tip">BOM 会直接关联供应商，为后续批次追溯、成本核算和赋码打底。</span>
            </div>
            <el-table :data="form.bomList" border class="dialog-table">
              <el-table-column label="物料类型" min-width="110">
                <template #default="scope">
                  <el-select v-model="scope.row.materialType" placeholder="请选择">
                    <el-option v-for="item in materialTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="物料名称" min-width="140">
                <template #default="scope">
                  <el-input v-model="scope.row.materialName" placeholder="如 涤盖棉面料 / 反光条" />
                </template>
              </el-table-column>
              <el-table-column label="规格说明" min-width="150">
                <template #default="scope">
                  <el-input v-model="scope.row.materialSpec" placeholder="如 320g/平方米" />
                </template>
              </el-table-column>
              <el-table-column label="成分说明" min-width="150">
                <template #default="scope">
                  <el-input v-model="scope.row.materialComposition" placeholder="如 聚酯纤维100%" />
                </template>
              </el-table-column>
              <el-table-column label="用量" min-width="100">
                <template #default="scope">
                  <el-input-number v-model="scope.row.dosage" :min="0" :precision="2" :step="0.1" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="单位" min-width="90">
                <template #default="scope">
                  <el-input v-model="scope.row.dosageUnit" placeholder="米/个/条" />
                </template>
              </el-table-column>
              <el-table-column label="关联供应商" min-width="180">
                <template #default="scope">
                  <el-select v-model="scope.row.supplierId" filterable clearable placeholder="请选择供应商">
                    <el-option v-for="item in supplierOptions" :key="item.supplierId" :label="item.supplierName" :value="item.supplierId" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="强制追溯" min-width="110">
                <template #default="scope">
                  <el-select v-model="scope.row.traceRequired" placeholder="请选择">
                    <el-option v-for="item in yesNoOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="排序" min-width="90">
                <template #default="scope">
                  <el-input-number v-model="scope.row.sortOrder" :min="1" :precision="0" controls-position="right" />
                </template>
              </el-table-column>
              <el-table-column label="备注" min-width="140">
                <template #default="scope">
                  <el-input v-model="scope.row.remark" placeholder="可填写位置或用途" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="90" fixed="right">
                <template #default="scope">
                  <el-button link type="danger" icon="Delete" @click="removeBomRow(scope.$index)">删除</el-button>
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

<script setup lang="ts" name="UniformStyle">
import { addStyle, delStyle, getStyle, listStyle, updateStyle } from "@/api/uniform/style";
import { listSupplierOptions } from "@/api/uniform/supplier";
import type { UniformStyle, UniformStyleBom, UniformStyleQueryParams, UniformStyleSize, UniformSupplier } from "@/types";

const { proxy } = getCurrentInstance()!;

const seasonOptions = [
  { label: "春秋", value: "spring_autumn" },
  { label: "夏装", value: "summer" },
  { label: "冬装", value: "winter" },
];

const styleTypeOptions = [
  { label: "运动装", value: "sportswear" },
  { label: "制服", value: "uniform" },
  { label: "礼服", value: "ceremonial" },
  { label: "外套", value: "outerwear" },
  { label: "其他", value: "other" },
];

const genderOptions = [
  { label: "中性", value: "unisex" },
  { label: "男", value: "male" },
  { label: "女", value: "female" },
];

const statusOptions = [
  { label: "正常", value: "0" },
  { label: "停用", value: "1" },
];

const codeModeOptions = [
  { label: "一物一码", value: "1" },
  { label: "一批一码", value: "2" },
];

const yesNoOptions = [
  { label: "否", value: "0" },
  { label: "是", value: "1" },
];

const specialTypeOptions = [
  { label: "标准", value: "0" },
  { label: "加肥", value: "1" },
  { label: "加长", value: "2" },
  { label: "加肥加长", value: "3" },
];

const materialTypeOptions = [
  { label: "主面料", value: "1" },
  { label: "辅料", value: "2" },
  { label: "包装", value: "3" },
];

const styleList = ref<UniformStyle[]>([]);
const supplierOptions = ref<UniformSupplier[]>([]);
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
  form: {} as UniformStyle,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    styleCode: undefined,
    styleName: undefined,
    season: undefined,
    styleType: undefined,
    gender: undefined,
    status: undefined,
  } as UniformStyleQueryParams,
  rules: {
    styleCode: [{ required: true, message: "款式编码不能为空", trigger: "blur" }],
    styleName: [{ required: true, message: "款式名称不能为空", trigger: "blur" }],
    season: [{ required: true, message: "季节不能为空", trigger: "change" }],
    styleType: [{ required: true, message: "类型不能为空", trigger: "change" }],
    gender: [{ required: true, message: "适用性别不能为空", trigger: "change" }],
    codeMode: [{ required: true, message: "赋码模式不能为空", trigger: "change" }],
    specialBodySupport: [{ required: true, message: "是否支持特体不能为空", trigger: "change" }],
    status: [{ required: true, message: "状态不能为空", trigger: "change" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listStyle(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      styleList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadSupplierOptions() {
  listSupplierOptions().then((response) => {
    supplierOptions.value = response.data || [];
  });
}

function createDefaultSizeRow(sortOrder = 1): UniformStyleSize {
  return {
    sizeCode: undefined,
    sizeName: undefined,
    specialType: "0",
    heightMin: undefined,
    heightMax: undefined,
    weightMin: undefined,
    weightMax: undefined,
    sortOrder,
  };
}

function createDefaultBomRow(sortOrder = 1): UniformStyleBom {
  return {
    materialType: "1",
    materialName: undefined,
    materialSpec: undefined,
    materialComposition: undefined,
    dosage: undefined,
    dosageUnit: "米",
    supplierId: undefined,
    traceRequired: "1",
    sortOrder,
    remark: undefined,
  };
}

function cancel() {
  open.value = false;
  reset();
}

function reset() {
  form.value = {
    styleId: undefined,
    styleCode: undefined,
    styleName: undefined,
    season: "spring_autumn",
    styleType: "sportswear",
    gender: "unisex",
    fabricName: undefined,
    fabricComposition: undefined,
    codeMode: "1",
    specialBodySupport: "0",
    careInstructions: undefined,
    status: "0",
    remark: undefined,
    sizeList: [createDefaultSizeRow()],
    bomList: [createDefaultBomRow()],
  };
  activeTab.value = "basic";
  proxy.resetForm("styleRef");
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

function handleSelectionChange(selection: UniformStyle[]) {
  ids.value = selection.map((item) => item.styleId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增校服款式";
}

function handleUpdate(row?: UniformStyle) {
  reset();
  const styleId = row?.styleId || ids.value[0];
  getStyle(styleId).then((response) => {
    form.value = {
      ...response.data!,
      sizeList: response.data?.sizeList?.length ? response.data.sizeList : [createDefaultSizeRow()],
      bomList: response.data?.bomList?.length ? response.data.bomList : [createDefaultBomRow()],
    };
    open.value = true;
    title.value = "修改校服款式";
  });
}

function addSizeRow() {
  form.value.sizeList = form.value.sizeList || [];
  form.value.sizeList.push(createDefaultSizeRow(form.value.sizeList.length + 1));
}

function removeSizeRow(index: number) {
  form.value.sizeList?.splice(index, 1);
}

function addBomRow() {
  form.value.bomList = form.value.bomList || [];
  form.value.bomList.push(createDefaultBomRow(form.value.bomList.length + 1));
}

function removeBomRow(index: number) {
  form.value.bomList?.splice(index, 1);
}

function normalizeSizeList(sizeList: UniformStyleSize[] = []) {
  return sizeList
    .filter((item) => item.sizeCode || item.sizeName)
    .map((item, index) => ({
      ...item,
      specialType: item.specialType || "0",
      sortOrder: item.sortOrder || index + 1,
    }));
}

function normalizeBomList(bomList: UniformStyleBom[] = []) {
  return bomList
    .filter((item) => item.materialName)
    .map((item, index) => ({
      ...item,
      materialType: item.materialType || "1",
      dosageUnit: item.dosageUnit || "米",
      traceRequired: item.traceRequired || "1",
      sortOrder: item.sortOrder || index + 1,
    }));
}

function submitForm() {
  proxy.$refs["styleRef"].validate((valid: boolean) => {
    if (!valid) {
      activeTab.value = "basic";
      return;
    }

    const sizeList = normalizeSizeList(form.value.sizeList);
    if (!sizeList.length) {
      activeTab.value = "size";
      proxy.$modal.msgError("请至少维护一条尺码矩阵");
      return;
    }
    if (sizeList.some((item) => !item.sizeCode || !item.sizeName)) {
      activeTab.value = "size";
      proxy.$modal.msgError("尺码矩阵中的尺码名称和尺码编码不能为空");
      return;
    }

    const bomList = normalizeBomList(form.value.bomList);
    if (!bomList.length) {
      activeTab.value = "bom";
      proxy.$modal.msgError("请至少维护一条 BOM 物料");
      return;
    }
    if (bomList.some((item) => !item.materialName)) {
      activeTab.value = "bom";
      proxy.$modal.msgError("BOM 物料名称不能为空");
      return;
    }

    const payload: UniformStyle = {
      ...form.value,
      sizeList,
      bomList,
    };
    const request = payload.styleId ? updateStyle(payload) : addStyle(payload);
    request.then(() => {
      proxy.$modal.msgSuccess(payload.styleId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformStyle) {
  const styleIds = row?.styleId || ids.value;
  proxy.$modal.confirm(`是否确认删除款式编号为 "${styleIds}" 的数据项？`).then(() => {
    return delStyle(styleIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download(
    "uniform/style/export",
    proxy.addDateRange({ ...queryParams.value }, dateRange.value),
    `uniform_style_${new Date().getTime()}.xlsx`,
  );
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

onMounted(() => {
  getList();
  loadSupplierOptions();
});
</script>

<style scoped lang="scss">
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
