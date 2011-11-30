package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.UralysProfile;
	import com.uralys.tribes.pages.Board;
	
	import flash.sampler.getGetterInvocationCount;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.utils.ObjectUtil;
	
	[Bindable]
	public class AccountManager{

		//============================================================================================//

		private static var instance:AccountManager = new AccountManager();
		public static function getInstance():AccountManager{
			return instance;
		}

		//============================================================================================//
		
		private var accountWrapper:RemoteObject;
		private var gameWrapper:RemoteObject;
		
		// -  ================================================================================
		
		public function AccountManager(){
			accountWrapper = new RemoteObject();
			accountWrapper.destination = "UralysAccountWrapper";
			accountWrapper.endpoint = Session.isLocal ? Names.LOCAL_LOGGER_SERVER_AMF_ENDPOINT 
													  : Names.URALYS_LOGGER_SERVER_AMF_ENDPOINT;

			gameWrapper = new RemoteObject();
			gameWrapper.destination = "GameWrapper";
			gameWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
		}

		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		private var email:String;
		private var password:String;
		public function register(email:String, password:String):void{
			this.email = email; // bckup pour etre parametre dans registered, et pour login automatique
			this.password = password; // bckup pour login automatique

			accountWrapper.createUralysAccount.addEventListener("result", registered);
			accountWrapper.createUralysAccount(email, password);
		}

		public function login(email:String, password:String):void{
			this.email = email; // bckup pour login automatique si on doit creer le profil automatiquement
			this.password = password; // bckup pour login automatique si on doit creer le profil automatiquement
			
			accountWrapper.login.addEventListener("result", resultLogin);
			accountWrapper.login(email, password);
		}
		
		//--------------------------------------------------------------------------------------------//
		
		public function changeLanguage(uralysUID:String, language:int):void{
			accountWrapper.changeLanguage(uralysUID, language);
		}
		
		public function changeMusicOn(uralysUID:String, musicOn:Boolean):void{
			gameWrapper.changeMusicOn(uralysUID, musicOn);
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		//-------------------------------------------------------------------------//
		// UralysLogger
		
		private function registered(event:ResultEvent):void{
			
			Session.uralysProfile = event.result as UralysProfile;
			
			if(Session.uralysProfile.uralysUID == "EMAIL_EXISTS"){
				FlexGlobals.topLevelApplication.message("This email is registered yet", 3);
				Session.WAIT_FOR_CONNECTION = false;
			}
			else{
				gameWrapper.createPlayer.addEventListener("result", playerCreated);
				gameWrapper.createPlayer(Session.uralysProfile.uralysUID, this.email); 
			}
		}
		
		
		private function resultLogin(event:ResultEvent):void
		{ 
			Session.uralysProfile = event.result as UralysProfile;
			Session.LANGUAGE = Session.uralysProfile.language;
			
			if(Session.uralysProfile.uralysUID == "WRONG_PWD"){
				FlexGlobals.topLevelApplication.message("Authentication Failed", 3);
				Session.WAIT_FOR_CONNECTION = false;
			}
			else{
				gameWrapper.getPlayer.addEventListener("result", receivedPlayer);
				gameWrapper.getPlayer(Session.uralysProfile.uralysUID, true); // newConnection = true
			}
		}

		//-------------------------------------------------------------------------//
		// TribesAndKhansServer

		public function playerCreated(event:ResultEvent):void
		{
			var uralysUID:String = event.result as String;
			
			if(!playerCreatedAutomatically)
				FlexGlobals.topLevelApplication.message(Translations.WELCOME.getItemAt(Session.LANGUAGE), 5);
			
			gameWrapper.getPlayer.addEventListener("result", receivedPlayer);
			gameWrapper.getPlayer(uralysUID, true); // newConnection = true
		}
		
		private var playerCreatedAutomatically:Boolean = false;
		public function receivedPlayer(event:ResultEvent):void
		{
			var player:Player = event.result as Player;
			
			if(player == null){
				playerCreatedAutomatically = true;
				gameWrapper.createPlayer.addEventListener("result", playerCreated);
				gameWrapper.createPlayer(Session.uralysProfile.uralysUID, this.email);
			}
			else{
				Session.player = player;
				trace(ObjectUtil.toString(Session.player));
				finalizeLogin();
			}
				
		}

		private function finalizeLogin():void
		{
			Session.WAIT_FOR_CONNECTION = false;
			Session.WAIT_FOR_SERVER = false;
			Session.LOGGED_IN = true;
			Pager.getInstance().goToPage(Board);
			
		}
		
	}
}