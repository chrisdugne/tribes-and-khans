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
		
		public static var H00:int = 1;
		public static var H06:int = 2;
		public static var H12:int = 3;
		public static var H18:int = 4;
		
		//-------------------------------------------------------//

		private var _gameUID:String;
		private var _name:String;
		private var _status:int;
		private var _currentTurn:int;
		private var _autoEndTurnTime:int;
		private var _players:ArrayCollection;
		
		public function Game(){}

		//-------------------------------------------------------//

/*		public function getPeriod():String{
			switch(autoEndTurnTime){
				case H00 : return 'Minuit';
				case H06 : return '06h';
				case H12 : return 'Midi';
				case H18 : return '18h';
				default : return null; //never
			}
		}*/
		
		//-------------------------------------------------------//
		
		public function get autoEndTurnTime():int
		{
			return _autoEndTurnTime;
		}

		public function set autoEndTurnTime(value:int):void
		{
			_autoEndTurnTime = value;
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