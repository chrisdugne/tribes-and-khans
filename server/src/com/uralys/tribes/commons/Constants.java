package com.uralys.tribes.commons;

public class Constants {

	public final static long SERVER_START = 1326434887434l;
	
	// temps en minutes entre chaque step de resources
	public final static long SERVER_STEP = 10;
	//public final static long SERVER_STEP = 1;

	// temps en minutes requis de presence sur une case pour recuperer une contree
	public final static long LAND_TIME = 60;
	//public final static long LAND_TIME = 1;

	// distance utilis�e pour le placement des joueurs
	public static final int PLAYER_DISTANCE = 10;

	// 100 arcs tuent 10 guerriers
	public static final int BOW_SHOT_COEFF = 10;

	// 1 guerrier compte pour 3 paysans.
	// ou encore, un paysan vaut 1/3 pt
	public static final int WARRIOR_WORTH_MEN = 3;
}
