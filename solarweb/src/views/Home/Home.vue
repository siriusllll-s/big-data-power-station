<template>
  <div class="home">
    <h2>欢迎使用光伏大数据管理平台</h2>
    <p>登录成功，当前用户：{{ userName }}</p>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
      userName: localStorage.getItem('userName') || ''
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}
</style>
