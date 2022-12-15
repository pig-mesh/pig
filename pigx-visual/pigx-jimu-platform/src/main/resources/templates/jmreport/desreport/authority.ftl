<#assign CACHE_VERSION = "v=1.0.13">
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width">
  <title>没有权限</title>
  <script>
    let base = "${base}";
  </script>
  <style>
    .no-authority{
        width: 100%;
        height: 100%;
        text-align: center;
        justify-content: center;
        position: absolute;
        top: 50%;
    }
    .no-authority span{
        font-size: 20px;
    }
    .img-position{
        width: 100%;
        height: 100%;
        text-align: center;
        justify-content: center;
        position: absolute;
        top: 17%;
        margin: 0 auto;
    }
  </style>
<body style="overflow: hidden">
<div id="app" style="overflow: hidden">
  <div class="img-position">
    <img src="${base}/jmreport/desreport_/chartsImg/authority/no-authority.png"/>
  </div>
  <div class="no-authority">
    <span>您没有权限访问该页面</span>
  </div>
</div>
</body>
</html>
