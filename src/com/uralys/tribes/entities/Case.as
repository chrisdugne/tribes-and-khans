package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.UnitMover;
	import com.uralys.tribes.managers.GameManager;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	
	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Case")]
	public class Case
	{
		//--------------------------------------------------------------//

		public function Case(i:int = 0, j:int = 0){
			_x = i;
			_y = j;
			
			if(Math.abs(i-j)%2 !=0){
				// la difference entre x et y n'est pas paire
				// c'est une 'non case' : un hexagone intermediaire qui ne fait pas partie du plateau
				_type = -1;
				_caseUID = "NON-CASE";
			}
			else{
				_type = 0;
				_caseUID = "empty";
			}
		}
		
		//--------------------------------------------------------------//
		
		protected var _caseUID:String;
		protected var _x:int;
		protected var _y:int;
		protected var _recordedMoves:ArrayCollection = new ArrayCollection();
		protected var _units:ArrayCollection;
		protected var _type:int; // type = -1 pour les 'non-cases'
		protected var _city:City;
		protected var _landOwner:Player;
		
		//--------------------------------------------------------------//
		
		
		public function get caseUID():String {
			return _caseUID;
		}
		
		public function set caseUID(o:String):void {
			_caseUID = o;
		}
		
		public function get x():int {
			return _x;
		}
		
		public function set x(o:int):void {
			_x = o;
		}
		
		public function get y():int {
			return _y;
		}
		
		public function set y(o:int):void {
			_y = o;
		}
		
		public function get recordedMoves():ArrayCollection {
			return _recordedMoves;
		}
		
		public function set recordedMoves(o:ArrayCollection):void {
			_recordedMoves = o;
		}
		
		public function get units():ArrayCollection {
			return _units;
		}
		
		public function set units(o:ArrayCollection):void {
			_units = o;
		}
		
		public function get type():int {
			return _type;
		}
		
		public function set type(o:int):void {
			_type = o;
		}
		
		public function get city():City {
			return _city;
		}
		
		public function set city(o:City):void {
			_city = o;
		}
		
		public function get landOwner():Player {
			return _landOwner;
		}
		
		public function set landOwner(o:Player):void {
			_landOwner = o;
		}
		
		//--------------------------------------------------------------//
		// Flex only
		
		public function refreshRecordedMove(moveToRefresh:Move):void
		{
			for each(var recordedMove:Move in recordedMoves){
				if(recordedMove.moveUID == moveToRefresh.moveUID){
					recordedMove.timeTo = moveToRefresh.timeTo;
					return;
				}
			}
		}

		//----------------------------------------//
		
		public var army:Unit;
		public var merchants:Unit;
		public var imageUnit:Image;
		public var refreshAlreadyHappened:Boolean = false;
		
		public function tryRefresh():Boolean{
			if(!refreshAlreadyHappened)
				return forceRefresh();
			
			return false;
		}

		public function forceRefresh(globalRefresh:Boolean = false):Boolean{
			var foundUnitsOnThisCase:Boolean = false;
			var now:Number = new Date().getTime();
			var recordedMovesToDelete:ArrayCollection = new ArrayCollection();
			
			if(recordedMoves != null && recordedMoves.length > 0){
				trace("**");
				trace("refreshing moves for " + _caseUID);
			}
			
			army = null;
			merchants = null;
				
			for each(var move:Move in recordedMoves){
				
				var unitInPlayer:Unit = Session.player.getUnit(move.unitUID);
				var unit:Unit = unitInPlayer == null ? getUnit(move.unitUID) : unitInPlayer;
				
				if(unitInPlayer != null){
					trace("unit.currentCaseUID : " + _caseUID);
					unit.currentCaseUID = _caseUID;
					unit.ownerStatus = Unit.PLAYER;
				}
				else{
					// plus tard, pour les ally, on va devoir checker si unit.playerUID est bien dans notre alliance.
					unit.ownerStatus = Unit.ENNEMY;				
				}
				
				if(globalRefresh){
					GameManager.getInstance().registerUnitInSession(unit);
				}
				
				trace("unit : " + unit.unitUID);
				trace("unit.status : " + unit.status);
				trace("move.timeFrom : " + move.timeFrom);
				trace("move.timeTo : " + move.timeTo);
				trace("now : " + now);
				
				if(unit.status != Unit.DESTROYED && move.timeFrom <= now && (move.timeTo > now || move.timeTo == -1))
				{
					trace("move actif : " + move.moveUID);
					
					// c'est le move actif, on recupere les unites qui sont sur la case
					foundUnitsOnThisCase = true;
					
					switch(unit.type){
						case 1:
							army = unit;
							break;
						case 2:
							merchants = unit;
							break;
					}
					
					unit.isModified = false;
				}
				else if(move.timeTo != -1 && move.timeTo < now){
					// move perimé
					trace("move périmé : " + move.moveUID);
					
					if(unitInPlayer == null){
						// on refresh unit dans Session.player.units : on enleve ce move terminé
						var indexToRemove:int = -1;
						for each (var moveInUnit:Move in unit.moves)
						{
							if(moveInUnit.moveUID == move.moveUID){
								indexToRemove = unit.moves.getItemIndex(moveInUnit);
								break;
							}
						}
						
						unit.moves.removeItemAt(indexToRemove);
					}
					
					// on stock le move a supprimer de recordedMoves pour le supprimer apres cette boucle.
					recordedMovesToDelete.addItem(move);
					
					// on stock le move dans la liste à supprimer
					Session.movesToDelete.addItem(move);
				}
				else{
					//move futur
				}
			}
			
			// on degage les move perimes des recordedMoves de la case
			for each (var moveToRemoveFromRecordedMoves:Move in recordedMovesToDelete)
				recordedMoves.removeItemAt(recordedMoves.getItemIndex(moveToRemoveFromRecordedMoves));
			
			refreshAlreadyHappened = true;
			return foundUnitsOnThisCase;
		}
		
		//--------------------------------------------------------------//
		
		private function getUnit(unitUID:String):Unit{
			
			for each(var unit:Unit in _units)
			{
				// si c'est une unite ennemie, elle ne peut pas etre modifiee
				// sinon, on va la trouver dans 'unitInPlayer' et on prendra donc celle qui sera manipule dans la Session
				// donc pas d'importance sur le set false ici, juste pour que graphiquement ca colle si cest une unite ennemie 
				unit.isModified = false; 
				
				if(unit.unitUID == unitUID || unit.unitUID.substr(4) == unitUID)
					return unit;
			}
			
			return null;
		}	

		//--------------------------------------------------------------//
		
	}
}