<template>
  <setting-content v-model="currentType" :groupTabs="groupTabs" :descs="descs">
    <mongo-form v-show="currentType === 'mongo'"></mongo-form>
  </setting-content>
</template>

<script>
import SettingContent from '../SettingContent'
import MongoForm from './MongoForm'
export default {
  name: 'StepContent',
  components: {
    SettingContent,
    MongoForm
  },
  data () {
    return {
      currentType: 'mongo',
      groupTabs: [
        { key: 'mongo', title: 'Mongo DB', icon: 'heart' },
        { key: 'mysql', title: 'Mysql DB', icon: 'heart' }
      ]
    }
  },
  computed: {
    descs () {
      const basicDesc = {
        title: '什么是数据持久层？',
        content:
          '数据持久层是后端云系统用户、数据仓库等业务数据的存放地，持久策略配置完成后，系统将根据所选策略执行表格与初始数据的创建等初始化工作，如数据库中已有数据则会优先读取而非重置。'
      }
      const descs = [basicDesc]
      switch (this.currentType) {
        case 'mongo':
          descs.push({
            title: 'Mongo DB',
            content: 'MongoDB 是一个基于分布式文件存储的数据库，它介于关系数据库和非关系数据库之间。'
          })
          break
        case 'mysql':
          descs.push({
            title: 'Mysql DB',
            content: 'MySQL 是当前最流行的关系型数据库管理系统，被广泛地应用在Internet上的中小型网站中。'
          })
          break
      }
      return descs
    }
  },
  methods: {

  }
}
</script>

<style lang="less" scoped>
</style>
