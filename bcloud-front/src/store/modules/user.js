import Vue from 'vue'
import { login, getInfo, logout } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    account: '',
    email: '',
    status: '',
    avatar: '',
    roles: [],
    extraInfo: {},
    menuKeys: [],
    resourceKeys: [],
    welcome: ''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INFO: (state, { key, value }) => {
      state[key] = value
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(token => {
          Vue.ls.set(ACCESS_TOKEN, token, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(userInfo => {
          Object.keys(userInfo).forEach(k => commit('SET_INFO', {
            key: k,
            value: userInfo[k]
          }))
          resolve(userInfo)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit }) {
      return new Promise((resolve) => {
        logout().then(() => {
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          commit('SET_TOKEN', '')
          Vue.ls.remove(ACCESS_TOKEN)
        })
      })
    }

  }
}

export default user
