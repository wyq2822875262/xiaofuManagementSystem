<template>
  <div class="app-container portal-entry-page">
    <el-row :gutter="20">
      <el-col :xs="24" :lg="15">
        <el-card shadow="hover" class="entry-card">
          <template #header>
            <div class="card-header">
              <div>
                <h3>C 端微站打开入口</h3>
                <p>将校服溯源码拼接成可分享链接，直接打开模块 4 微站。</p>
              </div>
            </div>
          </template>

          <el-form label-width="110px">
            <el-form-item label="溯源码">
              <el-input
                v-model="traceCode"
                placeholder="请输入溯源码，例如 BT202603300001"
                clearable
                @keyup.enter="handleOpenPortal"
              />
            </el-form-item>
            <el-form-item label="打开链接">
              <el-input :model-value="portalUrl" readonly />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleOpenPortal">打开微站</el-button>
              <el-button @click="handleCopyLink">复制链接</el-button>
              <el-button type="success" plain @click="fillDemoTrace">填入演示码</el-button>
            </el-form-item>
          </el-form>

          <div class="tips-block">
            <p>说明：</p>
            <p>1. 该链接可直接发给家长或在后台用于验收模块 4 页面。</p>
            <p>2. 微站支持匿名访问，不需要登录后台。</p>
            <p>3. 当前演示溯源码默认使用 `BT202603300001`。</p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="9">
        <el-card shadow="hover" class="entry-card preview-card">
          <template #header>
            <div class="card-header">
              <div>
                <h3>快速入口</h3>
                <p>常用操作一键完成。</p>
              </div>
            </div>
          </template>

          <div class="quick-actions">
            <el-button type="primary" link @click="handleOpenPortal">打开当前微站页</el-button>
            <el-button type="primary" link @click="handleOpenDemoPortal">打开演示微站页</el-button>
            <el-button type="primary" link @click="handleOpenDemoTraceList">前往溯源码管理页</el-button>
          </div>

          <el-alert
            title="如果你刚执行完模块 4 SQL，请刷新菜单缓存后再查看左侧菜单。"
            type="info"
            :closable="false"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts" name="UniformPortalEntry">
const { proxy } = getCurrentInstance()!;
const route = useRoute();

const DEMO_TRACE_CODE = "BT202603300001";
const traceCode = ref(String(route.query.traceCode || DEMO_TRACE_CODE));

const portalUrl = computed(() => {
  const normalizedCode = traceCode.value.trim() || DEMO_TRACE_CODE;
  return `${window.location.origin}/portal/trace/${encodeURIComponent(normalizedCode)}`;
});

function fillDemoTrace() {
  traceCode.value = DEMO_TRACE_CODE;
}

function handleOpenPortal() {
  const normalizedCode = traceCode.value.trim();
  if (!normalizedCode) {
    proxy?.$modal.msgWarning("请先输入溯源码");
    return;
  }
  window.open(`/portal/trace/${encodeURIComponent(normalizedCode)}`, "_blank");
}

function handleOpenDemoPortal() {
  traceCode.value = DEMO_TRACE_CODE;
  handleOpenPortal();
}

async function handleCopyLink() {
  try {
    await navigator.clipboard.writeText(portalUrl.value);
    proxy?.$modal.msgSuccess("链接已复制");
  } catch (error) {
    proxy?.$modal.msgError("复制失败，请手动复制链接");
  }
}

function handleOpenDemoTraceList() {
  proxy?.$tab.openPage("溯源码管理", "/uniform/trace");
}
</script>

<style scoped lang="scss">
.portal-entry-page {
  .entry-card {
    margin-bottom: 20px;
  }

  .card-header h3 {
    margin: 0 0 6px;
    font-size: 18px;
    color: #303133;
  }

  .card-header p {
    margin: 0;
    color: #909399;
    font-size: 13px;
  }

  .tips-block {
    margin-top: 8px;
    padding: 14px 16px;
    border-radius: 8px;
    background: #f7f8fa;
    color: #606266;
    line-height: 1.8;
  }

  .tips-block p {
    margin: 0;
  }

  .quick-actions {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    margin-bottom: 16px;
  }
}
</style>
