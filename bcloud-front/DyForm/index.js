import DyFormEditor from './DyFormEditor'
const installer = {
  vm: {},
  install (Vue) {
    Vue.component(DyFormEditor)
  }
}

export {
  DyFormEditor
}

export default installer
