package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Case;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Map;
	
	import flash.events.MouseEvent;
	import flash.ui.Mouse;
	
	import mx.utils.ObjectUtil;
	
	import spark.components.Group;
	

	/**
		BoardClickListener finds which entite has been clicked on.
		 - cities
		 - armies
		 - merchants
	*/
	public class BoardClickAnalyser
	{	
		//=====================================================================================//
		
		private static var instance : BoardClickAnalyser = new BoardClickAnalyser();
	
		public static function getInstance():BoardClickAnalyser{
			return instance;
		}

		public function BoardClickAnalyser(){}

		//=====================================================================================//
		
		public function rollOnCase(_case:Case):void{
			Session.COORDINATE_X = _case.x;
			Session.COORDINATE_Y = _case.y;			
		}
		
		public function rollOnCity(city:City):void{
			Session.COORDINATE_X = city.x;
			Session.COORDINATE_Y = city.y;			
		}
		
		public function clickOnCase(_case:Case):void
		{
			var thereIsACity:Boolean = false;
			var thereIsAMerchantThatCanBuildACityHere:Boolean = false;
			
			if(_case.city != null 
			&& _case.city.cityUID != "new")
				thereIsACity = true;

			if(_case.merchants != null
			&& _case.merchants.mayBuildAcity())
				thereIsAMerchantThatCanBuildACityHere = true;
			
			if(thereIsACity)
				clickOnCity(_case.city);
			else if(thereIsAMerchantThatCanBuildACityHere)
				Session.board.showBuildCity(_case.merchants);
		}
		
		
//		public function clickOnHouse(city:City):void
//		{
//			clickOnCity(city);				
//		}

		private function clickOnCity(city:City):void
		{
			if(Session.MOVE_A_UNIT)
				return;
			
			Session.board.showEnterCity(city);
		}
			
//		public function clickOnBoard(event:MouseEvent):void{
//			var clickX:int = event.localX;
//			var clickY:int = event.localY;
//			
//			for each(var city:City in Session.player.cities){
//				if(Math.sqrt(Math.pow(clickX - city.x,2) + Math.pow(clickY - city.y,2)) <= city.radius) {
//					if(city.cityUID == "new")
//						return;
//					
//					if(city.merchants.length + city.armies.length > 0)
//						board.appearSelectionChoice(city);
//					else
//						board.clickOnCity(city);
//					
//					return;
//				}
//			}
//
//			for each(var unit:Unit in Session.player.units){
//				// click on army
//				if(Math.sqrt(Math.pow(clickX - unit.x,2) + Math.pow(clickY - unit.y,2)) <= unit.radius) {
//					board.clickOnArmy(unit);
//					return;
//				}
//				
//				// click on army move
//				else if(unit.moves.length>0 && Math.sqrt(Math.pow(clickX - unit.moves.getItemAt(0).xTo,2) + Math.pow(clickY - unit.moves.getItemAt(0).yTo,2)) <= unit.radius) {
//					board.clickOnArmy(unit);
//					return;
//				}
//			}
//
//			for each(var merchant:Unit in Session.player.units){
//				// click on caravan
//				if(Math.sqrt(Math.pow(clickX - merchant.x,2) + Math.pow(clickY - merchant.y,2)) <= merchant.radius) {
//					if(merchant.mayBuildAcity())
//						board.appearMerchantChoice(merchant);
//					else
//						board.clickOnArmy(merchant);
//					return;
//				}
//				
//				// click on army move
//				else if(merchant.moves.length>0 && Math.sqrt(Math.pow(clickX - merchant.moves.getItemAt(0).xTo,2) + Math.pow(clickY - merchant.moves.getItemAt(0).yTo,2)) <= merchant.radius) {
//					board.clickOnArmy(merchant);
//					return;
//				}
//				
//			}
//		}

		//==================================================================================================//
	}
}