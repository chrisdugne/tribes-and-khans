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
					trace("plop " + city.armies.length);
					if(city.armies.length > 0)
						boardPage.appearSelectionChoice(city);
					else
						boardPage.clickOnCity(city);
				}
			}

			for each(var army:Army in Session.currentPlayer.armies){
				// click on army
				if(Math.sqrt(Math.pow(clickX - army.x,2) + Math.pow(clickY - army.y,2)) <= army.radius) {
					boardPage.clickOnArmy(army);
				}
				
				// click on army move
				else if(army.moves.length>0 && Math.sqrt(Math.pow(clickX - army.moves.getItemAt(0).xTo,2) + Math.pow(clickY - army.moves.getItemAt(0).yTo,2)) <= army.radius) {
					boardPage.clickOnArmy(army);
				}
			}
		}

		//==================================================================================================//
	}
}