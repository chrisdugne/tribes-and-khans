package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.MoveResult")]
	public class MoveResult
	{
		public function MoveResult(){}

		//--------------------------------------------------------------//
		
		protected var _cellDeparture:Cell;
		protected var _unit:Unit;
		
		//--------------------------------------------------------------//
		
		
		public function get unit():Unit
		{
			return _unit;
		}

		public function set unit(value:Unit):void
		{
			_unit = value;
		}

		public function get cellDeparture():Cell
		{
			return _cellDeparture;
		}

		public function set cellDeparture(value:Cell):void
		{
			_cellDeparture = value;
		}


	}
}