<script>
(function() {
    //https://blog.csdn.net/aizou6838/article/details/101664336
    function async_load(){
        var s = document.createElement('script');
        s.type = 'text/javascript';
        s.async = true;
        s.src = "https://hm.baidu.com/hm.js?5819d05c0869771ff6e6a81cdec5b2e8";
        var x = document.getElementsByTagName('script')[0];
        x.parentNode.insertBefore(s, x);
    }
    if (window.attachEvent)
        window.attachEvent('onload', async_load);
    else
        window.addEventListener('load', async_load, false);
})();
</script>