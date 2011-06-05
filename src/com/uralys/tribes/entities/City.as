
package com.uralys.tribes.entities
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.City")]
	public class City
	{	
		
		//---------------------------------------------------------------------------//

		protected var _cityUID:String;
		protected var _ownerUID:String;
		protected var _forgeUID:String;
		protected var _name:String;
		protected var _population:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _peopleCreatingWheat:int;
		protected var _peopleCreatingWood:int;
		protected var _peopleCreatingIron:int;
		protected var _x:int;
		protected var _y:int;
		protected var _equipmentStock:ArrayCollection = new ArrayCollection();
		protected var _smiths:ArrayCollection = new ArrayCollection();
		
		protected var _beginTime:Number;
		protected var _endTime:Number;
		
		//---------------------------------------------------------------------------//
		
		public function get cityUID():String {
			return _cityUID;
		}
	
		public function set cityUID(o:String):void {
			_cityUID = o;
		}

		public function get forgeUID():String {
			return _forgeUID;
		}
	
		public function set forgeUID(o:String):void {
			_forgeUID = o;
		}

		public function get ownerUID():String {
			return _ownerUID;
		}
	
		public function set ownerUID(o:String):void {
			_ownerUID = o;
		}
	
		public function get name():String {
			return _name;
		}
	
		public function set name(o:String):void {
			_name = o;
		}
	
		public function get population():int {
			refreshUnemployed();
			return _population;
		}
	
		public function set population(o:int):void {
			_population = o;
		}
	
		public function get wheat():int {
			return _wheat;
		}
	
		public function set wheat(o:int):void {
			_wheat = o;
		}
	
		public function get wood():int {
			return _wood;
		}
	
		public function set wood(o:int):void {
			_wood = o;
		}
	
		public function get iron():int {
			return _iron;
		}
	
		public function set iron(o:int):void {
			_iron = o;
		}

		public function get gold():int {
			return _gold;
		}
	
		public function set gold(o:int):void {
			_gold = o;
		}
	
		public function get peopleCreatingWheat():int {
			return _peopleCreatingWheat;
		}
	
		public function set peopleCreatingWheat(o:int):void {
			_peopleCreatingWheat = o;
			refreshUnemployed();
		}
	
		public function get peopleCreatingWood():int {
			return _peopleCreatingWood;
		}
	
		public function set peopleCreatingWood(o:int):void {
			_peopleCreatingWood = o;
			refreshUnemployed();
		}
	
		public function get peopleCreatingIron():int {
			return _peopleCreatingIron;
		}
	
		public function set peopleCreatingIron(o:int):void {
			_peopleCreatingIron = o;
			refreshUnemployed();
		}
	
		public function get x():int {
			return _x;
		}
	
		public function set x(o:int):void {
			_x = o;
		}
	
		public function get y():int {
			return _y;
		}
	
		public function set y(o:int):void {
			_y = o;
		}
	
		public function get radius():int {
			return Math.sqrt(_population)/2;
		}
	
		public function get equipmentStock():ArrayCollection {
			return _equipmentStock;
		}
	
		public function set equipmentStock(o:ArrayCollection):void {
			_equipmentStock = o;
		}
	
		public function get smiths():ArrayCollection {
			return _smiths;
		}
	
		public function set smiths(o:ArrayCollection):void {
			_smiths = o;
		}
		
		public function get beginTime():Number
		{
			return _beginTime;
		}
		
		public function set beginTime(value:Number):void
		{
			_beginTime = value;
		}
		
		public function get endTime():Number
		{
			return _endTime;
		}
		
		public function set endTime(value:Number):void
		{
			_endTime = value;
		}
		
	
		
		//===============================================================================================//
		// 	only on flex side
		//===============================================================================================//
		
		public var merchant:Unit;
		public var army:Unit;
		protected var _unemployed:int;
		protected var _armiesToFeed:int;
		protected var _armyRaised:int;
		protected var _armyReleased:int;

		public function set unitsToFeed(o:int):void{
			_armiesToFeed = o;
		}

		public function get unitsToFeed():int{
			return _armiesToFeed;
		}

		public function get armyRaised():int{
			return _armyRaised;
		}

		public function set armyRaised(o:int):void{
			_armyRaised = o;
			refreshUnemployed();
		}

		public function get armyReleased():int{
			return _armyReleased;
		}

		public function set armyReleased(o:int):void{
			_armyReleased = o;
			refreshUnemployed();
		}

		public function get unemployed():int{
			return _unemployed;
		}

		public function set unemployed(o:int):void{
			_unemployed = o;
		}
		
		public function refreshUnemployed():void{
			var nbSmiths:int = 0;
			
			for each(var smith:Smith in smiths){
				nbSmiths += smith.people;
			}
			
			unemployed = _population 
							- peopleCreatingWheat 
							- peopleCreatingWood 
							- peopleCreatingIron 
							- armyRaised
							+ armyReleased
							- nbSmiths;
		}
		
		//---------------------------------------------------------------//
		
		// resources management
		protected var _wheatEarned:int;
		protected var _wheatSpent:int;
		protected var _woodEarned:int;
		protected var _woodSpent:int;
		protected var _ironEarned:int;
		protected var _ironSpent:int;

		// market management
		protected var _wheatBought:int;
		protected var _woodBought:int;
		protected var _ironBought:int;
		protected var _bowsBought:int;
		protected var _swordsBought:int;
		protected var _armorsBought:int;
		protected var _wheatSold:int;
		protected var _woodSold:int;
		protected var _ironSold:int;
		protected var _bowsSold:int;
		protected var _swordsSold:int;
		protected var _armorsSold:int;
		protected var _goldEarned:int;
		protected var _goldSpent:int;

		public function get wheatEarned():int{
			return _wheatEarned;
		}
		
		public function set wheatEarned(o:int):void{
			_wheatEarned = o;
		}

		public function get wheatBought():int{
			return _wheatBought;
		}
		
		public function set wheatBought(o:int):void{
			_wheatBought = o;
		}

		public function get wheatSold():int{
			return _wheatSold;
		}
		
		public function set wheatSold(o:int):void{
			_wheatSold = o;
		}

		public function get wheatSpent():int{
			return _wheatSpent;
		}
		
		public function set wheatSpent(o:int):void{
			_wheatSpent = o;
		}

		public function get woodEarned():int{
			return _woodEarned;
		}
		
		public function set woodEarned(o:int):void{
			_woodEarned = o;
		}
		
		public function get woodBought():int{
			return _woodBought;
		}
		
		public function set woodBought(o:int):void{
			_woodBought = o;
		}
		
		public function get woodSold():int{
			return _woodSold;
		}
		
		public function set woodSold(o:int):void{
			_woodSold = o;
		}


		public function get woodSpent():int{
			return _woodSpent;
		}
		
		public function set woodSpent(o:int):void{
			_woodSpent = o;
		}

		public function get ironEarned():int{
			return _ironEarned;
		}
		
		public function set ironEarned(o:int):void{
			_ironEarned = o;
		}
		
		public function get ironBought():int{
			return _ironBought;
		}
		
		public function set ironBought(o:int):void{
			_ironBought = o;
		}
		
		public function get ironSold():int{
			return _ironSold;
		}
		
		public function set ironSold(o:int):void{
			_ironSold = o;
		}

		public function get ironSpent():int{
			return _ironSpent;
		}
		
		public function set ironSpent(o:int):void{
			_ironSpent = o;
		}
	
		public function get goldEarned():int{
			return _goldEarned;
		}
		
		public function set goldEarned(o:int):void{
			_goldEarned = o;
		}
		
		public function get goldSpent():int{
			return _goldSpent;
		}
		
		public function set goldSpent(o:int):void{
			_goldSpent = o;
		}
		
		public function get bowsBought():int{
			return _bowsBought;
		}
		
		public function set bowsBought(o:int):void{
			_bowsBought = o;
		}
		
		public function get swordsBought():int{
			return _swordsBought;
		}
		
		public function set swordsBought(o:int):void{
			_swordsBought = o;
		}
		
		public function get armorsBought():int{
			return _armorsBought;
		}
		
		public function set armorsBought(o:int):void{
			_armorsBought = o;
		}
		
		public function get bowsSold():int{
			return _bowsSold;
		}
		
		public function set bowsSold(o:int):void{
			_bowsSold = o;
		}
		
		public function get swordsSold():int{
			return _swordsSold;
		}
		
		public function set swordsSold(o:int):void{
			_swordsSold = o;
		}
		
		public function get armorsSold():int{
			return _armorsSold;
		}
		
		public function set armorsSold(o:int):void{
			_armorsSold = o;
		}

		//---------------------------------------------------------------//

		private var _bowStock:int; // stock dans la ville au debut du tour
		private var _bowsRestored:int; // bows restitues au stock àla fin du tour 
		private var _bowsEquiped:int; // bows pris du stock pour les armees
		private var _bowWorkers:int;
		
		public function set bowStock(o:int):void{
			_bowStock = o;
		}

		public function get bowStock():int{
			return _bowStock;
		}

		public function set bowWorkers(o:int):void{
			_bowWorkers = o;
		}

		public function get bowWorkers():int{
			return _bowWorkers;
		}
		
		public function set bowsRestored(o:int):void{
			_bowsRestored = o;
		}

		public function get bowsRestored():int{
			return _bowsRestored;
		}

		public function set bowsEquiped(o:int):void{
			_bowsEquiped = o;
		}

		public function get bowsEquiped():int{
			return _bowsEquiped;
		}
		
		//---------------------------------------------------------------//
		
		private var _swordStock:int;
		private var _swordsRestored:int; // bows restitues au stock à la fin du tour 
		private var _swordsEquiped:int; // bows pris du stock pour les armees
		private var _swordWorkers:int;
		
		public function set swordStock(o:int):void{
			_swordStock = o;
		}

		public function get swordStock():int{
			return _swordStock;
		}

		public function set swordWorkers(o:int):void{
			_swordWorkers = o;
		}

		public function get swordWorkers():int{
			return _swordWorkers;
		}
		
		public function set swordsRestored(o:int):void{
			_swordsRestored = o;
		}
		
		public function get swordsRestored():int{
			return _swordsRestored;
		}
		
		public function set swordsEquiped(o:int):void{
			_swordsEquiped = o;
		}
		
		public function get swordsEquiped():int{
			return _swordsEquiped;
		}
		//---------------------------------------------------------------//
		
		private var _armorStock:int;
		private var _armorsRestored:int; // bows restitues au stock à la fin du tour 
		private var _armorsEquiped:int; // bows pris du stock pour les armees
		private var _armorWorkers:int;
		
		public function set armorStock(o:int):void{
			_armorStock = o;
		}

		public function get armorStock():int{
			return _armorStock;
		}

		public function set armorWorkers(o:int):void{
			_armorWorkers = o;
		}

		public function get armorWorkers():int{
			return _armorWorkers;
		}
		
		public function set armorsRestored(o:int):void{
			_armorsRestored = o;
		}
		
		public function get armorsRestored():int{
			return _armorsRestored;
		}
		
		public function set armorsEquiped(o:int):void{
			_armorsEquiped = o;
		}
		
		public function get armorsEquiped():int{
			return _armorsEquiped;
		}
		
		//---------------------------------------------------------------//

		
		public function reset():void
		{
			woodSpent = 0;
			ironSpent = 0;
			wheatSpent = 0;
			wheatEarned = 0;
			woodEarned = 0;
			ironEarned = 0;
			armyRaised = 0;
			armyReleased = 0;
			_armiesToFeed = 0;
		}
	}
}
