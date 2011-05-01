package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Session;
	
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
		protected var _recordedMoves:ArrayCollection;
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
		
		
		public var armies:ArrayCollection = new ArrayCollection();
		public var merchants:ArrayCollection = new ArrayCollection();
		public var imageUnit:Image;
		
		
		public function refresh():Boolean{
			
			var foundUnitsOnThisCase:Boolean = false;
			var now:Number = new Date().getTime();
			armies.removeAll();
			merchants.removeAll();
				
			for each(var move:Move in recordedMoves){
				if(move.timeFrom < now && (move.timeTo > now || move.timeTo == -1))
				{
					foundUnitsOnThisCase = true;

					var unit:Unit = getUnit(move.unitUID);
					var unitInPlayer:Unit = Session.player.getUnit(unit.unitUID);
					
					if(unitInPlayer != null)
						unitInPlayer.currentCaseUID = _caseUID;
					
					switch(unit.type){
						case 1:
							armies.addItem(unit);
							break;
						case 2:
							merchants.addItem(unit);
							break;
					}
				}
			}
			
			return foundUnitsOnThisCase;
		}
		
		//--------------------------------------------------------------//
		
		public function getUnit(unitUID:String):Unit{
			
			for each(var unit:Unit in _units){
				if(unit.unitUID == unitUID || unit.unitUID.substr(4) == unitUID)
					return unit;
			}
			
			return null;
		}	
		
	}
}