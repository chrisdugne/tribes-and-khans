
package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Unit")]
	public class Unit
	{	
		//==========================================================================//
		
		public function Unit(){}
		
		// on ne peut pas le mettre dans le constructeur, car BlazeDS l'utilise aussi
		public function initNewUnit(i:int = 0, j:int = 0):void{
			_currentCase = Session.map[i][j];
			_unitUID = "NEW_"+Session.player.uralysUID+"_"+(Session.player.units.length+1)+"_"+(new Date().getTime());
		}
		
		//==========================================================================//
		
		private var _status:int;
		private var _currentCase:Case;
		private var _playerUID:String;
		
		protected var _unitUID:String;
		protected var _size:int;
		protected var _speed:int;
		protected var _value:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _radius:int;
		
		protected var _equipments:ArrayCollection = new ArrayCollection();
		protected var _moves:ArrayCollection = new ArrayCollection();
		protected var _conflicts:ArrayCollection = new ArrayCollection();
		
		//==========================================================================//
		
		public function get unitUID():String {
			return _unitUID;
		}
	
		public function set unitUID(o:String):void {
			_unitUID = o;
		}
	
		public function get size():int {
			return _size;
		}
	
		public function set size(o:int):void {
			_size = o;
		}
	
		public function get currentCase():Case {
			return _currentCase;
		}
	
		public function set currentCase(o:Case):void {
			_currentCase = o;
		}
	
		public function get speed():int {
			return _speed;
		}
	
		public function set speed(o:int):void {
			_speed = o;
		}
	
		public function get value():int {
			return size 
				+ (bows > size ? size : bows) 
				+ (swords > size ? size : swords) *2 
				+ (armors > size ? size : armors) *3;
		}
	
		public function set value(o:int):void {
			_value = o;
		}
		
		public function get wheat():int {
			return _wheat;
		}
		
		public function set wheat(o:int):void {
			_wheat = o;
		}
		
		public function get wood():int {
			return _wood;
		}
		
		public function set wood(o:int):void {
			_wood = o;
		}
		
		public function get iron():int {
			return _iron;
		}
		
		public function set iron(o:int):void {
			_iron = o;
		}
		
		public function get gold():int {
			return _gold;
		}
		
		public function set gold(o:int):void {
			_gold = o;
		}
		
		public function get radius():int {
			return Math.sqrt(_size)/2 + 2; // min 2 pixels
		}
	
		public function set radius(o:int):void {
			_radius = o;
		}
		
		public function get equipments():ArrayCollection {
			return _equipments;
		}
		
		public function set equipments(o:ArrayCollection):void {
			_equipments = o;
		}
		
		public function get moves():ArrayCollection {
			return _moves;
		}
		
		public function set moves(o:ArrayCollection):void {
			_moves = o;
		}
		
		public function get conflicts():ArrayCollection {
			return _conflicts;
		}
		
		public function set conflicts(o:ArrayCollection):void {
			_conflicts = o;
		}
		
		public function get status():int {
			return _status;
		}
		
		public function set status(o:int):void {
			_status = o;
		}
		
		public function get playerUID():String {
			return _playerUID;
		}
		
		public function set playerUID(o:String):void {
			_playerUID = o;
		}
		
		
		//==========================================================================//

		private var _bows:int;
		private var _swords:int;
		private var _armors:int;
		
		public function get bows():int{
			return _bows;
		}

		public function set bows(o:int):void{
			_bows = o;
		}

		public function get swords():int{
			return _swords;
		}

		public function set swords(o:int):void{
			_swords = o;
		}

		public function get armors():int{
			return _armors;
		}

		public function set armors(o:int):void{
			_armors = o;
		}

		//==========================================================================//

		public function mayBuildAcity():Boolean{
			trace('mayBuildAcity ? ' + type);
			return type == 2 
				&& wheat >= Numbers.CITY_WHEAT_BASE_PRICE + size
				&& wood >= Numbers.CITY_WOOD_BASE_PRICE + size * 10
				&& iron >= Numbers.CITY_IRON_BASE_PRICE + size * 10
				&& gold >= Numbers.CITY_GOLD_BASE_PRICE;
		}
		
		//==========================================================================//

		public var type:int; // 1 armee,  2 marchand
		//public var ellipseTo:Ellipse;
		//public var lineTo:Line;
		//public var ellipseToIsDrawn:Boolean = false;
		//public var lineToIsDrawn:Boolean = false;
		
		public var landExpected:int = -1;
		//public var tmpLandSquare:Rect;
		//public var armyCircle:Ellipse;
		
	}
}