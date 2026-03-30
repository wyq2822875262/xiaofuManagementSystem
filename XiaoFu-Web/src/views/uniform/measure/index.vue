<template>
  <div class="app-container">
    <el-alert
      title="后台会根据款式尺码矩阵自动推荐尺码；你也可以人工改码，并保留最终确认尺码。"
      type="info"
      :closable="false"
      class="tips-alert"
    />
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学校" prop="schoolName">
        <el-input v-model="queryParams.schoolName" placeholder="请输入学校名称" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="班级" prop="className">
        <el-input v-model="queryParams.className" placeholder="请输入班级名称" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="measureStatus">
        <el-select v-model="queryParams.measureStatus" placeholder="请选择状态" clearable style="width: 220px">
          <el-option v-for="item in measureStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['uniform:measure:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['uniform:measure:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['uniform:measure:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['uniform:measure:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="measureList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学生姓名" align="center" prop="studentName" min-width="100" />
      <el-table-column label="学校/班级" align="center" min-width="220" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.schoolName }} / {{ scope.row.gradeName || "-" }}{{ scope.row.className }}</span>
        </template>
      </el-table-column>
      <el-table-column label="款式" align="center" prop="styleName" min-width="140" show-overflow-tooltip />
      <el-table-column label="性别" align="center" prop="gender" width="80">
        <template #default="scope">
          <span>{{ formatOptionLabel(genderOptions, scope.row.gender) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="身高/体重" align="center" min-width="130">
        <template #default="scope">
          <span>{{ scope.row.heightValue || "-" }} / {{ scope.row.weightValue || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="推荐尺码" align="center" prop="recommendedSizeName" width="110" />
      <el-table-column label="最终尺码" align="center" prop="finalSizeName" width="110" />
      <el-table-column label="状态" align="center" prop="measureStatus" width="100">
        <template #default="scope">
          <el-tag :type="getMeasureStatusType(scope.row.measureStatus)">{{ formatOptionLabel(measureStatusOptions, scope.row.measureStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="量体时间" align="center" prop="measureTime" width="170" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['uniform:measure:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['uniform:measure:remove']">删除</el-button>
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

    <el-dialog :title="title" v-model="open" width="1080px" append-to-body>
      <el-form ref="measureRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="款式" prop="styleId">
              <el-select v-model="form.styleId" filterable placeholder="请选择款式" style="width: 100%" @change="handleStyleChange">
                <el-option v-for="item in styleOptions" :key="item.styleId" :label="item.styleName" :value="item.styleId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="录入来源" prop="sourceChannel">
              <el-radio-group v-model="form.sourceChannel">
                <el-radio v-for="item in sourceChannelOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
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
            <el-form-item label="学生姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入学生姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="studentNo">
              <el-input v-model="form.studentNo" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio v-for="item in genderOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="measureStatus">
              <el-select v-model="form.measureStatus" placeholder="请选择状态" style="width: 100%">
                <el-option v-for="item in measureStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身高(cm)" prop="heightValue">
              <el-input-number v-model="form.heightValue" :min="50" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重(kg)" prop="weightValue">
              <el-input-number v-model="form.weightValue" :min="10" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="量体时间" prop="measureTime">
              <el-date-picker
                v-model="form.measureTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择量体时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" class="recommend-col">
            <el-form-item label="推荐尺码">
              <div class="recommend-box">
                <div class="recommend-text">
                  {{ form.recommendedSizeName || form.recommendedSizeCode || "未推荐" }}
                </div>
                <el-button type="primary" plain @click="handleRecommend">自动推荐</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最终尺码" prop="finalSizeCode">
              <el-select v-model="form.finalSizeCode" filterable clearable placeholder="可人工改码" style="width: 100%" @change="handleFinalSizeChange">
                <el-option
                  v-for="item in currentSizeOptions"
                  :key="item.sizeId"
                  :label="`${item.sizeName || item.sizeCode} (${item.sizeCode})`"
                  :value="item.sizeCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最终尺码名">
              <el-input :model-value="form.finalSizeName || '-'" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="体型备注" prop="bodyRemark">
              <el-input v-model="form.bodyRemark" type="textarea" :rows="2" placeholder="如 偏瘦、肩宽、加肥需求等" />
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

<script setup lang="ts" name="UniformMeasureRecord">
import { addMeasureRecord, delMeasureRecord, getMeasureRecord, listMeasureRecord, recommendMeasureRecord, updateMeasureRecord } from "@/api/uniform/measure";
import { getStyle, listStyleOptions } from "@/api/uniform/style";
import type { UniformMeasureRecord, UniformMeasureRecordQueryParams, UniformStyle, UniformStyleSize } from "@/types";

const { proxy } = getCurrentInstance()!;

const genderOptions = [
  { label: "男", value: "male" },
  { label: "女", value: "female" },
];

const sourceChannelOptions = [
  { label: "后台录入", value: "manual" },
  { label: "家长H5", value: "wechat" },
];

const measureStatusOptions = [
  { label: "待复核", value: "0" },
  { label: "已确认", value: "1" },
  { label: "已下发", value: "2" },
];

const measureList = ref<UniformMeasureRecord[]>([]);
const styleOptions = ref<UniformStyle[]>([]);
const currentSizeOptions = ref<UniformStyleSize[]>([]);
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
  form: {} as UniformMeasureRecord,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    studentName: undefined,
    schoolName: undefined,
    className: undefined,
    measureStatus: undefined,
  } as UniformMeasureRecordQueryParams,
  rules: {
    styleId: [{ required: true, message: "款式不能为空", trigger: "change" }],
    schoolName: [{ required: true, message: "学校不能为空", trigger: "blur" }],
    className: [{ required: true, message: "班级不能为空", trigger: "blur" }],
    studentName: [{ required: true, message: "学生姓名不能为空", trigger: "blur" }],
    gender: [{ required: true, message: "性别不能为空", trigger: "change" }],
    heightValue: [{ required: true, message: "身高不能为空", trigger: "blur" }],
    weightValue: [{ required: true, message: "体重不能为空", trigger: "blur" }],
  },
});

const { form, queryParams, rules } = toRefs(data);

function getList() {
  loading.value = true;
  listMeasureRecord(proxy.addDateRange(queryParams.value, dateRange.value))
    .then((response) => {
      measureList.value = response.rows;
      total.value = response.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

function loadStyleOptions() {
  listStyleOptions().then((response) => {
    styleOptions.value = response.data || [];
  });
}

function loadStyleSizes(styleId?: number) {
  if (!styleId) {
    currentSizeOptions.value = [];
    return Promise.resolve();
  }
  return getStyle(styleId).then((response) => {
    currentSizeOptions.value = response.data?.sizeList || [];
    handleFinalSizeChange();
  });
}

function reset() {
  form.value = {
    measureId: undefined,
    styleId: undefined,
    schoolName: undefined,
    campusName: undefined,
    gradeName: undefined,
    className: undefined,
    studentName: undefined,
    studentNo: undefined,
    gender: "male",
    heightValue: undefined,
    weightValue: undefined,
    sourceChannel: "manual",
    measureTime: undefined,
    recommendedSizeCode: undefined,
    recommendedSizeName: undefined,
    finalSizeCode: undefined,
    finalSizeName: undefined,
    measureStatus: "0",
    bodyRemark: undefined,
    remark: undefined,
  };
  currentSizeOptions.value = [];
  proxy.resetForm("measureRef");
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

function handleSelectionChange(selection: UniformMeasureRecord[]) {
  ids.value = selection.map((item) => item.measureId!);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增量体测码";
}

function handleUpdate(row?: UniformMeasureRecord) {
  reset();
  const measureId = row?.measureId || ids.value[0];
  getMeasureRecord(measureId).then((response) => {
    form.value = response.data!;
    open.value = true;
    title.value = "修改量体测码";
    loadStyleSizes(form.value.styleId);
  });
}

function handleStyleChange(styleId?: number) {
  form.value.recommendedSizeCode = undefined;
  form.value.recommendedSizeName = undefined;
  form.value.finalSizeCode = undefined;
  form.value.finalSizeName = undefined;
  loadStyleSizes(styleId);
}

function handleRecommend() {
  if (!form.value.styleId || !form.value.heightValue || !form.value.weightValue) {
    proxy.$modal.msgWarning("请先选择款式并录入身高、体重");
    return;
  }
  recommendMeasureRecord(form.value).then((response) => {
    form.value = {
      ...form.value,
      ...response.data!,
    };
  });
}

function handleFinalSizeChange() {
  if (!form.value.finalSizeCode) {
    form.value.finalSizeName = undefined;
    return;
  }
  const target = currentSizeOptions.value.find((item) => item.sizeCode === form.value.finalSizeCode);
  form.value.finalSizeName = target?.sizeName || form.value.finalSizeName;
}

function submitForm() {
  proxy.$refs["measureRef"].validate((valid: boolean) => {
    if (!valid) {
      return;
    }
    const request = form.value.measureId ? updateMeasureRecord(form.value) : addMeasureRecord(form.value);
    request.then(() => {
      proxy.$modal.msgSuccess(form.value.measureId ? "修改成功" : "新增成功");
      open.value = false;
      getList();
    });
  });
}

function handleDelete(row?: UniformMeasureRecord) {
  const measureIds = row?.measureId || ids.value;
  proxy.$modal.confirm(`是否确认删除量体测码编号为 "${measureIds}" 的数据项？`).then(() => {
    return delMeasureRecord(measureIds);
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功");
    getList();
  }).catch(() => {});
}

function handleExport() {
  proxy.download("uniform/measure/export", proxy.addDateRange({ ...queryParams.value }, dateRange.value), `measure_record_${Date.now()}.xlsx`);
}

function formatOptionLabel(options: { label: string; value: string }[], value?: string) {
  return options.find((item) => item.value === value)?.label || "-";
}

function getMeasureStatusType(status?: string) {
  if (status === "2") return "success";
  if (status === "1") return "warning";
  return "info";
}

onMounted(() => {
  getList();
  loadStyleOptions();
});
</script>

<style scoped lang="scss">
.tips-alert {
  margin-bottom: 16px;
}

.recommend-box {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.recommend-text {
  min-height: 32px;
  line-height: 32px;
  padding: 0 12px;
  flex: 1;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  background: #fafafa;
  color: #303133;
}
</style>
