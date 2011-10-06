
package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.City")]
	public class City
	{	
		
		public var calculationDone:Boolean = false;

		//---------------------------------------------------------------------------//

		protected var _cityUID:String;
		protected var _ownerUID:String;
		protected var _name:String;

		protected var _beginTime:Number;
		protected var _endTime:Number;
		
		protected var _population:int;
		
		protected var _wheat:int;
		protected var _peopleCreatingWheat:int;
		protected var _wood:int;
		protected var _peopleCreatingWood:int;
		protected var _iron:int;
		protected var _peopleCreatingIron:int;

		protected var _x:int;
		protected var _y:int;
		protected var _gold:int;

		protected var _bows:int;
		protected var _swords:int;
		protected var _armors:int;

		protected var _stocks:ArrayCollection = new ArrayCollection();
		
		
		//---------------------------------------------------------------------------//
		
		public function get cityUID():String {
			return _cityUID;
		}
	
		public function set cityUID(o:String):void {
			_cityUID = o;
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
			refreshAvailableAsSmith();
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
			refreshAvailableAsSmith();
		}
	
		public function get peopleCreatingWood():int {
			return _peopleCreatingWood;
		}
	
		public function set peopleCreatingWood(o:int):void {
			_peopleCreatingWood = o;
			refreshUnemployed();
			refreshAvailableAsSmith();
		}
	
		public function get peopleCreatingIron():int {
			return _peopleCreatingIron;
		}
	
		public function set peopleCreatingIron(o:int):void {
			_peopleCreatingIron = o;
			refreshUnemployed();
			refreshAvailableAsSmith();
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
	
		public function get stocks():ArrayCollection {
			return _stocks;
		}
	
		public function set stocks(o:ArrayCollection):void {
			_stocks = o;
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
		
		public function get bows():int
		{
			return _bows;
		}
		
		public function set bows(value:int):void
		{
			_bows = value;
		}
		
		public function get swords():int
		{
			return _swords;
		}
		
		public function set swords(value:int):void
		{
			_swords = value;
		}
		
		public function get armors():int
		{
			return _armors;
		}
		
		public function set armors(value:int):void
		{
			_armors = value;
		}
		
	
		
		//===============================================================================================//
		// 	only on flex side
		//===============================================================================================//
		
		public var caravan:Unit;
		public var army:Unit;
		protected var _unemployed:int;
		protected var _availableAsSmith:int;
		protected var _armiesToFeed:int;

		public function set unitsToFeed(o:int):void{
			_armiesToFeed = o;
		}

		public function get unitsToFeed():int{
			return _armiesToFeed;
		}

		public function get unemployed():int{
			return _unemployed;
		}

		public function set unemployed(o:int):void{
			_unemployed = o;
		}
		
		public function refreshUnemployed():void{
			unemployed = _population 
						- workers;
		}

		public function get workers():int{
			var nbWorkers:int = peopleCreatingWheat 
					+ peopleCreatingWood 
					+ peopleCreatingIron 
					+ wheatStockBuilders
					+ woodStockBuilders
					+ ironStockBuilders
					+ bowStockBuilders
					+ swordStockBuilders
					+ armorStockBuilders
					+ bowWorkers
					+ swordWorkers
					+ armorWorkers;
			
			if(army != null && army.status == Unit.TO_BE_CREATED)
				nbWorkers += army.size;
			
			if(caravan != null && caravan.status == Unit.TO_BE_CREATED)
				nbWorkers += caravan.size;
			
			return nbWorkers;
		}
		
		
		public function get availableAsSmith():int{
			return _availableAsSmith;
		}

		public function set availableAsSmith(o:int):void{
			_availableAsSmith = o;
		}
		
		public function refreshAvailableAsSmith():void
		{
			availableAsSmith = _population - peopleCreatingWheat
										   - peopleCreatingWood
										   - peopleCreatingIron
		}
		
		//---------------------------------------------------------------//
		
		// resources management
		protected var _wheatEarned:int;
		protected var _woodEarned:int;
		protected var _ironEarned:int;
		
		private var _wheatStockCapacity:int;
		private var _wheatStockNextCapacity:int;
		private var _wheatStockBuilders:int;
		private var _wheatStockBeginTime:Number;
		private var _wheatStockEndTime:Number;

		private var _woodStockCapacity:int;
		private var _woodStockNextCapacity:int;
		private var _woodStockBuilders:int;
		private var _woodStockBeginTime:Number;
		private var _woodStockEndTime:Number;

		private var _ironStockCapacity:int;
		private var _ironStockNextCapacity:int;
		private var _ironStockBuilders:int;
		private var _ironStockBeginTime:Number;
		private var _ironStockEndTime:Number;

		//---------------------------------------------------------------//
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

		//---------------------------------------------------------------//
		// getters/setters for ressources and market management
		
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
		
		public function get wheatStockCapacity():int {
			return _wheatStockCapacity;
		}
		
		public function set wheatStockCapacity(o:int):void {
			_wheatStockCapacity = o;
		}
		
		public function get wheatStockNextCapacity():int {
			return _wheatStockNextCapacity;
		}
		
		public function set wheatStockNextCapacity(o:int):void {
			_wheatStockNextCapacity = o;
		}
		
		public function get wheatStockBuilders():int {
			return _wheatStockBuilders;
		}
		
		public function set wheatStockBuilders(o:int):void {
			_wheatStockBuilders = o;
		}
		
		public function get wheatStockBeginTime():Number {
			return _wheatStockBeginTime;
		}
		
		public function set wheatStockBeginTime(o:Number):void {
			_wheatStockBeginTime = o;
		}
		
		public function get wheatStockEndTime():Number {
			return _wheatStockEndTime;
		}
		
		public function set wheatStockEndTime(o:Number):void {
			_wheatStockEndTime = o;
		}
		
		public function get woodStockCapacity():int {
			return _woodStockCapacity;
		}
		
		public function set woodStockCapacity(o:int):void {
			_woodStockCapacity = o;
		}
		
		public function get woodStockNextCapacity():int {
			return _woodStockNextCapacity;
		}
		
		public function set woodStockNextCapacity(o:int):void {
			_woodStockNextCapacity = o;
		}
		
		public function get woodStockBuilders():int {
			return _woodStockBuilders;
		}
		
		public function set woodStockBuilders(o:int):void {
			_woodStockBuilders = o;
		}
		
		public function get woodStockBeginTime():Number {
			return _woodStockBeginTime;
		}
		
		public function set woodStockBeginTime(o:Number):void {
			_woodStockBeginTime = o;
		}
		
		public function get woodStockEndTime():Number {
			return _woodStockEndTime;
		}
		
		public function set woodStockEndTime(o:Number):void {
			_woodStockEndTime = o;
		}
		
		public function get ironStockCapacity():int {
			return _ironStockCapacity;
		}
		
		public function set ironStockCapacity(o:int):void {
			_ironStockCapacity = o;
		}
		
		public function get ironStockNextCapacity():int {
			return _ironStockNextCapacity;
		}
		
		public function set ironStockNextCapacity(o:int):void {
			_ironStockNextCapacity = o;
		}
		
		public function get ironStockBuilders():int {
			return _ironStockBuilders;
		}
		
		public function set ironStockBuilders(o:int):void {
			_ironStockBuilders = o;
		}
		
		public function get ironStockBeginTime():Number {
			return _ironStockBeginTime;
		}
		
		public function set ironStockBeginTime(o:Number):void {
			_ironStockBeginTime = o;
		}
		
		public function get ironStockEndTime():Number {
			return _ironStockEndTime;
		}
		
		public function set ironStockEndTime(o:Number):void {
			_ironStockEndTime = o;
		}


		//---------------------------------------------------------------//

		private var _bowStockCapacity:int;
		private var _bowStockNextCapacity:int;
		private var _bowStockBuilders:int;
		private var _bowStockBeginTime:Number;
		private var _bowStockEndTime:Number;

		private var _bowWorkers:int;
		private var _bowsBeingBuilt:int;
		private var _bowsBeingBuiltBeginTime:Number;
		private var _bowsBeingBuiltEndTime:Number;
		
		public function set bowWorkers(o:int):void{
			_bowWorkers = o;
		}

		public function get bowWorkers():int{
			return _bowWorkers;
		}
		
		public function set bowsBeingBuilt(o:int):void{
			_bowsBeingBuilt = o;
		}

		public function get bowsBeingBuilt():int{
			return _bowsBeingBuilt;
		}
		
		public function get bowStockCapacity():int {
			return _bowStockCapacity;
		}
		
		public function set bowStockCapacity(o:int):void {
			_bowStockCapacity = o;
		}
		
		public function get bowStockNextCapacity():int {
			return _bowStockNextCapacity;
		}
		
		public function set bowStockNextCapacity(o:int):void {
			_bowStockNextCapacity = o;
		}
		
		public function get bowStockBuilders():int {
			return _bowStockBuilders;
		}
		
		public function set bowStockBuilders(o:int):void {
			_bowStockBuilders = o;
		}
		
		public function get bowStockBeginTime():Number {
			return _bowStockBeginTime;
		}
		
		public function set bowStockBeginTime(o:Number):void {
			_bowStockBeginTime = o;
		}
		
		public function get bowStockEndTime():Number {
			return _bowStockEndTime;
		}
		
		public function set bowStockEndTime(o:Number):void {
			_bowStockEndTime = o;
		}
		
		public function get bowsBeingBuiltBeginTime():Number {
			return _bowsBeingBuiltBeginTime;
		}
		
		public function set bowsBeingBuiltBeginTime(o:Number):void {
			_bowsBeingBuiltBeginTime = o;
		}
		
		public function get bowsBeingBuiltEndTime():Number {
			return _bowsBeingBuiltEndTime;
		}
		
		public function set bowsBeingBuiltEndTime(o:Number):void {
			_bowsBeingBuiltEndTime = o;
		}
		
		//---------------------------------------------------------------//
		
		private var _swordStockCapacity:int;
		private var _swordStockNextCapacity:int;
		private var _swordStockBuilders:int;
		private var _swordStockBeginTime:Number;
		private var _swordStockEndTime:Number;
		
		private var _swordWorkers:int;
		private var _swordsBeingBuilt:int;
		private var _swordsBeingBuiltBeginTime:Number;
		private var _swordsBeingBuiltEndTime:Number;
		
		public function set swordWorkers(o:int):void{
			_swordWorkers = o;
		}

		public function get swordWorkers():int{
			return _swordWorkers;
		}
		
		public function set swordsBeingBuilt(o:int):void{
			_swordsBeingBuilt = o;
		}
		
		public function get swordsBeingBuilt():int{
			return _swordsBeingBuilt;
		}
		
		public function get swordStockCapacity():int {
			return _swordStockCapacity;
		}
		
		public function set swordStockCapacity(o:int):void {
			_swordStockCapacity = o;
		}
		
		public function get swordStockNextCapacity():int {
			return _swordStockNextCapacity;
		}
		
		public function set swordStockNextCapacity(o:int):void {
			_swordStockNextCapacity = o;
		}
		
		public function get swordStockBuilders():int {
			return _swordStockBuilders;
		}
		
		public function set swordStockBuilders(o:int):void {
			_swordStockBuilders = o;
		}
		
		public function get swordStockBeginTime():Number {
			return _swordStockBeginTime;
		}
		
		public function set swordStockBeginTime(o:Number):void {
			_swordStockBeginTime = o;
		}
		
		public function get swordStockEndTime():Number {
			return _swordStockEndTime;
		}
		
		public function set swordStockEndTime(o:Number):void {
			_swordStockEndTime = o;
		}
		
		public function get swordsBeingBuiltBeginTime():Number {
			return _swordsBeingBuiltBeginTime;
		}
		
		public function set swordsBeingBuiltBeginTime(o:Number):void {
			_swordsBeingBuiltBeginTime = o;
		}
		
		public function get swordsBeingBuiltEndTime():Number {
			return _swordsBeingBuiltEndTime;
		}
		
		public function set swordsBeingBuiltEndTime(o:Number):void {
			_swordsBeingBuiltEndTime = o;
		}
		
		//---------------------------------------------------------------//
		
		private var _armorStockCapacity:int;
		private var _armorStockNextCapacity:int;
		private var _armorStockBuilders:int;
		private var _armorStockBeginTime:Number;
		private var _armorStockEndTime:Number;
		
		private var _armorWorkers:int;
		private var _armorsBeingBuilt:int;
		private var _armorsBeingBuiltBeginTime:Number;
		private var _armorsBeingBuiltEndTime:Number;
		
		public function set armorWorkers(o:int):void{
			_armorWorkers = o;
		}

		public function get armorWorkers():int{
			return _armorWorkers;
		}
		
		public function set armorsBeingBuilt(o:int):void{
			_armorsBeingBuilt = o;
		}
		
		public function get armorsBeingBuilt():int{
			return _armorsBeingBuilt;
		}
		
		public function get armorStockCapacity():int {
			return _armorStockCapacity;
		}
		
		public function set armorStockCapacity(o:int):void {
			_armorStockCapacity = o;
		}
		
		public function get armorStockNextCapacity():int {
			return _armorStockNextCapacity;
		}
		
		public function set armorStockNextCapacity(o:int):void {
			_armorStockNextCapacity = o;
		}
		
		public function get armorStockBuilders():int {
			return _armorStockBuilders;
		}
		
		public function set armorStockBuilders(o:int):void {
			_armorStockBuilders = o;
		}
		
		public function get armorStockBeginTime():Number {
			return _armorStockBeginTime;
		}
		
		public function set armorStockBeginTime(o:Number):void {
			_armorStockBeginTime = o;
		}
		
		public function get armorStockEndTime():Number {
			return _armorStockEndTime;
		}
		
		public function set armorStockEndTime(o:Number):void {
			_armorStockEndTime = o;
		}
		
		public function get armorsBeingBuiltBeginTime():Number {
			return _armorsBeingBuiltBeginTime;
		}
		
		public function set armorsBeingBuiltBeginTime(o:Number):void {
			_armorsBeingBuiltBeginTime = o;
		}
		
		public function get armorsBeingBuiltEndTime():Number {
			return _armorsBeingBuiltEndTime;
		}
		
		public function set armorsBeingBuiltEndTime(o:Number):void {
			_armorsBeingBuiltEndTime = o;
		}


		
		//---------------------------------------------------------------//
		
		protected var _wheatRequiredToFeed:int;
		protected var _wheatExpected:int;
		
		public function get wheatRequiredToFeed():int
		{
			return _wheatRequiredToFeed;
		}

		public function set wheatRequiredToFeed(value:int):void
		{
			_wheatRequiredToFeed = value;
		}

		public function get wheatExpected():int
		{
			return _wheatExpected;
		}

		public function set wheatExpected(value:int):void
		{
			_wheatExpected = value;
		}
	}
}
