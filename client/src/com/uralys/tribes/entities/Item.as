
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Item")]
	public class Item
	{	


		public function get itemUID():String {
			return _itemUID;
		}
	
		public function set itemUID(o:String):void {
			_itemUID = o;
		}
	
		public function get wood():int {
			return _wood;
		}
	
		public function set wood(o:int):void {
			_wood = o;
		}
	
		public function get name():String {
			return _name;
		}
	
		public function set name(o:String):void {
			_name = o;
		}
	
		public function get peopleRequired():int {
			return _peopleRequired;
		}
	
		public function set peopleRequired(o:int):void {
			_peopleRequired = o;
		}
	
		public function get iron():int {
			return _iron;
		}
	
		public function set iron(o:int):void {
			_iron = o;
		}
	
		public function get goldPriceBase():Number {
			return _goldPriceBase;
		}
	
		public function set goldPriceBase(o:Number):void {
			_goldPriceBase = o;
		}
	
		public function get goldPriceCurrent():Number {
			return _goldPriceCurrent;
		}
	
		public function set goldPriceCurrent(o:Number):void {
			_goldPriceCurrent = o;
		}
	
		public function get value():int {
			return _value;
		}
	
		public function set value(o:int):void {
			_value = o;
		}
	
		protected var _itemUID:String;
		protected var _name:String = "";
		protected var _peopleRequired:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _goldPriceBase:Number;
		protected var _goldPriceCurrent:Number;
		protected var _value:int;

	
	}
}
