
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.City")]
	public class City
	{	

		public function get cityUID():String {
			return _cityUID;
		}
	
		public function set cityUID(o:String):void {
			_cityUID = o;
		}
	
		public function get name():String {
			return _name;
		}
	
		public function set name(o:String):void {
			_name = o;
		}
	
		public function get population():int {
			return _population;
		}
	
		public function set population(o:int):void {
			_population = o;
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
	
		public function get equipmentStock():ArrayCollection {
			return _equipmentStock;
		}
	
		public function set equipmentStock(o:ArrayCollection):void {
			_equipmentStock = o;
		}
	
		protected var _cityUID:String;
		protected var _name:String;
		protected var _population:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _x:int;
		protected var _y:int;
		protected var _radius:int;
		protected var _equipmentStock:ArrayCollection;


	}
}
