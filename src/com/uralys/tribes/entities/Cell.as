package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.UnitMover;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.tribes.renderers.Pawn;
	import com.uralys.utils.Utils;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.utils.ObjectUtil;
	
	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Cell")]
	public class Cell
	{
		//--------------------------------------------------------------//

		public function Cell(i:int = 0, j:int = 0){
			_x = i;
			_y = j;
			
			if(Math.abs(i-j)%2 !=0){
				// la difference entre x et y n'est pas paire
				// c'est une 'non case' : un hexagone intermediaire qui ne fait pas partie du plateau
				_type = -1;
				_cellUID = "NON-CASE";
			}
			else{
				_type = 0;
				_cellUID = "empty";
			}
		}
		
		//--------------------------------------------------------------//
		
		protected var _cellUID:String;
		protected var _x:int;
		protected var _y:int;
		protected var _group:int;
		protected var _type:int; // type = -1 pour les 'non-cases'
		protected var _city:City;
		protected var _landOwner:Player;
		protected var _challenger:Player;
		protected var _timeFromChallenging:Number;
				
		protected var _timeToChangeUnit:Number;
		protected var _nextCellUID:String;
		protected var _army:Unit;
		protected var _caravan:Unit;
		
		
		//--------------------------------------------------------------//
		

		public function get nextCellUID():String
		{
			return _nextCellUID;
		}

		public function set nextCellUID(value:String):void
		{
			_nextCellUID = value;
		}

		public function get timeToChangeUnit():Number
		{
			return _timeToChangeUnit;
		}

		public function set timeToChangeUnit(value:Number):void
		{
			_timeToChangeUnit = value;
		}

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

		public function get cellUID():String {
			return _cellUID;
		}
		
		public function set cellUID(o:String):void {
			_cellUID = o;
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
		
		public function get caravan():Unit
		{
			return _caravan;
		}
		
		public function set caravan(value:Unit):void
		{
			_caravan = value;
		}
		
		public function get army():Unit
		{
			return _army;
		}
		
		public function set army(value:Unit):void
		{
			_army = value;
		}
		
		//----------------------------------------//
		
		public var pawn:Pawn = new Pawn();
		
		//--------------------------------------------------------------//
		
		public function get unit(){
			if(army != null)
				return army
			else if(caravan != null)
				return caravan;
			
			else return null;
		}
	}
}