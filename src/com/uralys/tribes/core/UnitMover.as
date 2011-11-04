package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.components.UnitRenderer;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.utils.Map;
	import com.uralys.utils.Utils;
	
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.utils.ObjectUtil;

	public class UnitMover
	{
		public function UnitMover(){}
		
		//  - instance ================================================================================
		
		private static var instance : UnitMover = new UnitMover(); 
		
		public static function getInstance():UnitMover{
			return instance;
		}
		
		// - unit ============================================================================================
		
 		private var _unit:Unit;
		
		[Bindable]
		public function get unit():Unit
		{
			return _unit;
		}
		
		public function set unit(value:Unit):void
		{
			_unit = value;
		}
		
		// ============================================================================================

		private static var loop:Number = 0;
		private static var timers:Map = new Map();
		
		// ============================================================================================
	
		public function resetTimers():void
		{
			for each(var timer:Timer in timers.keySet()){
				timer.stop();
			}
			
			timers.removeAll();
		}
			
		// ============================================================================================

		public function listenTo(cell:Cell):void
		{
			var timeToWait:Number;
			if(cell.timeToChangeUnit > 0){
				timeToWait = cell.timeToChangeUnit - new Date().getTime();
				trace("listening cell.timeToChangeUnit " + cell.cellUID);
			}
			else if(cell.timeFromChallenging > 0){ 
				timeToWait = cell.timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS - new Date().getTime();
				trace("listening cell.timeFromChallenging " + cell.cellUID);
			}
			else{
				trace(cell.cellUID + " does not listen anything");
				return;
			}
			
			if(timeToWait > 0){
				trace("adding timer, timeToWait : " + timeToWait);
				var t:Timer = new Timer(timeToWait, 1);
				t.addEventListener(TimerEvent.TIMER_COMPLETE, pawnTimerIsDone);
				t.start();
				
				timers.put(t, cell);
				
				// on enleve la presence de la barre pour les pions qui ne sont pas les siens
				if(cell.visibleUnit.player.playerUID != Session.player.playerUID)
					cell.pawn.timeTo = -1;
				
				cell.pawn.resetProgress();
			}
			else
				trace("timeToWait < 0");
		}
		
		protected static function pawnTimerIsDone(e:TimerEvent):void
		{
			var cell:Cell = timers.get(e.currentTarget) as Cell;
			timers.remove(e.currentTarget);
			
			GameManager.getInstance().refreshCellFromServer(cell);
		}
	
		//----------------------------------------------------------------------------------------//
		
		// true losrque le joueur a rajouté au moins un move dans le deplacement de l'unite qu'il deplace.
		[Bindable] public static var newMoveAdded:Boolean = false;
		
		public function validateUnitMoves(cancel:Boolean):void
		{
			Session.MOVE_A_UNIT = false;
			Session.REMOVING_MOVES_ENABLE = true;
			
			if(!cancel){
				if(removingMoves)
					unit.moves.getItemAt(unit.moves.length-1).timeTo = -1;
				
				validatePendingMoves();					
				GameManager.getInstance().updateUnit(unit);
				
			}
			else if(removingMoves){
				for(var i:int = movesBeingRemoved.length-1; i >= 0; i--){
					unit.moves.addItem(movesBeingRemoved[i]);
				}
				
				movesBeingRemoved = [];
			}
			
			newMoveAdded = false;
			removingMoves = false;
			
			BoardDrawer.getInstance().removeAllUnitMovesImages();
			GameManager.getInstance().refreshUnit(unit);
		}
		
		private var removingMoves:Boolean = false;
		private var movesBeingRemoved:Array = [];
		public function deleteLastMove():void
		{
			removingMoves = true;
			newMoveAdded = true;
			
			movesBeingRemoved.push(unit.removeLastMove());
			refreshCurrentMoves();
		}
		
		// ============================================================================================

		/*
		* supprime les moves perimes de la liste
		*/
		public function refreshMoves(unit:Unit):void
		{
			trace("refreshMoves : " + unit.moves.length + " moves");
			
			var numOfLastIndexToRemove:int = -1;
			var now:Number = new Date().getTime();
			
			for each(var move:Move in unit.moves)
			{
				if(now > move.timeTo && move.timeTo != -1)
					numOfLastIndexToRemove++;
				else
					break;
			}
			
			for(var i:int = 0; i <= numOfLastIndexToRemove; i++){
				GameManager.getInstance().deleteMove(unit.moves.getItemAt(0).moveUID);
				unit.moves.removeItemAt(0);
			}
			
			trace("refreshMoves DONE");
		}
		
		// ============================================================================================
		
		// click sur le bouton deplacer
		public function prepareToMoveUnit(unit:Unit):void
		{
			this.unit = unit;
			
			if(Session.MOVE_A_UNIT)
				return;
			
			Session.MOVE_A_UNIT = true;
			
			refreshCurrentMoves();
		}
		
		//---------------------------------------------------------------------//
		
		private function refreshCurrentMoves():void
		{
			resetPendingMoves();
			BoardDrawer.getInstance().removeAllUnitMovesImages();
			
			var xPreviousMove:int = -1;
			var yPreviousMove:int = -1;
			var now:Number = new Date().getTime();
			
			for each(var move:com.uralys.tribes.entities.Move in unit.moves)
			{
				var displayProgress:Boolean = move.timeFrom < now && move.timeTo > now;
				BoardDrawer.getInstance().addMoveImages(move, xPreviousMove, yPreviousMove, displayProgress);
				
				xPreviousMove = Utils.getXFromCellUID(move.cellUID);
				yPreviousMove = Utils.getYFromCellUID(move.cellUID); 
			}	
		}
		
		private var movesPending:ArrayCollection;
		private var moveBeginsNow:Boolean;
		
		public function validatePendingMoves()
		{
			var previousMove:com.uralys.tribes.entities.Move = movesPending.getItemAt(0) as com.uralys.tribes.entities.Move;
			movesPending.removeItemAt(0);
			unit.removeLastMove(); // movesPending commence à partir du lastMove deja. 
			unit.moves.addItem(previousMove); // on met le bon lastMove (celui qui a ete trouve pour initialiser movesPending)
			
			if(moveBeginsNow){
				previousMove.timeFrom = new Date().getTime();
			}
			
			for each(var newMove:Move in movesPending)
			{
				var timeFrom:Number = previousMove.timeFrom + Numbers.BASE_TIME_PER_MOVE_MILLIS * Numbers.BASE_SPEED/unit.speed;
				
				newMove.timeFrom = timeFrom;
				newMove.timeTo = -1;
				previousMove.timeTo = timeFrom;
				
				unit.moves.addItem(newMove);
				
				// ajout du newMove dans les recordedMoves de la case courante
				//var caseSelected:Cell = Session.map[Utils.getXFromCaseUID(newMove.cellUID)][Utils.getYFromCaseUID(newMove.cellUID)] as Cell;
				//caseSelected.recordedMoves.addItem(newMove);
				
				// refresh du recordedMove qui correspond a lastMove sur la case du lastMove pour enregistrer le nouveau timeTo
				//var previousMoveX:int = Utils.getXFromCaseUID(previousMove.cellUID);
				//var previousMoveY:int = Utils.getYFromCaseUID(previousMove.cellUID);
				//var caseOfLastMove:Cell = Session.map[previousMoveX][previousMoveY] as Cell;
				//caseOfLastMove.refreshRecordedMove(previousMove);
				
				previousMove = newMove;
				moveBeginsNow = false;
				
				unit.cellUIDExpectedForLand = newMove.cellUID;
			}
		}


		public function resetPendingMoves()
		{
			trace("resetPendingMoves");
			movesPending = new ArrayCollection();
			moveBeginsNow = unit.moves.length == 1;
			
			// manipulation d'une unite : initialisation du deplacement
			var lastMove:com.uralys.tribes.entities.Move = unit.lastMove;
			movesPending.addItem(lastMove);
			
			lastMoveIsInCity = false;
			
			try{
				// si l'unite VA dans une ville 
				// et que la finalCase n'est PAS cette ville (sinon nouveau depart depuis la ville)
				if(unit.moves.length > 1
				&& unit.cellUIDExpectedForLand != (Session.map[lastMove.getX()][lastMove.getY()] as Cell).cellUID
				&& (Session.map[lastMove.getX()][lastMove.getY()] as Cell).city != null)
				{
					lastMoveIsInCity = true;
				}
			}
			catch(e:Error){}
			
			trace("lastMoveIsInCity : " + lastMoveIsInCity);
		}

		private var lastMoveIsInCity:Boolean = false;
		public function recordMove():void
		{
			if(lastMoveIsInCity){
				FlexGlobals.topLevelApplication.message(Translations.CITY_STOP.getItemAt(Session.LANGUAGE));
				newMoveAdded =  true; 
				return;
			}
			
			var lastMove:com.uralys.tribes.entities.Move = movesPending.getItemAt(movesPending.length-1) as com.uralys.tribes.entities.Move;
			var lastMoveX:int = Utils.getXFromCellUID(lastMove.cellUID);
			var lastMoveY:int = Utils.getYFromCellUID(lastMove.cellUID);
			
			var distance:int = Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y);
			
			if(movesPending.length == 10){
				FlexGlobals.topLevelApplication.message(Translations.LIMIT_NB_MOVES.getItemAt(Session.LANGUAGE));
				
				newMoveAdded =  true; 
				return;
			}
			else if(distance > 2 || lastMoveY == Session.COORDINATE_Y){
				FlexGlobals.topLevelApplication.message(Translations.TOO_LONG.getItemAt(Session.LANGUAGE));
				newMoveAdded =  true; 
				return;
			}
			else if(Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y) > 0){
				
				// ajout d'un move
				var newMove:com.uralys.tribes.entities.Move = new com.uralys.tribes.entities.Move();
				
				newMove.initNewMove(unit.unitUID, Session.COORDINATE_X, Session.COORDINATE_Y);
				movesPending.addItem(newMove);
				
				// refresh highlight images et rajoute les listeners sur les moves actifs
				BoardDrawer.getInstance().addMoveImages(newMove, lastMove.getX(), lastMove.getY(), false);
				
				// on ne peut plus enlever le dernier move, puisquon a rajoute des movesPendings
				Session.REMOVING_MOVES_ENABLE = false
				
				try{
					if((Session.map[Session.COORDINATE_X][Session.COORDINATE_Y] as Cell).city != null){
						lastMoveIsInCity = true;
					}
				}
				catch(e:Error){}
				
				newMoveAdded =  true; 
				return;
			}
			
			newMoveAdded =  false; 
		}
		
		// ============================================================================================
		
		// click sur le bouton tirer
		public function prepareShoot(unitRenderer:UnitRenderer):void
		{
			this.unit = unitRenderer.unit;
			unitRendererShooting = unitRenderer;
			
			if(Session.SHOOTING)
				return;
			
			Session.SHOOTING = true;
			
			BoardDrawer.getInstance().drawBowsToShoot();
		}
		
		//--------------------------------------------------------------------------//
		
		[Bindable] private static var unitRendererShooting:UnitRenderer;
		
		public function shoot():void
		{
			unitRendererShooting.refresh();

			var cellShooted:Cell = Session.map[Session.COORDINATE_X][Session.COORDINATE_Y];
			var shooter:Unit = Session.CURRENT_CELL_SELECTED.army;
			var target:Unit;
			
			if(cellShooted.army != null && cellShooted.army.player.playerUID != Session.player.playerUID){
				target = cellShooted.army;
			}
			else if(cellShooted.caravan != null && cellShooted.caravan.player.playerUID != Session.player.playerUID){
				target = cellShooted.caravan;
			}
			
			if(target != null){
				unit.lastShotTime = new Date().getTime();
				GameManager.getInstance().shoot(shooter, target, cellShooted.cellUID);
			}

			unitRendererShooting.cancelShoot();
		}

	}
}