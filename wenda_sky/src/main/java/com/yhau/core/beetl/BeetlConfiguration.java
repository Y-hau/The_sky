package com.yhau.core.beetl;

import org.apache.commons.lang3.StringUtils;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

	@Override
	public void initOther() {

		groupTemplate.registerFunctionPackage("tool", new StringUtils());

	}

}
