<template>
  <div class="screen-page" v-loading="loading">
    <div class="screen-shell">
      <header class="screen-header">
        <div class="header-copy">
          <span class="header-kicker">Uniform Data Screen</span>
          <h1>全链路数据监管舱</h1>
          <p>联动品质监管、物流飞线、履约进度与家长反馈，适配汇报、参观与投标展示场景。</p>
        </div>
        <div class="header-side">
          <div class="season-chip">{{ overview.seasonLabel || "校服监管季" }}</div>
          <div class="header-meta">
            <span>最近刷新</span>
            <strong>{{ overview.refreshedAt || "--" }}</strong>
          </div>
          <el-button type="primary" plain @click="fetchOverview(true)">刷新看板</el-button>
        </div>
      </header>

      <section class="metric-grid">
        <article v-for="card in metricCards" :key="card.code" class="metric-card" :class="`metric-${card.code}`">
          <div class="metric-label">{{ card.label }}</div>
          <div class="metric-value">
            <span>{{ card.value || "0" }}</span>
            <em>{{ card.unit }}</em>
          </div>
          <p>{{ card.description }}</p>
        </article>
      </section>

      <section class="board-grid">
        <div class="board-column">
          <article class="panel">
            <div class="panel-header">
              <div>
                <span class="panel-kicker">Quality Radar</span>
                <h3>国标合格雷达图</h3>
              </div>
              <div class="status-badge" :class="{ pass: inspectionResultText === '质检合格', fail: inspectionResultText === '质检拦截' }">
                {{ inspectionResultText }}
              </div>
            </div>

            <div class="inspection-summary">
              <div>
                <span>最新质检单</span>
                <strong>{{ latestInspection?.inspectionNo || "--" }}</strong>
              </div>
              <div>
                <span>学校 / 款式</span>
                <strong>{{ latestInspection?.schoolName || "--" }} / {{ latestInspection?.styleName || "--" }}</strong>
              </div>
              <div>
                <span>批次 / 标准</span>
                <strong>{{ latestInspection?.batchNo || "--" }} / {{ latestInspection?.gbStandard || "--" }}</strong>
              </div>
            </div>

            <div ref="radarRef" class="chart-block radar-chart" />

            <div class="inspection-grid">
              <div class="inspection-item">
                <span>甲醛</span>
                <strong>{{ formatMetric(latestInspection?.formaldehyde, "mg/kg") }}</strong>
              </div>
              <div class="inspection-item">
                <span>PH 值</span>
                <strong>{{ formatMetric(latestInspection?.phValue) }}</strong>
              </div>
              <div class="inspection-item">
                <span>异味</span>
                <strong>{{ latestInspection?.odorResult === "1" ? "有异味" : latestInspection ? "无异味" : "--" }}</strong>
              </div>
              <div class="inspection-item">
                <span>芳香胺</span>
                <strong>{{ latestInspection?.amineResult === "1" ? "不合格" : latestInspection ? "合格" : "--" }}</strong>
              </div>
              <div class="inspection-item">
                <span>色牢度</span>
                <strong>{{ formatMetric(latestInspection?.colorFastness) }}</strong>
              </div>
              <div class="inspection-item">
                <span>抗起球</span>
                <strong>{{ formatMetric(latestInspection?.pillingGrade) }}</strong>
              </div>
            </div>
          </article>

          <article class="panel">
            <div class="panel-header">
              <div>
                <span class="panel-kicker">Production Funnel</span>
                <h3>生产进度漏斗</h3>
              </div>
              <div class="panel-hint">实时工单阶段分布</div>
            </div>
            <div ref="funnelRef" class="chart-block funnel-chart" />
          </article>
        </div>

        <div class="board-column board-column-center">
          <article class="panel panel-main">
            <div class="panel-header">
              <div>
                <span class="panel-kicker">Twin Logistics</span>
                <h3>校服流转飞线地图</h3>
              </div>
              <div class="panel-hint">{{ logisticsFlows.length }} 条路线 · {{ shipmentTotal }} 件在途/待发</div>
            </div>

            <div class="flow-layout">
              <div ref="flowRef" class="chart-block flow-chart" />
              <div class="route-feed">
                <div v-for="item in topRoutes" :key="item.routeName" class="route-item">
                  <div class="route-head">
                    <span>{{ item.targetName }}</span>
                    <strong>{{ item.shipmentQuantity || 0 }} 件</strong>
                  </div>
                  <div class="route-bar">
                    <span :style="{ width: `${clampPercent(item.progressRate)}%` }" />
                  </div>
                  <p>{{ item.sourceName }} → {{ item.targetName }} · 装箱 {{ item.packedQuantity || 0 }} 件 · 进度 {{ formatPercent(item.progressRate) }}%</p>
                </div>
              </div>
            </div>
          </article>
        </div>

        <div class="board-column">
          <article class="panel">
            <div class="panel-header">
              <div>
                <span class="panel-kicker">Fulfillment</span>
                <h3>交付履约进度条</h3>
              </div>
              <div class="panel-hint">装箱率与签收率双视角</div>
            </div>

            <div class="progress-list">
              <div v-for="item in deliveryProgress" :key="item.schoolName" class="progress-item">
                <div class="progress-head">
                  <strong>{{ item.schoolName }}</strong>
                  <span>{{ item.statusLabel }}</span>
                </div>
                <div class="progress-metrics">
                  <span>总量 {{ item.totalQuantity || 0 }} 件</span>
                  <span>装箱 {{ item.packedQuantity || 0 }} 件</span>
                  <span>签收 {{ item.signedQuantity || 0 }} 件</span>
                </div>
                <div class="progress-row">
                  <label>发运准备</label>
                  <div class="bar-track">
                    <span class="bar-fill bar-fill-primary" :style="{ width: `${clampPercent(item.progressRate)}%` }" />
                  </div>
                  <b>{{ formatPercent(item.progressRate) }}%</b>
                </div>
                <div class="progress-row">
                  <label>签收完成</label>
                  <div class="bar-track">
                    <span class="bar-fill bar-fill-secondary" :style="{ width: `${clampPercent(item.signRate)}%` }" />
                  </div>
                  <b>{{ formatPercent(item.signRate) }}%</b>
                </div>
              </div>
            </div>
          </article>

          <article class="panel">
            <div class="panel-header">
              <div>
                <span class="panel-kicker">Parent Voice</span>
                <h3>售后反馈画像</h3>
              </div>
              <div class="panel-hint">正向评价动态词云</div>
            </div>

            <div class="feedback-cloud">
              <span
                v-for="(item, index) in feedbackCloud"
                :key="item.name"
                class="feedback-tag"
                :style="getFeedbackTagStyle(item, index)"
              >
                {{ item.name }}
              </span>
            </div>
          </article>

          <article class="panel">
            <div class="panel-header">
              <div>
                <span class="panel-kicker">Sizing Trend</span>
                <h3>尺码偏差分析饼图</h3>
              </div>
              <div class="panel-hint">按 BMI 趋势辅助次年备货</div>
            </div>

            <div class="pie-layout">
              <div ref="pieRef" class="chart-block pie-chart" />
              <div class="pie-legend">
                <div v-for="(item, index) in bodyTypeDistribution" :key="item.name" class="legend-item">
                  <span class="legend-dot" :style="{ background: pieColors[index % pieColors.length] }" />
                  <div>
                    <strong>{{ item.name }}</strong>
                    <p>{{ item.value || 0 }} 人 · {{ formatPercent(calculateShare(item.value)) }}%</p>
                  </div>
                </div>
              </div>
            </div>
          </article>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts" name="UniformScreen">
import * as echarts from "echarts";
import { getUniformScreenOverview } from "@/api/uniform/screen";
import type {
  UniformScreenFlowLine,
  UniformScreenIndicator,
  UniformScreenOverview,
  UniformScreenPieItem,
  UniformScreenProgressItem,
  UniformScreenStageStat,
  UniformScreenWordCloudItem,
} from "@/types";

const { proxy } = getCurrentInstance()!;

const loading = ref(false);
const overview = ref<UniformScreenOverview>({});

const radarRef = ref<HTMLElement | null>(null);
const funnelRef = ref<HTMLElement | null>(null);
const flowRef = ref<HTMLElement | null>(null);
const pieRef = ref<HTMLElement | null>(null);

let radarChart: echarts.EChartsType | null = null;
let funnelChart: echarts.EChartsType | null = null;
let flowChart: echarts.EChartsType | null = null;
let pieChart: echarts.EChartsType | null = null;
let autoRefreshTimer: ReturnType<typeof setInterval> | undefined;

const pieColors = ["#67f0ff", "#ffd166", "#7bd389", "#ff8c8c", "#b197fc"];

const metricCards = computed(() => overview.value.metricCards || []);
const latestInspection = computed(() => overview.value.latestInspection);
const qualityRadar = computed(() => overview.value.qualityRadar || []);
const productionFunnel = computed(() => overview.value.productionFunnel || []);
const logisticsFlows = computed(() => overview.value.logisticsFlows || []);
const deliveryProgress = computed(() => overview.value.deliveryProgress || []);
const feedbackCloud = computed(() => overview.value.feedbackCloud || []);
const bodyTypeDistribution = computed(() => overview.value.bodyTypeDistribution || []);

const inspectionResultText = computed(() => {
  if (!latestInspection.value) {
    return "暂无质检";
  }
  return latestInspection.value.result === "1" ? "质检拦截" : "质检合格";
});

const topRoutes = computed(() => logisticsFlows.value.slice(0, 4));

const shipmentTotal = computed(() =>
  logisticsFlows.value.reduce((total, item) => total + Number(item.shipmentQuantity || 0), 0)
);

const bodyTypeTotal = computed(() =>
  bodyTypeDistribution.value.reduce((total, item) => total + Number(item.value || 0), 0)
);

async function fetchOverview(showMessage = false) {
  loading.value = true;
  try {
    const response = await getUniformScreenOverview();
    overview.value = response.data || {};
    await nextTick();
    renderCharts();
    if (showMessage) {
      proxy?.$modal.msgSuccess("监管舱数据已刷新");
    }
  } catch (error) {
    proxy?.$modal.msgError("加载监管舱数据失败");
  } finally {
    loading.value = false;
  }
}

function renderCharts() {
  renderRadarChart();
  renderFunnelChart();
  renderFlowChart();
  renderPieChart();
}

function renderRadarChart() {
  if (!radarRef.value) {
    return;
  }
  radarChart = radarChart || echarts.init(radarRef.value);
  const radarData = qualityRadar.value;
  radarChart.setOption({
    color: ["#55f0ff"],
    tooltip: {
      trigger: "item",
      formatter: (params: any) => {
        const items = radarData.map((item, index) => `${item.name}: ${params.value[index] || 0}`);
        return items.join("<br/>");
      },
    },
    radar: {
      radius: "62%",
      center: ["50%", "54%"],
      indicator: radarData.map((item: UniformScreenIndicator) => ({
        name: item.name,
        max: 100,
      })),
      splitNumber: 4,
      axisName: {
        color: "#d9e7ff",
        fontSize: 12,
      },
      splitArea: {
        areaStyle: {
          color: ["rgba(89, 130, 255, 0.06)", "rgba(89, 130, 255, 0.03)"],
        },
      },
      splitLine: {
        lineStyle: {
          color: "rgba(113, 168, 255, 0.22)",
        },
      },
      axisLine: {
        lineStyle: {
          color: "rgba(113, 168, 255, 0.35)",
        },
      },
    },
    series: [
      {
        type: "radar",
        symbol: "circle",
        symbolSize: 8,
        data: [
          {
            value: radarData.map(item => Number(item.value || 0)),
            areaStyle: {
              color: "rgba(85, 240, 255, 0.28)",
            },
            lineStyle: {
              width: 2,
              color: "#55f0ff",
            },
            itemStyle: {
              color: "#9ef7ff",
            },
          },
        ],
      },
    ],
  });
}

function renderFunnelChart() {
  if (!funnelRef.value) {
    return;
  }
  funnelChart = funnelChart || echarts.init(funnelRef.value);
  const funnelData = [...productionFunnel.value].sort((a, b) => Number(b.value || 0) - Number(a.value || 0));
  funnelChart.setOption({
    tooltip: {
      trigger: "item",
      formatter: "{b}: {c} 单",
    },
    color: ["#6fe7ff", "#5ba3ff", "#7bd389", "#ffd166", "#ff8c8c", "#ff6b9f", "#7b61ff", "#ff9f43"],
    series: [
      {
        type: "funnel",
        top: 24,
        bottom: 16,
        left: "10%",
        width: "80%",
        min: 0,
        max: Math.max(...funnelData.map(item => Number(item.value || 0)), 1),
        minSize: "18%",
        maxSize: "96%",
        sort: "descending",
        gap: 6,
        label: {
          show: true,
          position: "inside",
          color: "#06121f",
          formatter: "{b}\n{c} 单",
        },
        labelLine: {
          show: false,
        },
        itemStyle: {
          borderColor: "rgba(4, 15, 30, 0.9)",
          borderWidth: 2,
        },
        emphasis: {
          label: {
            fontSize: 14,
          },
        },
        data: funnelData.map((item: UniformScreenStageStat) => ({
          name: item.stageLabel,
          value: Number(item.value || 0),
        })),
      },
    ],
  });
}

function renderFlowChart() {
  if (!flowRef.value) {
    return;
  }
  flowChart = flowChart || echarts.init(flowRef.value);
  const routes = logisticsFlows.value;
  const allLons = routes.flatMap(item => [Number(item.sourceLng || 0), Number(item.targetLng || 0)]);
  const allLats = routes.flatMap(item => [Number(item.sourceLat || 0), Number(item.targetLat || 0)]);
  const [minLon, maxLon] = getAxisRange(allLons);
  const [minLat, maxLat] = getAxisRange(allLats);

  const lineData = routes.map((item: UniformScreenFlowLine) => ({
    coords: [
      [Number(item.sourceLng || 0), Number(item.sourceLat || 0)],
      [Number(item.targetLng || 0), Number(item.targetLat || 0)],
    ],
    routeName: item.routeName,
    sourceName: item.sourceName,
    targetName: item.targetName,
    shipmentQuantity: Number(item.shipmentQuantity || 0),
    packedQuantity: Number(item.packedQuantity || 0),
    progressRate: Number(item.progressRate || 0),
  }));

  const sourcePoints = dedupePointData(routes, "source");
  const targetPoints = dedupePointData(routes, "target");
  const maxShipment = Math.max(...routes.map(item => Number(item.shipmentQuantity || 0)), 1);

  flowChart.setOption({
    grid: {
      left: 12,
      right: 12,
      top: 12,
      bottom: 12,
    },
    tooltip: {
      trigger: "item",
      formatter: (params: any) => {
        const data = params.data || {};
        if (params.seriesType === "lines") {
          return `${data.routeName}<br/>发运 ${data.shipmentQuantity} 件<br/>装箱 ${data.packedQuantity} 件<br/>准备进度 ${formatPercent(data.progressRate)}%`;
        }
        return `${data.name}<br/>坐标 ${Number(data.value?.[0] || 0).toFixed(3)}, ${Number(data.value?.[1] || 0).toFixed(3)}`;
      },
    },
    xAxis: {
      type: "value",
      min: minLon,
      max: maxLon,
      show: false,
    },
    yAxis: {
      type: "value",
      min: minLat,
      max: maxLat,
      show: false,
    },
    series: [
      {
        type: "lines",
        coordinateSystem: "cartesian2d",
        zlevel: 2,
        effect: {
          show: true,
          period: 5,
          trailLength: 0.25,
          symbol: "arrow",
          symbolSize: 8,
          color: "#ffd166",
        },
        lineStyle: {
          color: "#68f7ff",
          width: 2,
          opacity: 0.35,
          curveness: 0.24,
        },
        data: lineData,
      },
      {
        type: "scatter",
        coordinateSystem: "cartesian2d",
        zlevel: 3,
        symbolSize: 18,
        itemStyle: {
          color: "#ffe66d",
          shadowBlur: 20,
          shadowColor: "rgba(255, 230, 109, 0.45)",
        },
        label: {
          show: true,
          position: "top",
          color: "#fff3bf",
          fontSize: 12,
          formatter: "{b}",
        },
        data: sourcePoints,
      },
      {
        type: "effectScatter",
        coordinateSystem: "cartesian2d",
        zlevel: 4,
        rippleEffect: {
          brushType: "stroke",
          scale: 3,
        },
        symbolSize: (value: number[]) => 14 + (Number(value[2] || 0) / maxShipment) * 20,
        itemStyle: {
          color: "#55d9ff",
          shadowBlur: 18,
          shadowColor: "rgba(85, 217, 255, 0.5)",
        },
        label: {
          show: true,
          position: "right",
          color: "#d8f3ff",
          fontSize: 12,
          formatter: "{b}",
        },
        data: targetPoints,
      },
    ],
  });
}

function renderPieChart() {
  if (!pieRef.value) {
    return;
  }
  pieChart = pieChart || echarts.init(pieRef.value);
  pieChart.setOption({
    tooltip: {
      trigger: "item",
      formatter: "{b}: {c} 人 ({d}%)",
    },
    color: pieColors,
    series: [
      {
        type: "pie",
        radius: ["42%", "72%"],
        center: ["50%", "50%"],
        avoidLabelOverlap: true,
        label: {
          show: false,
        },
        itemStyle: {
          borderColor: "#06101f",
          borderWidth: 4,
        },
        data: bodyTypeDistribution.value.map((item: UniformScreenPieItem) => ({
          name: item.name,
          value: Number(item.value || 0),
        })),
      },
    ],
  });
}

function dedupePointData(routes: UniformScreenFlowLine[], pointType: "source" | "target") {
  const map = new Map<string, { name: string; value: [number, number, number] }>();
  routes.forEach((item) => {
    const name = pointType === "source" ? item.sourceName || "生产基地" : item.targetName || "学校";
    const lng = Number(pointType === "source" ? item.sourceLng || 0 : item.targetLng || 0);
    const lat = Number(pointType === "source" ? item.sourceLat || 0 : item.targetLat || 0);
    const amount = Number(item.shipmentQuantity || 0);
    if (!map.has(name)) {
      map.set(name, { name, value: [lng, lat, amount] });
    } else if (pointType === "target") {
      const current = map.get(name)!;
      current.value = [lng, lat, current.value[2] + amount];
    }
  });
  return Array.from(map.values());
}

function getAxisRange(values: number[]) {
  if (!values.length) {
    return [0, 1];
  }
  const min = Math.min(...values);
  const max = Math.max(...values);
  const padding = Math.max((max - min) * 0.16, 0.01);
  return [min - padding, max + padding];
}

function handleResize() {
  radarChart?.resize();
  funnelChart?.resize();
  flowChart?.resize();
  pieChart?.resize();
}

function disposeCharts() {
  radarChart?.dispose();
  funnelChart?.dispose();
  flowChart?.dispose();
  pieChart?.dispose();
  radarChart = null;
  funnelChart = null;
  flowChart = null;
  pieChart = null;
}

function startAutoRefresh() {
  autoRefreshTimer = setInterval(() => {
    fetchOverview();
  }, 60000);
}

function formatMetric(value?: number, unit = "") {
  if (value === undefined || value === null) {
    return "--";
  }
  const normalized = Number(value);
  return `${Number.isInteger(normalized) ? normalized : normalized.toFixed(1)}${unit ? ` ${unit}` : ""}`;
}

function formatPercent(value?: number) {
  const normalized = Number(value || 0);
  return normalized.toFixed(1).replace(/\.0$/, "");
}

function clampPercent(value?: number) {
  const normalized = Number(value || 0);
  return Math.min(100, Math.max(0, normalized));
}

function calculateShare(value?: number) {
  if (!bodyTypeTotal.value) {
    return 0;
  }
  return (Number(value || 0) / bodyTypeTotal.value) * 100;
}

function getFeedbackTagStyle(item: UniformScreenWordCloudItem, index: number) {
  const max = Math.max(...feedbackCloud.value.map(word => Number(word.value || 0)), 1);
  const ratio = Number(item.value || 0) / max;
  const palette = [
    ["rgba(103, 240, 255, 0.18)", "#8ef2ff"],
    ["rgba(255, 209, 102, 0.18)", "#ffd166"],
    ["rgba(123, 211, 137, 0.18)", "#9ce5a8"],
    ["rgba(177, 151, 252, 0.18)", "#c5b3ff"],
    ["rgba(255, 140, 140, 0.18)", "#ffb3b3"],
  ];
  const [background, color] = palette[index % palette.length];
  return {
    fontSize: `${14 + ratio * 16}px`,
    lineHeight: `${22 + ratio * 8}px`,
    background,
    color,
    transform: `translateY(${((index % 3) - 1) * 5}px)`,
  };
}

onMounted(() => {
  fetchOverview();
  startAutoRefresh();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  if (autoRefreshTimer) {
    clearInterval(autoRefreshTimer);
  }
  window.removeEventListener("resize", handleResize);
  disposeCharts();
});
</script>

<style scoped lang="scss">
.screen-page {
  --screen-bg: #07111f;
  --screen-card: rgba(8, 22, 38, 0.88);
  --screen-card-strong: rgba(6, 17, 31, 0.95);
  --screen-border: rgba(108, 180, 255, 0.18);
  --screen-text: #e7f2ff;
  --screen-subtext: #8fa9c2;
  --screen-primary: #67f0ff;
  --screen-accent: #ffd166;
  min-height: calc(100vh - 84px);
  padding: 0;
  background:
    radial-gradient(circle at top left, rgba(72, 191, 227, 0.18), transparent 30%),
    radial-gradient(circle at 80% 18%, rgba(255, 209, 102, 0.14), transparent 22%),
    linear-gradient(135deg, #07111f 0%, #0b1a2d 45%, #06111d 100%);
  color: var(--screen-text);
}

.screen-shell {
  padding: 20px;
}

.screen-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 18px;
  padding: 22px 24px;
  border: 1px solid var(--screen-border);
  border-radius: 24px;
  background: linear-gradient(135deg, rgba(13, 34, 58, 0.92), rgba(8, 18, 31, 0.98));
  box-shadow: 0 24px 60px rgba(0, 0, 0, 0.22);
}

.header-copy {
  h1 {
    margin: 6px 0 10px;
    font-family: "Bahnschrift", "DIN Alternate", "Arial Narrow", sans-serif;
    font-size: 34px;
    letter-spacing: 1px;
    color: #f4fbff;
  }

  p {
    margin: 0;
    color: var(--screen-subtext);
    font-size: 14px;
    line-height: 1.8;
  }
}

.header-kicker,
.panel-kicker {
  display: inline-block;
  font-size: 12px;
  letter-spacing: 1.8px;
  text-transform: uppercase;
  color: var(--screen-primary);
}

.header-side {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.season-chip {
  padding: 10px 14px;
  border-radius: 999px;
  background: rgba(103, 240, 255, 0.12);
  color: #aff7ff;
  font-size: 13px;
  border: 1px solid rgba(103, 240, 255, 0.22);
}

.header-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 180px;

  span {
    font-size: 12px;
    color: var(--screen-subtext);
  }

  strong {
    font-family: "Bahnschrift", "DIN Alternate", "Arial Narrow", sans-serif;
    font-size: 16px;
    color: #f7fbff;
  }
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 18px;
}

.metric-card {
  position: relative;
  overflow: hidden;
  padding: 20px 22px;
  border-radius: 22px;
  background: linear-gradient(160deg, rgba(10, 30, 51, 0.96), rgba(6, 18, 32, 0.94));
  border: 1px solid var(--screen-border);

  &::after {
    content: "";
    position: absolute;
    inset: auto -16% -45% auto;
    width: 140px;
    height: 140px;
    border-radius: 50%;
    background: rgba(103, 240, 255, 0.09);
    filter: blur(12px);
  }

  p {
    position: relative;
    margin: 10px 0 0;
    font-size: 13px;
    line-height: 1.7;
    color: var(--screen-subtext);
  }
}

.metric-label {
  position: relative;
  font-size: 13px;
  color: #b6cae3;
}

.metric-value {
  position: relative;
  display: flex;
  align-items: flex-end;
  gap: 8px;
  margin-top: 16px;

  span {
    font-family: "Bahnschrift", "DIN Alternate", "Arial Narrow", sans-serif;
    font-size: 38px;
    line-height: 1;
    letter-spacing: 1px;
    color: #ffffff;
  }

  em {
    font-style: normal;
    font-size: 14px;
    color: #c5d7eb;
    transform: translateY(-5px);
  }
}

.metric-qualityPassRate .metric-value span {
  color: #ffd166;
}

.metric-seasonalDelivery .metric-value span {
  color: #8ce99a;
}

.metric-activeSchools .metric-value span {
  color: #8fa8ff;
}

.board-grid {
  display: grid;
  grid-template-columns: 1.08fr 1.46fr 1fr;
  gap: 16px;
}

.board-column {
  display: flex;
  flex-direction: column;
  gap: 16px;
  min-width: 0;
}

.panel {
  padding: 18px;
  border-radius: 24px;
  border: 1px solid var(--screen-border);
  background:
    linear-gradient(180deg, rgba(11, 28, 48, 0.94), rgba(6, 18, 31, 0.98)),
    var(--screen-card);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.02);
}

.panel-main {
  height: 100%;
  min-height: 720px;
  background:
    radial-gradient(circle at top, rgba(103, 240, 255, 0.09), transparent 32%),
    linear-gradient(180deg, rgba(11, 28, 48, 0.94), rgba(6, 18, 31, 0.98));
}

.panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 14px;

  h3 {
    margin: 4px 0 0;
    font-size: 18px;
    color: #f5fbff;
  }
}

.panel-hint {
  font-size: 12px;
  color: var(--screen-subtext);
}

.status-badge {
  padding: 8px 12px;
  border-radius: 999px;
  font-size: 12px;
  color: #d5e6f6;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.08);

  &.pass {
    color: #b6f6cf;
    background: rgba(90, 204, 138, 0.14);
    border-color: rgba(90, 204, 138, 0.3);
  }

  &.fail {
    color: #ffd1d1;
    background: rgba(255, 107, 107, 0.14);
    border-color: rgba(255, 107, 107, 0.28);
  }
}

.inspection-summary {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 8px;

  div {
    padding: 14px 16px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.04);
    border: 1px solid rgba(255, 255, 255, 0.06);
  }

  span {
    display: block;
    margin-bottom: 8px;
    font-size: 12px;
    color: var(--screen-subtext);
  }

  strong {
    display: block;
    font-size: 14px;
    line-height: 1.6;
    color: #eef7ff;
  }
}

.chart-block {
  width: 100%;
}

.radar-chart {
  height: 300px;
}

.funnel-chart {
  height: 360px;
}

.flow-chart {
  height: 620px;
  border-radius: 20px;
  background:
    linear-gradient(180deg, rgba(18, 39, 64, 0.28), rgba(7, 15, 28, 0.08)),
    radial-gradient(circle at center, rgba(85, 217, 255, 0.06), transparent 55%);
}

.pie-chart {
  height: 250px;
}

.inspection-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin-top: 10px;
}

.inspection-item {
  padding: 12px 14px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.04);

  span {
    display: block;
    margin-bottom: 8px;
    font-size: 12px;
    color: var(--screen-subtext);
  }

  strong {
    color: #f4fbff;
    font-size: 15px;
  }
}

.flow-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 18px;
  align-items: stretch;
}

.route-feed {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.route-item {
  padding: 16px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);

  p {
    margin: 10px 0 0;
    font-size: 12px;
    line-height: 1.6;
    color: var(--screen-subtext);
  }
}

.route-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;

  span {
    font-size: 15px;
    color: #eff8ff;
  }

  strong {
    font-family: "Bahnschrift", "DIN Alternate", "Arial Narrow", sans-serif;
    color: #ffd166;
    font-size: 18px;
  }
}

.route-bar,
.bar-track {
  position: relative;
  overflow: hidden;
  height: 10px;
  margin-top: 12px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.08);
}

.route-bar span,
.bar-fill {
  display: block;
  height: 100%;
  border-radius: inherit;
}

.route-bar span,
.bar-fill-primary {
  background: linear-gradient(90deg, #67f0ff, #5b8cff);
}

.bar-fill-secondary {
  background: linear-gradient(90deg, #ffd166, #ff8c42);
}

.progress-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.progress-item {
  padding: 14px 16px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.progress-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;

  strong {
    font-size: 15px;
    color: #eff8ff;
  }

  span {
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 12px;
    color: #a9f5c9;
    background: rgba(123, 211, 137, 0.12);
  }
}

.progress-metrics {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 16px;
  margin: 10px 0 12px;
  font-size: 12px;
  color: var(--screen-subtext);
}

.progress-row {
  display: grid;
  grid-template-columns: 64px minmax(0, 1fr) 42px;
  align-items: center;
  gap: 10px;
  margin-top: 10px;

  label {
    font-size: 12px;
    color: var(--screen-subtext);
  }

  b {
    font-family: "Bahnschrift", "DIN Alternate", "Arial Narrow", sans-serif;
    font-size: 13px;
    color: #edf7ff;
    text-align: right;
  }
}

.feedback-cloud {
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  gap: 12px;
  min-height: 190px;
  padding: 8px 2px 2px;
}

.feedback-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 14px;
  border-radius: 999px;
  font-family: "Bahnschrift", "DIN Alternate", "Arial Narrow", sans-serif;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: transform 0.2s ease;
}

.feedback-tag:hover {
  transform: translateY(-2px) scale(1.02) !important;
}

.pie-layout {
  display: grid;
  grid-template-columns: 250px minmax(0, 1fr);
  gap: 8px 18px;
  align-items: center;
}

.pie-legend {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 12px;

  strong {
    display: block;
    color: #edf7ff;
    font-size: 14px;
  }

  p {
    margin: 4px 0 0;
    font-size: 12px;
    color: var(--screen-subtext);
  }
}

.legend-dot {
  width: 12px;
  height: 12px;
  flex: none;
  border-radius: 50%;
  box-shadow: 0 0 16px currentColor;
}

@media (max-width: 1680px) {
  .board-grid {
    grid-template-columns: 1fr 1.25fr;
  }

  .board-column:last-child {
    grid-column: 1 / -1;
  }
}

@media (max-width: 1280px) {
  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .board-grid {
    grid-template-columns: 1fr;
  }

  .panel-main {
    min-height: auto;
  }

  .flow-layout {
    grid-template-columns: 1fr;
  }

  .pie-layout {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 900px) {
  .screen-shell {
    padding: 14px;
  }

  .screen-header {
    flex-direction: column;
  }

  .header-side {
    width: 100%;
    justify-content: flex-start;
  }

  .metric-grid {
    grid-template-columns: 1fr;
  }

  .inspection-summary,
  .inspection-grid {
    grid-template-columns: 1fr;
  }

  .radar-chart,
  .funnel-chart,
  .flow-chart {
    height: 320px;
  }
}
</style>
