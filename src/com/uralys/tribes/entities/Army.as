
package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	
	import mx.collections.ArrayCollection;
	
	import spark.primitives.Ellipse;
	import spark.primitives.Line;
	import spark.primitives.Rect;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Army")]
	public class Army
	{	

		public function get armyUID():String {
			return _armyUID;
		}
	
		public function set armyUID(o:String):void {
			_armyUID = o;
		}
	
		public function get size():int {
			return _size;
		}
	
		public function set size(o:int):void {
			_size = o;
		}
	
		public function get speed():int {
			return _speed;
		}
	
		public function set speed(o:int):void {
			_speed = o;
		}
	
		public function get value():int {
			return _value;
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
		
		protected var _equipments:ArrayCollection = new ArrayCollection();
		protected var _moves:ArrayCollection = new ArrayCollection();
		protected var _armyUID:String;
		protected var _size:int;
		protected var _speed:int;
		protected var _value:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _x:int;
		protected var _y:int;
		protected var _radius:int;
		
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
		public var ellipseTo:Ellipse;
		public var lineTo:Line;
		public var ellipseToIsDrawn:Boolean = false;
		public var lineToIsDrawn:Boolean = false;
		
		public var landExpected:int = -1;
		public var tmpLandSquare:Rect;
		public var armyCircle:Ellipse;
		
	}
}
