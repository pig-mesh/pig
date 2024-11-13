<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Pig OAuth2 授权确认</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-50">
    <!-- 导航栏 -->
    <nav class="bg-white shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between h-16">
                <div class="flex">
                    <div class="flex-shrink-0 flex items-center">
                        <a href="#" class="text-xl font-bold text-gray-800">开放平台</a>
                    </div>
                </div>
                <div class="flex items-center space-x-4">
                    <a href="#" class="text-gray-600 hover:text-gray-900">${loginId}</a>
                    <a href="#" target="_blank" 
                       class="text-gray-600 hover:text-gray-900">技术支持</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- 主要内容 -->
    <div class="min-h-screen flex flex-col items-center pt-16">
        <div class="w-full max-w-md bg-white rounded-lg shadow-md p-8">
            <form id='confirmationForm' name='confirmationForm' 
                  action="${contextPath}/oauth2/doConfirm" method='post'>
                
                <input type="hidden" name="client_id" value="${clientId}">
                <input type="hidden" name="build_redirect_uri" value="true">
                <input type="hidden" name="response_type" value="code">
                <input type="hidden" name="redirect_uri" value="${redirectUri}">

                <h2 class="text-2xl font-semibold text-gray-800 mb-6">应用授权</h2>
                
                <div class="mb-6">
                    <p class="text-gray-700 mb-4">将获得以下权限：</p>
                    <div class="space-y-3">
                        <#list scopes as scope>
                            <div class="flex items-center">
                                <input type="checkbox" checked="checked" name="scope" value="${scope}"
                                       class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                                <label class="ml-3 text-gray-700">${scope}</label>
                            </div>
                        </#list>
                    </div>
                </div>

                <div class="mt-8">
                    <p class="text-sm text-gray-500 mb-6">
                        授权后表明你已同意 
                        <a href="#" class="text-indigo-600 hover:text-indigo-500">服务协议</a>
                    </p>
                    
                    <button type="submit" 
                            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        确认授权
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
