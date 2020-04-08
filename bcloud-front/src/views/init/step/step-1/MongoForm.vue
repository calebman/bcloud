<!-- Mongo DB 初始化所需信息  -->
<template>
  <a-form :form="form" style="max-width: 500px; margin: 0 auto 0;">
    <a-form-item label="连接字符串" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input
        placeholder="请填写Mongo DB的连接地址，例如 mongo://localhost"
        v-decorator="['uri', { initialValue: '', rules: [{required: true, message: '收款人名称必须核对'}] }]"
      />
    </a-form-item>
    <a-form-item label="数据库名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input
        placeholder="请填写一个合法的数据库名称，不可包含特殊字符"
        v-decorator="['dbName', { initialValue: 'bcloud', rules: [{required: true, message: '数据库名称必须填写'}] }]"
      />
    </a-form-item>
    <a-form-item label="Mongo 账户" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input
        placeholder="Mongo DB 安全验证账户"
        v-decorator="['user']"
      >
        <a-icon slot="prefix" type="user" style="color:rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
    <a-form-item label="Mongo 密码" :labelCol="labelCol" :wrapperCol="wrapperCol" help="没有配置安全验证可以不填写账户密码字段">
      <a-input
        type="password"
        placeholder="Mongo DB 安全验证密码"
        v-decorator="['pwd']"
      >
        <a-icon slot="prefix" type="lock" style="color:rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
  </a-form>
</template>

<script>
export default {
  name: 'MongoForm',
  components: {},
  props: {},
  data () {
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      form: this.$form.createForm(this)
    }
  },
  computed: {},
  watch: {},
  methods: {
    nextStep () {
      const {
        form: { validateFields }
      } = this
      // 先校验，通过表单校验后，才进入下一步
      validateFields((err, values) => {
        if (!err) {
          this.$emit('nextStep')
        }
      })
    }
  },
  created () {},
  mounted () {}
}
</script>

<style lang="less" scoped>
</style>
