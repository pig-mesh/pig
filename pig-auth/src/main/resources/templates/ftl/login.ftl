<#assign content>
    <div class="mb-7">
        <h3 class="font-semibold text-2xl text-gray-800 text-center">统一身份平台</h3>
    </div>
    
    <form class="form-signin" action="/oauth2/form" method="post">
        <input type="hidden" name="client_id" value="pig">
        <input type="hidden" name="grant_type" value="password">
        <div class="space-y-6">
            <div class="">
                <input class="w-full text-sm px-4 py-3 bg-gray-200 focus:bg-gray-100 border border-gray-200 rounded-lg focus:outline-none focus:border-purple-400"
                       type="text" placeholder="账号" name="username" required>
            </div>

            <div class="relative">
                <input placeholder="密码" type="password" name="password" required
                       class="w-full text-sm px-4 py-3 bg-gray-200 focus:bg-gray-100 border border-gray-200 rounded-lg focus:outline-none focus:border-purple-400">
            </div>

            <#if error??>
                <div class="relative text-center">
                    <span class="text-red-600">${error}</span>
                </div>
            </#if>

            <div>
                <button type="submit"
                        class="w-full flex justify-center bg-purple-800 hover:bg-purple-700 text-gray-100 p-3 rounded-lg tracking-wide font-semibold cursor-pointer transition ease-in duration-500">
                    登 录
                </button>
            </div>
        </div>
    </form>
</#assign>

<#include "layout/base.ftl">
