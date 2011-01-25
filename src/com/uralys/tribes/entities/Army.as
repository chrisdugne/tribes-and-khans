
package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

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
			return _radius;
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
		
		protected var _equipments:ArrayCollection;
		protected var _armyUID:String;
		protected var _size:int;
		protected var _speed:int;
		protected var _value:int;
		protected var _x:int;
		protected var _y:int;
		protected var _radius:int;
	}
}
