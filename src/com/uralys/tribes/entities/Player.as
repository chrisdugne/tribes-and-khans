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
		private var _moves:ArrayCollection;
		
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

		public function get moves():ArrayCollection
		{
			return _moves;
		}

		public function set moves(value:ArrayCollection):void
		{
			_moves = value;
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
}