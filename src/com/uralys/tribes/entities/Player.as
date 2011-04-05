package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Player")]
	public class Player
	{
		private var _uralysUID:String;
		private var _name:String;
		private var _allyUID:String;
		private var _cities:ArrayCollection;
		private var _units:ArrayCollection;
		private var _lands:ArrayCollection = new ArrayCollection();
		private var _conflicts:ArrayCollection = new ArrayCollection();
		
		public function Player(){}

		//-------------------------------------------------------//
		
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
			return _uralysUID;
		}

		public function set playerUID(value:String):void
		{
			_uralysUID = value;
		}

		public function get uralysUID():String
		{
			return _uralysUID;
		}

		public function set uralysUID(value:String):void
		{
			_uralysUID = value;
		}
		
		public function get cities():ArrayCollection
		{
			return _cities;
		}
		
		public function set cities(value:ArrayCollection):void
		{
			_cities = value;
		}
		public function get units():ArrayCollection
		{
			return _units;
		}
		
		public function set units(value:ArrayCollection):void
		{
			_units = value;
		}
		
		public function get lands():ArrayCollection
		{
			return _lands;
		}
		
		public function set lands(value:ArrayCollection):void
		{
			_lands = value;
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