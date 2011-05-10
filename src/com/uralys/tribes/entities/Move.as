package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Session;
	import com.uralys.utils.Utils;
	
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Move")]
	public class Move
	{
		public function Move(){
		}
		
		// on ne peut pas le mettre dans le constructeur, car BlazeDS l'utilise aussi


		public function initNewMove(unitUID:String, i:int, j:int, __timeFrom:Number = -1, __timeTo:Number = -1):void{
			_timeFrom = __timeFrom == -1 ? new Date().getTime() : __timeFrom;
			_moveUID = "NEW_"+timeFrom+"_"+i+"_"+j+"_"+unitUID;
			_caseUID = "case_"+i+"_"+j;
			_unitUID = unitUID;
			_timeTo = __timeTo;
		}
			
		//--------------------------------------------------------------//
		
		protected var _moveUID:String;
		protected var _unitUID:String;
		protected var _caseUID:String;
		protected var _timeFrom:Number;
		protected var _timeTo:Number;
		protected var _value:int;
		protected var _gathering:Gathering;
		
		//--------------------------------------------------------------//
		
		public function get gathering():Gathering
		{
			return _gathering;
		}
		
		public function set gathering(value:Gathering):void
		{
			_gathering = value;
		}
		
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
		
		//--------------------------------------------------------------//
		
		public function getX():int {
			return Utils.getXFromCaseUID(_caseUID);
		}

		public function getY():int {
			return Utils.getYFromCaseUID(_caseUID);
		}


	}
}