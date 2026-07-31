<template>
  <div>
    <el-tabs>
      <el-tab-pane label="用户列表">
        <el-table v-loading="loading" :data="users" border stripe>
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column prop="name" label="账号" />
          <el-table-column prop="trueName" label="姓名" />
          <el-table-column prop="phone" label="手机" />
          <el-table-column prop="eMail" label="邮箱" />
        </el-table>
        <el-button class="mt" type="primary" size="small" @click="load">刷新</el-button>
      </el-tab-pane>
      <el-tab-pane label="修改密码">
        <el-form :model="pwd" label-width="100px" style="max-width:420px">
          <el-form-item label="原密码"><el-input type="password" v-model="pwd.oldPassword" /></el-form-item>
          <el-form-item label="新密码"><el-input type="password" v-model="pwd.newPassword" /></el-form-item>
          <el-button type="primary" @click="changePwd">提交</el-button>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { changePassword, listUsers } from '@/api/User/User'
export default {
  name: 'Authority',
  data () {
    return { users: [], loading: false, pwd: { oldPassword: '', newPassword: '' } }
  },
  mounted () { this.load() },
  methods: {
    load () {
      this.loading = true
      listUsers().then(res => {
        this.loading = false
        if (res.successful) this.users = res.resultValue || []
        else this.$message.error(res.resultHint || '加载失败')
      }).catch(() => { this.loading = false })
    },
    changePwd () {
      changePassword(this.pwd).then(res => {
        if (res.successful) this.$message.success('修改成功，请重新登录')
        else this.$message.error(res.resultHint || '失败')
      })
    }
  }
}
</script>
<style scoped>.mt{margin-top:12px}</style>
