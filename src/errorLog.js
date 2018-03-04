import Vue from 'vue';
import store from './store'
Vue.config.errorHandler = function (err, vm, info, a) {

    Vue.nextTick(() => {
        store.commit('ADD_LOG', {
            message: err.message,
            stack: err.stack,
            info: info,
            url: window.location.href
        })
    })
}
