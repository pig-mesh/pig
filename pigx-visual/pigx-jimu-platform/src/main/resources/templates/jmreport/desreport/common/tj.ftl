<script>
(function() {
    //https://blog.csdn.net/aizou6838/article/details/101664336
    function async_load(){
        var s = document.createElement('script');
        s.type = 'text/javascript';
        s.async = true;
        s.src = "https://hm.baidu.com/hm.js?b756a4a8e1c58d613e27d1459c6d6076";
        var x = document.getElementsByTagName('script')[0];
        x.parentNode.insertBefore(s, x);
    }
    if (window.attachEvent)
        window.attachEvent('onload', async_load);
    else
        window.addEventListener('load', async_load, false);
})();
</script>