package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.UnitMover;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.tribes.renderers.Pawn;
	
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
		protected var _group:int;
		protected var _recordedMoves:ArrayCollection = new ArrayCollection();
		protected var _units:ArrayCollection;
		protected var _type:int; // type = -1 pour les 'non-cases'
		protected var _city:City;
		protected var _landOwner:Player;
		protected var _challenger:Player;
		protected var _timeFromChallenging:Number;
		
		//--------------------------------------------------------------//
		
		
		public function get timeFromChallenging():Number
		{
			return _timeFromChallenging;
		}

		public function set timeFromChallenging(value:Number):void
		{
			_timeFromChallenging = value;
		}

		public function get challenger():Player
		{
			return _challenger;
		}

		public function set challenger(value:Player):void
		{
			_challenger = value;
		}

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
		
		public function get group():int {
			return _group;
		}
		
		public function set group(o:int):void {
			_group = o;
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
		public var pawn:Pawn = new Pawn();
		public var imageGround:Image;
		public var imageHouses:ArrayCollection = new ArrayCollection();
		public var refreshAlreadyHappened:Boolean = false;
		
		public function tryRefresh():Boolean{
			if(!refreshAlreadyHappened)
				return forceRefresh();
			
			return false;
		}

		public function forceRefresh(globalRefresh:Boolean = false):Boolean
		{
			var foundUnitsOnThisCase:Boolean = false;
			var now:Number = new Date().getTime();
			var recordedMovesToDelete:ArrayCollection = new ArrayCollection();
			
			army = null;
			merchants = null;
			pawn.timeTo = -1;
			pawn.refreshProgress();
			
			for each(var move:Move in recordedMoves)
			{
				var unitInPlayer:Unit = Session.player.getUnit(move.unitUID);
				var unit:Unit = unitInPlayer == null ? getUnit(move.unitUID) : unitInPlayer;
				
				if(unit == null) // move futur
					continue;
				
				
				if(globalRefresh){
					GameManager.getInstance().registerUnitInSession(unit);
				}
				
				if(unit.status != Unit.DESTROYED && move.timeFrom <= now && (move.timeTo > now || move.timeTo == -1))
				{
					// c'est le move actif, on recupere les unites qui sont sur la case
					foundUnitsOnThisCase = true;
					
					pawn.unit = unit;
						
					if(unitInPlayer != null){
						unit.ownerStatus = Unit.PLAYER;
					}
					else{
						// plus tard, pour les ally, on va devoir checker si unit.playerUID est bien dans notre alliance.
						unit.ownerStatus = Unit.ENNEMY;				
					}
					
					switch(unit.type){
						case 1:
							army = unit;
							break;
						case 2:
							merchants = unit;
							break;
					}
					
					if(unit.ownerStatus == Unit.PLAYER && unit.status == Unit.FREE){
						// garder en tete qu'il y a un seul pion de visible
						// donc si il y a marchand et armee, on affiche la barre qui a le plus petit temps
						if(pawn.timeTo == -1 || pawn.timeTo > move.timeTo)
							pawn.timeTo = move.timeTo;
					}
					
					unit.isModified = false;
				}
				else if(move.timeTo != -1 && move.timeTo < now){
					// move perimé
					
					if(unit.ownerStatus == Unit.PLAYER){
						// on refresh unit dans Session.player.units : on enleve ce move terminé
						var indexToRemove:int = -1;
						for each (var moveInUnit:Move in unit.moves)
						{
							if(moveInUnit.moveUID == move.moveUID){
								indexToRemove = unit.moves.getItemIndex(moveInUnit);
								break;
							}
						}
						
						if(indexToRemove >= 0)
							unit.moves.removeItemAt(indexToRemove);
					}
					
					// on stock le move a supprimer de recordedMoves pour le supprimer apres cette boucle.
					recordedMovesToDelete.addItem(move);
					
					// on stock le move dans la liste à supprimer
					Session.movesToDelete.addItem(move);
				}
			}
			
			// on degage les move perimes des recordedMoves de la case
			for each (var moveToRemoveFromRecordedMoves:Move in recordedMovesToDelete)
				recordedMoves.removeItemAt(recordedMoves.getItemIndex(moveToRemoveFromRecordedMoves));
			
			// cette fois ci si le move actif a ete trouve, on set le timer (si c'est bien une unité du joueur)
			if(_challenger != null 
			&& ((army != null && army.ownerStatus == Unit.PLAYER) || (merchants != null && merchants.ownerStatus == Unit.PLAYER)))
			{
				trace("challenger : set timeTo : " + _timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS);
				pawn.status = Pawn.CONQUERING_LAND;
				pawn.timeTo = _timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS;
			}
			else
				pawn.status = Pawn.CLASSIC;
			
			pawn._case = this;
			pawn.refreshProgress();
			
			refreshAlreadyHappened = true;
			return foundUnitsOnThisCase;
		}
		
		//--------------------------------------------------------------//
		
		private function getUnit(unitUID:String):Unit
		{
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