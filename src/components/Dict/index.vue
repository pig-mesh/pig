<template>
  <div>
      <el-select class="filter-item" placeholder="请选择" v-model="dict">
        <el-option v-for="item in  dicts" :key="item.value" :label="item.label" :value="item.value"> </el-option>
      </el-select>
    {{dict}}
  </div>
</template>

<script>
  import fetch from '@/utils/fetch'

  export default {
    name: 'dict',
    props: ['type'],
    data() {
      return {
        dict: undefined,
        dicts : [],
      }
    },
    created() {
      fetch({
        url: '/admin/dict/type/' + this.type,
        method: 'get'
      }).then(response => {
        this.dicts = response.data
      })
    }
  }
</script>
