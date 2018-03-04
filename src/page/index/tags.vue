<template>
  <div class="tags-container">
      <div class="tags-breadcrumb">
           <i class="icon-navicon tag-collapse" :class="[{ 'tag-collapse_right': isCollapse }]" @click="showCollapse"></i>
           <Breadcrumb  class="tags-breadcrumb-list"></Breadcrumb>
      </div>
       <div class="tags-box">
         
          <div class="tags-list" ref="tagsList" >
            <div  class="tag-scroll" @mousewheel="hadelMousewheel" 
             @mouseup="hadelMouseUp" 
             @mousemove="hadelMousewheel" 
             @mousedown="hadelMousestart" 
             @touchup="hadelMouseUp" 
             @touchmove="hadelMousewheel" 
             @touchstart="hadelMousestart">
              <div class="tag-item" @contextmenu.prevent="openMenu(item,$event)" v-for="(item,index) in tagList" :key="index" @click="openUrl(item.value,item.label,item.num)">
                <span class="icon-yuan tag-item-icon" :class="{'is-active':nowTagValue==item.value}"></span> 
                <span class="tag-text">{{item.label}}</span> 
                <i class="el-icon-close tag-close" @click.stop="closeTag(item)"  v-if="item.close"></i>
              </div>
            </div>
           
          </div>
           <el-dropdown class="tags-menu pull-right">
            <el-button type="primary" size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item  @click.native="closeOthersTags">关闭其他</el-dropdown-item>
              <el-dropdown-item  @click.native="closeAllTags">关闭全部</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>       
        <!-- <ul class='contextmenu' v-show="visible" :style="{left:left+'px',top:top+'px'}">
          <li @click="closeTag(selectedTag)">关闭</li>
          <li @click="closeOthersTags">关闭其他</li>
          <li @click="closeAllTags">关闭全部</li>
        </ul> -->
    </div>
</template>
<script>
import { resolveUrlPath } from "@/util/util";
import { mapState, mapGetters } from "vuex";
import Breadcrumb from "./breadcrumb";
export default {
  name: "tags",
  components: {
    Breadcrumb
  },
  data() {
    return {
      visible: false,
      lock: false,
      startX: 0,
      startY: 0,
      endX: 0,
      endY: 0,
      top: 0,
      left: 0,
      selectedTag: {}
    };
  },
  created() {},
  mounted() {},
  watch: {
    visible(value) {
      if (value) {
        document.body.addEventListener("click", this.closeMenu);
      } else {
        document.body.removeEventListener("click", this.closeMenu);
      }
    }
  },
  computed: {
    ...mapGetters(["tagWel", "tagList", "isCollapse", "tag"]),
    nowTagValue: function() {
      const value = this.$route.query.src
        ? this.$route.query.src
        : this.$route.path;
      this.$store.commit("SET_TAG", value);
      return value;
    },
    tagListNum: function() {
      return this.tagList.length != 0;
    }
  },
  methods: {
    showCollapse() {
      this.$store.commit("SET_COLLAPSE");
    },
    hadelMouseUp(e) {
      this.lock = false;
    },
    hadelMousestart(e) {
      this.lock = true;
      if (e.clientX && e.clientY) {
        this.startX = e.clientX;
        this.startY = e.clientY;
      } else {
        this.startX = e.changedTouches[0].pageX;
        this.startY = e.changedTouches[0].pageY;
      }
    },
    hadelMousewheel(e) {
      const left = Number(this.$refs.tagsList.style.left.replace("px", ""));
      const step = 100; //一个tag长度
      const len = 10; //tag的个数
      const boundarystart = 0,
        boundaryend = -(this.tagList.length - len) * step;
      //鼠标滑轮滚动
      if (e.deltaY) {
        this.endY = e.deltaY;
        if (this.endY > 0 && left > boundaryend) {
          this.$refs.tagsList.style.left = left - step + "px";
        } else if (this.endY < 0 && left < boundarystart) {
          this.$refs.tagsList.style.left = left + step + "px";
        }
        return;
      }
      if (!this.lock) {
        return;
      }
      //鼠标滑动
      if (e.clientX && e.clientY) {
        this.endX = e.clientX;
        this.endY = e.clientY;
        //触摸屏滑动
      } else {
        //获取滑动屏幕时的X,Y
        this.endX = e.changedTouches[0].pageX;
        this.endY = e.changedTouches[0].pageY;
      }
      //获取滑动距离
      let distanceX = this.endX - this.startX;
      let distanceY = this.endY - this.startY;
      //判断滑动方向——向右滑动
      distanceX = distanceX * 0.08;
      if (distanceX > 0 && left <= boundarystart) {
        this.$refs.tagsList.style.left = left + distanceX + "px";
        //判断滑动方向——向左滑动
      } else if (distanceX < 0 && left >= boundaryend) {
        this.$refs.tagsList.style.left = left + distanceX + "px";
      }
    },
    openMenu(tag, e) {
      if (this.tagList.length == 1) {
        return;
      }
      this.visible = true;
      this.selectedTag = tag;
      this.left = e.clientX;
      this.top = e.clientY;
    },
    closeOthersTags() {
      this.$store.commit("DEL_TAG_OTHER");
    },
    closeMenu() {
      this.visible = false;
    },
    closeAllTags() {
      this.$store.commit("DEL_ALL_TAG");
      this.$store.commit("ADD_TAG", this.tagWel);
      this.$router.push({ path: resolveUrlPath(this.tagWel.value) });
    },
    openUrl(url, name, num) {
      this.$store.commit("ADD_TAG", {
        label: name,
        value: url,
        num: num
      });
      this.$router.push({ path: resolveUrlPath(url) });
    },
    eachTag(tag) {
      for (var key in this.tagList) {
        if (this.tagList[key].value == tag.value) {
          return key;
        }
      }
      return -1;
    },
    closeTag(item) {
      const key = this.eachTag(item);
      let tag;
      this.$store.commit("DEL_TAG", item);
      if (item.value == this.tag.value) {
        tag = this.tagList[key == 0 ? key : key - 1];
        this.openUrl(tag.value, tag.label, tag.num);
      }
    }
  }
};
</script>
<style lang="scss" scoped>

</style>


