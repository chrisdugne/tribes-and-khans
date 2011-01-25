
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Merchant")]
	public class Merchant
	{	

		public function get merchantUID():String {
			return _merchantUID;
		}
	
		public function set merchantUID(o:String):void {
			_merchantUID = o;
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
			return _radius;
		}
	
		public function set radius(o:int):void {
			_radius = o;
		}
	
		protected var _merchantUID:String;
		protected var _size:int;
		protected var _speed:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _x:int;
		protected var _y:int;
		protected var _radius:int;

	
	}
}
