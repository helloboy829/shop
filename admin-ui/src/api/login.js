import request from '@/utils/request'

// 登录方法
export function loginByUsername(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 获取用户详细信息
export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/auth/captchaImage',
    method: 'get'
  })
}
