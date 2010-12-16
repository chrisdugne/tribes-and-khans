package com.uralys.tribes.commons
{


public class Session{

	import com.uralys.tribes.entities.Profil;

	//=====================================================//

	[Bindable] public static var VERSION:String = "1.0.1";
	[Bindable] public static var isLocal:Boolean = false;

	//=====================================================//

	[Bindable] public static var WAIT_FOR_SERVER:Boolean = false;
	
	//=====================================================//
	
	[Bindable] public static var profil:Profil;
	
}
}