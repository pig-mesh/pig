var screenUtil =(function(){
   function init(){
        var viewport = document.querySelector("meta[name=viewport]");
        var width=window.screen.width;
        var r=width/1200;
        if(r<1)viewport.setAttribute('content', 'width=device-width, initial-scale='+r+', maximum-scale=1, user-scalable=no');
   }
   return {
       init,
   }
})()
screenUtil.init();