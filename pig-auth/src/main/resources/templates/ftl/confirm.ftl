<#assign content>
	<div class="mb-7">
		<h3 class="font-semibold text-2xl text-gray-800 dark:text-gray-200 text-center transition-colors">应用授权确认</h3>
		<div class="mt-4 flex items-center justify-center text-sm">
			<div class="px-4 py-2 bg-gray-50 dark:bg-gray-800 rounded-full transition-all">
				<div class="text-gray-700 dark:text-gray-300">
                    <#if principalName=="anonymousUser">
						<span class="text-gray-500 dark:text-gray-400">未登录用户</span>
                    <#else>
						<a href="https://pig4cloud.com" class="text-purple-600 dark:text-purple-400 hover:text-purple-700 dark:hover:text-purple-300 font-medium transition-colors">
                            ${principalName}
						</a>
                    </#if>
				</div>
			</div>
		</div>
	</div>

	<form id='confirmationForm' name='confirmationForm' action="${request.contextPath}/oauth2/authorize" method='post'>
		<input type="hidden" name="client_id" value="${clientId}">
		<input type="hidden" name="state" value="${state}">

		<div class="space-y-6">
			<div class="mb-4">
				<p class="text-gray-700 dark:text-gray-300 mb-3 transition-colors">将获得以下权限：</p>
				<div class="space-y-3 bg-gray-50 dark:bg-gray-800 p-4 rounded-lg border border-gray-200 dark:border-gray-700 transition-all">
                    <#list scopeList as scope>
						<div class="flex items-center">
							<input type="checkbox" checked="checked" name="scope" value="${scope}"
							       class="h-4 w-4 text-purple-600 focus:ring-purple-500 dark:focus:ring-purple-400 border-gray-300 dark:border-gray-600 rounded transition-colors">
							<label class="ml-3 text-gray-600 dark:text-gray-400 transition-colors">${scope}</label>
						</div>
                    </#list>
				</div>
			</div>

			<div class="text-sm text-gray-500 dark:text-gray-400 mb-4 transition-colors">
				授权后表明你已同意
				<a href="#" class="text-purple-600 dark:text-purple-400 hover:text-purple-700 dark:hover:text-purple-300 transition-colors">服务协议</a>
			</div>

			<div>
				<button type="submit" id="write-email-btn"
				        class="w-full flex justify-center bg-purple-600 hover:bg-purple-700 dark:bg-purple-700 dark:hover:bg-purple-600 text-gray-100 p-3 rounded-lg tracking-wide font-semibold cursor-pointer transition-all duration-300 transform hover:scale-[1.02]">
					确认授权
				</button>
			</div>
		</div>
	</form>
</#assign>

<#include "layout/base.ftl">
