<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item title="服务状态" name="1">
          <div><a href="http://localhost:5001" target="_blank">服务状态监控</a></div>
        </el-collapse-item>
        <el-collapse-item title="缓存状态" name="2">
          <div><a href="http://localhost:5005" target="_blank">服务状态监控</a></div>
        </el-collapse-item>
        <el-collapse-item title="ZipKin状态" name="3">
          <div><a href="http://localhost:5002" target="_blank">ZipKin状态监控</a></div>
        </el-collapse-item>
        <el-collapse-item title="ELK状态" name="4">
          <div><a href="http://106.14.69.75:5601" target="_blank">ELK状态监控</a></div>
        </el-collapse-item>
        <el-collapse-item title="文档接口" name="5">
          <div><a href="http://localhost:9999/swagger-ui.html" target="_blank">接口文档监控</a></div>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        activeName: '1'
      }
    }
  }
</script>


