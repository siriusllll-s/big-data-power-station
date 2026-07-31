<template>
  <div>
    <el-form :inline="true">
      <el-form-item label="设备类型">
        <el-select v-model="type" @change="load">
          <el-option label="集中式逆变器" :value="0" />
          <el-option label="直流汇流箱" :value="1" />
          <el-option label="直流汇流柜" :value="2" />
          <el-option label="气象站" :value="3" />
          <el-option label="电表" :value="4" />
        </el-select>
      </el-form-item>
      <el-button type="primary" icon="el-icon-refresh" @click="load">刷新</el-button>
    </el-form>
    <el-table v-loading="loading" :data="list" border stripe>
      <el-table-column type="index" label="#" width="60" />
      <el-table-column prop="name" label="设备名称" />
      <el-table-column prop="id" label="设备标识" />
      <el-table-column label="类型">
        <template slot-scope="s">{{ typeName }}</template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import { getDeviceListByType } from '@/api/Device/Device'
export default {
  name: 'DeviceIndex',
  data () { return { type: 0, list: [], loading: false } },
  computed: {
    typeName () {
      return { 0: '集中式逆变器', 1: '直流汇流箱', 2: '直流汇流柜', 3: '气象站', 4: '电表' }[this.type]
    }
  },
  mounted () { this.load() },
  methods: {
    load () {
      this.loading = true
      getDeviceListByType(this.type).then(res => {
        this.loading = false
        if (res.successful) this.list = res.resultValue || []
        else this.$message.error(res.resultHint || '失败')
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
