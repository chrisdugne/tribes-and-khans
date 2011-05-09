package com.uralys.tribes.commons
{
	import com.uralys.tribes.entities.Case;
	import com.uralys.tribes.pages.Board;
	
	import mx.collections.ArrayCollection;
	
	
	public class Session{ 
		
		import com.uralys.tribes.entities.UralysProfile;
		import com.uralys.tribes.entities.Player;
		import mx.core.FlexGlobals;

		//=====================================================//
		
		[Bindable] public static var VERSION:String = "1.1.10";
		[Bindable] public static var LOGGED_IN:Boolean = false; 
		[Bindable] public static var CONNECTED_TO_FACEBOOK:Boolean = false;
		
		[Bindable] public static var isLocal:Boolean = false;
		[Bindable] public static var LANGUAGE:int;
		
		//=====================================================//
		
		[Bindable] public static var APPLICATION_WIDTH:int;	
		
		//=====================================================//

		[Bindable] public static var MAP_WIDTH:int = 500;	
		[Bindable] public static var MAP_HEIGHT:int = 500;	
		
		//=====================================================//
		
		[Bindable] public static var WAIT_FOR_SERVER:Boolean = false;
		[Bindable] public static var WAIT_FOR_CONNECTION:Boolean = false; 
		
		//=====================================================//
		
		[Bindable] public static var uralysProfile:UralysProfile;
		[Bindable] public static var player:Player;
		
		//=====================================================//
		// liste des currentgames
		public static var GAMES_PLAYING:ArrayCollection = new ArrayCollection();
		
		// liste de tous les items
		public static var ITEMS:ArrayCollection = new ArrayCollection();

		// liste de toutes les cases loadees
		public static var CASES_LOADED:ArrayCollection = new ArrayCollection();
		
		//=====================================================//
		// Home : boolean pour charger la premiere liste des current games
		[Bindable] public static var firstHomeLoadingDone:Boolean = false;
		
		//=====================================================//
		// Board
		
		[Bindable] public static var board:Board;

		// pour que le tour soit bien sauvegarde avant de faire le refresh games+profil
		[Bindable] public static var TURN_SAVING_DONE:Boolean = true;

		// pour savoir si on click pour un deplacement de troupes
		//[Bindable] public static var CURRENT_SELECTION_IS_ARMY:Boolean = false;

		[Bindable] public static var COORDINATE_X:int;
		[Bindable] public static var COORDINATE_Y:int;

		[Bindable] public static var CURRENT_SELECTION_X:int;
		[Bindable] public static var CURRENT_SELECTION_Y:int;

		[Bindable] public static var CURRENT_CASE_SELECTED:Case;
		[Bindable] public static var MOVE_A_UNIT:Boolean;
		
		// cette liste est remplie par les case.refresh.
		// une fois remplie, on peut appeler le GameManager.deleteMoves
		public static var movesToDelete:ArrayCollection = new ArrayCollection();
		
		//=====================================================//
		// GameDetailsRenderer
		
		//couleur en session
		[Bindable] public static var gameColor:Number;
		
		//temps quil reste avant la fin du tour
		[Bindable] public static var remainingTime:String;
		
		//=====================================================//
		// BoardDrawer
		
		// les coordonnees de la case qui est au centre du dernier chargement de cases
		public static  var centerX:int;
		public static  var centerY:int;
		
		// Session.map[i][j] = case i,j 
		public static var map:Array;
		
		// les cercles des entities sont dessinees sur la map
		[Bindable] public static var DRAW_DETAILS:Boolean = true;
		
		// les noms des entities sont ecrites sur la map
		[Bindable] public static var DRAW_TEXTS:Boolean = true;
		
		// les images des entities sont dessinees sur la map
		[Bindable] public static var DRAW_IMAGES:Boolean = true;
		
	}
}