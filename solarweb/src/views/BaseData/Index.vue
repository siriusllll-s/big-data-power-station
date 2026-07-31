<template>
  <div>
    <el-tabs v-model="tab">
      <el-tab-pane label="省份字典" name="province">
        <el-table v-loading="loading" :data="provinces" border stripe>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="provinceCode" label="编码" />
          <el-table-column prop="province" label="省份" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { getProvinceList } from '@/api/Dictionary/Position'
export default {
  name: 'BaseData',
  data () { return { tab: 'province', provinces: [], loading: false } },
  mounted () {
    this.loading = true
    getProvinceList().then(res => {
      this.loading = false
      if (res.successful) this.provinces = res.resultValue || []
      else this.$message.error(res.resultHint || '加载失败')
    }).catch(() => { this.loading = false })
  }
}
</script>
