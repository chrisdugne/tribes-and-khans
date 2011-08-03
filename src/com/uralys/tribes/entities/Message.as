
package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Message")]
	public class Message
	{	
		//--------------------------------------------------------------//

		public static var UNREAD:int = 1;
		public static var READ:int = 2;
		public static var ARCHIVED:int = 3;
		public static var DELETED:int = 4;
		
		//--------------------------------------------------------------//
		
		protected var _messageUID:String;
		protected var _content:String;
		protected var _senderUID:String;
		protected var _senderName:String;
		protected var _status:int;
		protected var _time:Number;
		
		//--------------------------------------------------------------//
		
		
		public function get messageUID():String {
			return _messageUID;
		}
		
		public function set messageUID(o:String):void {
			_messageUID = o;
		}
		
		public function get content():String {
			return _content;
		}
		
		public function set content(o:String):void {
			_content = o;
		}
		
		public function get senderUID():String {
			return _senderUID;
		}
		
		public function set senderUID(o:String):void {
			_senderUID = o;
		}
		
		public function get senderName():String {
			return _senderName;
		}
		
		public function set senderName(o:String):void {
			_senderName = o;
		}
		
		public function get status():int {
			return _status;
		}
		
		public function set status(o:int):void {
			_status = o;
		}
		
		public function get time():Number {
			return _time;
		}
		
		public function set time(o:Number):void {
			_time = o;
		}
		

	}
}
