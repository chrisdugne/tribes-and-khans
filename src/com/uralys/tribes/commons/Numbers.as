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
	
	//==================================================================================================//
	// contrees

	[Bindable] public static var LAND_WIDTH:int = 100;
	[Bindable] public static var LAND_HEIGHT:int = 100;

	//==================================================================================================//
	// Item data
	
	[Bindable] public static var BOW_WOOD:int;
	[Bindable] public static var BOW_IRON:int;
	[Bindable] public static var BOW_PRICE:int;
	[Bindable] public static var BOW_VALUE:int;
	[Bindable] public static var BOW_PEOPLE_REQUIRED:int;

	[Bindable] public static var SWORD_WOOD:int;
	[Bindable] public static var SWORD_IRON:int;
	[Bindable] public static var SWORD_PRICE:int;
	[Bindable] public static var SWORD_VALUE:int;
	[Bindable] public static var SWORD_PEOPLE_REQUIRED:int;

	[Bindable] public static var ARMOR_WOOD:int;
	[Bindable] public static var ARMOR_IRON:int;
	[Bindable] public static var ARMOR_PRICE:int;
	[Bindable] public static var ARMOR_VALUE:int;
	[Bindable] public static var ARMOR_PEOPLE_REQUIRED:int;
	
	
	
	public static function loadItemData():void{
		for each(var item:Item in Session.ITEMS){
			
			if(item.name == "bow"){
				BOW_WOOD = item.wood;
				BOW_IRON = item.iron;
				BOW_PEOPLE_REQUIRED = item.peopleRequired;
				BOW_PRICE= item.goldPrice;
				BOW_VALUE= item.value;
			}

			else if(item.name == "sword"){
				SWORD_WOOD = item.wood;
				SWORD_IRON = item.iron;
				SWORD_PEOPLE_REQUIRED = item.peopleRequired;
				SWORD_PRICE= item.goldPrice;
				SWORD_VALUE= item.value;
			}

			else if(item.name == "armor"){
				ARMOR_WOOD = item.wood;
				ARMOR_IRON = item.iron;
				ARMOR_PEOPLE_REQUIRED = item.peopleRequired;
				ARMOR_PRICE= item.goldPrice;
				ARMOR_VALUE= item.value;
			}
		}
		
	}
	
	
}
}