<template>
  <header class="common-head" v-bind:class="{ w100: isMenuCollapsed }" id="common-head">
    <div class="menu-bar" v-on:click="toggleLeftMenuBar">
      <i class="icon icon-menuBar"></i>
    </div>

    <div class="fr setting">
      <el-dropdown class="set-icon" @command="handleCommand">
        <span class="el-dropdown-link">
          <img src="@/assets/images/user.png" class="head_img">
          <span>{{userName}}</span>
          <i class="el-icon-caret-bottom el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item icon="el-icon-lock" command="P-EDIT">密码修改</el-dropdown-item>
          <el-dropdown-item icon="el-icon-close" command="LOGOUT">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog title="密码修改" :append-to-body="true"
               :visible.sync="dialogPwdEdit" :width="dialogWidth" center
               :show-close="false" :close-on-click-modal="false">
      <el-form :model="pwdEditForm" status-icon :rules="rules" ref="pwdEditForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPassword" required>
          <el-input type="password" v-model="pwdEditForm.oldPassword" autocomplete="off" show-password maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword" required>
          <el-input type="password" v-model="pwdEditForm.newPassword" autocomplete="off" show-password maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword" required>
          <el-input type="password" v-model="pwdEditForm.confirmPassword" autocomplete="off" show-password maxlength="16"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('pwdEditForm')">确认修改</el-button>
          <el-button @click="cancelForm('pwdEditForm')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </header>

</template>
<script>
import { changePassword, loginOut } from '@/api/User/User'
export default {
  name: 'Header',
  data () {
    let validateOldPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入原密码'))
      } else {
        callback()
      }
    }

    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.pwdEditForm.confirmPassword !== '') {
          this.$refs.pwdEditForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入确认密码'))
      } else if (value !== this.pwdEditForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      userName: '',
      dialogPwdEdit: false,
      pwdEditForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      rules: {
        oldPassword: [
          { validator: validateOldPass, trigger: 'blur' }
        ],
        newPassword: [
          { min: 6, max: 16, message: '密码必须为6-16位', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      },
      dialogWidth: '30%'
    }
  },
  computed: {
    isMenuCollapsed: function () {
      return this.$store.getters['menu/isMenuCollapsed']
    }
  },
  mounted () {
    this.userName = localStorage.getItem('userName')
    window.onresize = () => {
      return (() => {
        this.setDialogWidth()
      })()
    }
  },
  methods: {
    setDialogWidth () {
      var val = document.body.clientWidth
      const def = 1000
      if (val > def) {
        this.dialogWidth = '30%'
      } else {
        this.dialogWidth = '300px'
      }
    },

    toggleLeftMenuBar: function () {
      this.$store.commit('menu/toggleMenu')
    },
    logOut: function () {
      loginOut()
      localStorage.removeItem('Authorization')
      localStorage.removeItem('userMenu')
      localStorage.removeItem('userAuth')
      localStorage.removeItem('userName')
      localStorage.removeItem('userId')
      this.$router.push('/login')
      window.sessionStorage.removeItem('breadcrumb')
    },

    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          changePassword(this.pwdEditForm).then(data => {
            if (data.successful) {
              this.$message({
                message: '密码修改成功，请重新登录！',
                type: 'success'
              })
              this.dialogPwdEdit = false
              this.cancelForm('pwdEditForm')
              this.logOut()
            } else {
              this.$message.error(data.resultHint)
            }
          })
        } else {
          return false
        }
      })
    },
    cancelForm (formName) {
      this.$refs[formName].resetFields()
      this.dialogPwdEdit = false
    },
    handleCommand: function (cmd) {
      if (cmd === 'LOGOUT') {
        this.logOut()
      } else {
        this.dialogPwdEdit = true
      }
    }
  }
}
</script>
<style scoped>
.common-head{
  background: url('../../assets/images/top_bg1.png') no-repeat;
  background-size: cover;
}

 .el-dropdown-link {
   cursor: pointer;
   color: white;
   font-size: 13px;
 }

.el-dropdown-link{
  display: flex;
  align-items: center;
}
.head_img{
  margin-right: 10px;
  height: 18px;
}
.setting {
  position: relative;
  margin-right: 20px;
}
.setting .set-icon {
  width: auto;
  height: 55px;
  text-align: center;
  line-height: 55px;
  display: inline-block;
  cursor: pointer;
  position: relative;
  user-select: none;
}
</style>
