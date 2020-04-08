<template>
  <a-card :bordered="false">
    <a-steps class="steps" :current="currentTab">
      <a-step title="数据持久层配置" />
      <a-step title="文件持久层配置" />
      <a-step title="管理员账户配置" />
      <a-step title="完成系统初始化" />
    </a-steps>
    <div class="content">
      <step1 v-if="currentTab === 0" @nextStep="nextStep"/>
      <step2 v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep"/>
      <step3 v-if="currentTab === 2" @prevStep="prevStep" @finish="finish"/>
    </div>
    <div class="ant-pro-footer-toolbar actions" :style="{ width: isSideMenu() && isDesktop() ? `calc(100% - ${sidebarOpened ? 256 : 80}px)` : '100%'}">
      <a-button @click="nextStep">上一步</a-button>
      <a-button style="margin-left: 8px" type="primary" @click="nextStep">下一步</a-button>
    </div>
  </a-card>
</template>

<script>
import FooterToolBar from '@/components/FooterToolbar'
import Step1 from './step-1/StepContent'
import Step2 from './Step2'
import Step3 from './Step3'
import { mixin, mixinDevice } from '@/utils/mixin'
export default {
  name: 'StepForm',
  components: {
    FooterToolBar,
    Step1,
    Step2,
    Step3
  },
  mixins: [mixin, mixinDevice],
  data () {
    return {
      description: '将一个冗长或用户不熟悉的表单任务分成多个步骤，指导用户完成。',
      currentTab: 0,

      // form
      form: null
    }
  },
  methods: {

    // handler
    nextStep () {
      if (this.currentTab < 2) {
        this.currentTab += 1
      }
    },
    prevStep () {
      if (this.currentTab > 0) {
        this.currentTab -= 1
      }
    },
    finish () {
      this.currentTab = 0
    }
  }
}
</script>

<style lang="less" scoped>
  .steps {
    max-width: 950px;
    margin: 16px auto;
  }
  .actions {
    text-align: center;
  }
</style>
