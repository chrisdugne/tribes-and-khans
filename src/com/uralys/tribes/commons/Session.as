package com.uralys.tribes.commons
{
	import mx.collections.ArrayCollection;
	
	
	public class Session{ 
		
		import com.uralys.tribes.entities.UralysProfile;
		import com.uralys.tribes.entities.Profil;
		import com.uralys.tribes.entities.Player;
		import mx.core.FlexGlobals;
		
		//=====================================================//
		
		[Bindable] public static var VERSION:String = "1.0.24";
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
		[Bindable] public static var currentPlayer:Player;
		
		//=====================================================//
		// liste des currentgames
		public static var GAMES_PLAYING:ArrayCollection = new ArrayCollection();
		
		// liste de tous les items
		public static var ITEMS:ArrayCollection = new ArrayCollection();
		
		//=====================================================//
		// Home : boolean pour charger la premiere liste des current games
		[Bindable] public static var firstHomeLoadingDone:Boolean = false;
		
		//=====================================================//
		// Board
		
		// pour que le tour soit bien sauvegarde avant de faire le refresh games+profil
		[Bindable] public static var TURN_SAVING_DONE:Boolean = true;

		// pour savoir si on click pour un deplacement de troupes
		[Bindable] public static var CURRENT_SELECTION_IS_ARMY:Boolean = false;
		
		//=====================================================//
		// GameDetailsRenderer
		
		//couleur en session
		[Bindable] public static var gameColor:Number;
		
		//temps quil reste avant la fin du tour
		[Bindable] public static var remainingTime:String;
		
		//=====================================================//
		// BoardDrawer
		
		// les cercles des entities sont dessinees sur la map
		[Bindable] public static var DRAW_DETAILS:Boolean = true;
		
		// les noms des entities sont ecrites sur la map
		[Bindable] public static var DRAW_TEXTS:Boolean = true;
		
		// les images des entities sont dessinees sur la map
		[Bindable] public static var DRAW_IMAGES:Boolean = false;
		
	}
}