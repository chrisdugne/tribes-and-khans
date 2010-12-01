package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Profil")]
	public class Profil
	{
		private var _uralysUID:String;
		private var _email:String;
		private var _players:ArrayCollection;
		
		public function Profil(){}

		//-------------------------------------------------------//
		
		public function get players():ArrayCollection
		{
			return _players;
		}

		public function set players(value:ArrayCollection):void
		{
			_players = value;
		}

		public function get email():String
		{
			return _email;
		}

		public function set email(value:String):void
		{			
			_email = value;
		}

		public function get uralysUID():String
		{
			return _uralysUID;
		}

		public function set uralysUID(value:String):void
		{
			_uralysUID = value;
		}

	}
}