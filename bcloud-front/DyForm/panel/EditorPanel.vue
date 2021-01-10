<!-- 表单编辑器组件 -->
<template>
  <div class="widget-form-container">
    <vue-scroll :ops="{ bar: { background: '#c1c1c1', size: '6px' } }">
      <a-form-model v-bind="formSetting.options" :model="formValue" class="form-model">
        <nested-element
          :style="editorPanelStyle"
          :list="formSetting.children"
          v-on="$listeners"
        ></nested-element>
      </a-form-model>
    </vue-scroll>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import VueScroll from 'vuescroll'
import NestedElement from './element/NestedElement'
import PanelInjectMixin from '../common/panel-inject-mixin'
export default {
  name: 'EditorPanel',
  mixins: [PanelInjectMixin],
  components: {
    Draggable,
    VueScroll,
    NestedElement
  },
  mounted () {
    document.body.ondrop = function (event) {
      const isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
      if (isFirefox) {
        event.preventDefault()
        event.stopPropagation()
      }
    }
  },
  data () {
    return {
      editorPanelStyle: {
        minHeight: '620px'
      }
    }
  }
}
</script>

<style lang="less" scoped>
.widget-form-container {
  height: 100%;
  width: 100%;
  .form-model {
    height: 100%;
  }
}
</style>
