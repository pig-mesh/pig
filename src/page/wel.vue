<template>
  <div class="pull-chheight wel-contailer">
    <div class="banner-text">
      <h2>{{website.wel.title}}</h2>
      <span :class="['actor',{typeing:isText}]">{{text}}</span>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "wel",
  data() {
    return {
      DATA: [],
      text: "",
      actor: "",
      count: 0,
      isText: false
    };
  },
  computed: {
    ...mapGetters(["website"])
  },
  created() {
    this.DATA = this.website.wel.list;
    this.actor = this.DATA[this.count] || "";
    setTimeout(() => {
      this.isText = true;
      this.setData();
    }, 2000);
  },
  methods: {
    getData() {
      if (this.count < this.DATA.length - 1) {
        this.count++;
      } else {
        this.count = 0;
      }
      this.isText = true;
      this.actor = this.DATA[this.count];
    },
    setData() {
      let num = 0;
      let count = 0;
      let active = false;
      let timeoutstart = 5000;
      let timeoutend = 1000;
      let timespeed = 10;
      setInterval(() => {
        if (this.isText) {
          if (count == this.actor.length) {
            active = true;
          } else {
            active = false;
          }
          if (active) {
            num--;
            this.text = this.actor.substr(0, num);
            if (num == 0) {
              this.isText = false;
              setTimeout(() => {
                count = 0;
                this.getData();
              }, timeoutend);
            }
          } else {
            num++;
            this.text = this.actor.substr(0, num);
            if (num == this.actor.length) {
              this.isText = false;
              setTimeout(() => {
                this.isText = true;
                count = this.actor.length;
              }, timeoutstart);
            }
          }
        }
      }, timespeed);
    }
  }
};
</script>

<style scoped="scoped" lang="scss">
.wel-contailer {
  position: relative;
}
.banner-text {
  position: relative;
  padding: 0 20px;
  font-size: 20px;
  text-align: center;
  color: #333;
}
.banner-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.8;
  display: none;
}
.actor {
  height: 250px;
  overflow: hidden;
  font-size: 18px;
  color: #333;
}

.actor:after {
  content: "";
  width: 3px;
  height: 25px;
  vertical-align: -5px;
  margin-left: 5px;
  background-color: #333;
  display: inline-block;
  animation: blink 0.4s infinite alternate;
}

.typeing:after {
  animation: none;
}

@keyframes blink {
  to {
    opacity: 0;
  }
}
</style>
