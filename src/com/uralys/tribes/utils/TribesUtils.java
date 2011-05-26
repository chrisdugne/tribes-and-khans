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
	
	// groupes de 15*15 sur un damier de 405*405
	// 27*27 groupes
	public static int getGroup(int x, int y) {
		return x/15 + (y/15)*27;
	}

}
