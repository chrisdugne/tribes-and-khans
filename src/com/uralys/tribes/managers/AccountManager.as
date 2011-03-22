package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Profil;
	import com.uralys.tribes.entities.UralysProfile;
	import com.uralys.tribes.pages.Home;
	
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
		private var playerWrapper:RemoteObject;
		
		// -  ================================================================================
		
		public function AccountManager(){
			accountWrapper = new RemoteObject();
			accountWrapper.destination = "UralysAccountWrapper";
			accountWrapper.endpoint = Session.isLocal ? Names.LOCAL_LOGGER_SERVER_AMF_ENDPOINT 
													  : Names.URALYS_LOGGER_SERVER_AMF_ENDPOINT;

			playerWrapper = new RemoteObject();
			playerWrapper.destination = "PlayerWrapper";
			playerWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
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
		
		
		// la liste des games vient d'etre refreshed.
		// on la stocke pour la passer en param dans le gotopage(Home) qd on a recu le profil refreshed
		private var games:ArrayCollection;
		public function refreshProfil(games:ArrayCollection):void{
			this.games = games;
			playerWrapper.getProfil.addEventListener("result", refreshedProfil);
			playerWrapper.getProfil(Session.uralysProfile.uralysUID);
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
				playerWrapper.createProfil.addEventListener("result", profilCreated);
				playerWrapper.createProfil(Session.uralysProfile.uralysUID, this.email); 
			}
		}
		
		
		private function resultLogin(event:ResultEvent):void{
			
			Session.uralysProfile = event.result as UralysProfile;
			Session.LANGUAGE = Session.uralysProfile.language;
			
			if(Session.uralysProfile.uralysUID == "WRONG_PWD"){
				FlexGlobals.topLevelApplication.message("Authentication Failed", 3);
				Session.WAIT_FOR_CONNECTION = false;
			}
			else{
				playerWrapper.getProfil.addEventListener("result", receivedProfil);
				playerWrapper.getProfil(Session.uralysProfile.uralysUID);
			}
		}

		//-------------------------------------------------------------------------//
		// TribesAndKhansServer

		public function profilCreated(event:ResultEvent):void{
			Session.profil = event.result as Profil;
			
			if(!profilCreatedAutomatically)
				FlexGlobals.topLevelApplication.message("Profil Uralys cree. Bienvenue !", 5);
			
			finalizeLogin();
		}
		
		private var profilCreatedAutomatically:Boolean = false;
		public function receivedProfil(event:ResultEvent):void{
			var profil:Profil = event.result as Profil;
			
			if(profil == null){
				profilCreatedAutomatically = true;
				playerWrapper.createProfil.addEventListener("result", profilCreated);
				playerWrapper.createProfil(Session.uralysProfile.uralysUID, this.email);
			}
			else{
				
				Session.profil = profil;
				finalizeLogin();
			}
				
		}

		public function refreshedProfil(event:ResultEvent):void{
			var profil:Profil = event.result as Profil;
			
			Session.profil = profil;
			Pager.getInstance().goToPage(Home, Home.CURRENT_GAMES, this.games);
		}

		private function finalizeLogin():void{
			
			Session.WAIT_FOR_CONNECTION = false;
			Session.WAIT_FOR_SERVER = false;
			Session.LOGGED_IN = true;
			Pager.getInstance().goToPage(Home);
			
		}
		
	}
}