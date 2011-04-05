package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Move")]
	public class Move
	{
		public function Move(){}
		
		//--------------------------------------------------------------//
		
		protected var _moveUID:String;
		protected var _unitUID:String;
		protected var __case:Case;
		protected var _timeFrom:Number;
		protected var _timeTo:Number;
		protected var _valur:int;
		
		//--------------------------------------------------------------//
		
		
		public function get moveUID():String {
			return _moveUID;
		}
		
		public function set moveUID(o:String):void {
			_moveUID = o;
		}
		
		public function get unitUID():String {
			return _unitUID;
		}
		
		public function set unitUID(o:String):void {
			_unitUID = o;
		}
		
		public function get _case():Case {
			return __case;
		}
		
		public function set _case(o:Case):void {
			__case = o;
		}
		
		public function get timeFrom():Number {
			return _timeFrom;
		}
		
		public function set timeFrom(o:Number):void {
			_timeFrom = o;
		}
		
		public function get timeTo():Number {
			return _timeTo;
		}
		
		public function set timeTo(o:Number):void {
			_timeTo = o;
		}
		
		public function get valur():int {
			return _valur;
		}
		
		public function set valur(o:int):void {
			_valur = o;
		}
		

	}
}