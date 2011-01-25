
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Weapon")]
	public class Weapon
	{	


		public function get weaponUID():String {
			return _weaponUID;
		}
	
		public function set weaponUID(o:String):void {
			_weaponUID = o;
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
	
		public function get goldPrice():int {
			return _goldPrice;
		}
	
		public function set goldPrice(o:int):void {
			_goldPrice = o;
		}
	
		public function get value():int {
			return _value;
		}
	
		public function set value(o:int):void {
			_value = o;
		}
	
		protected var _weaponUID:String;
		protected var _wood:int;
		protected var _iron:int;
		protected var _goldPrice:int;
		protected var _value:int;

	
	}
}
