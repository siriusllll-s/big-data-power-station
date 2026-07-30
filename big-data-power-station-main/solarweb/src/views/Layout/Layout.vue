<template>
  <div class="main">
    <Header></Header>
    <Left></Left>
    <div class="common-right" v-bind:class="{'w100':isMenuCollapsed}" id="common-right">
      <div class="content">
        <el-breadcrumb separator="/" class="breadcrumb-nav">
          <el-breadcrumb-item v-for="item in levelList" :key="item.path" :to="item.path">{{item.title}}</el-breadcrumb-item>
        </el-breadcrumb>
        <router-view/>
      </div>
    </div>
  </div>
</template>
<script>
import Left from '@/views/Layout/Left'
import Header from '@/views/Layout/Header'
export default {
  components: {
    Left,
    Header
  },
  data () {
    return {
      levelList: []
    }
  },
  watch: {
    $route () {
      this.getBreadcrumb()
    }
  },
  computed: {
    isMenuCollapsed: function () {
      return this.$store.getters['menu/isMenuCollapsed']
    }
  },
  created () {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb () {
      let bl = window.sessionStorage.getItem('breadcrumb')
      if (bl !== undefined) {
        this.levelList = JSON.parse(bl)
        if (this.levelList == null) {
          this.levelList = []
        }
      }
      let matched = this.$route.matched.filter(item => item.name)
      if (matched !== undefined && matched.length > 1) {
        const crumb = matched[1]
        let link = { path: crumb.path, title: crumb.meta.title }
        let that = this
        let pos = -1
        let even = function (obj) {
          if (that.levelList == null) {
            return false
          }
          return that.levelList.some((item, index) => {
            if (item.path === obj.path) {
              pos = index
              return true
            }
          })
        }
        if (!even(link)) {
          this.levelList.push(link)
          window.sessionStorage.setItem('breadcrumb', JSON.stringify(this.levelList))
        } else {
          let arr = this.levelList.slice(0, pos + 1)
          this.levelList = arr
          window.sessionStorage.setItem('breadcrumb', JSON.stringify(arr))
        }
      }
    }
  }
}
</script>
<style scoped>
  .el-breadcrumb__item
  {
    font-size: 15px;
  }
.common-right{
  background: url('../../assets/images/main_bg.png') no-repeat;
  background-size: 100% 100%;
}
.content{
  background: rgba(255, 255, 255, 0.7);
  padding: 20px 35px;
  border-radius: 10px;
}
  .breadcrumb-nav{
    padding-bottom: 20px;
  }
</style>
<style>
  :-webkit-full-screen #common-head {
    display: none!important;
  }
  :-moz-full-screen #common-head {
    display: none!important;
  }
  :-ms-fullscreen #common-head {
    display: none!important;
  }
  :fullscreen #common-head {
    display: none!important;
  }

  :-webkit-full-screen #common-left {
    display: none!important;
  }
  :-moz-full-screen #common-left {
    display: none!important;
  }
  :-ms-fullscreen #common-left {
    display: none!important;
  }
  :fullscreen #common-left {
    display: none!important;
  }
</style>
