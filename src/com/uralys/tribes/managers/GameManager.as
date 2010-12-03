package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.pages.GameInCreation;
	
	import mx.collections.ArrayCollection;

	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	

	[Bindable]
	public class GameManager{
		
		//============================================================================================//
		
		private static var instance:GameManager = new GameManager();
		public static function getInstance():GameManager{
			return instance;
		}

		//============================================================================================//
		
		private var gameWrapper:RemoteObject;

		// -  ================================================================================
		
		public function GameManager(){
			gameWrapper = new RemoteObject();
			gameWrapper.destination = "GameWrapper";
			gameWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
			gameWrapper.createGame.addEventListener("result", gameCreated);
		}

		//============================================================================================//
		// CONTROLS
		//============================================================================================//
		//  Controles pour les actions depuis les vues
		
		
		
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function createGame(name:String, period:int):void{
			gameWrapper.createGame(Session.profil.uralysUID, name, period);
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		public function gameCreated(event:ResultEvent):void{
			var game:Game = event.result as Game;
			Pager.getInstance().goToPage(GameInCreation, game);
		}
	}
}