package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.entity.SysMessageRelationEntity;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.OrgTreeVO;
import com.pig4cloud.pigx.admin.api.vo.SysMessageReadVO;
import com.pig4cloud.pigx.admin.api.vo.SysMessageVO;
import com.pig4cloud.pigx.admin.mapper.SysMessageMapper;
import com.pig4cloud.pigx.admin.mapper.SysMessageRelationMapper;
import com.pig4cloud.pigx.admin.mapper.SysUserMapper;
import com.pig4cloud.pigx.admin.service.SysMessageService;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 站内信息
 *
 * @author pig
 * @date 2023-10-25 13:37:25
 */
@Service
@RequiredArgsConstructor
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageEntity>
		implements SysMessageService {

	private final SysMessageRelationMapper relationMapper;

	private final SysUserMapper userMapper;

	/**
	 * 发送信息
	 * @param id id
	 * @return true/false
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean sendMessage(Long id) {
		// 更新信息状态
		SysMessageEntity sysMessage = new SysMessageEntity();
		sysMessage.setSendFlag(YesNoEnum.YES.getCode());
		baseMapper.update(sysMessage, Wrappers.<SysMessageEntity>lambdaQuery().eq(SysMessageEntity::getId, id));
		return Boolean.TRUE;
	}

	/**
	 * 保存消息及其关联关系
	 * @param sysMessage 消息对象
	 * @return 是否保存成功
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveOrUpdateMessage(SysMessageVO sysMessage) {
		SysMessageEntity messageEntity = new SysMessageEntity();
		BeanUtils.copyProperties(sysMessage, messageEntity);

		if (Objects.isNull(sysMessage.getId())) {
			baseMapper.insert(messageEntity);
		}
		else {
			baseMapper.updateById(sysMessage);
		}

		// 删除原有用户
		relationMapper.delete(Wrappers.<SysMessageRelationEntity>lambdaQuery()
			.eq(SysMessageRelationEntity::getMsgId, messageEntity.getId()));

		List<OrgTreeVO> userList = sysMessage.getUserList();
		if (CollUtil.isNotEmpty(userList)) {
			userList.forEach(user -> {
				SysMessageRelationEntity relationEntity = new SysMessageRelationEntity();
				relationEntity.setMsgId(messageEntity.getId());
				relationEntity.setUserId(user.getId());
				relationMapper.insert(relationEntity);
			});
		}
		else {
			userMapper.selectList(Wrappers.emptyWrapper()).stream().map(SysUser::getUserId).forEach(userId -> {
				SysMessageRelationEntity relationEntity = new SysMessageRelationEntity();
				relationEntity.setMsgId(messageEntity.getId());
				relationEntity.setUserId(userId);
				relationMapper.insert(relationEntity);
			});
		}
		return Boolean.TRUE;
	}

	/**
	 * 获取用户的信息列表
	 * @return list
	 */
	@Override
	public IPage<SysMessageVO> pageUserMessage(Page page, SysMessageVO sysMessage) {
		MPJLambdaWrapper<SysMessageEntity> wrapper = new MPJLambdaWrapper<SysMessageEntity>()
			.selectAll(SysMessageEntity.class)
			.select(SysMessageRelationEntity::getReadFlag)
			.leftJoin(SysMessageRelationEntity.class, SysMessageRelationEntity::getMsgId, SysMessageEntity::getId)
			.eq(SysMessageRelationEntity::getUserId, SecurityUtils.getUser().getId())
			.eq(SysMessageEntity::getSendFlag, YesNoEnum.YES.getCode())
			.eq(StrUtil.isNotBlank(sysMessage.getCategory()), SysMessageEntity::getCategory, sysMessage.getCategory())
			.eq(StrUtil.isNotBlank(sysMessage.getReadFlag()), SysMessageRelationEntity::getReadFlag,
					sysMessage.getReadFlag())
			.orderByDesc(SysMessageEntity::getSort)
			.orderByDesc(SysMessageEntity::getCreateTime);
		return baseMapper.selectJoinPage(page, SysMessageVO.class, wrapper);
	}

	/**
	 * 读取信息
	 * @param id id
	 * @return true/false
	 */
	@Override
	public Boolean readMessage(Long id) {
		SysMessageRelationEntity relationEntity = new SysMessageRelationEntity();
		relationEntity.setReadFlag(YesNoEnum.YES.getCode());
		relationMapper.update(relationEntity,
				Wrappers.<SysMessageRelationEntity>lambdaQuery()
					.eq(SysMessageRelationEntity::getMsgId, id)
					.eq(SysMessageRelationEntity::getUserId, SecurityUtils.getUser().getId()));
		return Boolean.TRUE;
	}

	/**
	 * 获取信息
	 * @param id id
	 * @return messageVO
	 */
	@Override
	public SysMessageVO getMessage(Long id) {
		SysMessageEntity messageEntity = baseMapper.selectById(id);
		SysMessageVO SysMessageVO = new SysMessageVO();
		BeanUtils.copyProperties(messageEntity, SysMessageVO);

		// 全体通知
		if (messageEntity.getAllFlag().equals(YesNoEnum.YES.getCode())) {
			return SysMessageVO;
		}
		List<Long> userIdList = relationMapper
			.selectList(Wrappers.<SysMessageRelationEntity>lambdaQuery().eq(SysMessageRelationEntity::getMsgId, id))
			.stream()
			.map(SysMessageRelationEntity::getUserId)
			.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(userIdList)) {
			List<OrgTreeVO> orgNodeUserVoList = userMapper.selectBatchIds(userIdList).stream().map(user -> {
				OrgTreeVO nodeUserVo = new OrgTreeVO();
				nodeUserVo.setId(user.getUserId());
				nodeUserVo.setAvatar(user.getAvatar());
				nodeUserVo.setName(user.getName());
				return nodeUserVo;
			}).collect(Collectors.toList());

			SysMessageVO.setUserList(orgNodeUserVoList);
		}

		return SysMessageVO;
	}

	/**
	 * 查询用户阅读情况
	 * @param page 分页
	 * @param messageId 消息ID
	 * @param name 姓名
	 * @return page
	 */
	@Override
	public Page pageUserRead(Page page, Long messageId, String name) {
		MPJLambdaWrapper<SysMessageRelationEntity> wrapper = new MPJLambdaWrapper<SysMessageRelationEntity>()
			.selectAll(SysMessageRelationEntity.class)
			.select(SysUser::getUsername, SysUser::getName, SysUser::getUserId)
			.select(SysMessageEntity::getTitle)
			.leftJoin(SysUser.class, SysUser::getUserId, SysMessageRelationEntity::getUserId)
			.leftJoin(SysMessageEntity.class, SysMessageEntity::getId, SysMessageRelationEntity::getMsgId)
			.eq(SysMessageRelationEntity::getMsgId, messageId)
			.like(StrUtil.isNotBlank(name), SysUser::getName, name);
		return relationMapper.selectJoinPage(page, SysMessageReadVO.class, wrapper);
	}

}
