package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SysPostServiceImplTests {

	@Test
	void reordersSelectedSlotsAndPreservesOtherPositions() {
		StubService service = new StubService();
		service.sort(List.of(4L, 2L));
		assertThat(service.updates).extracting(SysPost::getPostId).containsExactly(1L, 4L, 3L, 2L);
		assertThat(service.updates).extracting(SysPost::getPostSort).containsExactly(1, 2, 3, 4);
	}

	@Test
	void rejectsMissingDuplicateNullAndUnavailableIdsWithoutWriting() {
		StubService service = new StubService();
		assertThatThrownBy(() -> service.sort(null)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> service.sort(List.of())).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> service.sort(List.of(1L, 1L))).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> service.sort(Arrays.asList(1L, null))).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> service.sort(List.of(1L, 99L))).isInstanceOf(IllegalArgumentException.class);
		assertThat(service.updates).isNull();
	}

	@Test
	void failsWhenBatchUpdateDoesNotSucceed() {
		StubService service = new StubService();
		service.updateResult = false;
		assertThatThrownBy(() -> service.sort(List.of(2L, 1L))).isInstanceOf(IllegalStateException.class)
			.hasMessage("排序保存失败");
	}

	private static class StubService extends SysPostServiceImpl {

		private Collection<SysPost> updates;

		private boolean updateResult = true;

		@Override
		public List<SysPost> list(Wrapper<SysPost> queryWrapper) {
			return List.of(1L, 2L, 3L, 4L).stream().map(id -> {
				SysPost post = new SysPost();
				post.setPostId(id);
				post.setPostSort(id < 3 ? 0 : 10);
				return post;
			}).toList();
		}

		@Override
		public boolean updateBatchById(Collection<SysPost> entityList) {
			updates = entityList;
			return updateResult;
		}

	}

}
