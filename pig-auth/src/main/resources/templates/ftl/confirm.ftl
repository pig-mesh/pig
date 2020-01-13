<!DOCTYPE html>
<html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title>PigX第三方授权</title>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/signin.css"/>
</head>

<body>
<nav class="navbar navbar-default container-fluid">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">开放平台</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-5">
            <p class="navbar-text navbar-right">
                <a target="_blank" href="https://pig4cloud.com">技术支持</a>
            </p>
            <p class="navbar-text navbar-right">
                <a target="_blank" href="https://pig4cloud.com">${user.username}</a>
            </p>
        </div>
    </div>
</nav>
<div style="padding-top: 80px;width: 300px; color: #555; margin:0px auto;">
    <form id='confirmationForm' name='confirmationForm' action="/oauth/authorize" method='post'>
        <input name='user_oauth_approval' value='true' type='hidden'/>
        <p>
            <a href="${app.website!''}" target="_blank">${app.appName!'未定义应用名称'}</a> 将获得以下权限：</p>
        <ul class="list-group">
            <li class="list-group-item"> <span>
              <#list scopeList as scope>
                  <input type="hidden" name="${scope}" value="true"/>
                  <input type="checkbox" disabled checked="checked"/><label>${scope}</label>
              </#list>
        </ul>
        <p class="help-block">授权后表明你已同意 <a>服务协议</a></p>
        <button class="btn btn-success pull-right" type="submit" id="write-email-btn">授权</button>
        </p>
    </form>
</div>
<footer>
    <p>support by: pig4cloud.com</p>
    <p>email: <a href="mailto:wangiegie@gmail.com">wangiegie@gmail.com</a>.</p>
</footer>
</body>
</html>
