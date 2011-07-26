package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Player")]
	public class Player
	{
		private var _uralysUID:String;
		private var _name:String;
		private var _musicOn:Boolean;
		private var _allyUID:String;
		private var _lastStep:Number;
		private var _cities:ArrayCollection;
		private var _units:ArrayCollection = new ArrayCollection();
		private var _conflicts:ArrayCollection = new ArrayCollection();
		private var _readMessages:ArrayCollection = new ArrayCollection();
		private var _newMessages:ArrayCollection = new ArrayCollection();

		private var _nbLands:int;
		private var _nbCities:int;
		private var _nbArmies:int;
		private var _nbPopulation:int;

		private var _profile:String;
		
		//-------------------------------------------------------//

		public function Player(){}

		//-------------------------------------------------------//
		
		public function get profile():String
		{
			return _profile;
		}

		public function set profile(value:String):void
		{
			_profile = value;
		}

		public function get musicOn():Boolean
		{
			return _musicOn;
		}

		public function set musicOn(value:Boolean):void
		{
			_musicOn = value;
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
		
		public function get lastStep():Number
		{
			return _lastStep;
		}
		
		public function set lastStep(value:Number):void
		{
			_lastStep = value;
		}
		
		public function get allyUID():String
		{
			return _allyUID;
		}

		public function set allyUID(value:String):void
		{
			_allyUID = value;
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
		
		public function get nbLands():int
		{
			return _nbLands;
		}
		
		public function set nbLands(value:int):void
		{
			_nbLands = value;
		}

		public function get nbCities():int
		{
			return _nbCities;
		}
		
		public function set nbCities(value:int):void
		{
			_nbCities = value;
		}

		public function get nbArmies():int
		{
			return _nbArmies;
		}
		
		public function set nbArmies(value:int):void
		{
			_nbArmies = value;
		}

		public function get nbPopulation():int
		{
			return _nbPopulation;
		}
		
		public function set nbPopulation(value:int):void
		{
			_nbPopulation = value;
		}
		
		public function get conflicts():ArrayCollection
		{
			return _conflicts;
		}
		
		public function set conflicts(value:ArrayCollection):void
		{
			_conflicts = value;
		}

		public function get newMessages():ArrayCollection
		{
			return _newMessages;
		}
		
		public function set newMessages(value:ArrayCollection):void
		{
			_newMessages = value;
		}

		public function get readMessages():ArrayCollection
		{
			return _readMessages;
		}
		
		public function set readMessages(value:ArrayCollection):void
		{
			_readMessages = value;
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
		
		//-------------------------------------------------------//
		
		public function getUnit(unitUID:String):Unit
		{
			trace("Session.getunit " + unitUID);
			for each(var unit:Unit in units){
				if(unit.unitUID == unitUID)
					return unit;
			}
			
			trace("NOT FOUND : NULL");
			return null;
		}	

		public function refreshUnit(unitRefreshed:Unit):void{
			
			var indexFound:int = -1;
			
			for each(var unit:Unit in units){
				if(unit.unitUID == unitRefreshed.unitUID){
					indexFound = units.getItemIndex(unit);
					break;
				}
			}
			
			if(indexFound >= 0){
				units.removeItemAt(indexFound);
			}
			
			units.addItem(unitRefreshed);
		}	
		
		//-------------------------------------------------------//
}
}