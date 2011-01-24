package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Game")]
	public class Game
	{	
		//-----------------------------------------------------------------------------------//
		
		public static var IN_CREATION:int = 1;
		public static var RUNNING:int = 2;
		
		//-------------------------------------------------------//

		private var _gameUID:String;
		private var _name:String;
		private var _status:int;
		private var _currentTurn:int;
		private var _nbMinByTurn:int;
		private var _beginTurnTimeMillis:Number;
		private var _players:ArrayCollection;
		
		public function Game(){}

		//-------------------------------------------------------//

		public function get beginTurnTimeMillis():Number
		{
			return _beginTurnTimeMillis;
		}

		public function set beginTurnTimeMillis(value:Number):void
		{
			_beginTurnTimeMillis = value;
		}
		
		public function get nbMinByTurn():int
		{
			return _nbMinByTurn;
		}

		public function set nbMinByTurn(value:int):void
		{
			_nbMinByTurn = value;
		}

		public function get currentTurn():int
		{
			return _currentTurn;
		}

		public function set currentTurn(value:int):void
		{
			_currentTurn = value;
		}

		public function get status():int
		{
			return _status;
		}

		public function set status(value:int):void
		{
			_status = value;
		}

		public function get players():ArrayCollection
		{
			return _players;
		}

		public function set players(value:ArrayCollection):void
		{
			_players = value;
		}

		public function get name():String
		{
			return _name;
		}

		public function set name(value:String):void
		{
			_name = value;
		}

		public function get gameUID():String
		{
			return _gameUID;
		}

		public function set gameUID(value:String):void
		{
			_gameUID = value;
		}

	}
}