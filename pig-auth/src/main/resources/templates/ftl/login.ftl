<#assign content>
    <div class="mb-8 text-center">
        <svg class="w-16 h-16 mx-auto mb-4 text-purple-600 dark:text-purple-400" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 14.5V16.5M7 10.0288C7.47142 10.0288 7.86284 9.63734 7.86284 9.16592C7.86284 8.69449 7.47142 8.30307 7 8.30307C6.52858 8.30307 6.13716 8.69449 6.13716 9.16592C6.13716 9.63734 6.52858 10.0288 7 10.0288ZM17 10.0288C17.4714 10.0288 17.8628 9.63734 17.8628 9.16592C17.8628 8.69449 17.4714 8.30307 17 8.30307C16.5286 8.30307 16.1372 8.69449 16.1372 9.16592C16.1372 9.63734 16.5286 10.0288 17 10.0288ZM12 12.5C13.6569 12.5 15 11.1569 15 9.5C15 7.84315 13.6569 6.5 12 6.5C10.3431 6.5 9 7.84315 9 9.5C9 11.1569 10.3431 12.5 12 12.5Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M12 21C16.9706 21 21 16.9706 21 12C21 7.02944 16.9706 3 12 3C7.02944 3 3 7.02944 3 12C3 16.9706 7.02944 21 12 21Z" stroke="currentColor" stroke-width="1.5"/>
            <path d="M17.6972 19.7C16.0993 18.0307 14.125 17 12 17C9.87499 17 7.90072 18.0307 6.30283 19.7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        <p class="mt-2 text-sm text-gray-600 dark:text-gray-400">安全便捷的企业级认证服务</p>
    </div>

    <form class="form-signin" action="${request.contextPath}/oauth2/form" method="post">
        <input type="hidden" name="client_id" value="pig">
        <input type="hidden" name="grant_type" value="password">
        <div class="space-y-6">
            <div class="">
                <input class="w-full text-sm px-4 py-3 bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:outline-none focus:border-purple-400 dark:focus:border-purple-500 dark:text-gray-300 transition-colors"
                       type="text" placeholder="账号" name="username" required>
            </div>

            <div class="relative">
                <input placeholder="密码" type="password" name="password" required
                       class="w-full text-sm px-4 py-3 bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:outline-none focus:border-purple-400 dark:focus:border-purple-500 dark:text-gray-300 transition-colors">
            </div>

            <#if error??>
                <div class="relative text-center">
                    <span class="text-red-600 dark:text-red-400">${error}</span>
                </div>
            </#if>

            <div>
                <button type="submit"
                        class="w-full flex justify-center bg-purple-600 hover:bg-purple-700 dark:bg-purple-700 dark:hover:bg-purple-600 text-gray-100 p-3 rounded-lg tracking-wide font-semibold cursor-pointer transition-all duration-300 transform hover:scale-[1.02]">
                    登 录
                </button>
            </div>
        </div>
    </form>
</#assign>

<#include "layout/base.ftl">
