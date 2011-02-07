package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Move")]
	public class Move
	{
		private var _moveUID:String;
		private var _xFrom:int;
		private var _xTo:int;
		private var _yFrom:int;
		private var _yTo:int;
		
		public function Move(){}

		//-------------------------------------------------------//


		public function get moveUID():String
		{
			return _moveUID;
		}

		public function set moveUID(value:String):void
		{
			_moveUID = value;
		}
		
		public function get xFrom():int
		{
			return _xFrom;
		}
		
		public function set xFrom(value:int):void
		{
			_xFrom = value;
		}
		
		public function get xTo():int
		{
			return _xTo;
		}

		public function set xTo(value:int):void
		{
			_xTo = value;
		}

		public function get yTo():int
		{
			return _yTo;
		}

		public function set yTo(value:int):void
		{
			_yTo = value;
		}

		public function get yFrom():int
		{
			return _yFrom;
		}

		public function set yFrom(value:int):void
		{
			_yFrom = value;
		}
	}
}