package com.datang.miou.views.gen.params;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxItemParser implements ItemParser {

	@Override
	public List<ParamTableItem> parse(InputStream is) throws Exception {
		// TODO 自动生成的方法存根
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParserHandler handler = new ItemParserHandler();
		parser.parse(is, handler);
		return handler.getItems();
	}

	@Override
	public String serialize(List<ParamTableItem> items) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}
	
	private class ItemParserHandler extends DefaultHandler {
		private List<ParamTableItem> items;
		private ParamTableItem item;
		private StringBuilder builder;
		
		public List<ParamTableItem> getItems() {
			// TODO 自动生成的方法存根
			return items;
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			items = new ArrayList<ParamTableItem>();
			builder = new StringBuilder();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("item")) {
				item = new ParamTableItem(); 
			}
			builder.setLength(0);
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("type")) {
				item.setType(Integer.parseInt(builder.toString()));
			} else if (localName.equals("description")) {
				item.setDescription(builder.toString());
			} else if (localName.equals("item")) {
				items.add(item);
			}
		}
	}

}
