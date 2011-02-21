
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Report")]
	public class Report
	{	
		
		public function get reportUID():String {
			return _reportUID;
		}
		
		public function set reportUID(o:String):void {
			_reportUID = o;
		}
		
		public function get playerUID():String {
			return _playerUID;
		}
		
		public function set playerUID(o:String):void {
			_playerUID = o;
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
		
		protected var _reportUID:String;
		protected var _playerUID:String;
		protected var _status:int;
		protected var _report:String;

	}
}
