package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Profil;
	import com.uralys.tribes.pages.Home;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.validators.EmailValidator;
	
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
			accountWrapper.endpoint = Names.URALYS_LOGGER_SERVER_AMF_ENDPOINT;
			accountWrapper.login.addEventListener("result", resultLogin);
			accountWrapper.createUralysAccount.addEventListener("result", registered);

			playerWrapper = new RemoteObject();
			playerWrapper.destination = "PlayerWrapper";
			playerWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
			playerWrapper.createProfil.addEventListener("result", profilCreated);
			playerWrapper.getProfil.addEventListener("result", receivedProfil);
		}

		
		//============================================================================================//
		// CONTROLS
		//============================================================================================//
		//  Controles pour les actions depuis les vues
		
		
		
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		private var email:String;
		public function register(email:String, password:String):void{
			this.email = email; // bckup pour etre parametre dans registered
			accountWrapper.createUralysAccount(email, password);
		}

		public function login(email:String, password:String):void{
			accountWrapper.login(email, password);
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		//-------------------------------------------------------------------------//
		// UralysLogger
		
		public function registered(event:ResultEvent):void{
			if(event.result == "EMAIL_EXISTS"){
				Alert.show("This email is registered yet");
				Session.WAIT_FOR_SERVER = false;
			}
			else
				playerWrapper.createProfil(event.result, email); // event.result == uralysUID
		}
		
		
		public function resultLogin(event:ResultEvent):void{
			
			var uralysUID:String = event.result as String;
			
			if(uralysUID == "WRONG_PWD"){
				Alert.show("authentication failed");
				Session.WAIT_FOR_SERVER = false;
			}
			else
				playerWrapper.getProfil(uralysUID);
		}

		//-------------------------------------------------------------------------//
		// TribesAndKhansServer

		public function profilCreated(event:ResultEvent):void{
			Session.profil = event.result as Profil;
			Alert.show("Profil Cree.");
			Session.WAIT_FOR_SERVER = false;
		}
		
		public function receivedProfil(event:ResultEvent):void{
			var profil:Profil = event.result as Profil;
			Session.profil = profil;
			Session.WAIT_FOR_SERVER = false;
			Pager.getInstance().goToPage(Home);
		}

	}
}