package com.datang.miou.datastructure;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Building implements Serializable{
	
		private String mName;
		
		public Building(String name) {
			this.mName = name;
		}

		public String getName() {
			return this.mName;
		}
		
		public void setName(String name) {
			this.mName = name;
		}
		
		@Override
		public String toString() {
			return this.mName;
		}
}
