import { getUserData, getRoleData } from '@/api/admin'
const user = {
    state: {

    },
    actions: {
        GetUserData({ commit, state, dispatch }, page) {
            return new Promise((resolve, reject) => {
                getUserData(page).then(res => {
                    const data = res.data;
                    resolve(data);
                })
            })
        },
        GetRoleData({ commit, state, dispatch }, page) {
            return new Promise((resolve, reject) => {
                getRoleData(page).then(res => {
                    const data = res.data;
                    resolve(data);
                })
            })
        },

    },
    mutations: {

    }

}
export default user