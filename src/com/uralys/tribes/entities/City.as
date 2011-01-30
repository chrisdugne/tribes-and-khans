
package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

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

		public function get forgeUID():String {
			return _forgeUID;
		}
	
		public function set forgeUID(o:String):void {
			_forgeUID = o;
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
	
		public function get peopleCreatingWheat():int {
			return _peopleCreatingWheat;
		}
	
		public function set peopleCreatingWheat(o:int):void {
			_peopleCreatingWheat = o;
		}
	
		public function get peopleCreatingWood():int {
			return _peopleCreatingWood;
		}
	
		public function set peopleCreatingWood(o:int):void {
			_peopleCreatingWood = o;
		}
	
		public function get peopleCreatingIron():int {
			return _peopleCreatingIron;
		}
	
		public function set peopleCreatingIron(o:int):void {
			_peopleCreatingIron = o;
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
			return _population/50;
		}
	
		public function get equipmentStock():ArrayCollection {
			return _equipmentStock;
		}
	
		public function set equipmentStock(o:ArrayCollection):void {
			_equipmentStock = o;
		}
	
		public function get smiths():ArrayCollection {
			return _smiths;
		}
	
		public function set smiths(o:ArrayCollection):void {
			_smiths = o;
		}
	
		protected var _cityUID:String;
		protected var _forgeUID:String;
		protected var _name:String;
		protected var _population:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _peopleCreatingWheat:int;
		protected var _peopleCreatingWood:int;
		protected var _peopleCreatingIron:int;
		protected var _gold:int;
		protected var _x:int;
		protected var _y:int;
		protected var _equipmentStock:ArrayCollection;
		protected var _smiths:ArrayCollection;


	}
}
