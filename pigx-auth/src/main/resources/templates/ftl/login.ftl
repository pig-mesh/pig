<!doctype html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#-- 内网部署下载相对应放文件放在 resource目录离线加载-->
	<script src="https://cdn.tailwindcss.com"></script>
	<link href="/css/signin.css" rel="stylesheet">
</head>
<body>
<div class="h-screen bg-white relative flex flex-col space-y-10 justify-center items-center">
	<div class="bg-white md:shadow-lg shadow-none rounded p-6 w-96">
		<h1 class="text-3xl font-bold leading-normal text-center">统一身份平台</h1>
		<form class="space-y-5 mt-5" action="/token/form" method="post">
			<input type="hidden" name="client_id" class="form-control" value="pig" placeholder="所属客户端" >

			<div class="mb-4 relative">
                <#if tenantList??>
					<select class="w-full rounded px-3 border border-gray-500 pt-5 pb-2 focus:outline-none input active:outline-none" placeholder="所属租户" name="TENANT-ID">
                        <#list tenantList as tenant>
							<option value="${tenant.id}">${tenant.name}</option>
                        </#list>
					</select>
                </#if>
			</div>
			<div class="mb-4 relative">
				<input id="username" required name="username"
				       class="w-full rounded px-3 border border-gray-500 pt-5 pb-2 focus:outline-none input active:outline-none"
				       type="text" autofocus>
				<label for="username"
				       class="label absolute mb-0 -mt-2 pl-3 leading-tighter text-gray-500 text-base mt-2 cursor-text">账号</label>
			</div>
			<div class="relative flex items-center border border-gray-500 focus:ring focus:border-blue-500 rounded">
				<input id="password" required name="password"
				       class="w-full rounded px-3 pt-5 outline-none pb-2 focus:outline-none active:outline-none input active:border-blue-500"
				       type="password"/>
				<label for="password"
				       class="label absolute mb-0 -mt-2 pl-3 leading-tighter text-gray-500 text-base mt-2 cursor-text">密码</label>
			</div>
			<button class="w-full text-center bg-blue-700 hover:bg-blue-900 rounded-full text-white py-3 font-medium">
				登 录
			</button>
		</form>
        <#if error??>
			<p class="text-sm leading-normal text-red-900">${error?html}</p>
        </#if>
	</div>
</div>
</body>
</html>
