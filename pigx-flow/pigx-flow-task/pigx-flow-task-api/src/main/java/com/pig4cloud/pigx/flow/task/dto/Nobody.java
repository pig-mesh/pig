package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.List;

@Data
public class Nobody {

	private String handler;

	private List<NodeUser> assignedUser;

}
