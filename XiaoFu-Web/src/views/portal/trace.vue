<template>
  <div class="portal-page">
    <div class="portal-shell">
      <section class="hero-section">
        <div class="hero-copy">
          <p class="eyebrow">YUNSHANG PORTAL</p>
          <h1>校服全息溯源与寻物微站</h1>
          <p class="hero-text">
            扫码即可查看面料来源、生产工序、质检结论与洗护建议；若校服已绑定数字寻物贴，拾得者也能快速确认归属信息。
          </p>
          <div class="hero-tags">
            <span>全息溯源档案</span>
            <span>数字洗护小百科</span>
            <span>数字寻物贴</span>
          </div>
        </div>
        <div class="hero-panel">
          <div class="search-card">
            <p class="search-label">扫码 / 手动查询</p>
            <h2>输入溯源码查看数字档案</h2>
            <el-input
              v-model="traceInput"
              placeholder="支持 BT202603300001 或 XFTRACE|BT202603300001|..."
              size="large"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button :loading="loading" @click="handleSearch">查询</el-button>
              </template>
            </el-input>
            <p class="helper-text">演示码：BT202603300001</p>
          </div>
        </div>
      </section>

      <section v-if="loading" class="loading-section">
        <el-skeleton :rows="8" animated />
      </section>

      <template v-else-if="archive">
        <section class="summary-strip">
          <div class="summary-card">
            <span class="summary-label">溯源码</span>
            <strong>{{ archive.traceCode || "-" }}</strong>
          </div>
          <div class="summary-card">
            <span class="summary-label">款式</span>
            <strong>{{ archive.styleName || "-" }}</strong>
          </div>
          <div class="summary-card">
            <span class="summary-label">出厂日期</span>
            <strong>{{ formatDate(archive.factoryDate) }}</strong>
          </div>
          <div class="summary-card">
            <span class="summary-label">归属学校</span>
            <strong>{{ archive.schoolName || "-" }}</strong>
          </div>
        </section>

        <section class="section-head">
          <div>
            <p class="eyebrow">TRACE ARCHIVE</p>
            <h2>全息溯源档案</h2>
          </div>
          <el-tag type="success" effect="dark">{{ formatCodeMode(archive.codeMode) }}</el-tag>
        </section>

        <section class="content-grid">
          <article class="panel">
            <div class="panel-title">
              <h3>成衣身份卡</h3>
              <p>从工单到出厂信息一目了然</p>
            </div>
            <div class="detail-grid">
              <div class="detail-item">
                <span>工单号</span>
                <strong>{{ archive.workOrderNo || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>客户订单号</span>
                <strong>{{ archive.sourceOrderNo || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>季节</span>
                <strong>{{ formatSeason(archive.season) }}</strong>
              </div>
              <div class="detail-item">
                <span>品类</span>
                <strong>{{ formatStyleType(archive.styleType) }}</strong>
              </div>
              <div class="detail-item">
                <span>原料批次</span>
                <strong>{{ archive.batchNo || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>供应商</span>
                <strong>{{ archive.supplierName || "-" }}</strong>
              </div>
            </div>
          </article>

          <article class="panel">
            <div class="panel-title">
              <h3>面料源头</h3>
              <p>可追溯到批次、缸号与成分结构</p>
            </div>
            <div class="detail-grid">
              <div class="detail-item">
                <span>主面料</span>
                <strong>{{ archive.fabricName || archive.materialName || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>面料成分</span>
                <strong>{{ archive.fabricComposition || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>颜色</span>
                <strong>{{ archive.colorName || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>克重</span>
                <strong>{{ formatMetric(archive.gramWeight, "g/m2") }}</strong>
              </div>
              <div class="detail-item">
                <span>缸号</span>
                <strong>{{ archive.vatNo || "-" }}</strong>
              </div>
              <div class="detail-item">
                <span>面料检测日期</span>
                <strong>{{ formatDate(archive.materialInspectDate) }}</strong>
              </div>
            </div>
            <div class="report-links">
              <button class="link-btn" :disabled="!archive.materialReportFileUrl" @click="openReport(archive.materialReportFileUrl)">
                查看面料检测报告
              </button>
              <span class="report-no">报告编号：{{ archive.materialReportNo || "-" }}</span>
            </div>
          </article>
        </section>

        <section class="content-grid lower-grid">
          <article class="panel timeline-panel">
            <div class="panel-title">
              <h3>生产车间足迹</h3>
              <p>关键工序、责任人与设备来源全程留痕</p>
            </div>
            <div v-if="processList.length" class="timeline">
              <div v-for="item in processList" :key="item.processType + item.startTime" class="timeline-item">
                <div class="timeline-node"></div>
                <div class="timeline-content">
                  <div class="timeline-head">
                    <strong>{{ item.processName || "-" }}</strong>
                    <span>{{ item.deviceType || "-" }}</span>
                  </div>
                  <p>责任人：{{ item.operatorName || "-" }} / 质检员：{{ item.inspectorName || "-" }}</p>
                  <p>开始：{{ formatDateTime(item.startTime) }} / 结束：{{ formatDateTime(item.endTime) }}</p>
                  <p>合格 {{ item.passQuantity ?? 0 }} 件，不良 {{ item.defectiveQuantity ?? 0 }} 件</p>
                  <p v-if="item.remark">{{ item.remark }}</p>
                </div>
              </div>
            </div>
            <el-empty v-else description="暂未记录工序轨迹" />
          </article>

          <article class="panel report-panel">
            <div class="panel-title">
              <h3>质检报告缩略图</h3>
              <p>国标合规结果与 CMA/CNAS 红章信息展示</p>
            </div>
            <div class="report-thumb">
              <div class="stamp">CMA / CNAS</div>
              <p class="thumb-code">{{ archive.inspectionNo || "待补充质检编号" }}</p>
              <h4>{{ archive.gbStandard || "GB/T 31888-2015" }}</h4>
              <ul class="qa-metrics">
                <li>甲醛：{{ formatMetric(archive.formaldehyde, "mg/kg") }}</li>
                <li>PH：{{ archive.phValue ?? "-" }}</li>
                <li>色牢度：{{ archive.colorFastness ?? "-" }}</li>
                <li>起球率：{{ archive.pillingGrade ?? "-" }}</li>
              </ul>
              <p class="thumb-result">{{ archive.conclusion || "该批次校服质检信息已归档。" }}</p>
              <button class="report-action" :disabled="!archive.inspectionReportFileUrl" @click="openReport(archive.inspectionReportFileUrl)">
                打开完整质检报告
              </button>
            </div>
          </article>
        </section>

        <section class="section-head care-head">
          <div>
            <p class="eyebrow">CARE GUIDE</p>
            <h2>数字洗护小百科</h2>
          </div>
        </section>

        <section class="care-section panel">
          <div class="care-overview">
            <div>
              <h3>款式专属洗护说明</h3>
              <p>{{ archive.careInstructions || "当前款式暂无专属洗护文本，以下为系统自动生成建议。" }}</p>
            </div>
            <div class="care-badges">
              <span v-for="tip in careTips" :key="tip">{{ tip }}</span>
            </div>
          </div>
        </section>

        <section class="section-head">
          <div>
            <p class="eyebrow">FIND MY UNIFORM</p>
            <h2>数字寻物贴</h2>
          </div>
          <el-tag :type="ownerCard.bound ? 'success' : 'info'">{{ ownerCard.bound ? "已绑定" : "待绑定" }}</el-tag>
        </section>

        <section class="lost-found-grid">
          <article class="panel owner-panel">
            <div class="panel-title">
              <h3>归属信息</h3>
              <p>拾得者扫码后即可看到以下归还线索</p>
            </div>
            <template v-if="ownerCard.bound">
              <div class="owner-card">
                <div class="owner-highlight">
                  <span>所属学生</span>
                  <strong>{{ ownerCard.studentName || "-" }}</strong>
                </div>
                <div class="owner-meta">
                  <p>学校：{{ ownerCard.schoolName || "-" }}</p>
                  <p>班级：{{ ownerCard.className || "-" }}</p>
                  <p>联系人：{{ ownerCard.contactName || "未填写" }}</p>
                  <p>联系电话：{{ ownerCard.maskedContactPhone || "未填写" }}</p>
                  <p>绑定时间：{{ formatDateTime(ownerCard.bindTime) }}</p>
                </div>
                <p class="owner-remark">{{ ownerCard.ownerRemark || "建议交由班主任或学校德育处协助归还。" }}</p>
              </div>
            </template>
            <el-empty v-else description="当前校服尚未绑定寻物贴信息" />
          </article>

          <article class="panel bind-panel">
            <div class="panel-title">
              <h3>家长绑定 / 更新信息</h3>
              <p>填写学生与班级信息后，后续扫码即可辅助快速归还</p>
            </div>
            <el-form ref="bindFormRef" :model="bindForm" :rules="bindRules" label-position="top" class="bind-form">
              <el-form-item label="学校名称" prop="schoolName">
                <el-input v-model="bindForm.schoolName" placeholder="例如：北塔中学" />
              </el-form-item>
              <el-form-item label="班级名称" prop="className">
                <el-input v-model="bindForm.className" placeholder="例如：2026级初一3班" />
              </el-form-item>
              <el-form-item label="学生姓名" prop="studentName">
                <el-input v-model="bindForm.studentName" placeholder="例如：张小福" />
              </el-form-item>
              <el-form-item label="联系人">
                <el-input v-model="bindForm.contactName" placeholder="可选，例如：张妈妈" />
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="bindForm.contactPhone" placeholder="可选，更新时请重新填写完整号码" />
              </el-form-item>
              <el-form-item label="归还提示">
                <el-input v-model="bindForm.ownerRemark" type="textarea" :rows="3" placeholder="例如：可交给班主任或联系家长协助核实" />
              </el-form-item>
              <el-button class="submit-btn" type="primary" :loading="submitting" @click="submitBinding">保存绑定信息</el-button>
            </el-form>
          </article>
        </section>
      </template>

      <section v-else class="empty-state panel">
        <div class="empty-copy">
          <h2>{{ hasSearched ? "未找到对应档案" : "等待查询溯源码" }}</h2>
          <p>{{ emptyDescription }}</p>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from "vue";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import { getPortalTraceArchive, saveLostFoundBinding } from "@/api/uniform/portal";
import type { UniformLostFoundBindRequest, UniformLostFoundCard, UniformPortalProcessNode, UniformPortalTraceArchive } from "@/types";

const route = useRoute();
const router = useRouter();

const loading = ref(false);
const submitting = ref(false);
const hasSearched = ref(false);
const traceInput = ref("");
const archive = ref<UniformPortalTraceArchive>();
const emptyDescription = ref("请输入溯源码，或使用吊牌二维码进入此页面。");
const bindFormRef = ref<FormInstance>();

const bindForm = reactive<UniformLostFoundBindRequest>({
  schoolName: "",
  className: "",
  studentName: "",
  contactName: "",
  contactPhone: "",
  ownerRemark: "",
});

const bindRules: FormRules<UniformLostFoundBindRequest> = {
  schoolName: [{ required: true, message: "请输入学校名称", trigger: "blur" }],
  className: [{ required: true, message: "请输入班级名称", trigger: "blur" }],
  studentName: [{ required: true, message: "请输入学生姓名", trigger: "blur" }],
};

const processList = computed<UniformPortalProcessNode[]>(() => archive.value?.processList || []);
const careTips = computed<string[]>(() => archive.value?.careTips || []);
const ownerCard = computed<UniformLostFoundCard>(() => archive.value?.lostFoundCard || { bound: false });
const currentTraceCode = computed(() => archive.value?.traceCode || normalizeTraceCode(traceInput.value));

watch(
  () => [route.params.traceCode, route.query.code],
  ([paramTraceCode, queryTraceCode]) => {
    const rawValue = String(paramTraceCode || queryTraceCode || "");
    const normalizedCode = normalizeTraceCode(rawValue);
    traceInput.value = normalizedCode;

    if (!normalizedCode) {
      archive.value = undefined;
      hasSearched.value = false;
      emptyDescription.value = "请输入溯源码，或使用吊牌二维码进入此页面。";
      return;
    }

    if (String(paramTraceCode || "") !== normalizedCode || route.query.code) {
      router.replace({ path: `/portal/trace/${encodeURIComponent(normalizedCode)}` });
      return;
    }

    fetchArchive(normalizedCode);
  },
  { immediate: true }
);

function normalizeTraceCode(value?: string) {
  let text = String(value || "").trim();
  try {
    text = decodeURIComponent(text);
  } catch (error) {
    text = text.trim();
  }
  if (!text) {
    return "";
  }
  if (text.startsWith("XFTRACE|")) {
    const segments = text.split("|");
    return (segments[1] || "").trim();
  }
  return text;
}

function handleSearch() {
  const normalizedCode = normalizeTraceCode(traceInput.value);
  if (!normalizedCode) {
    ElMessage.warning("请输入有效的溯源码");
    return;
  }
  router.replace({ path: `/portal/trace/${encodeURIComponent(normalizedCode)}` });
}

function fetchArchive(traceCode: string) {
  hasSearched.value = true;
  loading.value = true;
  getPortalTraceArchive(traceCode)
    .then((response) => {
      archive.value = response.data;
      emptyDescription.value = "请输入溯源码，或使用吊牌二维码进入此页面。";
      fillBindForm();
    })
    .catch((error: any) => {
      archive.value = undefined;
      fillBindForm();
      emptyDescription.value = error?.message || "未找到对应的溯源档案，请核对溯源码后重试。";
    })
    .finally(() => {
      loading.value = false;
    });
}

function fillBindForm() {
  bindForm.schoolName = ownerCard.value.schoolName || archive.value?.schoolName || "";
  bindForm.className = ownerCard.value.className || "";
  bindForm.studentName = ownerCard.value.studentName || "";
  bindForm.contactName = ownerCard.value.contactName || "";
  bindForm.contactPhone = "";
  bindForm.ownerRemark = ownerCard.value.ownerRemark || "拾到后可交给班主任或学校德育处协助归还。";
}

function submitBinding() {
  if (!currentTraceCode.value) {
    ElMessage.warning("请先查询有效的溯源码");
    return;
  }
  bindFormRef.value?.validate((valid) => {
    if (!valid) {
      return;
    }
    submitting.value = true;
    saveLostFoundBinding(currentTraceCode.value, { ...bindForm })
      .then(() => {
        ElMessage.success("寻物贴信息已保存");
        fetchArchive(currentTraceCode.value);
      })
      .finally(() => {
        submitting.value = false;
      });
  });
}

function openReport(url?: string) {
  const reportUrl = resolveAssetUrl(url);
  if (!reportUrl) {
    return;
  }
  window.open(reportUrl, "_blank");
}

function resolveAssetUrl(url?: string) {
  if (!url) {
    return "";
  }
  if (/^https?:\/\//.test(url)) {
    return url;
  }
  return `${import.meta.env.VITE_APP_BASE_API}${url}`;
}

function formatSeason(value?: string) {
  const mapping: Record<string, string> = {
    spring_autumn: "春秋",
    summer: "夏装",
    winter: "冬装",
  };
  return mapping[value || ""] || "-";
}

function formatStyleType(value?: string) {
  const mapping: Record<string, string> = {
    sportswear: "运动装",
    uniform: "制服",
    ceremonial: "礼服",
    outerwear: "外套",
    other: "其他",
  };
  return mapping[value || ""] || "-";
}

function formatCodeMode(value?: string) {
  return value === "2" ? "一批一码" : "一物一码";
}

function formatMetric(value?: number | string, unit?: string) {
  if (value === undefined || value === null || value === "") {
    return "-";
  }
  return unit ? `${value} ${unit}` : String(value);
}

function formatDate(value?: string) {
  if (!value) {
    return "-";
  }
  return String(value).slice(0, 10);
}

function formatDateTime(value?: string) {
  if (!value) {
    return "-";
  }
  return String(value).replace("T", " ").slice(0, 19);
}
</script>

<style scoped lang="scss">
.portal-page {
  min-height: 100vh;
  padding: 24px 0 56px;
  background:
    radial-gradient(circle at top left, rgba(245, 199, 110, 0.3), transparent 26%),
    radial-gradient(circle at top right, rgba(32, 98, 164, 0.18), transparent 28%),
    linear-gradient(180deg, #f4efe6 0%, #f8f5ef 40%, #eef4f7 100%);
  color: #142133;
}

.portal-page,
.portal-page * {
  box-sizing: border-box;
}

.portal-shell {
  width: min(1180px, calc(100% - 32px));
  margin: 0 auto;
}

.hero-section {
  display: grid;
  grid-template-columns: 1.3fr 0.9fr;
  gap: 24px;
  margin-bottom: 24px;
}

.hero-copy,
.hero-panel,
.panel,
.summary-card {
  border: 1px solid rgba(20, 33, 51, 0.08);
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: 0 24px 60px rgba(20, 33, 51, 0.08);
  backdrop-filter: blur(16px);
}

.hero-copy {
  padding: 36px;
}

.hero-panel {
  display: flex;
  align-items: center;
  padding: 24px;
  background: linear-gradient(145deg, rgba(10, 52, 99, 0.92), rgba(16, 100, 111, 0.88));
}

.eyebrow,
.search-label,
.summary-label {
  margin: 0;
  color: #8d5a22;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.22em;
  text-transform: uppercase;
}

.hero-copy h1,
.section-head h2,
.empty-copy h2 {
  margin: 10px 0 14px;
  font-family: "STSong", "Noto Serif SC", serif;
  font-size: clamp(32px, 5vw, 52px);
  line-height: 1.08;
}

.hero-text,
.panel-title p,
.empty-copy p {
  margin: 0;
  color: #526174;
  font-size: 16px;
  line-height: 1.8;
}

.hero-text,
.panel-title p,
.detail-item strong,
.timeline-content p,
.thumb-result,
.care-overview p,
.owner-meta,
.owner-remark,
.report-no,
.helper-text {
  word-break: break-word;
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 10px;
  margin-top: 28px;
}

.hero-tags span,
.care-badges span {
  display: inline-flex;
  align-items: center;
  padding: 10px 14px;
  border-radius: 999px;
  background: rgba(186, 124, 49, 0.12);
  color: #8a5726;
  font-size: 13px;
  font-weight: 600;
}

.search-card {
  width: 100%;
  padding: 28px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.96);
}

.search-card h2 {
  margin: 8px 0 18px;
  color: #142133;
  font-size: 24px;
  line-height: 1.3;
}

.helper-text {
  margin: 12px 0 0;
  color: rgba(20, 33, 51, 0.62);
  font-size: 13px;
}

.loading-section,
.empty-state {
  padding: 28px;
}

.summary-strip,
.content-grid,
.lost-found-grid {
  display: grid;
  gap: 18px;
}

.summary-strip {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  margin-bottom: 26px;
}

.summary-card {
  padding: 20px 22px;
}

.summary-card strong {
  display: block;
  margin-top: 10px;
  font-size: 18px;
  color: #142133;
  word-break: break-word;
  line-height: 1.5;
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 28px 0 16px;
}

.section-head h2 {
  margin: 8px 0 0;
  font-size: clamp(26px, 4vw, 38px);
}

.content-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.lower-grid,
.lost-found-grid {
  margin-top: 18px;
}

.lower-grid,
.lost-found-grid {
  grid-template-columns: 1.1fr 0.9fr;
}

.panel {
  padding: 24px;
}

.panel-title h3 {
  margin: 0;
  font-size: 22px;
  color: #142133;
}

.panel-title p {
  margin-top: 8px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  margin-top: 22px;
}

.detail-item {
  padding: 14px 16px;
  border-radius: 18px;
  background: linear-gradient(180deg, rgba(244, 239, 230, 0.92), rgba(249, 247, 241, 0.75));
}

.detail-item span {
  display: block;
  color: #7a8695;
  font-size: 13px;
}

.detail-item strong {
  display: block;
  margin-top: 8px;
  color: #142133;
  line-height: 1.6;
}

.report-links {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 14px;
  margin-top: 20px;
}

.link-btn,
.report-action {
  border: none;
  border-radius: 999px;
  background: #0c5b67;
  color: #fff;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
}

.link-btn {
  padding: 10px 16px;
}

.report-action {
  width: 100%;
  padding: 12px 16px;
}

.link-btn:disabled,
.report-action:disabled {
  background: #cad6dd;
  cursor: not-allowed;
}

.report-no {
  color: #607182;
  font-size: 14px;
}

.timeline {
  position: relative;
  margin-top: 24px;
  padding-left: 10px;
}

.timeline::before {
  content: "";
  position: absolute;
  left: 14px;
  top: 8px;
  bottom: 8px;
  width: 2px;
  background: linear-gradient(180deg, #0c5b67, rgba(12, 91, 103, 0.12));
}

.timeline-item {
  position: relative;
  display: flex;
  gap: 14px;
  padding-left: 6px;
}

.timeline-item + .timeline-item {
  margin-top: 18px;
}

.timeline-node {
  position: relative;
  z-index: 1;
  width: 18px;
  height: 18px;
  margin-top: 6px;
  border: 4px solid #f4efe6;
  border-radius: 50%;
  background: #0c5b67;
  flex: none;
}

.timeline-content {
  flex: 1;
  padding: 16px 18px;
  border-radius: 18px;
  background: rgba(239, 246, 248, 0.84);
  color: #445569;
  line-height: 1.8;
}

.timeline-content p {
  margin: 4px 0 0;
}

.timeline-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
  color: #142133;
}

.report-thumb {
  position: relative;
  margin-top: 22px;
  padding: 30px 24px 22px;
  border-radius: 24px;
  background: linear-gradient(180deg, #fffdf9 0%, #fbf4e7 100%);
  border: 1px solid rgba(169, 109, 35, 0.14);
}

.stamp {
  position: absolute;
  right: 18px;
  top: 18px;
  padding: 10px 12px;
  border: 3px solid rgba(177, 41, 41, 0.7);
  border-radius: 999px;
  color: rgba(177, 41, 41, 0.82);
  font-size: 12px;
  font-weight: 700;
  transform: rotate(-10deg);
}

.thumb-code {
  margin: 0;
  color: #8d5a22;
  font-size: 13px;
  letter-spacing: 0.08em;
}

.report-thumb h4 {
  margin: 10px 0 16px;
  color: #142133;
  font-size: 24px;
}

.qa-metrics {
  padding-left: 18px;
  margin: 0 0 16px;
  color: #526174;
  line-height: 1.9;
}

.thumb-result {
  min-height: 72px;
  margin: 0 0 18px;
  color: #445569;
  line-height: 1.8;
}

.care-head {
  margin-top: 28px;
}

.care-section {
  margin-top: 0;
}

.care-overview {
  display: grid;
  grid-template-columns: 0.9fr 1.1fr;
  gap: 24px;
  align-items: center;
}

.care-overview h3 {
  margin: 0 0 10px;
  font-size: 22px;
}

.care-overview p {
  margin: 0;
  color: #526174;
  line-height: 1.9;
}

.care-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.owner-card {
  margin-top: 22px;
}

.owner-highlight {
  padding: 20px;
  border-radius: 20px;
  background: linear-gradient(135deg, #0b4d58, #10646f);
  color: #fff;
}

.owner-highlight span {
  display: block;
  font-size: 13px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  opacity: 0.78;
}

.owner-highlight strong {
  display: block;
  margin-top: 10px;
  font-size: 32px;
  font-family: "STSong", "Noto Serif SC", serif;
}

.owner-meta {
  margin-top: 18px;
  padding: 18px;
  border-radius: 18px;
  background: rgba(239, 246, 248, 0.84);
  color: #445569;
  line-height: 1.9;
}

.owner-meta p,
.owner-remark {
  margin: 0;
}

.owner-remark {
  margin-top: 14px;
  color: #8a5726;
  line-height: 1.8;
}

.bind-form {
  margin-top: 18px;
}

.submit-btn {
  width: 100%;
  margin-top: 6px;
}

.empty-copy {
  text-align: center;
  padding: 24px 12px;
}

@media (max-width: 980px) {
  .hero-section,
  .content-grid,
  .lower-grid,
  .lost-found-grid,
  .care-overview,
  .summary-strip {
    grid-template-columns: 1fr;
  }

  .hero-copy h1,
  .section-head h2,
  .empty-copy h2 {
    font-size: 32px;
  }

  .hero-panel {
    padding: 18px;
  }

  .search-card h2 {
    font-size: 22px;
  }

  .timeline-head {
    align-items: flex-start;
    justify-content: flex-start;
  }
}

@media (max-width: 640px) {
  .portal-page {
    padding: 16px 0 40px;
  }

  .portal-shell {
    width: min(100% - 20px, 1180px);
  }

  .hero-copy,
  .panel,
  .summary-card,
  .search-card {
    padding: 20px;
  }

  .hero-copy h1,
  .section-head h2,
  .empty-copy h2 {
    font-size: 28px;
    line-height: 1.2;
  }

  .panel-title h3,
  .care-overview h3 {
    font-size: 20px;
    line-height: 1.4;
  }

  .search-card h2,
  .report-thumb h4 {
    font-size: 20px;
    line-height: 1.4;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

  .section-head {
    align-items: flex-start;
    gap: 12px;
    flex-direction: column;
  }

  .section-head :deep(.el-tag) {
    align-self: flex-start;
  }

  .hero-tags,
  .care-badges,
  .report-links {
    flex-direction: column;
  }

  .hero-tags span,
  .care-badges span {
    width: 100%;
    justify-content: center;
    text-align: center;
    white-space: normal;
    line-height: 1.6;
  }

  .report-links {
    align-items: stretch;
  }

  .link-btn {
    width: 100%;
    text-align: center;
  }

  .timeline {
    padding-left: 0;
  }

  .timeline::before {
    left: 8px;
  }

  .timeline-item {
    gap: 10px;
    padding-left: 0;
  }

  .timeline-head {
    flex-direction: column;
    align-items: flex-start;
    gap: 6px;
  }

  .timeline-content {
    padding: 14px 16px;
  }

  .report-thumb {
    padding: 20px 18px 18px;
  }

  .stamp {
    position: static;
    display: inline-flex;
    margin-bottom: 14px;
    transform: none;
  }

  .qa-metrics {
    padding-left: 16px;
  }

  .thumb-result {
    min-height: auto;
  }

  .owner-highlight strong {
    font-size: 28px;
    line-height: 1.2;
  }

  .search-card :deep(.el-input-group__append) {
    padding: 0;
  }

  .search-card :deep(.el-input-group__append .el-button) {
    min-width: 72px;
    margin: 0;
  }
}

@media (max-width: 420px) {
  .portal-shell {
    width: min(100% - 16px, 1180px);
  }

  .hero-copy,
  .panel,
  .summary-card,
  .search-card,
  .report-thumb,
  .timeline-content,
  .owner-meta {
    padding: 16px;
  }

  .hero-copy h1,
  .section-head h2,
  .empty-copy h2 {
    font-size: 24px;
  }

  .eyebrow,
  .search-label,
  .summary-label {
    letter-spacing: 0.12em;
  }

  .owner-highlight strong {
    font-size: 24px;
  }
}
</style>
