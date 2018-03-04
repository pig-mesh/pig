
const getters = {
    tag: state => state.tags.tag,
    isCollapse: state => state.common.isCollapse,
    isLock: state => state.common.isLock,
    isFullScren: state => state.common.isFullScren,
    tagList: state => state.tags.tagList,
    tagCurrent: state => state.tags.tagCurrent,
    tagWel: state => state.tags.tagWel,
    token: state => state.user.token,
    roles: state => state.user.roles,
    permission: state => state.user.permission,
    menu: state => state.user.menu,
    menuAll: state => state.user.menuAll,
    errLog: state => state.errLog.errLog
}
export default getters
