<template>
  <div class="common-left" :class="{toggled:isMenuCollapsed}" id="common-left">
    <img src="@/assets/images/top_left.png" class="top_left" alt="">
    <el-menu
      :default-active="$route.path"
      class="el-menu-vertical-demo"
      background-color="rgba(0, 0, 0, 0.2)"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
      :collapse="isMenuCollapsed">
      <el-menu-item v-for="item in userMenu" :index="item.menuUrl" :key="item.id">
        <i :class="item.menuIcon"></i>
        <span slot="title">{{ item.name }}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script>
import { modulesToMenu } from '@/config/modules'
export default {
  name: 'Left',
  data () {
    return { userMenu: [] }
  },
  computed: {
    isMenuCollapsed () {
      return this.$store.getters['menu/isMenuCollapsed']
    }
  },
  mounted () {
    const saved = localStorage.getItem('userMenu')
    if (saved) {
      try {
        this.userMenu = JSON.parse(saved)
      } catch (e) {
        this.userMenu = modulesToMenu()
      }
    } else {
      this.userMenu = modulesToMenu()
      localStorage.setItem('userMenu', JSON.stringify(this.userMenu))
    }
  }
}
</script>
<style scoped>
.top_left { display:block; margin:1px; max-width:100%; }
.el-menu { border-right:0; }
.el-menu-item { height:50px; }
</style>
