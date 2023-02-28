package com.pig4cloud.pigx.common.audit.support;

import lombok.experimental.UtilityClass;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

/**
 * javers 审计工具
 *
 * @author lengleng
 * @date 2023/2/26
 */
@UtilityClass
public class DataAuditor {

	private final Javers javers = JaversBuilder.javers().withListCompareAlgorithm(LEVENSHTEIN_DISTANCE).build();

	public Changes compare(Object newObj, Object oldObj) {
		Diff compare = javers.compare(newObj, oldObj);
		return compare.getChanges();
	}

}
