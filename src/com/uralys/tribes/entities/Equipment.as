
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
	
		public function get item():Item {
			return _item;
		}
	
		public function set item(o:Item):void {
			_item = o;
		}
	
		public function get size():int {
			return _size;
		}
	
		public function set size(o:int):void {
			_size = o;
		}
	
		protected var _equimentUID:String;
		protected var _item:Item;
		protected var _size:int;


	}
}
