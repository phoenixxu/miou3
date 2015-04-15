package com.datang.miou.datastructure;

public class Synchronize {
	private String mType;
	private TestCommand mCommand;
	
	public Synchronize() {
		this.mCommand = new TestCommand();
	}
	
	public String getType() {
		return mType;
	}
	public void setType(String type) {
		this.mType = type;
	}
	public TestCommand getCommand() {
		return mCommand;
	}
	public void setCommand(TestCommand command) {
		mCommand = command;
	}
	
	@Override
	public String toString() {
		return "(" + this.mType + " " + this.mCommand.toString() + ")";
	}
}
