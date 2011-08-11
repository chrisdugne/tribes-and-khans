package com.uralys.tribes.commons
{
	import com.uralys.tribes.entities.Item;
	import mx.collections.ArrayCollection;

	public class Numbers{

	
		public static var SERVER_START:Number = 1306434887434;
	
		// step toutes les 10 minutes
		[Bindable]
		public static var TIME_PER_STEP:int = 10;
	
		[Bindable]
		public static var BASE_SPEED:int = 3;
	
		// temps de deplacement de base (pour une vitesse de base de 3 cases par heure)
		[Bindable]
		public static var BASE_TIME_PER_MOVE_MILLIS:int = Session.isLocal ? 1000*3 : 20*60*1000;
//		public static var BASE_TIME_PER_MOVE_MILLIS:int = 20*60*1000;
		[Bindable]
		public static var BASE_TIME_FOR_LAND_CONQUEST_MILLIS:int = Session.isLocal ? 2*1000 : 60*60*1000;
//		public static var BASE_TIME_FOR_LAND_CONQUEST_MILLIS:int = 60*60*1000;
	
		// une ville se construit en 12h
		[Bindable]
		public static var TIME_TO_BUILD_A_CITY:int = 12*60*60*1000;
	
		// Main data  ==============================================================
		// Colors			[Bindable]
		public static var GREEN:Number = 0x395E3E;
		[Bindable]
		public static var BLUE:Number = 0x2B40C4;
		[Bindable]
		public static var RED:Number = 0xA80F0F;
		[Bindable]
		public static var BLACK:Number = 0x000000;
		[Bindable]
		public static var WHITE:Number = 0xFFFFFF;
		[Bindable]
		public static var YELLOW:Number = 0xC4BC45;
		
		[Bindable] public static var colorPlayer:Number = 0x5CBF7A; 
		[Bindable] public static var colorEnnemy:Number = 0xBF5C5C;
		[Bindable] public static var colorAlly:Number = 0x5C7FBF;
	
		//==================================================================================================//
		// game COEFF
		
		
		/*
			toutes les 5 min : ressources et constructions
			
			100 personnes mangent 1 de ble toutes les 5 min
		                         12 / h
								288 / 24h 
		
		    exemple avec pop 1000 - workers : 200 ble 200 fer 600 bois 
			step 
			ble : wheatSpent  : 10
			      wheatEarned : workers/20;
		
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
	
		[Bindable] public static var WHEAT_PRICE_SELL:Number = 1.8;
		[Bindable] public static var WOOD_PRICE_SELL:Number = 3;
		[Bindable] public static var IRON_PRICE_SELL:Number = 4.5;
		[Bindable] public static var BOW_PRICE_SELL:Number = 18;
		[Bindable] public static var SWORD_PRICE_SELL:Number = 27;
		[Bindable] public static var ARMOR_PRICE_SELL:Number = 45;
		
		[Bindable] public static var CITY_MERCHANT_BASE:Number = 350;
		[Bindable] public static var CITY_WHEAT_BASE_PRICE:Number = 1000;
		[Bindable] public static var CITY_IRON_BASE_PRICE:Number = 10000;
		[Bindable] public static var CITY_WOOD_BASE_PRICE:Number = 10000;
		[Bindable] public static var CITY_GOLD_BASE_PRICE:Number = 500;
	
		//==================================================================================================//
		// 
		
		// speed = nb minutes pour avancer d'une case
		[Bindable] public static var ARMY_BASE_SPEED:int = 3;
	
		// speed = nb minutes pour avancer d'une case
		[Bindable] public static var MERCHANT_BASE_SPEED:int = 4;
		
		
		//==================================================================================================//
		// contrees
	
		[Bindable] public static var LAND_WIDTH:int = 100;
		[Bindable] public static var LAND_HEIGHT:int = 86;
		
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