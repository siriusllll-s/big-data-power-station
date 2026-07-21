<template>
  <div class="common-left" :class="{'toggled':isMenuCollapsed}" id="common-left">
    <img src="@/assets/images/top_left.png" class="top_left">
    <el-menu
      :default-active="this.$route.path"
      class="el-menu-vertical-demo"
      background-color="rgba(0, 0, 0, 0.2)"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
      >
      <template v-for="item in userMenu">
      <el-menu-item  :index="item.menuUrl" v-if="item.menuUrl" :key="'m-'+item.id">
        <template slot="title">
          <i :class="item.menuIcon"></i>
          <span slot="title">{{item.name}}</span>
        </template>
      </el-menu-item>
      <el-submenu :index="item.name" v-if="item.childs" :key="'s-'+item.id">
        <template slot="title">
          <i :class="item.menuIcon"> </i>
          <span slot="title">{{item.name}}</span>
        </template>
          <el-menu-item  v-for="child in item.childs" :index="child.menuUrl" :key="child.id">
            <template>
              <i :class="child.menuIcon"></i>
              <span>{{child.name}}</span>
            </template>
          </el-menu-item>
      </el-submenu>
      </template>
    </el-menu>
  </div>
</template>
<script>
export default {
  name: 'Left',
  data () {
    return {
      userMenu: []
    }
  },
  computed: {
    isMenuCollapsed: function () {
      return this.$store.getters['menu/isMenuCollapsed']
    }
  },
  mounted () {
    this.userMenu = JSON.parse(localStorage.getItem('userMenu') || '[]')
  },
  methods: {}
}
</script>
<style scoped>
  .top_left {
    display: block;
    margin: 1px;
  }

  .el-menu {
    border-right: 0;
  }

  .el-menu-item {
    color: #fff;
    height: 50px;
  }

  .el-menu-item a{
    color: #fff;
  }

</style>
