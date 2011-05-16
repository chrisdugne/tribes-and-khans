package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	public class DataContainer4UnitSaved
	{
		public function DataContainer4UnitSaved(){}

		//--------------------------------------------------------------//
		
		protected var _casesAltered:ArrayCollection;
		protected var _unitsAltered:ArrayCollection;
		
		//--------------------------------------------------------------//
		
		
		public function get casesAltered():ArrayCollection {
			return _casesAltered;
		}
		
		public function set casesAltered(o:ArrayCollection):void {
			_casesAltered = o;
		}
		
		public function get unitsAltered():ArrayCollection {
			return _unitsAltered;
		}
		
		public function set unitsAltered(o:ArrayCollection):void {
			_unitsAltered = o;
		}
	}
}