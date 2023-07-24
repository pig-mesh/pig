package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.constant.FormTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.task.mapper.ProcessCopyMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessCopyService;
import com.pig4cloud.pigx.flow.task.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.task.service.IProcessService;
import com.pig4cloud.pigx.flow.task.vo.FormItemVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程抄送数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@Service
@RequiredArgsConstructor
public class ProcessCopyServiceImpl extends ServiceImpl<ProcessCopyMapper, ProcessCopy> implements IProcessCopyService {

	private final ObjectMapper objectMapper;

	private final IProcessService processService;

	private final IProcessNodeDataService nodeDataService;

	/**
	 * 查询单个抄送详细信息
	 * @param id
	 * @return
	 */
	@SneakyThrows
	@Override
	public R querySingleDetail(long id) {
		ProcessCopy processCopy = this.getById(id);
		String flowId = processCopy.getFlowId();
		Process oaForms = processService.getByFlowId(flowId);
		if (oaForms == null) {
			return R.failed("流程不存在");
		}
		String formData = processCopy.getFormData();

		Map<String, Object> variableMap = objectMapper.readValue(formData, new TypeReference<>() {
		});

		String nodeId = processCopy.getNodeId();

		Node node = nodeDataService.getNodeData(flowId, nodeId).getData();
		Map<String, String> formPerms = node.getFormPerms();

		List<FormItemVO> jsonObjectList = objectMapper.readValue(oaForms.getFormItems(), new TypeReference<>() {
		});
		for (FormItemVO formItemVO : jsonObjectList) {
			String fid = formItemVO.getId();
			String perm = formPerms.get(fid);
			formItemVO.setPerm(StrUtil.isBlankIfStr(perm) ? ProcessInstanceConstant.FormPermClass.HIDE : perm);

			if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
				// 明细

				List<Map<String, Object>> subParamList = MapUtil.get(variableMap, fid,
						new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
						});

				Object value = formItemVO.getProps().getValue();

				List<List<FormItemVO>> l = new ArrayList<>();
				for (Map<String, Object> map : subParamList) {
					List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
					for (FormItemVO itemVO : subItemList) {
						itemVO.getProps().setValue(map.get(itemVO.getId()));

						String permSub = formPerms.get(itemVO.getId());
						if (StrUtil.isNotBlank(permSub)) {
							itemVO.setPerm(ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub)
									? ProcessInstanceConstant.FormPermClass.READ : permSub

							);

						}
						else {
							itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
						}

					}
					l.add(subItemList);
				}
				formItemVO.getProps().setValue(l);

			}
			else {
				formItemVO.getProps().setValue(variableMap.get(fid));

			}

		}
		Dict set = Dict.create().set("formItems", jsonObjectList);

		return R.ok(set);
	}

}
