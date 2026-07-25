package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.pig4cloud.pig.admin.api.dto.SysAreaSortDTO;
import com.pig4cloud.pig.admin.api.dto.SysAreaUpdateDTO;
import com.pig4cloud.pig.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pig.admin.api.vo.SysAreaChildCountVO;
import com.pig4cloud.pig.admin.api.vo.SysAreaManageVO;
import com.pig4cloud.pig.admin.mapper.SysAreaMapper;
import com.pig4cloud.pig.common.core.util.R;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysAreaServiceImplTests {

	@Test
	void saveAreaUsesSubmittedEntityWithoutExtraValidation() {
		SysAreaServiceImpl service = spy(new SysAreaServiceImpl());
		SysAreaEntity area = area(3L, null, null);
		doReturn(List.of()).when(service).list(any(Wrapper.class));
		doReturn(true).when(service).save(area);
		doReturn(true).when(service).updateBatchById(any(Collection.class));

		R result = service.saveArea(area);

		assertThat(result.isOk()).isTrue();
		assertThat(area.getPid()).isEqualTo(100000L);
		assertThat(area.getAreaSort()).isZero();
		verify(service).save(area);
	}

	@Test
	void listManageChildrenLoadsChildCountsFromMapper() {
		SysAreaServiceImpl service = new SysAreaServiceImpl();
		SysAreaMapper mapper = mock(SysAreaMapper.class);
		ReflectionTestUtils.setField(service, "baseMapper", mapper);
		SysAreaEntity root = area(1L, 100000L, 0L);
		root.setName("全国");
		SysAreaChildCountVO childCount = new SysAreaChildCountVO();
		childCount.setPid(100000L);
		childCount.setChildCount(32L);
		when(mapper.selectList(any(Wrapper.class))).thenReturn(List.of(root));
		when(mapper.selectChildCounts(Set.of(100000L))).thenReturn(List.of(childCount));

		List<SysAreaManageVO> result = service.listManageChildren(0L);

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getChildCount()).isEqualTo(32L);
		assertThat(result.get(0).getHasChildren()).isTrue();
		verify(mapper).selectChildCounts(Set.of(100000L));
	}

	@Test
	void removeAreasRejectsEmptyIds() {
		SysAreaServiceImpl service = new SysAreaServiceImpl();

		R result = service.removeAreas(List.of());

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).contains("不能为空");
	}

	@Test
	void updateAreaRejectsNationwideRoot() {
		SysAreaServiceImpl service = new SysAreaServiceImpl();
		SysAreaMapper mapper = mock(SysAreaMapper.class);
		ReflectionTestUtils.setField(service, "baseMapper", mapper);
		when(mapper.selectById(1L)).thenReturn(area(1L, 100000L, 0L));
		SysAreaUpdateDTO update = new SysAreaUpdateDTO();
		update.setId(1L);
		update.setName("全国");
		update.setAreaStatus("1");
		update.setHot("0");

		R result = service.updateArea(update);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).contains("根节点");
	}

	@Test
	void updateAreaWritesOnlyEditableFields() {
		SysAreaServiceImpl service = new SysAreaServiceImpl();
		SysAreaMapper mapper = mock(SysAreaMapper.class);
		ReflectionTestUtils.setField(service, "baseMapper", mapper);
		when(mapper.selectById(2L)).thenReturn(area(2L, 110000L, 100000L));
		when(mapper.updateById(any(SysAreaEntity.class))).thenReturn(1);
		SysAreaUpdateDTO update = new SysAreaUpdateDTO();
		update.setId(2L);
		update.setName(" 北京市 ");
		update.setAreaStatus("1");
		update.setHot("1");

		R result = service.updateArea(update);

		ArgumentCaptor<SysAreaEntity> captor = ArgumentCaptor.forClass(SysAreaEntity.class);
		verify(mapper).updateById(captor.capture());
		SysAreaEntity saved = captor.getValue();
		assertThat(result.isOk()).isTrue();
		assertThat(saved.getName()).isEqualTo("北京市");
		assertThat(saved.getAreaStatus()).isEqualTo("1");
		assertThat(saved.getHot()).isEqualTo("1");
		assertThat(saved.getPid()).isNull();
		assertThat(saved.getAdcode()).isNull();
		assertThat(saved.getAreaType()).isNull();
		assertThat(saved.getAreaSort()).isNull();
	}

	@Test
	void updateAreaSortRejectsDuplicateIds() {
		SysAreaServiceImpl service = new SysAreaServiceImpl();
		SysAreaSortDTO sortDTO = new SysAreaSortDTO();
		sortDTO.setPid(100000L);
		sortDTO.setAreaIds(List.of(1L, 1L));

		R result = service.updateAreaSort(sortDTO);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).contains("不能重复");
	}

	@Test
	@SuppressWarnings("unchecked")
	void updateAreaSortWritesDescendingValuesInVisualOrder() {
		SysAreaServiceImpl service = spy(new SysAreaServiceImpl());
		SysAreaEntity first = area(1L, 110000L, 100000L);
		SysAreaEntity second = area(2L, 120000L, 100000L);
		doReturn(List.of(first, second)).when(service).list(any(Wrapper.class));
		doReturn(true).when(service).updateBatchById(any(Collection.class));

		SysAreaSortDTO sortDTO = new SysAreaSortDTO();
		sortDTO.setPid(100000L);
		sortDTO.setAreaIds(List.of(2L, 1L));

		R result = service.updateAreaSort(sortDTO);
		ArgumentCaptor<Collection<SysAreaEntity>> updatesCaptor = ArgumentCaptor.forClass(Collection.class);
		verify(service).updateBatchById(updatesCaptor.capture());
		List<SysAreaEntity> updates = updatesCaptor.getValue().stream().toList();

		assertThat(result.isOk()).isTrue();
		assertThat(result.getData()).isEqualTo(true);
		assertThat(updates).extracting(SysAreaEntity::getId).containsExactly(2L, 1L);
		assertThat(updates).extracting(SysAreaEntity::getAreaSort).containsExactly(2L, 1L);
	}

	@Test
	@SuppressWarnings("unchecked")
	void removeAreasRejectsNationwideRoot() {
		SysAreaServiceImpl service = spy(new SysAreaServiceImpl());
		SysAreaEntity root = area(1L, 100000L, 0L);
		doReturn(List.of(root)).when(service).listByIds(any(Collection.class));

		R result = service.removeAreas(List.of(1L));

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).contains("根节点");
	}

	private SysAreaEntity area(Long id, Long adcode, Long pid) {
		SysAreaEntity area = new SysAreaEntity();
		area.setId(id);
		area.setAdcode(adcode);
		area.setPid(pid);
		return area;
	}

}
