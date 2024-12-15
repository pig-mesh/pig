<!doctype html>
<html class="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><#if title??>${title}<#else>Pig 统一身份认证</#if></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            darkMode: 'class',
            theme: {
                extend: {
                    colors: {
                        primary: '#6366f1'
                    }
                }
            }
        }
    </script>
    <style>
        .dark-mode-toggle {
            transition: all 0.3s ease;
        }
        .dark-mode-toggle:hover {
            transform: scale(1.1);
        }
        .animate-spin-slow {
            animation: spin 2s linear infinite;
        }
    </style>
    <#if extraHead??>${extraHead}</#if>
</head>
<body class="min-h-screen">
<!-- 暗黑模式切换按钮 -->
<div class="fixed top-6 right-6 z-50">
    <button id="theme-toggle" class="dark-mode-toggle p-3 rounded-xl bg-purple-100/90 dark:bg-gray-800/90 text-purple-600 dark:text-yellow-300 hover:bg-purple-200 dark:hover:bg-gray-700 focus:outline-none transition-all duration-300 shadow-lg">
        <!-- 月亮图标 -->
        <svg id="moon-icon" class="w-6 h-6 hidden dark:block" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"></path>
        </svg>
        <!-- 太阳图标 -->
        <svg id="sun-icon" class="w-6 h-6 block dark:hidden" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"></path>
        </svg>
    </button>
</div>

<!-- 背景渐变 -->
<div class="bg-purple-900 fixed top-0 left-0 bg-gradient-to-b from-gray-900 via-gray-900 to-purple-800 w-full h-full -z-10">
</div>

<div class="relative min-h-screen flex flex-col md:flex-row items-center justify-center px-4 py-12 md:px-8">
    <!-- 左侧说明文本 -->
    <div class="flex-1 max-w-xl mb-8 md:mb-0 md:mr-12 z-10">
        <div class="hidden md:block">
            <h2 class="text-3xl font-bold text-white mb-6">统一身份认证平台</h2>
            <p class="text-gray-300 text-lg leading-relaxed opacity-90">
                为企业提供一套集中式的账号、权限、认证、审计工具，帮助企业打通身份数据孤岛，实现"一个账号、一次认证、多点通行"的效果，强化企业安全体系的同时，提升组织管理效率，助力企业数字化升级转型。
            </p>
        </div>
    </div>

    <!-- 右侧内容区 -->
    <div class="flex-1 w-full max-w-md z-10">
        <div class="p-8 md:p-10 bg-white dark:bg-gray-900 rounded-2xl shadow-2xl backdrop-blur-sm transition-all duration-300">
            <#if content??>${content}</#if>

            <!-- 版权信息 -->
            <div class="mt-8 pt-6 text-center text-gray-400 dark:text-gray-500 text-sm border-t border-gray-100 dark:border-gray-800">
                <span>
                    Copyright © 2021-2025
                    <a href="#" class="text-purple-600 dark:text-purple-400 hover:text-purple-700 dark:hover:text-purple-300 transition-colors">PIGCLOUD</a>
                </span>
            </div>
        </div>
    </div>
</div>

<!-- 底部波浪装饰 -->
<svg class="fixed bottom-0 left-0 w-full transition-colors -z-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320">
    <path class="fill-white dark:fill-gray-900 transition-colors"
          d="M0,0L40,42.7C80,85,160,171,240,197.3C320,224,400,192,480,154.7C560,117,640,75,720,74.7C800,75,880,117,960,154.7C1040,192,1120,224,1200,213.3C1280,203,1360,149,1400,122.7L1440,96L1440,320L1400,320C1360,320,1280,320,1200,320C1120,320,1040,320,960,320C880,320,800,320,720,320C640,320,560,320,480,320C400,320,320,320,240,320C160,320,80,320,40,320L0,320Z">
    </path>
</svg>

<script>
    // 主题切换功能
    const themeToggle = document.getElementById('theme-toggle');
    const html = document.documentElement;

    // 检查本地存储中的主题设置
    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        html.classList.add('dark');
    } else {
        html.classList.remove('dark');
    }

    // 切换主题
    themeToggle.addEventListener('click', () => {
        html.classList.toggle('dark');

        // 保存主题设置到本地存储
        if (html.classList.contains('dark')) {
            localStorage.theme = 'dark';
        } else {
            localStorage.theme = 'light';
        }

        // 添加点击动画效果
        themeToggle.classList.add('animate-spin-slow');
        setTimeout(() => {
            themeToggle.classList.remove('animate-spin-slow');
        }, 300);
    });
</script>
</body>
</html>
