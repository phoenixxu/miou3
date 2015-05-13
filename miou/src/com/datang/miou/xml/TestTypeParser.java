package com.datang.miou.xml;

import java.io.InputStream;
import java.util.List;

import com.datang.miou.datastructure.TestType;

public interface TestTypeParser {
	public List<TestType> parse(InputStream is) throws Exception;
	public String serialize(List<TestType> tables) throws Exception;
}
