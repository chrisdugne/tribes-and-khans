package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Cell;
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
		BoardClickListener
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
		
		public function rollOnCase(_case:Cell):void{
			Session.COORDINATE_X = _case.x;
			Session.COORDINATE_Y = _case.y;			
		}
		
		public function rollOnCity(city:City):void{
			Session.COORDINATE_X = city.x;
			Session.COORDINATE_Y = city.y;			
		}
		
		public function clickOnCase(_cell:Cell):void
		{
			if(_cell.city != null 
			&& _cell.city.cityUID != "new")
				clickOnCity(_cell.city);
		}
		
		
		private function clickOnCity(city:City):void
		{
			if(Session.MOVE_A_UNIT)
				return;
			
			Session.board.showEnterCity(city);
		}
			
		//==================================================================================================//
	}
}