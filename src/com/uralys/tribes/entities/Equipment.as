
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Equipment")]
	public class Equipment
	{	

	
		public function get equimentUID():String {
			return _equimentUID;
		}
	
		public function set equimentUID(o:String):void {
			_equimentUID = o;
		}
	
		public function get weaponUID():String {
			return _weaponUID;
		}
	
		public function set weaponUID(o:String):void {
			_weaponUID = o;
		}
	
		public function get size():int {
			return _size;
		}
	
		public function set size(o:int):void {
			_size = o;
		}
	
		protected var _equimentUID:String;
		protected var _weaponUID:String;
		protected var _size:int;


	}
}
