<!-- SettingContent component description  -->
<template>
  <div class="setting-content">
    <a-tabs
      class="tabs"
      v-model="currentTab"
      tabPosition="bottom"
      :animated="false"
      :tabBarStyle="{ 'border-bottom': '1px solid #e8e8e8', 'border-top': 'none' }"
    >
      <a-button slot="tabBarExtraContent" icon="fire">自定义</a-button>
      <a-tab-pane v-for="tab in groupTabs" :key="tab.key">
        <span slot="tab">
          <a-icon :type="tab.icon" />
          {{ tab.title }}
        </span>
        <div class="tab-content">
          <slot></slot>
        </div>
      </a-tab-pane>
    </a-tabs>
    <div class="setting-desc">
      <h3>说明</h3>
      <div v-for="(desc, i) in descs" :key="i">
        <h4>{{ desc.title }}</h4>
        <p>{{ desc.content }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SettingContent',
  components: {},
  props: {
    value: {
      type: String,
      default: ''
    },
    groupTabs: {
      type: Array,
      default: () => {
        return []
      }
    },
    descs: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data () {
    return {
      currentTab: ''
    }
  },
  watch: {
    value: {
      handler (val) {
        this.currentTab = val
      },
      immediate: true
    },
    currentTab (val) {
      this.$emit('input', val)
    }
  },
  methods: {
    handleTabChange (key) {
      this.$emit('on-tab-change', key)
    }
  },
  mounted () {
    const elements = document.getElementsByClassName('ant-tabs-ink-bar')
    elements.forEach(element => {
      element.className += ' setting-content-tabs-custom-ink-bar'
    })
  }
}
</script>

<style lang="less" scope>
.setting-content {
  .tabs {
    .tab-content {
      min-height: 320px;
      padding-top: 40px;
      padding-bottom: 20px;
    }
    .setting-content-tabs-custom-ink-bar {
      top: auto !important;
      bottom: 1px !important;
    }
  }
  .setting-desc {
    padding: 20px 36px;
    color: rgba(0, 0, 0, 0.45);

    h3 {
      margin: 0 0 12px;
      color: rgba(0, 0, 0, 0.45);
      font-size: 16px;
      line-height: 32px;
    }

    h4 {
      margin: 0 0 4px;
      color: rgba(0, 0, 0, 0.45);
      font-size: 14px;
      line-height: 22px;
    }

    p {
      margin-top: 0;
      margin-bottom: 12px;
      line-height: 22px;
    }
  }
}
</style>
