<!DOCTYPE html>
<html>
<html>
<head>
	<meta charset="UTF-8"/>
	<meta name="viewport"
	      content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
	<title>Pig 第三方授权</title>
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
                <#if principalName=="anonymousUser">
					未登录
                <#else>
					<a target="_blank" href="https://pig4cloud.com">${principalName}</a>
                </#if>
			</p>
		</div>
	</div>
</nav>
<div style="padding-top: 80px;width: 300px; color: #555; margin:0px auto;">
	<form id='confirmationForm' name='confirmationForm' action="/oauth2/authorize" method='post'>
		<input type="hidden" name="client_id" value="${clientId}">
		<input type="hidden" name="state" value="${state}">

		<p>
			将获得以下权限：</p>
		<ul class="list-group">
			<li class="list-group-item"> <span>
              <#list scopeList as scope>
	              <input type="checkbox" checked="checked" name="scope" value="${scope}"/><label>${scope}</label>
              </#list>
		</ul>
		<p class="help-block">授权后表明你已同意 <a>服务协议</a></p>
		<button class="btn btn-success pull-right" type="submit" id="write-email-btn">授权</button>
		</p>
	</form>
</div>
</body>
</html>
