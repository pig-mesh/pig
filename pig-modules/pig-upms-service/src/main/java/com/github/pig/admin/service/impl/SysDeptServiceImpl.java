package com.github.pig.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.common.util.TreeUtil;
import com.github.pig.admin.mapper.SysDeptMapper;
import com.github.pig.admin.mapper.SysDeptRelationMapper;
import com.github.pig.admin.model.dto.DeptTree;
import com.github.pig.admin.model.entity.SysDept;
import com.github.pig.admin.model.entity.SysDeptRelation;
import com.github.pig.admin.service.SysDeptService;
import com.github.pig.common.constant.CommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysDeptRelationMapper sysDeptRelationMapper;

    /**
     * 添加信息部门
     *
     * @param dept 部门
     * @return
     */
    @Override
    public Boolean insertDept(SysDept dept) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        this.insert(sysDept);
        this.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 维护部门关系
     * @param sysDept 部门
     */
    private void insertDeptRelation(SysDept sysDept) {
        //增加部门关系表
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setDescendant(sysDept.getParentId());
        List<SysDeptRelation> deptRelationList = sysDeptRelationMapper.selectList(new EntityWrapper<>(deptRelation));
        for (SysDeptRelation sysDeptRelation : deptRelationList) {
            sysDeptRelation.setDescendant(sysDept.getDeptId());
            sysDeptRelationMapper.insert(sysDeptRelation);
        }
        //自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        sysDeptRelationMapper.insert(own);
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    public Boolean deleteDeptById(Integer id) {
        SysDept sysDept = new SysDept();
        sysDept.setDeptId(id);
        sysDept.setUpdateTime(new Date());
        sysDept.setDelFlag(CommonConstant.STATUS_DEL);
        this.deleteById(sysDept);
        sysDeptMapper.deleteDeptRealtion(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    public Boolean updateDeptById(SysDept sysDept) {
        //更新部门状态
        this.updateById(sysDept);
        //删除部门关系
        sysDeptMapper.deleteDeptRealtion(sysDept.getDeptId());
        //新建部门关系
        this.insertDeptRelation(sysDept);
        return null;
    }

    /**
     * 查询部门树
     *
     * @param sysDeptEntityWrapper
     * @return 树
     */
    @Override
    public List<DeptTree> selectListTree(EntityWrapper<SysDept> sysDeptEntityWrapper) {
        return getDeptTree(this.selectList(sysDeptEntityWrapper), 0);
    }

    /**
     * 查询部门列表
     *
     * @param sysDeptEntityWrapper sysDeptEntityWrapper
     * @return 部门列表
     */
    private List<SysDept> selectDeptDtoList(EntityWrapper<SysDept> sysDeptEntityWrapper) {
        return sysDeptMapper.selectDeptDtoList(sysDeptEntityWrapper.getEntity().getDelFlag());
    }


    /**
     * 构建部门树
     *
     * @param depts 部门
     * @param root  根节点
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts, int root) {
        List<DeptTree> trees = new ArrayList<>();
        DeptTree node;
        for (SysDept dept : depts) {
            if (dept.getParentId().equals(dept.getDeptId())) {
                continue;
            }
            node = new DeptTree();
            node.setId(dept.getDeptId());
            node.setParentId(dept.getParentId());
            node.setName(dept.getName());
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }
}
