<template>
    <iframe ref="iframe" src="http://localhost:9999/swagger-ui.html" frameborder="0" style="border:0;" width="100%"></iframe>
</template>

<script>
  export default {
    mounted() {
      this.iframeInit()
    },
    methods: {
      iframeInit() {
        const iframe = this.$refs.iframe
        const clientHeight = document.documentElement.clientHeight - 84
        iframe.style.height = `${clientHeight}px`
      }
    }
  }
</script>

<style scoped>
</style>
