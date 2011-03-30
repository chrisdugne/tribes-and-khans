package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Player")]
	public class Player
	{
		private var _playerUID:String;
		private var _name:String;
		private var _gameUID:String;
		private var _gameName:String;
		private var _lastTurnPlayed:int;
		private var _cities:ArrayCollection;
		private var _armies:ArrayCollection;
		private var _merchants:ArrayCollection;
		private var _allies:ArrayCollection;
		private var _lands:ArrayCollection = new ArrayCollection();
		private var _reports:ArrayCollection = new ArrayCollection();
		private var _conflicts:ArrayCollection = new ArrayCollection();
		
		public function Player(){}

		//-------------------------------------------------------//
		

		public function get gameName():String
		{
			return _gameName;
		}

		public function set gameName(value:String):void
		{
			_gameName = value;
		}

		public function get gameUID():String
		{
			return _gameUID;
		}

		public function set gameUID(value:String):void
		{
			_gameUID = value;
		}

		public function get lastTurnPlayed():int
		{
			return _lastTurnPlayed;
		}

		public function set lastTurnPlayed(value:int):void
		{
			_lastTurnPlayed = value;
		}

		public function get name():String
		{
			return _name;
		}

		public function set name(value:String):void
		{
			_name = value;
		}

		public function get playerUID():String
		{
			return _playerUID;
		}

		public function set playerUID(value:String):void
		{
			_playerUID = value;
		}
		
		public function get cities():ArrayCollection
		{
			return _cities;
		}
		
		public function set cities(value:ArrayCollection):void
		{
			_cities = value;
		}
		public function get armies():ArrayCollection
		{
			return _armies;
		}
		
		public function set armies(value:ArrayCollection):void
		{
			_armies = value;
		}
		
		public function get allies():ArrayCollection
		{
			return _allies;
		}
		
		public function set allies(value:ArrayCollection):void
		{
			_allies = value;
		}
		
		public function get merchants():ArrayCollection
		{
			return _merchants;
		}
		
		public function set merchants(value:ArrayCollection):void
		{
			_merchants = value;
		}
		
		public function get lands():ArrayCollection
		{
			return _lands;
		}
		
		public function set lands(value:ArrayCollection):void
		{
			_lands = value;
		}
		
		public function get reports():ArrayCollection
		{
			return _reports;
		}
		
		public function set reports(value:ArrayCollection):void
		{
			_reports = value;
		}
		
		public function get conflicts():ArrayCollection
		{
			return _conflicts;
		}
		
		public function set conflicts(value:ArrayCollection):void
		{
			_conflicts = value;
		}

		//-------------------------------------------------------//
		// flex only
		
		private var _hasAlreadyPlayedHisTurn:Boolean;
		
		public function get hasAlreadyPlayedHisTurn():Boolean
		{
			return _hasAlreadyPlayedHisTurn;
		}
		
		public function set hasAlreadyPlayedHisTurn(value:Boolean):void
		{
			_hasAlreadyPlayedHisTurn = value;
		}
}
}