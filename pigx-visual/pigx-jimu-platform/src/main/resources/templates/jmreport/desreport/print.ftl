<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>打印页面</title>
</head>
<body>
    <input type="text" id='printInput' placeholder="请输入打印地址" style="width: 650px;">
    <button onclick="printFn()">打印</button>
    <iframe id='report' frameborder="0" width="100%" height="100%"></iframe>
   <script>

        function getRequestUrl() {
            var url = location.search;
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for(var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        }

        function printFn(){
          const printText = document.getElementById('printInput').value;
          if(!printText) alert('请输入打印地址');
          document.getElementById('report').src=printText;
        }  
   </script>
</body>
</html>