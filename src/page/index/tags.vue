<template>
  <div class="tags-container">
    <!-- tag盒子 -->
    <div class="tags-box" ref="tagBox">
      <div class="tags-list" ref="tagsList" @mousewheel="hadelMousewheel" @mouseup="hadelMouseUp" @mousemove="hadelMouse" @mousedown="hadelMousestart" @touchup="hadelMouseUp" @touchmove="hadelMouse" @touchstart="hadelMousestart">
        <div ref="tagsPageOpened" class="tag-item" :class="{'is-active':nowTagValue==item.value}" :name="item.value" @contextmenu.prevent="openMenu(item,$event)" v-for="(item,index) in tagList" :key="index" @click="openUrl(item)">
          <span class="icon-yuan tag-item-icon"></span>
          <span class="tag-text">{{item.label}}</span>
          <i class="el-icon-close tag-close" @click.stop="closeTag(item)" v-if="item.close"></i>
        </div>
      </div>
      <el-dropdown class="tags-menu pull-right">
        <el-button type="primary" size="mini">
          更多
          <i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="closeOthersTags">关闭其他</el-dropdown-item>
          <el-dropdown-item @click.native="closeAllTags">关闭全部</el-dropdown-item>
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
import { resolveUrlPath, setUrlPath } from "@/util/util";
import { mapState, mapGetters } from "vuex";
export default {
  name: "tags",
  data() {
    return {
      visible: false,
      tagBodyLeft: 0,
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
  mounted() {
    this.init();
  },
  watch: {
    $route(to) {
      this.init();
    },
    visible(value) {
      if (value) {
        document.body.addEventListener("click", this.closeMenu);
      } else {
        document.body.removeEventListener("click", this.closeMenu);
      }
    },
    tagBodyLeft(value) {
      this.$refs.tagsList.style.left = value + "px";
    }
  },
  computed: {
    ...mapGetters(["tagWel", "tagList", "isCollapse", "tag"]),
    nowTagValue: function() {
      return setUrlPath(this.$route);
    },
    tagListNum: function() {
      return this.tagList.length != 0;
    }
  },
  methods: {
    init() {
      this.refsTag = this.$refs.tagsPageOpened;
      setTimeout(() => {
        this.refsTag.forEach((item, index) => {
          if (this.tag.value === item.attributes.name.value) {
            let tag = this.refsTag[index];
            this.moveToView(tag);
          }
        });
      }, 1);
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
    hadelMouse(e) {
      const boundarystart = 0,
        boundaryend =
          this.$refs.tagsList.offsetWidth - this.$refs.tagBox.offsetWidth + 100;
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
      distanceX = parseInt(distanceX * 0.8);
      if (distanceX > 0 && this.tagBodyLeft < boundarystart) {
        this.tagBodyLeft = this.tagBodyLeft + distanceX;
        //判断滑动方向——向左滑动
      } else if (distanceX < 0 && this.tagBodyLeft >= -boundaryend) {
        this.tagBodyLeft = this.tagBodyLeft + distanceX;
      }
    },
    hadelMousewheel(e) {
      const step = 0.8 * 90; //一个tag长度
      const boundarystart = 0,
        boundaryend =
          this.$refs.tagsList.offsetWidth - this.$refs.tagBox.offsetWidth + 100;
      // Y>0向左滑动
      if (e.deltaY > 0 && this.tagBodyLeft >= -boundaryend) {
        this.tagBodyLeft = this.tagBodyLeft - step;
        // Y<0向右滑动
      } else if (e.deltaY < 0 && this.tagBodyLeft < boundarystart) {
        this.tagBodyLeft = this.tagBodyLeft + step;
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
      this.$router.push({
        path: resolveUrlPath(this.tagWel.value),
        query: this.tagWel.query
      });
    },
    moveToView(tag) {
      if (tag.offsetLeft < -this.tagBodyLeft) {
        // 标签在可视区域左侧
        this.tagBodyLeft = -tag.offsetLeft + 10;
      } else if (
        tag.offsetLeft + 10 > -this.tagBodyLeft &&
        tag.offsetLeft + tag.offsetWidth <
          -this.tagBodyLeft + this.$refs.tagBox.offsetWidth
      ) {
        // 标签在可视区域
      } else {
        // 标签在可视区域右侧
        this.tagBodyLeft = -(
          tag.offsetLeft -
          (this.$refs.tagBox.offsetWidth - 100 - tag.offsetWidth) +
          20
        );
      }
    },
    openUrl(item) {
      this.$router.push({
        path: resolveUrlPath(item.value, item.label),
        query: item.query
      });
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
        this.openUrl(tag);
      }
    }
  }
};
</script>
<style lang="scss" scoped>

</style>


