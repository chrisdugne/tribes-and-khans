package com.uralys.tribes.managers {
	
	import com.uralys.tribes.constants.Names;
	import com.uralys.tribes.constants.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.pages.Home;
	
	import mx.collections.ArrayCollection;
	
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	[Bindable]
	public class AccountManager{

		//============================================================================================//

		private static var instance:AccountManager = new AccountManager();
		public static function getInstance():AccountManager{
			return instance;
		}

		//============================================================================================//
		
		private var accountWrapper:RemoteObject;
		
		// -  ================================================================================
		
		public function AccountManager(){
			accountWrapper = new RemoteObject();
			accountWrapper.destination = "PlayerWrapper";
			accountWrapper.endpoint = Names.URALYS_LOGGER_SERVER_AMF_ENDPOINT;
			accountWrapper.login.addEventListener("result", resultLogin);
		}
		
		
		//============================================================================================//
		// CONTROLS
		//============================================================================================//
		//  Controles pour les actions depuis les vues
		
		
		
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function register(email:String, password:String):void{
			accountWrapper.createPlayer(email, password);
		}

		public function login(email:String, password:String):void{
			accountWrapper.login(email, password);
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		public function resultLogin(event:ResultEvent):void{
			
			var message:String = event.result as String;
			
			if(message == "WRONG_PWD")
				trace("authentication failed");
			else{
				Session.PLAYER_UID = message;
				Pager.getInstance().goToPage(Home);
			}
		}
	}
}