package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Session;
	
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Move")]
	public class Move
	{
		public function Move(){
		}
		
		// on ne peut pas le mettre dans le constructeur, car BlazeDS l'utilise aussi
		public function initNewMove(unitUID:String, i:int, j:int, __timeFrom:Number = -1):void{
			_timeFrom = __timeFrom == -1 ? new Date().getTime() : __timeFrom;
			_moveUID = "NEW_"+i+"_"+j+"_"+unitUID+"_"+timeFrom;
			_caseUID = "case_"+i+"_"+j;
			_unitUID = unitUID;
			_timeTo = -1;
		}
			
		//--------------------------------------------------------------//
		
		protected var _moveUID:String;
		protected var _unitUID:String;
		protected var _caseUID:String;
		protected var _timeFrom:Number;
		protected var _timeTo:Number;
		protected var _value:int;
		
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
		
		public function get caseUID():String {
			return _caseUID;
		}
		
		public function set caseUID(o:String):void {
			_caseUID = o;
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
		
		public function get value():int {
			return _value;
		}
		
		public function set value(o:int):void {
			_value = o;
		}
		

	}
}