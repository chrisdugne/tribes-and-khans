package com.uralys.tribes.commons
{
	import com.uralys.tribes.entities.Item;

public class Numbers{


import mx.collections.ArrayCollection;

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

	public static var CLICK_SCOPE:int = 100;

	//==================================================================================================//
	// game COEFF
	
	public static var WHEAT_EARNING_COEFF:Number = 5;
	public static var WOOD_EARNING_COEFF:Number = 3;
	public static var IRON_EARNING_COEFF:Number = 2;

	[Bindable] public static var WHEAT_PRICE_BUY:Number = 2;
	[Bindable] public static var WOOD_PRICE_BUY:Number = 3.33;
	[Bindable] public static var IRON_PRICE_BUY:Number = 5;
	[Bindable] public static var BOW_PRICE_BUY:Number = 20;
	[Bindable] public static var SWORD_PRICE_BUY:Number = 30;
	[Bindable] public static var ARMOR_PRICE_BUY:Number = 50;

	[Bindable] public static var WHEAT_PRICE_SELL:Number = 1.8;
	[Bindable] public static var WOOD_PRICE_SELL:Number = 3;
	[Bindable] public static var IRON_PRICE_SELL:Number = 4.5;
	[Bindable] public static var BOW_PRICE_SELL:Number = 18;
	[Bindable] public static var SWORD_PRICE_SELL:Number = 27;
	[Bindable] public static var ARMOR_PRICE_SELL:Number = 45;
	
	[Bindable] public static var CITY_WHEAT_BASE_PRICE:Number = 1000;
	[Bindable] public static var CITY_IRON_BASE_PRICE:Number = 10000;
	[Bindable] public static var CITY_WOOD_BASE_PRICE:Number = 10000;
	[Bindable] public static var CITY_GOLD_BASE_PRICE:Number = 500;

	//==================================================================================================//
	// contrees

	[Bindable] public static var LAND_WIDTH:int = 100;
	[Bindable] public static var LAND_HEIGHT:int = 100;
	
	[Bindable] public static var NOTHING:int = 0;
	[Bindable] public static var FOREST:int = 1;
	[Bindable] public static var PLAIN:int = 2;
	[Bindable] public static var LAKE:int = 3;
	[Bindable] public static var ROCK:int = 4;
	

	//==================================================================================================//
	// Item data
	
	[Bindable] public static var BOW_WOOD:int;
	[Bindable] public static var BOW_IRON:int;
	[Bindable] public static var BOW_PRICE:Number;
	[Bindable] public static var BOW_VALUE:int;
	[Bindable] public static var BOW_PEOPLE_REQUIRED:int;

	[Bindable] public static var SWORD_WOOD:int;
	[Bindable] public static var SWORD_IRON:int;
	[Bindable] public static var SWORD_PRICE:Number;
	[Bindable] public static var SWORD_VALUE:int;
	[Bindable] public static var SWORD_PEOPLE_REQUIRED:int;

	[Bindable] public static var ARMOR_WOOD:int;
	[Bindable] public static var ARMOR_IRON:int;
	[Bindable] public static var ARMOR_PRICE:Number;
	[Bindable] public static var ARMOR_VALUE:int;
	[Bindable] public static var ARMOR_PEOPLE_REQUIRED:int;
	
	
	
	public static function loadItemData():void{
		for each(var item:Item in Session.ITEMS){
			
			if(item.name == "bow"){
				BOW_WOOD = item.wood;
				BOW_IRON = item.iron;
				BOW_PEOPLE_REQUIRED = item.peopleRequired;
				BOW_PRICE= item.goldPriceCurrent;
				BOW_VALUE= item.value;
			}

			else if(item.name == "sword"){
				SWORD_WOOD = item.wood;
				SWORD_IRON = item.iron;
				SWORD_PEOPLE_REQUIRED = item.peopleRequired;
				SWORD_PRICE= item.goldPriceCurrent;
				SWORD_VALUE= item.value;
			}

			else if(item.name == "armor"){
				ARMOR_WOOD = item.wood;
				ARMOR_IRON = item.iron;
				ARMOR_PEOPLE_REQUIRED = item.peopleRequired;
				ARMOR_PRICE= item.goldPriceCurrent;
				ARMOR_VALUE= item.value;
			}
		}
		
	}
	
	
}
}