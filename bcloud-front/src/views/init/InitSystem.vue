<!-- 系统初始化页面 -->
<template>
  <div class="init-system-page-warpper">
    <div class="container">
      <div class="top">
        <div class="header">
          <a href="/">
            <img src="~@/assets/logo.svg" class="logo" alt="logo" />
            <span class="title">初始化 BCloud</span>
          </a>
        </div>
        <div class="desc">系统首次启动时需要执行持久层与管理员的初始化策略，请按以下步骤完成系统初始化操作</div>
      </div>
      <div class="content">
        <!-- <a-card :bodyStyle="{ padding: '8px' }">
          <div slot="title">
            <a-steps>
              <a-step status="current" title="数据持久层配置">
                <a-icon type="table" slot="icon" />
              </a-step>
              <a-step status="wait" title="文件持久层配置">
                <a-icon type="folder" slot="icon" />
              </a-step>
              <a-step status="wait" title="管理员账户配置">
                <a-icon type="user-add" slot="icon" />
              </a-step>
            </a-steps>
          </div>
          <div class="setting-container">
            <a-menu class="options" mode="vertical">
              <a-menu-item key="1">
                <a-icon type="mail" />
                Mongo DB
              </a-menu-item>
              <a-menu-item key="2">
                <a-icon type="calendar" />
                Mysql DB
              </a-menu-item>
            </a-menu>
          </div>
        </a-card> -->
        <step-form />
      </div>
    </div>
  </div>
</template>

<script>
import StepForm from './step/StepForm'
export default {
  name: 'InitSystem',
  components: {
    StepForm
  },
  props: {},
  data () {
    return {
      tabList: [
        {
          key: 'tab1',
          // tab: 'tab1',
          scopedSlots: { tab: 'customRender' }
        },
        {
          key: 'tab2',
          tab: 'tab2'
        }
      ]
    }
  },
  computed: {},
  watch: {},
  methods: {},
  created () {},
  mounted () {}
}
</script>

<style lang="less" scoped>
.init-system-page-warpper {
  height: 100%;
  .container {
    width: 100%;
    min-height: 100%;
    background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
    background-size: 100%;
    padding: 40px 0 40px;
    position: relative;
    .top {
      text-align: center;
      .header {
        height: 44px;
        line-height: 44px;
        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
          border-style: none;
        }
        .title {
          font-size: 33px;
          color: rgba(0, 0, 0, 0.85);
          font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }
      .desc {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        margin-top: 12px;
        margin-bottom: 20px;
      }
    }
    .content {
      width: 1200px;
      margin: 0 auto;
      .setting-container {
        .options {
          width: 156px;
          min-height: 450px;
        }
      }
    }
  }
}
</style>
