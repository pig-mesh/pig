import { setStore, getStore } from '@/util/store'
const errLog = {
    state: {
        errLog: getStore({ name: 'errLog' }) || [],
    },
    mutations: {
        ADD_LOG: (state, log) => {
            state.errLog.push(log)
            setStore({ name: 'errLog', content: state.errLog })
        },
        CLEAR_ALL_LOG: (state, action) => {
            state.errLog = [];
            setStore({ name: 'errLog', content: state.errLog })
        }
    }

};

export default errLog;