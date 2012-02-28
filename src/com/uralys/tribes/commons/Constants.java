package com.uralys.tribes.commons;

public class Constants {

	public final static long SERVER_START = 1306434887434l;
	
	// temps en minutes entre chaque step de resources
	public final static long SERVER_STEP = 10;

	// temps en minutes requis de presence sur une case pour recuperer une contree
	//public final static long LAND_TIME = 60;
	public final static long LAND_TIME = 1;

	// distance utilisée pour le placement des joueurs
	public static final int PLAYER_DISTANCE = 10;

	// 100 arcs tuent 10 guerriers
	public static final int BOW_SHOT_COEFF = 10;
}
