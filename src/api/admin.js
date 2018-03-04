import { userTableData, roleTableData } from '@/mock/admin'
import { DIC } from '@/const/dic'
export const getUserData = (page) => {
    return new Promise((resolve, reject) => {
        resolve({ data: userTableData });
    })
}

export const getRoleData = (page) => {
    return new Promise((resolve, reject) => {
        resolve({ data: roleTableData });
    })
}

export const getDic = (type) => {
    return new Promise((resolve, reject) => {
        resolve({ data: DIC[type] });
    })
}