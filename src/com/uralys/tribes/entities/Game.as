package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Board")]
	public class Game
	{
		private var _gameUID:String;
		private var _name:String;
		private var _players:ArrayCollection;
		
		public function Game(){}

		//-------------------------------------------------------//
		
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