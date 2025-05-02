package com.pig4cloud.pigx.flow.task.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NodeStatusEnum {

    WKS(0, "未开始"), JXZ(1, "进行中"), YJS(2, "已结束");

    private final int code;

    private final String name;

}
