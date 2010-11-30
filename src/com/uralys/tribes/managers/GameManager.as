package com.uralys.tribes.managers {
	
	import com.asfusion.mate.events.Dispatcher;
	import com.uralys.tribes.constants.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.events.AccountEvent;
	import com.uralys.tribes.events.GameEvent;
	import com.uralys.tribes.pages.GameInCreation;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class GameManager{

		//============================================================================================//

		private var dispatcher:Dispatcher = new Dispatcher();	

		private static var instance:GameManager = new GameManager();
		public static function getInstance():GameManager{
			return instance;
		}
		
		//============================================================================================//
		// CONTROLS
		//============================================================================================//
		//  Controles pour les actions depuis les vues
		
		
		
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function createGame(name:String):void{
			var event:GameEvent = new GameEvent(GameEvent.GET_GAMES);
			
			event.gameName = name;
			event.playerUID = Session.PLAYER_UID;
			
			dispatcher.generator = GameEvent;
			dispatcher.dispatchEvent( event );
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		public function gameCreated(result:Object):void{
			Pager.getInstance().goToPage(GameInCreation, result as Game);
		}
	}
}