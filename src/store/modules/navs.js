import { getSessionStore, setSessionStore } from '@/util/yun'
const tagObj = {
    label: '',
    value: '',
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
        tagList: JSON.parse(getSessionStore('tagList')) || [],
        tag: JSON.parse(getSessionStore('tag')) || tagObj,
        tagWel: {
            label: '欢迎页面',
            value: '#/wel',
            close: false,
        }
    },
    actions: {

    },
    mutations: {
        ADD_TAG: (state, action) => {
            state.tag = action;
            setSessionStore('tag', state.tag);
            if (state.tagList.some(a => a.value === action.value)) return
            state.tagList.push({
                label: action.label,
                value: action.value,
            })
            state.tagList = setFistTag(state.tagList);
            setSessionStore('tagList', state.tagList);
        },
        DEL_ALL: (state, action) => {
            state.tag = tagObj;
            state.tagList = [];
            setSessionStore('tag', state.tag);
            setSessionStore('tagList', state.tagList);
        },
        DEL_TAG: (state, action) => {
            for (const [i, a] of state.tagList.entries()) {
                if (a.value === action.value) {
                    state.tagList.splice(i, 1)
                    state.tagList = setFistTag(state.tagList);
                    setSessionStore('tag', state.tag);
                    setSessionStore('tagList', state.tagList);
                    break
                }
            }
        },
    }
}
export default navs