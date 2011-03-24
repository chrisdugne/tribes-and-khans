
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.MoveConflict")]
	public class MoveConflict
	{	
		
		public function get armySize():int {
			return _armySize;
		}
		
		public function set armySize(o:int):void {
			_armySize = o;
		}
		
		public function get armyBows():int {
			return _armyBows;
		}
		
		public function set armyBows(o:int):void {
			_armyBows = o;
		}
		
		public function get armySwords():int {
			return _armySwords;
		}
		
		public function set armySwords(o:int):void {
			_armySwords = o;
		}
		
		public function get armyArmors():int {
			return _armyArmors;
		}
		
		public function set armyArmors(o:int):void {
			_armyArmors = o;
		}
		
		public function get xFrom():int {
			return _xFrom;
		}
		
		public function set xFrom(o:int):void {
			_xFrom = o;
		}
		
		public function get yFrom():int {
			return _yFrom;
		}
		
		public function set yFrom(o:int):void {
			_yFrom = o;
		}
		
		protected var _armySize:int;
		protected var _armyBows:int;
		protected var _armySwords:int;
		protected var _armyArmors:int;
		protected var _xFrom:int;
		protected var _yFrom:int;


	}
}
