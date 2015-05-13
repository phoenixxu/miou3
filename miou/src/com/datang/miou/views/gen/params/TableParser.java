package com.datang.miou.views.gen.params;

import java.io.InputStream;
import java.util.List;

public interface TableParser {
	public List<Table> parse(InputStream is) throws Exception;
	public String serialize(List<Table> tables) throws Exception;
}
