package com.pig4cloud.pigx.common.audit.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 表达式处理器
 *
 * @author lengleng
 * @date 2023/2/26
 */
public class SpelParser implements ApplicationContextAware {

	private final static SpelExpressionParser parser = new SpelExpressionParser();

	private static ApplicationContext applicationContext;

	public static Object parser(ProceedingJoinPoint joinPoint, String spel) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		for (int i = 0; i < joinPoint.getArgs().length; i++) {
			String paramName = ((MethodSignature) joinPoint.getSignature()).getParameterNames()[i];
			context.setVariable(paramName, joinPoint.getArgs()[i]);
		}
		return parser.parseExpression(spel).getValue(context);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpelParser.applicationContext = applicationContext;
	}

}
