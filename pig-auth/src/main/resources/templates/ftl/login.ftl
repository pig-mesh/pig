<!doctype html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#-- 内网部署下载相对应放文件放在 resource目录离线加载-->
	<script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
<!-- component -->
<div
		class="bg-purple-900 absolute top-0 left-0 bg-gradient-to-b from-gray-900 via-gray-900 to-purple-800 bottom-0 leading-5 h-full w-full overflow-hidden">

</div>
<div
		class="relative   min-h-screen  sm:flex sm:flex-row  justify-center bg-transparent rounded-3xl shadow-xl">
	<div class="flex-col flex  self-center lg:px-14 sm:max-w-4xl xl:max-w-md  z-10">
		<div class="self-start hidden lg:flex flex-col  text-gray-300">
			<p class="pr-3 text-sm opacity-75">
				为企业提供一套集中式的账号、权限、认证、审计工具，帮助企业打通身份数据孤岛，实现“一个账号、一次认证、多点通行”的效果，强化企业安全体系的同时，提升组织管理效率，助力企业数字化升级转型。</p>
		</div>
	</div>
	<div class="flex justify-center self-center  z-10">
		<div class="p-12 bg-white mx-auto rounded-3xl w-96 ">
			<div class="mb-7">
				<h3 class="font-semibold text-2xl text-gray-800 text-center">统一身份平台</h3>
			</div>
			<form class="form-signin" action="/token/form" method="post">
				<input type="hidden" name="client_id" class="form-control" value="pig" placeholder="所属客户端">
				<input type="hidden" name="grant_type" class="form-control" value="password" placeholder="所属客户端">
				<div class="space-y-6">
					<div class="">
						<input class=" w-full text-sm  px-4 py-3 bg-gray-200 focus:bg-gray-100 border  border-gray-200 rounded-lg focus:outline-none focus:border-purple-400"
						       type="text" placeholder="账号" name="username" required>
					</div>


					<div class="relative">
						<input placeholder="密码" type="password" name="password" required
						       class=" w-full text-sm  px-4 py-3 bg-gray-200 focus:bg-gray-100 border  border-gray-200 rounded-lg focus:outline-none focus:border-purple-400">
					</div>

                    <#if error??>
						<div class="relative text-center">
							<span class="text-red-600">${error}</span>
						</div>
                    </#if>

					<div>
						<button type="submit"
						        class="w-full flex justify-center bg-purple-800  hover:bg-purple-700 text-gray-100 p-3  rounded-lg tracking-wide font-semibold  cursor-pointer transition ease-in duration-500">
							登 录
						</button>
					</div>
				</div>
			</form>
			<div class="mt-7 text-center text-gray-300 text-xs">
					<span>
                Copyright © 2021-2023
                <a href="#" rel="" target="_blank" title="Codepen aji"
                   class="text-purple-500 hover:text-purple-600 ">PIGCLOUD</a></span>
			</div>
		</div>
	</div>
</div>
</div>
<svg class="absolute bottom-0 left-0 " xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320">
	<path fill="#fff" fill-opacity="1"
	      d="M0,0L40,42.7C80,85,160,171,240,197.3C320,224,400,192,480,154.7C560,117,640,75,720,74.7C800,75,880,117,960,154.7C1040,192,1120,224,1200,213.3C1280,203,1360,149,1400,122.7L1440,96L1440,320L1400,320C1360,320,1280,320,1200,320C1120,320,1040,320,960,320C880,320,800,320,720,320C640,320,560,320,480,320C400,320,320,320,240,320C160,320,80,320,40,320L0,320Z"></path>
</svg>
</body>
</html>
