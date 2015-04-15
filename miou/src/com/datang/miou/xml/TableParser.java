package com.datang.miou.xml;

import java.io.InputStream;
import java.util.List;

import com.datang.miou.views.gen.params.Table;

public interface TableParser {
	public List<Table> parse(InputStream is) throws Exception;
	public String serialize(List<Table> tables) throws Exception;
}
