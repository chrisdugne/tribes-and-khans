package com.uralys.tribes.core
{
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Player;

	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.pages.Board;

	import com.uralys.utils.Map;
	
	import flash.events.MouseEvent;
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
		
		public function clickOnBoard(event:MouseEvent, player:Player, board:Group):void{
			var clickX:int = event.localX;
			var clickY:int = event.localY;
			
			for each(var city:City in player.cities){
				if(Math.sqrt(Math.pow(clickX - city.x,2) + Math.pow(clickY - city.y,2)) <= city.radius) {
					boardPage.clickOnCity(city);
				}
			}
		}

		//==================================================================================================//
	}
}