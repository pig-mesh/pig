import { setStore, getStore, removeStore } from '@/util/store'
import { validatenull } from "@/util/validate"
const tagObj = {
    label: '',
    value: '',
    num: '',
    close: true,
}
function setFistTag(list) {
    if (list.length == 1) {
        list[0].close = false;
    } else {
        list.some(a => {
            a.close = true
        })
    }
    return list;

}
const navs = {
    state: {
        tagList: getStore({ name: 'tagList' }) || [],
        tag: getStore({ name: 'tag' }) || tagObj,
        tagWel: {
            label: "首页",
            value: "/wel/index"
        },
        tagCurrent: [{
            label: "首页",
            value: "/wel/index"
        }],
    },
    actions: {

    },
    mutations: {
        ADD_TAG: (state, action) => {
            state.tag = action;
            setStore({ name: 'tag', content: state.tag, type: 'session' })
            if (state.tagList.some(a => a.value === action.value)) return
            state.tagList.push({
                label: action.label,
                value: action.value,
            })
            state.tagList = setFistTag(state.tagList);
            setStore({ name: 'tagList', content: state.tagList })
        },
        SET_TAG_CURRENT: (state, tagCurrent) => {
            state.tagCurrent = tagCurrent;
            setStore({ name: 'tagCurrent', content: state.tagCurrent })
        },
        SET_TAG: (state, value) => {
            for (const [i, v] of state.tagList.entries()) {
                if (v.value === value) {
                    state.tag = state.tagList[i];
                    setStore({ name: 'tag', content: state.tag })
                    break
                }
            }
        },
        DEL_ALL_TAG: (state, action) => {
            state.tag = tagObj;
            state.tagList = [];
            removeStore({ name: 'tag' });
            removeStore({ name: 'tagList' });
        },
        DEL_TAG_OTHER: (state, action) => {
            for (const [i, v] of state.tagList.entries()) {
                if (v.value === state.tag.value) {
                    state.tagList = state.tagList.slice(i, i + 1)
                    state.tag = state.tagList[0];
                    state.tagList[0].close = false;
                    setStore({ name: 'tag', content: state.tag })
                    setStore({ name: 'tagList', content: state.tagList })
                    break
                }
            }

        },
        DEL_TAG: (state, action) => {
            for (const [i, a] of state.tagList.entries()) {
                if (a.value === action.value) {
                    state.tagList.splice(i, 1)
                    state.tagList = setFistTag(state.tagList);
                    setStore({ name: 'tag', content: state.tagList, type: 'session' })
                    setStore({ name: 'tagList', content: state.tagList, type: 'session' })
                    break
                }
            }
        },
    }
}
export default navs