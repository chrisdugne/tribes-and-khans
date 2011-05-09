package com.uralys.tribes.utils;

public class TribesUtils {

	// caseUID : case_i_j
	public static int getX(String caseUID){
		String[] split = caseUID.split("_");
		return Integer.parseInt(split[1]);
	}

	public static int getY(String caseUID){
		String[] split = caseUID.split("_");
		return Integer.parseInt(split[2]);
	}
}
