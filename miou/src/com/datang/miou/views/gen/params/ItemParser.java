package com.datang.miou.views.gen.params;

import java.io.InputStream;
import java.util.List;

public interface ItemParser {
	public List<ParamTableItem> parse(InputStream is) throws Exception;
	public String serialize(List<ParamTableItem> items) throws Exception;
}