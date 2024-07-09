package com.pig4cloud.pigx.flow.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Node {

	private String id;

	private String parentId;

	private String headId;

	private String tailId;

	private String placeHolder;

	private Integer type;

	@JsonProperty(value = "nodeName")
	private String name;

	private Boolean error;

	@JsonProperty("childNode")
	private Node children;

	private String eventConfig;

	private Integer assignedType;

	private Boolean multiple;

	private Integer multipleMode;

	private Integer deptLeaderLevel;

	private String formUserId;

	private String formUserName;

	private List<NodeUser> nodeUserList;

	private List<Node> conditionNodes;

	private Map<String, String> formPerms;

	private Nobody nobody;

	private Boolean groupMode;

	private List<GroupCondition> conditionList;

	private Refuse refuse;

}
