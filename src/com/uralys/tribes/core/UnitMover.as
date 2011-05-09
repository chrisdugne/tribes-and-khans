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
		
		public function addTimer(move:Move):void{
			
			for each (var moveListened:Move in timers.values()){
				if(move.moveUID == moveListened.moveUID){
					trace(move.moveUID + " est deja ecouté");
					return;
				}
			}
			
			trace("move.timeTo " + move.timeTo);
			var timeToWait:Number = move.timeTo - new Date().getTime();
			
			var t:Timer = new Timer(timeToWait, 1);
			t.addEventListener(TimerEvent.TIMER_COMPLETE, moveIsDone);
			t.start();
			
			timers.put(t, move);
		}
		
		// ============================================================================================
		
		private function moveIsDone(e:TimerEvent):void{
			trace("-----");
			trace("un move se produit !")
			
			var moveToPerform:Move = timers.get(e.currentTarget) as Move;
			var unit:Unit  = Session.player.getUnit(moveToPerform.unitUID);
			
			trace(moveToPerform.moveUID);
			
			timers.remove(e.currentTarget);
			
			// refresh de la case dont on part : suppression du move dans case.recordedMoves et dans unit.moves
			var caseToRefresh:Case = Session.map[moveToPerform.getX()][moveToPerform.getY()] as Case;		
			caseToRefresh.forceRefresh();
			
			// efface le 'pion' de la case
			BoardDrawer.getInstance().refreshUnits(caseToRefresh);
			
			// le nouveau move a ecouter :
			var newCurrentMove:Move = unit.moves.getItemAt(0) as Move;
			
			// on le met a l'ecoute dans le CronMover (ici) si il a un timeTo
			if(newCurrentMove.timeTo != -1)
				addTimer(newCurrentMove);
			
			// refresh de la nouvelle case active : ajout de l'unité sur la case
			var newCaseToRefresh:Case = Session.map[newCurrentMove.getX()][newCurrentMove.getY()] as Case;
			trace("newCaseToRefresh : " + newCaseToRefresh.caseUID);
			newCaseToRefresh.forceRefresh();

			// affiche le 'pion' de la case
			BoardDrawer.getInstance().refreshUnits(newCaseToRefresh);
			
			// sauvegarde de l'unite cote serveur
			GameManager.getInstance().updateUnit(unit);
			
			// on refresh les villes au cas ou le deplacement fait partir/arriver une unite de/dans une ville
			Session.board.refreshUnitsInCity();
			
			trace("move resolu");
			trace("-----");
		}

		// ============================================================================================
		
		public function recordMove(unit:Unit){
			
			// manipulation d'une unite : gestion du deplacement
			var lastMove:com.uralys.tribes.entities.Move = unit.moves.getItemAt(unit.moves.length-1) as com.uralys.tribes.entities.Move;
			var lastMoveX:int = Utils.getXFromCaseUID(lastMove.caseUID);
			var lastMoveY:int = Utils.getYFromCaseUID(lastMove.caseUID);
			
			var distance:int = Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y);
			
			if(distance > 2 || lastMoveY == Session.COORDINATE_Y){
				FlexGlobals.topLevelApplication.message("distance trop longue, choisir une case à cote du dernier deplacement");
			}
			else if(Math.abs(lastMoveX - Session.COORDINATE_X) + Math.abs(lastMoveY - Session.COORDINATE_Y) > 0){
				
				// ajout d'un move
				var newMove:com.uralys.tribes.entities.Move = new com.uralys.tribes.entities.Move();
				
				var timeFrom:Number;
				var timeTo:Number;
				
				
				if(unit.moves.length == 1){
					timeFrom = new Date().getTime() + Numbers.BASE_TIME_PER_MOVE_MILLIS * unit.speed/Numbers.BASE_SPEED;
				}
				else{
					timeFrom = lastMove.timeFrom + Numbers.BASE_TIME_PER_MOVE_MILLIS * unit.speed/Numbers.BASE_SPEED;
				}
				
				lastMove.timeTo = timeFrom;
				newMove.initNewMove(unit.unitUID, Session.COORDINATE_X, Session.COORDINATE_Y, timeFrom);
				unit.moves.addItem(newMove);
				
				// ajout du newMove dans les recordedMoves de la case courante
				var caseSelected:Case = Session.map[Session.COORDINATE_X][Session.COORDINATE_Y] as Case;
				caseSelected.recordedMoves.addItem(newMove);
				
				// refresh du recordedMove qui correspond a lastMove sur la case du lastMove pour enregistrer le nouveau timeTo
				var caseOfLastMove:Case = Session.map[lastMoveX][lastMoveY] as Case;
				caseOfLastMove.refreshRecordedMove(lastMove);
				
				// refresh highlight images et rajoute les listeners sur les moves actifs
				BoardDrawer.getInstance().removeAllUnitMovesImages();
				BoardDrawer.getInstance().addAllUnitMovesImages(unit);
			}
		}
	}
}