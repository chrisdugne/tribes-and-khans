package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Case;
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
		
		public function addTimer(moves:Array):void
		{
			var firstMove:Move = moves[0] as Move; 
			trace("registering a timer for move " + firstMove.moveUID);
			trace("case :  " + firstMove.caseUID);
			
			for each (var moveToRegister:Move in moves){
				trace("move to register : " + moveToRegister.moveUID);
			}
			
			for each (var movesListened:Array in timers.values())
			{
				var moveListened:Move = movesListened[0] as Move;
				
				if(firstMove.moveUID == moveListened.moveUID){
					trace(firstMove.moveUID + " est deja ecouté");
					timers.refresh(movesListened, moves);
					return;
				}
			}
			
			var timeToWait:Number = firstMove.timeTo - new Date().getTime();
			
			var t:Timer = new Timer(timeToWait, 1);
			t.addEventListener(TimerEvent.TIMER_COMPLETE, moveIsDone);
			t.start();
			
			timers.put(t, moves);
		}
		
		// ============================================================================================
		
		private function moveIsDone(e:TimerEvent):void{
			trace("-----");
			trace("un move se produit !")
			
			var moves:Array = timers.get(e.currentTarget) as Array;
			var moveToPerform:Move = moves.shift() as Move;
			
			trace(moveToPerform.moveUID);
			
			for each (var moveToRegister:Move in moves){
				trace("moves listened : " + moveToRegister.moveUID);
			}
			
			timers.remove(e.currentTarget);
			
			// refresh de la case dont on part : suppression du move dans case.recordedMoves et dans unit.moves
			var caseToRefresh:Case = Session.map[moveToPerform.getX()][moveToPerform.getY()] as Case;		
			caseToRefresh.forceRefresh();
			
			// efface le 'pion' de la case
			BoardDrawer.getInstance().refreshUnits(caseToRefresh);
			
			// on recupere le suivant
			var newCurrentMove:Move = moves[0] as Move;
			trace("newCurrentMove to listen : " + newCurrentMove.getX()+","+newCurrentMove.getY());
			
			// on ecoute le nouveau move si son timeTo n'est pas illimité
			if(newCurrentMove.timeTo != -1)
				addTimer(moves);
			
			// refresh de la nouvelle case active : ajout de l'unité sur la case
			var newCaseToRefresh:Case = Session.map[newCurrentMove.getX()][newCurrentMove.getY()] as Case;
			newCaseToRefresh.forceRefresh();

			// affiche le 'pion' de la case
			BoardDrawer.getInstance().refreshUnits(newCaseToRefresh);
			
			// on refresh les villes au cas ou le deplacement fait partir/arriver une unite de/dans une ville
			Session.board.refreshUnitsInCity();
			
			trace("move resolu");
			trace("-----");
		}

		// ============================================================================================
		
		private var movesPending:ArrayCollection;
		private var moveBeginsNow:Boolean;
		
		public function validatePendingMoves(unit:Unit){
			trace("validatePendingMoves");
			var previousMove:com.uralys.tribes.entities.Move = movesPending.getItemAt(0) as com.uralys.tribes.entities.Move;

			
			movesPending.removeItemAt(0);
			
			for each(var newMove:Move in movesPending){
				
				var timeFrom:Number;
				
				if(moveBeginsNow){
					timeFrom = new Date().getTime() + Numbers.BASE_TIME_PER_MOVE_MILLIS * unit.speed/Numbers.BASE_SPEED;
				}
				else{
					timeFrom = previousMove.timeFrom + Numbers.BASE_TIME_PER_MOVE_MILLIS * unit.speed/Numbers.BASE_SPEED;
				}
				
				newMove.timeFrom = timeFrom;
				newMove.timeTo = -1;
				previousMove.timeTo = timeFrom;
				
				trace("previousMove : " + previousMove.getX()+","+previousMove.getY());
				trace(previousMove.timeFrom + " | " + previousMove.timeTo);
				trace("newMove : " + newMove.getX()+","+newMove.getY());
				trace(newMove.timeFrom + " | " + newMove.timeTo);
				trace("*");

				
				unit.moves.addItem(newMove);
				
				// ajout du newMove dans les recordedMoves de la case courante
				var caseSelected:Case = Session.map[Utils.getXFromCaseUID(newMove.caseUID)][Utils.getYFromCaseUID(newMove.caseUID)] as Case;
				caseSelected.recordedMoves.addItem(newMove);
				
				// refresh du recordedMove qui correspond a lastMove sur la case du lastMove pour enregistrer le nouveau timeTo
				var previousMoveX:int = Utils.getXFromCaseUID(previousMove.caseUID);
				var previousMoveY:int = Utils.getYFromCaseUID(previousMove.caseUID);
				var caseOfLastMove:Case = Session.map[previousMoveX][previousMoveY] as Case;
				caseOfLastMove.refreshRecordedMove(previousMove);
				
				previousMove = newMove;
				moveBeginsNow = false;
			}

			if(unit.moves.length > 1)
				UnitMover.getInstance().addTimer(unit.moves.toArray());
		}

		public function resetPendingMoves(unit:Unit){
			trace("resetPendingMoves");
			trace("moves already recorded : " + unit.moves.length);
			
			movesPending = new ArrayCollection();
			moveBeginsNow = unit.moves.length == 1;
			
			// manipulation d'une unite : initialisation du deplacement
			var lastMove:com.uralys.tribes.entities.Move = unit.moves.getItemAt(unit.moves.length-1) as com.uralys.tribes.entities.Move;
			movesPending.addItem(lastMove);
			
			trace("lastMove : " + lastMove.getX()+","+lastMove.getY());
			trace(lastMove.timeFrom + " | " + lastMove.timeTo);
		}

		public function recordMove(unit:Unit){
			trace("recordMove");
			
			var lastMove:com.uralys.tribes.entities.Move = movesPending.getItemAt(movesPending.length-1) as com.uralys.tribes.entities.Move;
			var lastMoveX:int = Utils.getXFromCaseUID(lastMove.caseUID);
			var lastMoveY:int = Utils.getYFromCaseUID(lastMove.caseUID);
			trace("lastMoveX : " + lastMoveX);
			trace("lastMoveY : " + lastMoveY);
			
			var distance:int = Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y);
			
			if(distance > 2 || lastMoveY == Session.COORDINATE_Y){
				FlexGlobals.topLevelApplication.message("distance trop longue, choisir une case à cote du dernier deplacement");
			}
			else if(Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y) > 0){
				
				// ajout d'un move
				var newMove:com.uralys.tribes.entities.Move = new com.uralys.tribes.entities.Move();
				
				newMove.initNewMove(unit.unitUID, Session.COORDINATE_X, Session.COORDINATE_Y);
				movesPending.addItem(newMove);
				
				// refresh highlight images et rajoute les listeners sur les moves actifs
				BoardDrawer.getInstance().addMoveImages(newMove, lastMove.getX(), lastMove.getY());
			}
		}
	}
}