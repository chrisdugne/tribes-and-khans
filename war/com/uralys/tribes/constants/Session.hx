package com.skooairs.constants
{

import mx.collections.ArrayCollection;

import forms.Play;
import pages.Game;

import com.skooairs.entities.Player;

public class Session{

	//=====================================================//

	[Bindable] public static var VERSION:String = "1.0.7";
	[Bindable] public static var FACEBOOK_UID:String = "";
	
	public static var FIRST_VIEW:int;

	[Bindable] public static var GAME_OVER:Boolean = true;
	[Bindable] public static var DISP_LAUNCH_TIME:Boolean = false;
	[Bindable] public static var NB_BOOMS:int;

	[Bindable] public static var POINTS:int;
	[Bindable] public static var CURRENT_RECORD:int;

	[Bindable] public static var BOOM_SCALE_LOCK:Boolean = false;

	//=====================================================//

	[Bindable] public static var NEXT_SQUARE_X:int = -45;
	[Bindable] public static var NEXT_SQUARE_Y:int = 115;

	[Bindable] public static var WAITING_SQUARE_X:int = -44;
	[Bindable] public static var WAITING_SQUARE_Y:int = 447;

	[Bindable] public static var CURRENT_SQUARE_X:int = 228;
	[Bindable] public static var CURRENT_SQUARE_Y:int = 3;

	[Bindable] public static var CURRENT_SHADOW_X:int = Numbers.X.getItemAt(4) as int;
	[Bindable] public static var CURRENT_SHADOW_Y:int = Numbers.Y.getItemAt(3) as int;

	[Bindable] public static var CURRENT_SHADOW_BOARD_X:int = 4;
	[Bindable] public static var CURRENT_SHADOW_BOARD_Y:int = 3;

	[Bindable] public static var CURRENT_WAY_FROM:int;

	//=====================================================//
	
	//public static var session: FacebookSessionUtil;
	//public static var fbook:Facebook;
	
	[Bindable] public static var LANGUAGE:int;
	[Bindable] public static var newPlayer:Boolean = false;
	
	[Bindable] public static var CURRENT_TUTO_AVAILABLE:int = 0;

	//=====================================================//


	[Bindable] public static var player:Player;

	[Bindable] public static var friendUIDs:Array = []; // facebook ids
	[Bindable] public static var friendPlayerUIDs:ArrayCollection = new ArrayCollection(); // playerUIDs

	//=====================================================//

	[Bindable] 
	public static var game:Game;
	public static var play:Play;

	//=====================================================//

	public static var TIME:int = Numbers.TIME_2_MIN;
	public static var COLORS:int = 3;
	[Bindable] public static var SPEED:int = 1;

	//=====================================================//
	// SquareMover
	
	public static var cases:Array = [[],[],[],[],[],[],[],[],[]];

	//=====================================================//
	
	
}
}