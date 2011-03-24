
package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Conflict")]
	public class Conflict
	{	
		
		public function get conflictUID():String {
			return _conflictUID;
		}
		
		public function set conflictUID(o:String):void {
			_conflictUID = o;
		}
		
		public function get moveAllies():ArrayCollection {
			return _moveAllies; 
		}
		
		public function set moveAllies(o:ArrayCollection):void {
			_moveAllies = o;
		}
		
		public function get moveEnnemies():ArrayCollection {
			return _moveEnnemies;
		}
		
		public function set moveEnnemies(o:ArrayCollection):void {
			_moveEnnemies = o;
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
		
		public function get status():int {
			return _status;
		}
		
		public function set status(o:int):void {
			_status = o;
		}
		
		public function get report():String {
			return _report;
		}
		
		public function set report(o:String):void {
			_report = o;
		}
		
		protected var _conflictUID:String;
		protected var _moveAllies:ArrayCollection;
		protected var _moveEnnemies:ArrayCollection;
		protected var _x:int;
		protected var _y:int;
		protected var _status:int;
		protected var _report:String;

	}
}
