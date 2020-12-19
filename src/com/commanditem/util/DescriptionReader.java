package com.commanditem.util;

import java.util.ArrayList;
import java.util.List;

public class DescriptionReader {

	public static List<String> read(String in) {
		List<String> out = new ArrayList<>();
		if(in == null || in.equals("null")) return out;
		String[] arr = in.split("/");
		for(String s : arr) {
			out.add(s);
		}
		return out;
	}
	
	public static String formatBack(List<String> in) {
		String out = "";
		for(String s : in) {
			out += s + "/";
		}
		out = out.substring(0, out.length()-1);
		out.replace("&", "§");
		return out;
	}
	
}
