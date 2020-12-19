package com.commanditem.util;

import java.util.HashMap;

public class ArgsOutput{
	private HashMap<String, String> keysValues;
	
	ArgsOutput(HashMap<String, String> keysValues){
		this.keysValues = keysValues;
	}
	
	public String get(String key) {
		String out = keysValues.get(key);
		return out == null ? "null" : out;
	}
	
	public String get(String key, String outDefault) {
		String out = keysValues.get(key);
		return out == null ? outDefault : out;
	}
	
}
