package com.immune.joinsearcher.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {
	
	public static List<String> sort(List<String> list){
		Collections.sort(list, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		
		return list;
	}
	
	public static String tokenize(List<String> list, String token){
		StringBuilder sb = new StringBuilder();
		
		for (String entry : list) {
			sb.append(entry);
			sb.append(token);
		}
		
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public static String sortTokenize(List<String> list, String token){
		list = sort(list);
		return tokenize(list, token);
	}
	
	public static void main(String[] args) {
		System.out.println(tokenize(Arrays.asList("sabbir", "manish", "tulashi"), ";"));
		System.out.println(sortTokenize(Arrays.asList("sabbir", "manish", "tulashi"), ";"));
		System.out.println(sortTokenize(Arrays.asList("sabbir"), ";"));
	}

}
