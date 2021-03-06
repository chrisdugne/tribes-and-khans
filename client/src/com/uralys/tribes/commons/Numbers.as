package com.uralys.tribes.commons
{
	import com.uralys.tribes.entities.Item;
	import mx.collections.ArrayCollection;

	public class Numbers{

	
		public static var SERVER_START:Number = 1326434887434;

		[Bindable]
		public static var BASE_SPEED:int = 3;

		// Main data  ==============================================================
	
		// step toutes les 10 minutes
		[Bindable]
		public static var TIME_PER_STEP:int = 10;
		//public static var TIME_PER_STEP:int = 1;
	
	
		// temps de deplacement de base (pour une vitesse de base de 3 cases par heure)
		[Bindable]
		public static var BASE_TIME_PER_MOVE_MILLIS:int = 20*60*1000;
		//public static var BASE_TIME_PER_MOVE_MILLIS:int = 1000*5;

		[Bindable]
		public static var BASE_TIME_FOR_LAND_CONQUEST_MILLIS:int = 60*60*1000;
		//public static var BASE_TIME_FOR_LAND_CONQUEST_MILLIS:int = 60*1000;

		// tir accessible toutes les 10 minutes
		public static var BASE_TIME_FOR_SHOOTING:int = 10*60*1000;
		//public static var BASE_TIME_FOR_SHOOTING:int = 20*1000;
	
		// une ville se construit en 12h
		[Bindable]
		public static var TIME_TO_BUILD_A_CITY:int = 12*60*60*1000;
		//public static var TIME_TO_BUILD_A_CITY:int = 20*1000;
	
		// ==============================================================
		// Colors			[Bindable] public static var CLASSIC_COLOR:Number = 0x9C9C9C;
		[Bindable] public static var GREEN:Number = 0x348C4F;
		[Bindable] public static var BLUE:Number = 0x5C7FBF;
		[Bindable] public static var RED:Number = 0xBF5C5C;
		[Bindable] public static var BLACK:Number = 0x000000;
		[Bindable] public static var WHITE:Number = 0xFFFFFF;
		[Bindable] public static var YELLOW:Number = 0xC4BC45;
		
		[Bindable] public static var colorPlayer:Number = 0x348C4F;
		[Bindable] public static var colorEnnemy:Number = 0xBF5C5C;
		[Bindable] public static var colorAlly:Number = 0x5C7FBF;
	
		//==================================================================================================//
		// game COEFF
		
		
		/*
		conso ble 1/100h/10min
		crea ble  5/100h/10min
		bois 	  3/100h/10min
		fer 	  2/100h/10min
		
		contree = 1or/contree /10min
		
		- constructions
		
		arc 	5 bois + 
		epee 	3 fer
		armure 	5 fer
		
		- prix : nb or par unité
		
		achat
		WHEAT_PRICE_BUY:Number = 2;
		WOOD_PRICE_BUY:Number = 3.33;
		IRON_PRICE_BUY:Number = 5;
		BOW_PRICE_BUY:Number = 20;
		SWORD_PRICE_BUY:Number = 30;
		ARMOR_PRICE_BUY:Number = 50;
		
		vente = 80% achat
		WHEAT_PRICE_SELL:Number = 1.6;
		WOOD_PRICE_SELL:Number = 2.7;
		IRON_PRICE_SELL:Number = 4.3;
		BOW_PRICE_SELL:Number = 16;
		SWORD_PRICE_SELL:Number = 23;
		ARMOR_PRICE_SELL:Number = 40;
		
		
		1 ble achat : 2 = 20min avec 1 contree = 5min avec 4 contrees 		 ====> 1h = 4 contrees
		1 bois achat : 3.33 = 33min avec 1 contree = 5min avec 6.5 contrees  ====> 1h = 6.5 contrees
		1 fer achat : 5 = 50min avec 1 contree = 5min avec 10 contrees 		 ====> 1h = 10 contrees

		*/
		
		[Bindable] public static var FEED_COEFF:Number = 1/100;
		[Bindable] public static var WHEAT_EARNING_COEFF:Number = 5/100;
		[Bindable] public static var WOOD_EARNING_COEFF:Number = 3/100;
		[Bindable] public static var IRON_EARNING_COEFF:Number = 2/100;
	
		[Bindable] public static var WHEAT_PRICE_BUY:Number = 2;
		[Bindable] public static var WOOD_PRICE_BUY:Number = 3.33;
		[Bindable] public static var IRON_PRICE_BUY:Number = 5;
		[Bindable] public static var BOW_PRICE_BUY:Number = 20;
		[Bindable] public static var SWORD_PRICE_BUY:Number = 30;
		[Bindable] public static var ARMOR_PRICE_BUY:Number = 50;
	
		// 80%
		[Bindable] public static var WHEAT_PRICE_SELL:Number = 1.6;
		[Bindable] public static var WOOD_PRICE_SELL:Number = 2.7;
		[Bindable] public static var IRON_PRICE_SELL:Number = 4;
		[Bindable] public static var BOW_PRICE_SELL:Number = 16;
		[Bindable] public static var SWORD_PRICE_SELL:Number = 23;
		[Bindable] public static var ARMOR_PRICE_SELL:Number = 40;
		
		[Bindable] public static var CITY_MERCHANT_BASE:Number = 500;
		[Bindable] public static var CITY_WHEAT_BASE_PRICE:Number = 1000;
		[Bindable] public static var CITY_IRON_BASE_PRICE:Number = 3000;
		[Bindable] public static var CITY_IRON_RATIO_PRICE:Number = 3;
		[Bindable] public static var CITY_WOOD_BASE_PRICE:Number = 5000;
		[Bindable] public static var CITY_WOOD_RATIO_PRICE:Number = 5;
		[Bindable] public static var CITY_GOLD_BASE_PRICE:Number = 500;

		[Bindable] public static var BOW_WEIGHT:Number = 5;
		[Bindable] public static var SWORD_WEIGHT:Number = 5;
		[Bindable] public static var ARMOR_WEIGHT:Number = 5;
		
		[Bindable] public static var TOTAL_CARAVAN_WEIGHT:Number = 50;
	
		//==================================================================================================//
		// 
		
		// speed = nb minutes pour avancer d'une case
		[Bindable] public static var ARMY_BASE_SPEED:int = 3;
	
		// speed = nb minutes pour avancer d'une case
		[Bindable] public static var MERCHANT_BASE_SPEED:int = 4;
		
		
		//==================================================================================================//
		// contrees
	
		[Bindable] public static var LAND_WIDTH:int = 142;
		[Bindable] public static var LAND_HEIGHT:int = 123;
		
		[Bindable] public static var NOTHING:int = 0;
		[Bindable] public static var FOREST:int = 1;
		[Bindable] public static var PLAIN:int = 2;
		[Bindable] public static var LAKE:int = 3;
		[Bindable] public static var ROCK:int = 4;
		
		//==================================================================================================//
		
		[Bindable] public static var REPORT_GROUND_FIGHT:int = 1;
		[Bindable] public static var REPORT_GROUND_GATHERING:int = 2;
		[Bindable] public static var REPORT_BOW_SHOT:int = 3;
		
		//==================================================================================================//
		// Item data
		
		[Bindable] public static var BOW_TIME:int = 20*60*1000;
		//[Bindable] public static var BOW_TIME:int = 5*1000;
		[Bindable] public static var BOW_WOOD:int;
		[Bindable] public static var BOW_IRON:int;
		[Bindable] public static var BOW_PRICE:Number;
		[Bindable] public static var BOW_VALUE:int;
		[Bindable] public static var BOW_PEOPLE_REQUIRED:int;
	
		[Bindable] public static var SWORD_TIME:int = 30*60*1000;
		//[Bindable] public static var SWORD_TIME:int = 10*1000;
		[Bindable] public static var SWORD_WOOD:int;
		[Bindable] public static var SWORD_IRON:int;
		[Bindable] public static var SWORD_PRICE:Number;
		[Bindable] public static var SWORD_VALUE:int;
		[Bindable] public static var SWORD_PEOPLE_REQUIRED:int;
	
		[Bindable] public static var ARMOR_TIME:int = 45*60*1000;
		//[Bindable] public static var ARMOR_TIME:int = 15*1000;
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