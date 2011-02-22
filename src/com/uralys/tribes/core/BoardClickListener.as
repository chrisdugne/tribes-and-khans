package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Army;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Map;
	
	import flash.events.MouseEvent;
	import flash.ui.Mouse;
	
	import spark.components.Group;
	

	/**
		BoardClickListener finds which entite has been clicked on.
		 - cities
		 - armies
		 - merchants
	*/
	public class BoardClickListener
	{	
		//=====================================================================================//
		
		public static var boardPage : Board;
		
		//=====================================================================================//
	
		private static var instance : BoardClickListener = new BoardClickListener();
	
		public static function getInstance():BoardClickListener{
			return instance;
		}

		public function BoardClickListener(){}

		//=====================================================================================//
		
		public function clickOnBoard(event:MouseEvent):void{
			var clickX:int = event.localX;
			var clickY:int = event.localY;
			
			for each(var city:City in Session.currentPlayer.cities){
				if(Math.sqrt(Math.pow(clickX - city.x,2) + Math.pow(clickY - city.y,2)) <= city.radius) {
					if(city.cityUID == "new")
						return;
					
					if(city.merchants.length + city.armies.length > 0)
						boardPage.appearSelectionChoice(city);
					else
						boardPage.clickOnCity(city);
					
					return;
				}
			}

			for each(var army:Army in Session.currentPlayer.armies){
				// click on army
				if(Math.sqrt(Math.pow(clickX - army.x,2) + Math.pow(clickY - army.y,2)) <= army.radius) {
					boardPage.clickOnArmy(army);
					return;
				}
				
				// click on army move
				else if(army.moves.length>0 && Math.sqrt(Math.pow(clickX - army.moves.getItemAt(0).xTo,2) + Math.pow(clickY - army.moves.getItemAt(0).yTo,2)) <= army.radius) {
					boardPage.clickOnArmy(army);
					return;
				}
			}

			for each(var merchant:Army in Session.currentPlayer.merchants){
				// click on caravan
				if(Math.sqrt(Math.pow(clickX - merchant.x,2) + Math.pow(clickY - merchant.y,2)) <= merchant.radius) {
					if(merchant.mayBuildAcity())
						boardPage.appearMerchantChoice(merchant);
					else
						boardPage.clickOnArmy(merchant);
					return;
				}
				
				// click on army move
				else if(merchant.moves.length>0 && Math.sqrt(Math.pow(clickX - merchant.moves.getItemAt(0).xTo,2) + Math.pow(clickY - merchant.moves.getItemAt(0).yTo,2)) <= merchant.radius) {
					boardPage.clickOnArmy(merchant);
					return;
				}
				
			}
		}

		//==================================================================================================//
	}
}