<template>
  <div>
    <el-button type="primary" @click="$router.push('/device')">设备信息</el-button>
    <el-button type="primary" plain @click="$router.push('/factory')">设备厂商</el-button>
  </div>
</template>
<script>
export default { name: 'DeviceIndex' }
</script>
