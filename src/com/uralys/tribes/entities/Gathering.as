package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Gathering")]
	public class Gathering
	{
		public function Gathering(){}
		
		//--------------------------------------------------------------//
		
		protected var _gatheringUID:String;
		protected var _allyUID:String;
		protected var _newUnitUID:String;
		protected var _unitUIDs:ArrayCollection;
		
		//--------------------------------------------------------------//
		
		
		public function get gatheringUID():String {
			return _gatheringUID;
		}
		
		public function set gatheringUID(o:String):void {
			_gatheringUID = o;
		}
		
		public function get allyUID():String {
			return _allyUID;
		}
		
		public function set allyUID(o:String):void {
			_allyUID = o;
		}
		
		public function get newUnitUID():String {
			return _newUnitUID;
		}
		
		public function set newUnitUID(o:String):void {
			_newUnitUID = o;
		}
		
		public function get unitUIDs():ArrayCollection {
			return _unitUIDs;
		}
		
		public function set unitUIDs(o:ArrayCollection):void {
			_unitUIDs = o;
		}
		

	}
}