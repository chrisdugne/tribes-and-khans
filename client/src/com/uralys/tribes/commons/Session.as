package com.uralys.tribes.commons
{
	import com.uralys.tribes.components.CityHeader;
	import com.uralys.tribes.entities.Ally;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.pages.Board;
	
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.formatters.DateFormatter;
	
	
	public class Session extends Object
	{ 
		//=====================================================//

		import com.uralys.tribes.entities.UralysProfile;
		import com.uralys.tribes.entities.Player;

		//=====================================================//
		
		[Bindable] public static var VERSION:String = "1.3.49";
		[Bindable] public static var LOGGED_IN:Boolean = false; 
		[Bindable] public static var CONNECTED_TO_FACEBOOK:Boolean = false;
		
		[Bindable] public static var isLocal:Boolean = false;
		[Bindable] public static var LANGUAGE:int;
		
		//=====================================================//
		
		[Bindable] public static var APPLICATION_WIDTH:int = 0;	
		[Bindable] public static var APPLICATION_HEIGHT:int = 0;	
		
		//=====================================================//

		[Bindable] public static var HEADER_HEIGHT:int = 30;	
		[Bindable] public static var FOOTER_HEIGHT:int = 30;	

		//=====================================================//

		[Bindable] public static var MAP_WIDTH:int = 880;	
		[Bindable] public static var MAP_HEIGHT:int = 540;	

		//=====================================================//
		
		[Bindable] public static var LOGGED_IN_FORCE_STEPS_DONE:Boolean = false; 
		[Bindable] public static var WAIT_FOR_SERVER:Boolean = false;
		[Bindable] public static var WAIT_FOR_CONNECTION:Boolean = false; 
		[Bindable] public static var WAIT_FOR_UNIT_SAVED:Boolean = false; // pour attendre le retour du service avant d'autoriser le deplacement
		[Bindable] public static var GAME_OVER:Boolean = false;
		[Bindable] public static var INSIDE_A_CITY:Boolean = false;
		
		//=====================================================//

		[Bindable] public static var allCitiesInSession:ArrayCollection = new ArrayCollection(); 
		
		//=====================================================//

		[Bindable] public static var uralysProfile:UralysProfile;
		[Bindable] public static var player:Player;
		[Bindable] public static var playerLoaded:Player; // le dernier joueur dont on a charge le profil TAK
		[Bindable] public static var allyLoaded:Ally; // la derniere ally dont on a charge le profil
		
		//=====================================================//

		[Bindable] public static var playersCitiesBoard:ArrayCollection; 
		[Bindable] public static var playersArmiesBoard:ArrayCollection; 
		[Bindable] public static var playersPopulationBoard:ArrayCollection; 
		[Bindable] public static var playersLandsBoard:ArrayCollection; 

		[Bindable] public static var alliesCitiesBoard:ArrayCollection; 
		[Bindable] public static var alliesArmiesBoard:ArrayCollection; 
		[Bindable] public static var alliesPopulationBoard:ArrayCollection; 
		[Bindable] public static var alliesLandsBoard:ArrayCollection; 
		
		//=====================================================//
		
		// liste de tous les items
		public static var ITEMS:ArrayCollection = new ArrayCollection();

		// liste de toutes les cases loadees
		public static var CELLS_LOADED:ArrayCollection = new ArrayCollection();
		
		//=====================================================//
		// Board
		
		[Bindable] public static var board:Board;

		public static var timer:Timer = new Timer( 1000 );

		// pour savoir si on click pour un deplacement de troupes
		//[Bindable] public static var CURRENT_SELECTION_IS_ARMY:Boolean = false;

		// nombre de tuiles chargees par coté.
		[Bindable] public static var nbTilesByEdge:int;

		[Bindable] public static var COORDINATE_X:int;
		[Bindable] public static var COORDINATE_Y:int;

		[Bindable] public static var LEFT_LIMIT_LOADED:int;
		[Bindable] public static var RIGHT_LIMIT_LOADED:int;
		[Bindable] public static var TOP_LIMIT_LOADED:int;
		[Bindable] public static var BOTTOM_LIMIT_LOADED:int;

		[Bindable] public static var CURRENT_SELECTION_X:int;
		[Bindable] public static var CURRENT_SELECTION_Y:int;

		[Bindable] public static var CENTER_X:int;
		[Bindable] public static var CENTER_Y:int;

		[Bindable] public static var CURRENT_CELL_SELECTED:Cell;

		//=====================================================//
		
		[Bindable] public static var SHOOTING:Boolean;
		[Bindable] public static var MOVE_A_UNIT:Boolean;
		[Bindable] public static var REMOVING_MOVES_ENABLE:Boolean;
		
		// cette liste est remplie par les case.refresh.
		// une fois remplie, on peut appeler le GameManager.deleteMoves
		public static var movesToDelete:ArrayCollection = new ArrayCollection();

		// contient toutes les unitUID
		public static var allUnits:ArrayCollection = new ArrayCollection();
		
		//=====================================================//
		// BoardDrawer
		
		// les coordonnees de la case qui est tout en haut à gauche du dernier chargement de cases
		public static  var firstCellX:int;
		public static  var firstCellY:int;

		// permet d'afficher ou de cacher les bar de progression sur les moves
		[Bindable] public static  var SHOW_MOVES_PROGRESS_BARS:Boolean = true;

		// permet d'afficher ou de cacher les contrees et frontieres
		[Bindable] public static  var LANDS_VISIBLE:Boolean = true;
		
		// Session.map[i][j] = case i,j 
		public static var map:Array;
		
		// les cercles des entities sont dessinees sur la map
		[Bindable] public static var DRAW_DETAILS:Boolean = true;
		
		// les noms des entities sont ecrites sur la map
		[Bindable] public static var DRAW_TEXTS:Boolean = true;
		
		// les images des entities sont dessinees sur la map
		[Bindable] public static var DRAW_IMAGES:Boolean = true;
		
		//=====================================================//
		
		[Bindable] public static var dateFormatter : DateFormatter = new DateFormatter();
		[Bindable] public static var timeFormatter : DateFormatter = new DateFormatter();
		
	}
}