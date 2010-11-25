package com.skooairs.constants
{

public class Numbers{


import mx.collections.ArrayCollection;


	[Bindable]
	public static var FACEBOOK_USER:int = 1;

	[Bindable]
	public static var GOOGLE_USER:int = 2;
	

	// Main data  ==============================================================
	
	[Bindable]
	public static var VIEW_HOME:int = 0;

	[Bindable]
	public static var VIEW_SELECTIONS:int = 1;

	[Bindable]
	public static var VIEW_PLAY:int = 2;

	[Bindable]
	public static var VIEW_BOARDS:int = 3;
	
	//==================================================================================================//
	// Colors	[Bindable]
	public static var GREEN:Number = 0x009933;
	[Bindable]
	public static var BLUE:Number = 0x3399FF;
	[Bindable]
	public static var RED:Number = 0xCC0000;
	[Bindable]
	public static var BLACK:Number = 0x000000;
	[Bindable]
	public static var WHITE:Number = 0xFFFFFF;
	[Bindable]
	public static var YELLOW:Number = 0xFFFF66;

	//==================================================================================================//
	// credits
	
	public static var BUY_100_CREDITS:int = 1;
	public static var BUY_300_CREDITS:int = 2;
	public static var BUY_1000_CREDITS:int = 3;
	public static var BUY_2500_CREDITS:int = 4;
	public static var BUY_6000_CREDITS:int = 5;

	//=====================================================//

	
	public static var TIME_2_MIN:int = 2;
	public static var TIME_3_MIN:int = 3;
	public static var TIME_5_MIN:int = 5;
	public static var TIME_7_MIN:int = 7;
	public static var TIME_10_MIN:int = 10;
	public static var TIME_NO_LIMIT:int = 0;

	//=====================================================//
	
	[Bindable]
	public static var CONSTRUCTOR:int = 0;
	[Bindable]
	public static var DESTRUCTOR:int = 1;


	public static var NB_CLICK_FOR_A_BOOM:int = 10;

	//==================================================================================================//
	// tutorials
	
	//[Bindable] public static var TUTO_EDIT_FOOL_0:int = 0;

	//==================================================================================================//
	
	[Bindable]
	public static var X:ArrayCollection = new ArrayCollection([48, 93, 138, 183, 228, 273, 318, 363, 408]);	
	[Bindable]
	public static var Y:ArrayCollection = new ArrayCollection([48, 93, 138, 183, 228, 273, 318, 363, 408]);	

	public static var BOARD_LEFT:int = 3;
	public static var BOARD_RIGHT:int = 453;
	public static var BOARD_TOP:int = 3;
	public static var BOARD_BOTTOM:int = 453;
}
}