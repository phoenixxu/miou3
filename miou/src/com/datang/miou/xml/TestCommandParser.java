package com.datang.miou.xml;

import java.io.InputStream;
import java.util.List;

import com.datang.miou.datastructure.TestCommand;

public interface TestCommandParser {
	public List<TestCommand> parse(InputStream is) throws Exception;
	public String serialize(List<TestCommand> tables) throws Exception;
}