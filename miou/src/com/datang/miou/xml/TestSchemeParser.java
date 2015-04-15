package com.datang.miou.xml;

import java.io.InputStream;
import java.util.List;

import com.datang.miou.datastructure.TestScheme;
import com.datang.miou.views.gen.params.Table;

public interface TestSchemeParser {
	public List<TestScheme> parse(InputStream is) throws Exception;
	public String serialize(List<TestScheme> schemes) throws Exception;
}
