package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.utils.Map;
	import com.uralys.utils.Utils;
	
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;

	public class UnitMover
	{
		public function UnitMover(){}
		
		//  - instance ================================================================================
		
		private static var instance : UnitMover = new UnitMover(); 
		
		public static function getInstance():UnitMover{
			return instance;
		}
		
		// ============================================================================================

		private static var loop:Number = 0;
		private static var timers:Map = new Map();
		
		// ============================================================================================
	
		public function refreshTimers():void
		{
			trace("refreshTimers for " + Session.allUnits.length); 
			for each(var timer:Timer in timers.keySet()){
				timer.stop();
			}
			
			timers.removeAll();
			
			for each(var unit:Unit in Session.allUnits){
				addTimer(unit.moves.toArray());
			}
		}
			
		// ============================================================================================

		
		public function addTimer(moves:Array):void
		{
			var now:Number = new Date().getTime();
			
			// on degage les moves perimés
			while(moves.length > 0  && moves[0].timeTo != -1 && moves[0].timeTo < now){
				moves.shift();
			}

			if(moves.length == 0)
				return;
			
			var firstMove:Move = moves[0] as Move; 
			
			if(firstMove.timeTo == -1)
				return;
			
			for each (var movesListened:Array in timers.values())
			{
				var moveListened:Move = movesListened[0] as Move;
				
				if(firstMove.moveUID == moveListened.moveUID){
					trace(firstMove.moveUID + " est deja ecouté, refreshing registered moves");
					timers.refresh(movesListened, moves);
					return;
				}
			}
			
			var timeToWait:Number = firstMove.timeTo - new Date().getTime();
			trace("listening move " + firstMove.moveUID);
			
			var t:Timer = new Timer(timeToWait, 1);
			t.addEventListener(TimerEvent.TIMER_COMPLETE, moveIsDone);
			t.start();
			
			timers.put(t, moves);
		}
		
		// ============================================================================================
		
		private function moveIsDone(e:TimerEvent):void
		{
			try{
				trace("moveIsDone");
				var moves:Array = timers.get(e.currentTarget) as Array;
				var moveToPerform:Move = moves.shift() as Move;
				
				timers.remove(e.currentTarget);
				
				// refresh de la case dont on part : suppression du move dans case.recordedMoves et dans unit.moves
				var caseToRefresh:Cell = Session.map[moveToPerform.getX()][moveToPerform.getY()] as Cell;		
				caseToRefresh.forceRefresh();
				
				// efface le 'pion' de la case
				BoardDrawer.getInstance().refreshUnits(caseToRefresh);
				
				// on recupere le suivant
				var newCurrentMove:Move = moves[0] as Move;
				
				// on ecoute le nouveau move si son timeTo n'est pas illimité
				if(newCurrentMove.timeTo != -1)
					addTimer(moves);
				
				
				// refresh de la nouvelle case active : ajout de l'unité sur la case
				var newCaseToRefresh:Cell = Session.map[newCurrentMove.getX()][newCurrentMove.getY()] as Cell;
				newCaseToRefresh.forceRefresh();
	
				// affiche le 'pion' de la case
				BoardDrawer.getInstance().refreshUnits(newCaseToRefresh);
				
				// refresh du status de toutes les unites
				GameManager.getInstance().refreshStatusOfAllUnitsInSession();
				
				// on refresh les villes au cas ou le deplacement fait partir/arriver une unite de/dans une ville
				Session.board.refreshUnitsInCity(moveToPerform.unitUID);
				
				// on refresh les moves si ils sont affiches
				if(Session.MOVE_A_UNIT){
					BoardDrawer.getInstance().removeAllUnitMovesImages();
					Session.board.onUnitClick();
				}
				
				// l'unité a bougé : au cas ou on enleve le 'build here'
				Session.board.buildCityForm.visible = false;
				Session.board.buildCityForm.includeInLayout = false;
				
			}
			catch(e:Error){
				trace("error on moveIsDone");
			}
		}

		// ============================================================================================
		
		private var movesPending:ArrayCollection;
		private var moveBeginsNow:Boolean;
		
		public function validatePendingMoves(unit:Unit)
		{
			var previousMove:com.uralys.tribes.entities.Move = movesPending.getItemAt(0) as com.uralys.tribes.entities.Move;
			movesPending.removeItemAt(0);
			unit.removeLastMove(); // movesPending commence à partir du lastMove deja. 
			unit.moves.addItem(previousMove); // on met le bon lastMove (celui qui a ete trouve pour initialiser movesPending)
			
			for each(var newMove:Move in movesPending)
			{
				var timeFrom:Number;
				
				if(moveBeginsNow){
					timeFrom = new Date().getTime() + Numbers.BASE_TIME_PER_MOVE_MILLIS * Numbers.BASE_SPEED/unit.speed;
				}
				else{
					timeFrom = previousMove.timeFrom + Numbers.BASE_TIME_PER_MOVE_MILLIS * Numbers.BASE_SPEED/unit.speed;
				}
				
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

		/*
		 * supprime les moves perimes de la liste
		 *
		 * return false si l'unité devait en fait etre detruite 
		 * (=> dans le cas du bug ou endTime = -1 mais pas de move avec timeTo = -1 et finalcase ne contient pas le move)
		 */
		public function refreshMoves(unit:Unit):Boolean
		{
			trace("refreshMoves : " + unit.moves.length + " moves");
			// hack ici arrive que des armees n'ont pas de move..
			// donc elles ne devraient pas apparaitre ici, leur endTime est faux ! (il ne devrait pas etre à -1)
			if(unit.moves.length == 0){
				trace("no moves !!!bug : unit.endTime ne devrait pas etre à -1");
				trace("donc cette armee aurait du etre destroyed");
				return false;
			}
			
			var numOfLastIndexToRemove:int = -1;
			var now:Number = new Date().getTime();
			
			for each(var move:Move in unit.moves)
			{
				trace("checking move : " + move.moveUID);
				if(now > move.timeTo && move.timeTo != -1)
					numOfLastIndexToRemove++;
				else
					break;
			}
			
			if(numOfLastIndexToRemove == (unit.moves.length-1)){
				trace("on va supprimer tous les moves");
				trace("donc le dernier n'a pas de timeTo à -1");
				trace("donc cette armee aurait du etre destroyed");
				return false;
			}
				
				
			for(var i:int = 0; i <= numOfLastIndexToRemove; i++)
				unit.moves.removeItemAt(0);
			
			
			trace("nbMoves remaining : " + unit.moves.length);
			unit.currentCaseUID = (unit.moves.getItemAt(0) as Move).cellUID;
			
			trace("unit.currentCaseUID : " + unit.currentCaseUID);
			trace("refreshMoves DONE");
			return true;
		}

		public function resetPendingMoves(unit:Unit)
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
		public function recordMove(unit:Unit):Boolean
		{
			if(lastMoveIsInCity){
				FlexGlobals.topLevelApplication.message(Translations.CITY_STOP.getItemAt(Session.LANGUAGE));
				return true;
			}
			
			var lastMove:com.uralys.tribes.entities.Move = movesPending.getItemAt(movesPending.length-1) as com.uralys.tribes.entities.Move;
			var lastMoveX:int = Utils.getXFromCaseUID(lastMove.cellUID);
			var lastMoveY:int = Utils.getYFromCaseUID(lastMove.cellUID);
			
			var distance:int = Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y);
			
			if(movesPending.length == 10){
				FlexGlobals.topLevelApplication.message(Translations.LIMIT_NB_MOVES.getItemAt(Session.LANGUAGE));
				return true;
			}
			else if(distance > 2 || lastMoveY == Session.COORDINATE_Y){
				FlexGlobals.topLevelApplication.message(Translations.TOO_LONG.getItemAt(Session.LANGUAGE));
				return true;
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
				
				return true;
			}
			
			return false;
		}
	}
}