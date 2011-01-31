package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.pages.GameInCreation;
	import com.uralys.tribes.pages.Home;
	import com.uralys.tribes.pages.Map;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
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
		}

		//============================================================================================//
		// CONTROLS
		//============================================================================================//
		//  Controles pour les actions depuis les vues
		
		
		
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function createGame(name:String, playerName:String, period:int):void{
			gameWrapper.createGame.addEventListener("result", receivedCurrentGames);
			gameWrapper.createGame(Session.profil.uralysUID, name, playerName, period);
		}

		public function getCurrentGames():void{
			gameWrapper.getCurrentGames.addEventListener("result", receivedCurrentGames);
			gameWrapper.getCurrentGames(Session.profil.uralysUID);
		}

		public function getGamesToJoin():void{
			gameWrapper.getGamesToJoin.addEventListener("result", receivedGamesToJoin);
			gameWrapper.getGamesToJoin();
		}
		
		public function join(gameUID:String, playerName:String):void{
			gameWrapper.joinGame.addEventListener("result", receivedCurrentGames);
			gameWrapper.joinGame(Session.profil.uralysUID, gameUID, playerName);
		}
		
		public function launchGame(gameUID:String):void{
			gameWrapper.launchGame.addEventListener("result", gameLaunched);
			gameWrapper.launchGame(gameUID);
		}

		public function loadItems():void{
			gameWrapper.loadItems.addEventListener("result", itemsLoaded);
			gameWrapper.loadItems();
		}
		
		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		
		private function receivedCurrentGames(event:ResultEvent):void{
			// refresh des games en session
			Session.GAMES_PLAYING = event.result as ArrayCollection;
			
			AccountManager.getInstance().refreshProfil(event.result as ArrayCollection);
		}
		
		private function receivedGamesToJoin(event:ResultEvent):void{
			
			var games:ArrayCollection = new ArrayCollection();
			
			// enleve tous les jeux deja en cours
			for each(var gameToJoin:Game in event.result){
	
				var joinedYet:Boolean = false;
				for each(var gameJoinedYet:Game in Session.GAMES_PLAYING){
					if(gameToJoin.gameUID == gameJoinedYet.gameUID)
						joinedYet = true;
				}
				
				if(!joinedYet)
					games.addItem(gameToJoin);
			}
			
			Pager.getInstance().goToPage(Home, Home.GAMES_TO_JOIN, games);
		}
		
		private function gameLaunched(event:ResultEvent):void{
			//refresh de la liste
			getCurrentGames();
		}

		private function itemsLoaded(event:ResultEvent):void{
			Session.ITEMS = event.result as ArrayCollection;
			Numbers.loadItemData();
		}
			
	}
}