package com.datang.miou.xml;

import java.io.InputStream;
import com.datang.miou.datastructure.AutoTestUnit;
import com.datang.miou.datastructure.TestScript;

public interface TestScriptParser {
	public TestScript parse(InputStream is) throws Exception;
	public String serialize(TestScript testScript) throws Exception;
}
