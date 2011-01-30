

package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Smith")]
	public class Smith
	{	
	
		public function get smithUID():String {
			return _smithUID;
		}
	
		public function set smithUID(o:String):void {
			_smithUID = o;
		}
	
		public function get item():Item {
			return _item;
		}
	
		public function set item(o:Item):void {
			_item = o;
		}
	
		public function get people():int {
			return _people;
		}
	
		public function set people(o:int):void {
			_people = o;
		}
	
		protected var _smithUID:String;
		protected var _item:Item;
		protected var _people:int;
		
	}
}
