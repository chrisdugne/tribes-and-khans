package com.uralys.tribes.managers {
	
	import com.asfusion.mate.events.Dispatcher;
	import com.uralys.tribes.constants.Session;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.events.AccountEvent;
	import com.uralys.tribes.pages.Map;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class AccountManager{

		//============================================================================================//

		private var dispatcher:Dispatcher = new Dispatcher();	

		private static var instance:AccountManager = new AccountManager();
		public static function getInstance():AccountManager{
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
		
		public function register(email:String, password:String):void{
			var event:AccountEvent = new AccountEvent(AccountEvent.REGISTER);
			
			event.email = email;
			event.password = password;
			
			dispatcher.generator = AccountEvent;
			dispatcher.dispatchEvent( event );
		}

		public function login(email:String, password:String):void{
			var event:AccountEvent = new AccountEvent(AccountEvent.LOGIN);
			
			event.email = email;
			event.password = password;
			
			dispatcher.generator = AccountEvent;
			dispatcher.dispatchEvent( event );
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		public function resultLogin(message:Object):void{
			
			if(message == "WRONG_PWD")
				trace("authentication failed");
			else{
				Session.PLAYER_UID = message as String;
				Pager.getInstance().goToPage(Map);
			}
		}
	}
}