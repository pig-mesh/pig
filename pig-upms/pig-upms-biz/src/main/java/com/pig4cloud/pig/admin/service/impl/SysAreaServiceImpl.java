package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.SysAreaSortDTO;
import com.pig4cloud.pig.admin.api.dto.SysAreaUpdateDTO;
import com.pig4cloud.pig.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pig.admin.api.vo.SysAreaChildCountVO;
import com.pig4cloud.pig.admin.api.vo.SysAreaManageVO;
import com.pig4cloud.pig.admin.mapper.SysAreaMapper;
import com.pig4cloud.pig.admin.service.SysAreaService;
import com.pig4cloud.pig.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pig.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 行政区划
 *
 * @author lbw
 * @date 2024-02-16 22:40:06
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysAreaEntity> implements SysAreaService {

	private static final Long AREA_ROOT_ADCODE = 100000L;

	private static final int SEARCH_RESULT_LIMIT = 50;

	/**
	 * 查询行政区划树
	 * @param sysArea 查询条件
	 * @return 树
	 */
	@Override
	public List<Tree<Long>> selectTree(SysAreaEntity sysArea) {
		// 构建查询条件
		LambdaQueryWrapper<SysAreaEntity> wrapper = Wrappers.lambdaQuery();

		// 添加 areaType 过滤：小于等于指定级别
		wrapper.le(StrUtil.isNotBlank(sysArea.getAreaType()), SysAreaEntity::getAreaType, sysArea.getAreaType());

		// 保留原有条件
		wrapper.eq(SysAreaEntity::getAreaStatus, YesNoEnum.YES.getCode())
			.orderByDesc(SysAreaEntity::getAreaSort)
			.orderByAsc(SysAreaEntity::getAdcode);

		// 执行查询
		List<SysAreaEntity> entityList = baseMapper.selectList(wrapper);

		List<TreeNode<Long>> nodeList = CollUtil.newArrayList();
		for (SysAreaEntity sysAreaEntity : entityList) {
			TreeNode<Long> treeNode = new TreeNode<>(sysAreaEntity.getAdcode(), sysAreaEntity.getPid(),
					sysAreaEntity.getName(), -Optional.ofNullable(sysAreaEntity.getAreaSort()).orElse(0L));

			HashMap<String, Object> extraMap = MapUtil.of(SysAreaEntity.Fields.adcode, sysAreaEntity.getAdcode());
			extraMap.put(SysAreaEntity.Fields.hot, sysAreaEntity.getHot());
			treeNode.setExtra(extraMap);
			nodeList.add(treeNode);
		}

		return TreeUtil.build(nodeList, Optional.ofNullable(sysArea.getPid()).orElse(100000L));
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysArea 行政区划
	 * @return Page
	 */
	@Override
	public Page selectPage(Page page, SysAreaEntity sysArea) {
		LambdaQueryWrapper<SysAreaEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysArea.getName()), SysAreaEntity::getName, sysArea.getName());
		wrapper.eq(Objects.nonNull(sysArea.getAdcode()), SysAreaEntity::getPid, sysArea.getAdcode());
		wrapper.orderByAsc(SysAreaEntity::getAreaType);
		wrapper.orderByDesc(SysAreaEntity::getAreaSort);
		wrapper.orderByAsc(SysAreaEntity::getAdcode);
		return baseMapper.selectPage(page, wrapper);
	}

	@Override
	public List<SysAreaManageVO> listManageChildren(Long pid) {
		Long parentCode = Optional.ofNullable(pid).orElse(0L);
		List<SysAreaEntity> children = list(Wrappers.<SysAreaEntity>lambdaQuery()
			.eq(SysAreaEntity::getPid, parentCode)
			.orderByDesc(SysAreaEntity::getAreaSort)
			.orderByAsc(SysAreaEntity::getAdcode));
		return toManageList(children);
	}

	@Override
	public List<SysAreaManageVO> searchManage(String keyword) {
		String normalizedKeyword = StrUtil.trim(keyword);
		if (StrUtil.isBlank(normalizedKeyword)) {
			return List.of();
		}

		LambdaQueryWrapper<SysAreaEntity> wrapper = Wrappers.lambdaQuery();
		Long adcode = parseAdcode(normalizedKeyword);
		if (Objects.nonNull(adcode)) {
			wrapper.and(query -> query.like(SysAreaEntity::getName, normalizedKeyword)
				.or()
				.eq(SysAreaEntity::getAdcode, adcode));
		}
		else {
			wrapper.like(SysAreaEntity::getName, normalizedKeyword);
		}
		wrapper.orderByAsc(SysAreaEntity::getAreaType)
			.orderByDesc(SysAreaEntity::getAreaSort)
			.orderByAsc(SysAreaEntity::getAdcode);

		Page<SysAreaEntity> page = new Page<>(1, SEARCH_RESULT_LIMIT, false);
		return toManageList(baseMapper.selectPage(page, wrapper).getRecords());
	}

	@Override
	public boolean existsByAdcode(Long adcode) {
		return Objects.nonNull(adcode)
				&& count(Wrappers.<SysAreaEntity>lambdaQuery().eq(SysAreaEntity::getAdcode, adcode)) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R updateAreaSort(SysAreaSortDTO sortDTO) {
		if (Objects.equals(sortDTO.getPid(), 0L)) {
			return R.failed("全国根节点不支持排序");
		}

		List<Long> areaIds = sortDTO.getAreaIds();
		Set<Long> uniqueIds = new HashSet<>(areaIds);
		if (uniqueIds.size() != areaIds.size()) {
			return R.failed("行政区划排序ID不能重复");
		}

		List<SysAreaEntity> siblings = list(Wrappers.<SysAreaEntity>lambdaQuery()
			.eq(SysAreaEntity::getPid, sortDTO.getPid()));
		Set<Long> siblingIds = siblings.stream().map(SysAreaEntity::getId).collect(Collectors.toSet());
		if (siblings.size() != areaIds.size() || !siblingIds.equals(uniqueIds)) {
			return R.failed("行政区划列表已变化，请刷新后重试");
		}

		Map<Long, SysAreaEntity> siblingMap = siblings.stream()
			.collect(Collectors.toMap(SysAreaEntity::getId, Function.identity()));
		if (!updateBatchById(buildSortUpdates(areaIds, siblingMap))) {
			throw new IllegalStateException("行政区划排序保存失败");
		}
		return R.ok(true);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveArea(SysAreaEntity sysArea) {
		Long pid = Optional.ofNullable(sysArea.getPid()).orElse(AREA_ROOT_ADCODE);
		sysArea.setPid(pid);

		List<SysAreaEntity> siblings = list(Wrappers.<SysAreaEntity>lambdaQuery()
			.eq(SysAreaEntity::getPid, pid)
			.orderByDesc(SysAreaEntity::getAreaSort)
			.orderByAsc(SysAreaEntity::getAdcode));
		sysArea.setAreaSort(0L);
		if (!save(sysArea)) {
			return R.ok(false);
		}

		List<Long> orderedIds = siblings.stream()
			.map(SysAreaEntity::getId)
			.collect(Collectors.toCollection(ArrayList::new));
		orderedIds.add(sysArea.getId());
		Map<Long, SysAreaEntity> areaMap = siblings.stream()
			.collect(Collectors.toMap(SysAreaEntity::getId, Function.identity()));
		areaMap.put(sysArea.getId(), sysArea);
		if (!updateBatchById(buildSortUpdates(orderedIds, areaMap))) {
			throw new IllegalStateException("行政区划新增排序初始化失败");
		}
		return R.ok(true);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R updateArea(SysAreaUpdateDTO updateDTO) {
		SysAreaEntity current = getById(updateDTO.getId());
		if (Objects.isNull(current)) {
			return R.failed("行政区划不存在或已删除");
		}
		if (Objects.equals(current.getAdcode(), AREA_ROOT_ADCODE)) {
			return R.failed("全国根节点不允许修改");
		}

		SysAreaEntity update = new SysAreaEntity();
		update.setId(updateDTO.getId());
		update.setName(StrUtil.trim(updateDTO.getName()));
		update.setAreaStatus(updateDTO.getAreaStatus());
		update.setHot(updateDTO.getHot());
		return updateById(update) ? R.ok(true) : R.failed("行政区划更新失败");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R removeAreas(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return R.failed("行政区划ID不能为空");
		}

		Set<Long> uniqueIds = new HashSet<>(ids);
		if (uniqueIds.size() != ids.size()) {
			return R.failed("行政区划ID不能重复");
		}

		List<SysAreaEntity> areas = listByIds(uniqueIds);
		if (areas.size() != uniqueIds.size()) {
			return R.failed("行政区划不存在或已删除");
		}
		if (areas.stream().anyMatch(area -> Objects.equals(area.getAdcode(), AREA_ROOT_ADCODE))) {
			return R.failed("全国根节点不允许删除");
		}

		Set<Long> adcodes = areas.stream().map(SysAreaEntity::getAdcode).collect(Collectors.toSet());
		long childCount = count(Wrappers.<SysAreaEntity>lambdaQuery().in(SysAreaEntity::getPid, adcodes));
		if (childCount > 0) {
			return R.failed("所选行政区划包含下级节点，请先处理下级行政区划");
		}
		if (!removeByIds(uniqueIds)) {
			throw new IllegalStateException("行政区划删除失败");
		}
		return R.ok(true);
	}

	private List<SysAreaManageVO> toManageList(List<SysAreaEntity> areas) {
		if (CollUtil.isEmpty(areas)) {
			return List.of();
		}

		Set<Long> parentCodes = areas.stream().map(SysAreaEntity::getAdcode).collect(Collectors.toSet());
		Map<Long, Long> childCounts = baseMapper.selectChildCounts(parentCodes)
			.stream()
			.collect(Collectors.toMap(SysAreaChildCountVO::getPid, SysAreaChildCountVO::getChildCount));
		Map<Long, SysAreaEntity> areaMap = loadAncestorMap(areas);

		return areas.stream()
			.map(area -> toManageVO(area, childCounts.getOrDefault(area.getAdcode(), 0L), areaMap))
			.toList();
	}

	private Map<Long, SysAreaEntity> loadAncestorMap(List<SysAreaEntity> areas) {
		Map<Long, SysAreaEntity> areaMap = areas.stream()
			.collect(Collectors.toMap(SysAreaEntity::getAdcode, Function.identity(), (left, right) -> left));
		Set<Long> pendingCodes = areas.stream()
			.map(SysAreaEntity::getPid)
			.filter(code -> Objects.nonNull(code) && code > 0 && !areaMap.containsKey(code))
			.collect(Collectors.toSet());

		while (CollUtil.isNotEmpty(pendingCodes)) {
			List<SysAreaEntity> parents = list(Wrappers.<SysAreaEntity>lambdaQuery()
				.in(SysAreaEntity::getAdcode, pendingCodes));
			if (CollUtil.isEmpty(parents)) {
				break;
			}
			parents.forEach(parent -> areaMap.put(parent.getAdcode(), parent));
			pendingCodes = parents.stream()
				.map(SysAreaEntity::getPid)
				.filter(code -> Objects.nonNull(code) && code > 0 && !areaMap.containsKey(code))
				.collect(Collectors.toSet());
		}
		return areaMap;
	}

	private SysAreaManageVO toManageVO(SysAreaEntity area, long childCount, Map<Long, SysAreaEntity> areaMap) {
		SysAreaManageVO vo = new SysAreaManageVO();
		vo.setId(area.getId());
		vo.setPid(area.getPid());
		vo.setAdcode(area.getAdcode());
		vo.setName(area.getName());
		vo.setAreaType(area.getAreaType());
		vo.setHot(area.getHot());
		vo.setAreaStatus(area.getAreaStatus());
		vo.setAreaSort(area.getAreaSort());
		vo.setHasChildren(childCount > 0);
		vo.setChildCount(childCount);

		List<SysAreaEntity> path = buildPath(area, areaMap);
		vo.setPathCodes(path.stream().map(SysAreaEntity::getAdcode).toList());
		vo.setPathName(path.stream()
			.map(SysAreaEntity::getName)
			.collect(Collectors.joining(StrUtil.SPACE + "/" + StrUtil.SPACE)));
		return vo;
	}

	private List<SysAreaEntity> buildPath(SysAreaEntity area, Map<Long, SysAreaEntity> areaMap) {
		LinkedList<SysAreaEntity> path = new LinkedList<>();
		Set<Long> visitedCodes = new HashSet<>();
		SysAreaEntity current = area;
		while (Objects.nonNull(current) && visitedCodes.add(current.getAdcode())) {
			path.addFirst(current);
			current = areaMap.get(current.getPid());
		}
		return path;
	}

	private List<SysAreaEntity> buildSortUpdates(List<Long> orderedIds, Map<Long, SysAreaEntity> areaMap) {
		List<SysAreaEntity> updates = new ArrayList<>(orderedIds.size());
		for (int index = 0; index < orderedIds.size(); index++) {
			SysAreaEntity original = areaMap.get(orderedIds.get(index));
			SysAreaEntity update = new SysAreaEntity();
			update.setId(original.getId());
			update.setAreaSort((long) orderedIds.size() - index);
			updates.add(update);
		}
		return updates;
	}

	private Long parseAdcode(String keyword) {
		try {
			return Long.valueOf(keyword);
		}
		catch (NumberFormatException ignored) {
			return null;
		}
	}

}
