package com.uralys.tribes.commons
{


public class Session{

	import com.uralys.tribes.entities.UralysProfile;
	import com.uralys.tribes.entities.Profil;
	import mx.core.FlexGlobals;

	//=====================================================//

	[Bindable] public static var VERSION:String = "1.0.2";
	[Bindable] public static var LOGGED_IN:Boolean = false; 
	[Bindable] public static var CONNECTED_TO_FACEBOOK:Boolean = false;

	[Bindable] public static var isLocal:Boolean = false;
	[Bindable] public static var LANGUAGE:int;
	
	//=====================================================//

	[Bindable] public static var APPLICATION_WIDTH:int;	
	
	//=====================================================//

	[Bindable] public static var WAIT_FOR_SERVER:Boolean = false;
	[Bindable] public static var WAIT_FOR_CONNECTION:Boolean = false; 
	
	//=====================================================//
	
	[Bindable] public static var uralysProfile:UralysProfile;
	[Bindable] public static var profil:Profil;	
	
	
}
}