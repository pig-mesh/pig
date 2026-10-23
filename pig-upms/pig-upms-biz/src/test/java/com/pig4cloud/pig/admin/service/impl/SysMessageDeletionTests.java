package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.pig4cloud.pig.admin.api.entity.SysMessageRelationEntity;
import com.pig4cloud.pig.admin.mapper.SysMessageRelationMapper;
import com.pig4cloud.pig.common.security.service.PigUser;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SysMessageDeletionTests {

	@Test
	void deletesOnlyTheCurrentUsersReceiptAndAllowsRepeatedDeletion() {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), "relation"),
				SysMessageRelationEntity.class);
		SysMessageRelationMapper relations = mock(SysMessageRelationMapper.class);
		SysMessageServiceImpl service = new SysMessageServiceImpl(null, null, relations, null, null, null, null);
		PigUser user = mock(PigUser.class);
		when(user.getId()).thenReturn(1001L);
		try {
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
			assertThat(service.removeUserMessage(123L)).isTrue();
			assertThat(service.removeUserMessage(123L)).isTrue();
		}
		finally {
			SecurityContextHolder.clearContext();
		}
		ArgumentCaptor<LambdaQueryWrapper<SysMessageRelationEntity>> condition = ArgumentCaptor
			.forClass(LambdaQueryWrapper.class);
		verify(relations, times(2)).delete(condition.capture());
		for (LambdaQueryWrapper<SysMessageRelationEntity> query : condition.getAllValues()) {
			assertThat(query.getSqlSegment()).contains("msg_id =", "user_id =");
			assertThat(query.getParamNameValuePairs().values()).containsExactlyInAnyOrder(123L, 1001L);
		}
		verifyNoMoreInteractions(relations);
	}

}
