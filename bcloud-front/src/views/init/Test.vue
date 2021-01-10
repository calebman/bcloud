<template>
  <dy-form-editor v-model="formSetting"></dy-form-editor>
</template>

<script>
import { DyFormEditor } from '../../../DyForm'
export default {
  name: 'Test',
  components: {
    DyFormEditor
  },
  data () {
    return {
      formSetting: {
        component: 'AFormModel',
        options: {
          labelCol: { span: 4 },
          wrapperCol: { span: 14 }
        },
        children: [
          {
            _key: 'zxcasd',
            type: 'basic',
            component: 'AIconInput',
            formOptions: {
              prop: 'name',
              label: '姓名'
            },
            options: {}
          },
          {
            _key: 'sdasdasd',
            type: 'layout',
            component: 'ARow',
            options: {
              gutter: 16
            },
            children: [
              {
                _key: '1231sasd',
                component: 'ACol',
                options: {
                  name: '第一项'
                },
                children: [
                  {
                    _key: '1231sasd123123',
                    type: 'basic',
                    component: 'AIconInput',
                    formOptions: {
                      prop: 'idCard',
                      label: '身份证号码'
                    }
                  }
                ]
              },
              {
                _key: '112221',
                component: 'ACol',
                options: {
                  name: '第二项'
                },
                children: [
                  {
                    _key: '1231sasdasdx',
                    type: 'basic',
                    component: 'ASelect',
                    formOptions: {
                      prop: 'sex',
                      label: '性别'
                    }
                  }
                ]
              }
            ]
          },
          {
            _key: 'qwe1',
            type: 'layout',
            component: 'ATabs',
            options: {},
            children: [
              {
                _key: 'qwe2',
                component: 'ATabPanel',
                options: {
                  name: '工作信息'
                },
                children: [
                  {
                    _key: 'qwe5',
                    type: 'basic',
                    component: 'AIconInput',
                    formOptions: {
                      prop: 'company',
                      label: '所在公司'
                    },
                    options: {}
                  }
                ]
              },
              {
                _key: 'qwe3',
                component: 'ATabPanel',
                options: {
                  name: '家庭信息'
                },
                children: [
                  {
                    _key: 'qwe4',
                    type: 'basic',
                    component: 'ASelect',
                    formOptions: {
                      prop: 'address',
                      label: '户口籍贯'
                    },
                    options: {}
                  }
                ]
              }
            ]
          },
          {
            _key: '123123',
            type: 'basic',
            component: 'ADatePicker',
            formOptions: {
              prop: 'birth',
              label: '出生年月'
            },
            options: {}
          }
        ]
      }
    }
  }
}
</script>
