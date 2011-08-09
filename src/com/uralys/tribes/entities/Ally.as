package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Ally")]
	public class Ally
	{
		//--------------------------------------------------------------//

		public function Ally(){}
		
		//--------------------------------------------------------------//
		
		protected var _allyUID:String;
		protected var _name:String;
		protected var _nbPlayers:int;
		protected var _nbLands:int;
		protected var _nbPopulation:int;
		protected var _nbCities:int;
		protected var _nbArmies:int;
		protected var _profile:String;
		protected var _players:ArrayCollection;
		
		//--------------------------------------------------------------//
		
		
		public function get allyUID():String {
			return _allyUID;
		}
		
		public function set allyUID(o:String):void {
			_allyUID = o;
		}
		
		public function get name():String {
			return _name;
		}
		
		public function set name(o:String):void {
			_name = o;
		}
		
		public function get nbPlayers():int {
			return _nbPlayers;
		}
		
		public function set nbPlayers(o:int):void {
			_nbPlayers = o;
		}
		
		public function get nbLands():int {
			return _nbLands;
		}
		
		public function set nbLands(o:int):void {
			_nbLands = o;
		}
		
		public function get nbPopulation():int {
			return _nbPopulation;
		}
		
		public function set nbPopulation(o:int):void {
			_nbPopulation = o;
		}
		
		public function get nbCities():int {
			return _nbCities;
		}
		
		public function set nbCities(o:int):void {
			_nbCities = o;
		}
		
		public function get nbArmies():int {
			return _nbArmies;
		}
		
		public function set nbArmies(o:int):void {
			_nbArmies = o;
		}
		
		public function get profile():String {
			return _profile;
		}
		
		public function set profile(o:String):void {
			_profile = o;
		}
		
		public function get players():ArrayCollection {
			return _players;
		}
		
		public function set players(o:ArrayCollection):void {
			_players = o;
		}
		
		
		

	}
}