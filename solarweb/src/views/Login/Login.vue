<template>
  <div class="auth-page">
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>
    <div class="bg-orb orb-3"></div>
    <div class="auth-shell">
      <section class="brand-panel">
        <div class="brand-badge">SOLAR BIG DATA</div>
        <h1>光伏大数据<br/>智能管理平台</h1>
        <p>实时监控 · 智能分析 · 高效运维</p>
        <ul class="feature-list">
          <li><i class="el-icon-data-analysis"></i> 电站数据全景看板</li>
          <li><i class="el-icon-monitor"></i> 设备实时状态监控</li>
          <li><i class="el-icon-s-opportunity"></i> 发电量预测与分析</li>
        </ul>
        <div class="brand-footer">青岛科技 · Photovoltaic Cloud</div>
      </section>

      <section class="form-panel">
        <div class="form-tabs">
          <button :class="{active: mode==='login'}" @click="switchMode('login')">登录</button>
          <button :class="{active: mode==='register'}" @click="switchMode('register')">注册</button>
        </div>

        <el-form
          v-if="mode==='login'"
          :model="loginForm"
          :rules="loginRules"
          ref="loginForm"
          class="auth-form"
          @submit.native.prevent>
          <h2>欢迎回来</h2>
          <p class="sub">使用账号密码进入系统</p>
          <el-form-item prop="name">
            <el-input
              v-model="loginForm.name"
              prefix-icon="el-icon-user"
              placeholder="用户名"
              maxlength="20"
              clearable />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              prefix-icon="el-icon-lock"
              type="password"
              placeholder="密码"
              maxlength="16"
              show-password />
          </el-form-item>
          <div class="form-extra">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <span class="hint">默认测试账号 admin / 123456</span>
          </div>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="loading"
            @click="submitLogin('loginForm')">
            {{ loading ? '登录中...' : '立即登录' }}
          </el-button>
        </el-form>

        <el-form
          v-else
          :model="registerForm"
          :rules="registerRules"
          ref="registerForm"
          class="auth-form"
          @submit.native.prevent>
          <h2>创建账号</h2>
          <p class="sub">注册后可登录体验平台功能</p>
          <el-form-item prop="name">
            <el-input
              v-model="registerForm.name"
              prefix-icon="el-icon-user"
              placeholder="用户名（4-20位字母数字）"
              maxlength="20"
              clearable />
          </el-form-item>
          <el-form-item prop="trueName">
            <el-input
              v-model="registerForm.trueName"
              prefix-icon="el-icon-s-custom"
              placeholder="真实姓名"
              maxlength="30"
              clearable />
          </el-form-item>
          <el-form-item prop="phone">
            <el-input
              v-model="registerForm.phone"
              prefix-icon="el-icon-phone"
              placeholder="手机号（可选）"
              maxlength="15"
              clearable />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              prefix-icon="el-icon-lock"
              type="password"
              placeholder="密码（6-16位）"
              maxlength="16"
              show-password />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              prefix-icon="el-icon-lock"
              type="password"
              placeholder="确认密码"
              maxlength="16"
              show-password />
          </el-form-item>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="loading"
            @click="submitRegister('registerForm')">
            {{ loading ? '注册中...' : '注册并登录' }}
          </el-button>
        </el-form>
      </section>
    </div>
  </div>
</template>

<script>
import { login, register } from '@/api/Login/Login'

export default {
  data () {
    const validateConfirm = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次密码不一致'))
      } else {
        callback()
      }
    }
    return {
      mode: 'login',
      loading: false,
      rememberMe: true,
      loginForm: {
        name: localStorage.getItem('rememberUser') || '',
        password: ''
      },
      registerForm: {
        name: '',
        trueName: '',
        phone: '',
        password: '',
        confirmPassword: ''
      },
      loginRules: {
        name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      registerRules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 4, max: 20, message: '用户名长度 4-20', trigger: 'blur' },
          { pattern: /^[A-Za-z0-9_]+$/, message: '仅支持字母数字下划线', trigger: 'blur' }
        ],
        trueName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        phone: [{ pattern: /^$|^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 16, message: '密码长度 6-16', trigger: 'blur' }
        ],
        confirmPassword: [{ required: true, validator: validateConfirm, trigger: 'blur' }]
      }
    }
  },
  created () {
    window.addEventListener('keydown', this.onKeydown)
  },
  destroyed () {
    window.removeEventListener('keydown', this.onKeydown, false)
  },
  methods: {
    switchMode (mode) {
      this.mode = mode
      this.loading = false
    },
    onKeydown (e) {
      const key = e.keyCode || (window.event && window.event.keyCode)
      if (key === 13) {
        if (this.mode === 'login') this.submitLogin('loginForm')
        else this.submitRegister('registerForm')
      }
    },
    persistLogin (data) {
      localStorage.setItem('Authorization', data.resultValue.token || ('mock-token-' + Date.now()))
      localStorage.setItem('userName', data.resultValue.name)
      localStorage.setItem('userId', data.resultValue.id)
      localStorage.setItem('userMenu', JSON.stringify(data.resultValue.menuList || []))
      const authList = data.resultValue.authList || []
      const auth = authList.map(item => item.code).filter(Boolean)
      localStorage.setItem('userAuth', JSON.stringify(auth))
      if (this.rememberMe) {
        localStorage.setItem('rememberUser', data.resultValue.name)
      } else {
        localStorage.removeItem('rememberUser')
      }
      this.$router.push('/home')
    },
    submitLogin (formName) {
      this.$refs[formName].validate(valid => {
        if (!valid) return
        this.loading = true
        login(this.loginForm).then(data => {
          if (data.successful) {
            this.$message.success('登录成功')
            this.persistLogin(data)
          } else {
            this.$message.error(data.resultHint || '登录失败')
          }
        }).catch(() => {
          this.$message.error('网络异常，请稍后重试')
        }).finally(() => {
          this.loading = false
        })
      })
    },
    submitRegister (formName) {
      this.$refs[formName].validate(valid => {
        if (!valid) return
        this.loading = true
        const payload = {
          name: this.registerForm.name,
          trueName: this.registerForm.trueName,
          phone: this.registerForm.phone,
          password: this.registerForm.password
        }
        register(payload).then(data => {
          if (data.successful) {
            this.$message.success('注册成功，正在登录')
            return login({
              name: payload.name,
              password: payload.password
            }).then(loginData => {
              if (loginData.successful) {
                this.persistLogin(loginData)
              } else {
                this.mode = 'login'
                this.loginForm.name = payload.name
                this.$message.warning('注册成功，请手动登录')
              }
            })
          }
          this.$message.error(data.resultHint || '注册失败')
        }).catch(() => {
          this.$message.error('网络异常，请稍后重试')
        }).finally(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: linear-gradient(135deg, #0b1f3a 0%, #123a5c 45%, #0f766e 100%);
  position: relative;
  overflow: hidden;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
}
.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.45;
  pointer-events: none;
}
.orb-1 { width: 320px; height: 320px; background: #38bdf8; top: -60px; left: -40px; }
.orb-2 { width: 280px; height: 280px; background: #34d399; bottom: -40px; right: 10%; }
.orb-3 { width: 200px; height: 200px; background: #818cf8; top: 30%; right: -40px; }

.auth-shell {
  width: min(980px, 100%);
  min-height: 560px;
  display: grid;
  grid-template-columns: 1.05fr 0.95fr;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(8px);
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.18);
  z-index: 1;
}

.brand-panel {
  padding: 48px 40px;
  color: #f8fafc;
  background:
    linear-gradient(160deg, rgba(14, 116, 144, 0.55), rgba(15, 23, 42, 0.35)),
    url('../../assets/images/login.jpg') center/cover no-repeat;
  display: flex;
  flex-direction: column;
}
.brand-badge {
  display: inline-block;
  width: fit-content;
  padding: 6px 12px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.16);
  border: 1px solid rgba(255, 255, 255, 0.25);
  font-size: 12px;
  letter-spacing: 1px;
}
.brand-panel h1 {
  margin: 28px 0 12px;
  font-size: 36px;
  line-height: 1.25;
  font-weight: 700;
}
.brand-panel > p {
  margin: 0 0 28px;
  color: rgba(248, 250, 252, 0.85);
  font-size: 15px;
}
.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1;
}
.feature-list li {
  margin-bottom: 14px;
  padding: 12px 14px;
  border-radius: 12px;
  background: rgba(15, 23, 42, 0.28);
  border: 1px solid rgba(255, 255, 255, 0.12);
  font-size: 14px;
}
.feature-list i { margin-right: 8px; color: #67e8f9; }
.brand-footer {
  margin-top: 20px;
  font-size: 12px;
  color: rgba(248, 250, 252, 0.7);
}

.form-panel {
  background: rgba(255, 255, 255, 0.96);
  padding: 36px 40px 32px;
  display: flex;
  flex-direction: column;
}
.form-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
  background: #f1f5f9;
  border-radius: 14px;
  padding: 6px;
  margin-bottom: 24px;
}
.form-tabs button {
  border: 0;
  background: transparent;
  height: 40px;
  border-radius: 10px;
  cursor: pointer;
  color: #64748b;
  font-size: 15px;
  font-weight: 600;
  transition: all .2s ease;
}
.form-tabs button.active {
  background: #fff;
  color: #0f766e;
  box-shadow: 0 6px 16px rgba(15, 23, 42, 0.08);
}
.auth-form h2 {
  margin: 0 0 6px;
  color: #0f172a;
  font-size: 26px;
}
.auth-form .sub {
  margin: 0 0 22px;
  color: #64748b;
  font-size: 13px;
}
.auth-form >>> .el-input__inner {
  height: 46px;
  border-radius: 12px;
  border-color: #e2e8f0;
  background: #f8fafc;
}
.auth-form >>> .el-input__inner:focus {
  border-color: #14b8a6;
  background: #fff;
}
.form-extra {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: -4px 0 18px;
  color: #64748b;
  font-size: 12px;
}
.hint { opacity: 0.85; }
.submit-btn {
  width: 100%;
  height: 46px;
  border: 0;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  background: linear-gradient(135deg, #0ea5e9, #14b8a6);
  box-shadow: 0 12px 24px rgba(14, 165, 233, 0.28);
}
.submit-btn:hover,
.submit-btn:focus {
  background: linear-gradient(135deg, #0284c7, #0d9488);
}

@media (max-width: 860px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }
  .brand-panel {
    min-height: 220px;
    padding: 28px 24px;
  }
  .brand-panel h1 { font-size: 28px; }
  .feature-list { display: none; }
  .form-panel { padding: 28px 22px; }
}
</style>
