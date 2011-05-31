package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.BoardDrawer;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.core.UnitMover;
	import com.uralys.tribes.entities.Case;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.DataContainer4UnitSaved;
	import com.uralys.tribes.entities.Equipment;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Item;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Smith;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Utils;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.rpc.CallResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.utils.ObjectUtil;

	[Bindable]
	public class GameManager{
		
		//============================================================================================//
		
		private static var instance:GameManager = new GameManager();
		public static function getInstance():GameManager{
			return instance;
		}

		//============================================================================================//
		
		private var savePlayerResponder:CallResponder;
		private var loadCasesResponder:CallResponder;

		// -  ================================================================================
		
		public function GameManager()
		{
			savePlayerResponder = new CallResponder();
			loadCasesResponder = new CallResponder();
		}
		
		private function getGameWrapper():RemoteObject{
			
			var gameWrapper:RemoteObject = new RemoteObject();
			gameWrapper.destination = "GameWrapper";
			gameWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
			
			return gameWrapper;
		}
		
		//=====================================================================================//
		// CONTROLS
		//============================================================================================//
		
		private function loginForceSteps():void {
			trace("loginForceSteps");
			var now:Number = new Date().getTime();
			var timeSpentMillis:Number = now - Numbers.SERVER_START;
			
			var nbStepsSinceBeginning:int = timeSpentMillis/(Numbers.TIME_PER_STEP*60*1000);
			var nbStepsMissed:int = nbStepsSinceBeginning - Session.player.lastStep;
			
			trace(nbStepsMissed + " missed");
			for(var i:int = 0; i < nbStepsMissed-1; i++){
				saveStep(false,true);
			}
		
			// enregistre le statut lors du dernier step a rattrapper
			if(nbStepsMissed > 0)
				saveStep(true);

			
			var city:City = Session.player.cities.getItemAt(0) as City;
			BoardDrawer.getInstance().refreshMap(city.x, city.y);
		}
		
		public function saveStep(needRefreshOfTheCity:Boolean, loginCatchUp:Boolean = false)
		{
			for each(var city:City in Session.player.cities)
			{
				city.wheat += city.wheatEarned - city.wheatSpent;
				city.wood += city.woodEarned - city.woodSpent;
				city.iron += city.ironEarned - city.ironSpent;

				var starvation:Boolean = false;
				
				if(city.wheat < 0){
					starvation = true;
					city.wheat = 0;
				}
				
				var armyRaised:int = city.armyRaised - city.armyReleased;
				city.population = calculatePopulation(city.population, starvation, armyRaised);

				refreshCitySmithsAndEquipments(city);
				
				city.reset();
				refreshCity(city, starvation);
			}
			
			(Session.player.cities.getItemAt(0) as City).gold += Session.player.nbLands;
			
			if(!loginCatchUp){
				savePlayer();
			}
			
			if(needRefreshOfTheCity)
				Session.board.refreshBoard(city);
		}
		
		
		private function refreshCitySmithsAndEquipments(city:City):void
		{
			for each(var equipment:Equipment in city.equipmentStock)
			{
				switch(equipment.item.name)
				{
					case "bow" :
						equipment.size = city.bowStock
						+ city.bowsRestored
						- city.bowsEquiped
						+ city.bowWorkers * equipment.item.peopleRequired;
						
						break;
					case "sword" :
						equipment.size = city.swordStock
						+ city.swordsRestored
						- city.swordsEquiped
						+ city.swordWorkers * equipment.item.peopleRequired;
						break;
					case "armor" :
						equipment.size = city.armorStock
						+ city.armorsRestored
						- city.armorsEquiped
						+ city.armorWorkers * equipment.item.peopleRequired;
						break;
				}
			}
			
			for each(var smith:Smith in city.smiths)
			{
				switch(smith.item.name){
					case "bow" :
						smith.people = city.bowWorkers;
						break;
					case "sword" :
						smith.people = city.swordWorkers;
						break;
					case "armor" :
						smith.people = city.armorWorkers;
						break;
				}
			}
		}
		
		
		//-------------------------------------------------------------------------//
		
		
		
		public function createUnit(unit:Unit, cityUID):void
		{
			if(unit == null)
				return;
			
			var bows:Equipment = new Equipment();
			var swords:Equipment = new Equipment();
			var armors:Equipment = new Equipment();
			
			bows.size = unit.bows;
			swords.size = unit.swords;
			armors.size = unit.armors;
			
			for each(var item:Item in Session.ITEMS){
				switch(item.name){
					case "bow" :
						bows.item = item;
						break;
					case "sword" :
						swords.item = item;
						break;
					case "armor" :
						armors.item = item;
						break;
				}
			}
			
			bows.equimentUID = unit.unitUID + "_" + bows.item.itemUID; 
			swords.equimentUID = unit.unitUID + "_" + swords.item.itemUID; 
			armors.equimentUID = unit.unitUID + "_" + armors.item.itemUID; 
			
			unit.equipments.addItem(bows);
			unit.equipments.addItem(swords);
			unit.equipments.addItem(armors);
			
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.createUnit.addEventListener("result", unitSaved);
			gameWrapper.createUnit(Session.player.uralysUID, unit, cityUID);
			currentUnitBeingSaved = unit;
			
			unit.status = Unit.FREE;
			
			for each(var equipment:Equipment in unit.equipments){
				if(equipment.equimentUID.indexOf("NEW_") != -1)
					equipment.equimentUID = equipment.equimentUID.substring(4);
			}
			
			for each(var move:Move in unit.moves){
				if(move.moveUID.indexOf("NEW_") != -1)
					move.moveUID = move.moveUID.substring(4);
			}
		}
		
		public function updateUnit(unit:Unit, cityUID:String = null):void{
			
			trace("gameManager.updateUnit : " + unit.unitUID);
			
			if(unit == null)
				return;
			
			refreshUnitEquipmentForServerSide(unit);
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.updateUnit.addEventListener("result", unitSaved);
			gameWrapper.updateUnit(unit, cityUID);
			currentUnitBeingSaved = unit;
		}

		
		private function refreshUnitEquipmentForServerSide(unit:Unit):void
		{
			for each(var equipment:Equipment in unit.equipments){
				switch(equipment.item.name){
					case "bow" :
						equipment.size = unit.bows;
						break;
					case "sword" :
						equipment.size = unit.swords;
						break;
					case "armor" :
						equipment.size = unit.armors;
						break;
				}
			}
		}

		public function prepareUnitForClientSide(unit:Unit):Boolean
		{
			var now:Number = new Date().getTime();
			
			trace("prepareUnitForClientSide : " + unit.unitUID);
			trace("unit.endTime : " + unit.endTime);
			trace("now : " + now);
			
			if(unit.endTime != -1 && unit.endTime < now)
			{
				unit.status = Unit.DESTROYED;
				return false;
			}
			
			if(unit.type == 1)
			{
				for each(var equipment:Equipment in unit.equipments){
					switch(equipment.item.name){
						case "bow" :
							unit.bows += equipment.size;
							break;
						case "sword" :
							unit.swords += equipment.size;
							break;
						case "armor" :
							unit.armors += equipment.size;
							break;
					}
				}
			}

			UnitMover.getInstance().refreshMoves(unit);

			unit.ownerStatus = Unit.PLAYER;
			unit.isModified = false;	
			
			return true;
		}
		
		//-------------------------------------------------------------------------------//

		/**
		 * utilisee lors d'un loadcases uniquement 
		 * le case.forceRefresh trouve toutes les unites
		 * et appelle ici pour les enregistrer au passage
		 * ensuite on va faire un addTimer sur chacune des unites
		 */
		public function registerUnitInSession(unit:Unit):void{
			
			var foundInSession:Boolean = false;
			
			for each(var unitInSession:Unit in Session.allUnits){
				if(unit.unitUID == unitInSession.unitUID){
					foundInSession = true;
					break;
				}
			}
			
			if(!foundInSession)
				Session.allUnits.addItem(unit);
		}
		
		//-------------------------------------------------------------------------------//
		
		public function refreshCity(city:City, starvation:Boolean):void
		{
			// recuperation des stocks de la forge
			for each(var equipment:Equipment in city.equipmentStock){
				switch(equipment.item.name){
					case "bow" :
						city.bowStock = equipment.size;
						break;
					case "sword" :
						city.swordStock = equipment.size;
						break;
					case "armor" :
						city.armorStock = equipment.size;
						break;
				}
			}
			
			// recuperations des workers pour chaque item de la forge
			// et verification si on peut laisser autant de smith (si les stocks sont suffisants)
			for each(var smith:Smith in city.smiths){
				
				switch(smith.item.name){
					case "bow" :
						city.bowWorkers = -1;
						// la depense en bois pour les arcs est plus grande que le stock de bois
						if(Numbers.BOW_WOOD * smith.people > city.wood)
							city.bowWorkers = Math.floor(city.wood/Numbers.BOW_WOOD);
						
						// la depense en fer pour les arcs est plus grande que le stock de fer
						if(Numbers.BOW_IRON * smith.people > city.iron)
							city.bowWorkers = Math.floor(city.iron/Numbers.BOW_IRON);
						
						
						if(city.bowWorkers == -1) // stock suffisant
							city.bowWorkers = smith.people;

						break;
					case "sword" :
						city.swordWorkers = -1;
						
						// la depense en bois pour les epees est plus grande que le stock de bois
						if(Numbers.SWORD_WOOD * smith.people > city.wood)
							city.swordWorkers = Math.floor(city.wood/Numbers.SWORD_WOOD);
						
						// la depense en fer pour les epees est plus grande que le stock de fer
						if(Numbers.SWORD_IRON * smith.people > city.iron)
							city.swordWorkers = Math.floor(city.iron/Numbers.SWORD_IRON);
						
						if(city.swordWorkers == -1) // stock suffisant
							city.swordWorkers = smith.people;
						
						break;
					case "armor" :
						city.armorWorkers = -1;
						
						// la depense en bois pour les armoures est plus grande que le stock de bois
						if(Numbers.ARMOR_WOOD * smith.people > city.wood)
							city.armorWorkers = Math.floor(city.wood/Numbers.ARMOR_WOOD);
						
						// la depense en fer pour les armoures est plus grande que le stock de fer
						if(Numbers.ARMOR_IRON * smith.people > city.iron)
							city.armorWorkers = Math.floor(city.iron/Numbers.ARMOR_IRON);
						
						if(city.armorWorkers == -1) // stock suffisant
							city.armorWorkers = smith.people;
						
						break;
				}
			}
			
			if(starvation){
				city.bowWorkers = 0;
				city.swordWorkers = 0;
				city.armorWorkers = 0;
				city.peopleCreatingWheat = city.population;
				city.peopleCreatingWood = 0;
				city.peopleCreatingIron = 0;
			}
			
			
			// calcul des depenses prevues avec les choix du tour precedent
			city.woodSpent += city.bowWorkers * Numbers.BOW_WOOD;
			city.woodSpent += city.swordWorkers * Numbers.SWORD_WOOD;
			city.woodSpent += city.armorWorkers * Numbers.ARMOR_WOOD;
			
			city.ironSpent += city.bowWorkers * Numbers.BOW_IRON;
			city.ironSpent += city.swordWorkers * Numbers.SWORD_IRON;
			city.ironSpent += city.armorWorkers * Numbers.ARMOR_IRON;

			
			city.wheatSpent = city.population * Numbers.FEED_COEFF;
			city.wheatSpent += city.unitsToFeed;
			
			city.refreshUnemployed();
			city.peopleCreatingWheat += city.unemployed;
			city.refreshUnemployed();
			
			// calcul des resources gagnees avec le precedent choix de peopleCreatingXXX
			city.wheatEarned = Numbers.WHEAT_EARNING_COEFF * city.peopleCreatingWheat;
			city.woodEarned = Numbers.WOOD_EARNING_COEFF * city.peopleCreatingWood;
			city.ironEarned = Numbers.IRON_EARNING_COEFF * city.peopleCreatingIron;
			
		}
		
		
		private function calculatePopulation(population:int, starvation:Boolean, armyRaised:int):int {
			
			var maxPercentage:int = (12000 - population)/1000;
			
			var naturalEvolutionPercentage:int = Utils.random(maxPercentage)+1;
			var armyPercentage:int = 100*armyRaised/population;
			
			if(population < 200)
				naturalEvolutionPercentage *= 5;
			else if(population < 500)
				naturalEvolutionPercentage *= 4;
			else if(population < 1000)
				naturalEvolutionPercentage *= 3;
			else if(population < 2000)
				naturalEvolutionPercentage *= 2;
			
			
			var evolutionPercentage:int = naturalEvolutionPercentage - Math.sqrt(armyPercentage);
			
			if(starvation)
				evolutionPercentage -= 25;
			
			var populationEvolution:int =  population*evolutionPercentage/100;
			
			
			populationEvolution /= 20;

			if(population > 10000)
				populationEvolution /= 10;
			
			return population + populationEvolution - armyRaised;
		}
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function loadItems():void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.loadItems.addEventListener("result", itemsLoaded);
			gameWrapper.loadItems();
		} 

		public function loadCases(centerX:int, centerY:int):void
		{
			var groups:Array = Utils.getGroups(centerX, centerY);
			groups.sort(Array.NUMERIC);
			
			Session.firstCaseX = (groups[0]%27) * 15; // modulo 27
			Session.firstCaseY = ((int)(groups[0]/27)) * 15; // divisé par 27
			
			trace("loadCases center : [ " + centerX + " | " + centerY + " ]");
			trace(groups);
			trace("Session.firstCaseX : " + Session.firstCaseX);
			trace("Session.firstCaseY : " + Session.firstCaseY);
			
			Session.WAIT_FOR_SERVER = true;
			
			var caseUIDs:ArrayCollection = new ArrayCollection();
			
			Session.LEFT_LIMIT_LOADED  	 = (centerX - Numbers.NB_HORIZONTAL_TILES_BY_LOADING/2) * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4); 
			Session.RIGHT_LIMIT_LOADED	 = (centerX + Numbers.NB_HORIZONTAL_TILES_BY_LOADING/2) * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
			Session.TOP_LIMIT_LOADED 	 = (centerY - Numbers.NB_VERTICAL_TILES_BY_LOADING/2) * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2); 
			Session.BOTTOM_LIMIT_LOADED	 = (centerY + Numbers.NB_VERTICAL_TILES_BY_LOADING/2) * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2); 
			
			loadCasesResponder.addEventListener("result", casesLoaded);
			loadCasesResponder.token = getGameWrapper().loadCases(groups);
   			
		}

		public function savePlayer():void{
			savePlayerResponder.token = getGameWrapper().savePlayer(Session.player);
		}

		public function deleteUnit (unit:Unit):void{
			getGameWrapper().deleteUnit(Session.player.uralysUID, unit.unitUID);
		}

		public function deleteUnits (unitUIDs:ArrayCollection):void{
			getGameWrapper().deleteUnits(Session.player.uralysUID, unitUIDs);
		}

		public function changeName():void{
			getGameWrapper().changeName(Session.player.uralysUID, Session.player.name);			
		}

		public function changeCityName(newName:String):void{
			getGameWrapper().changeCityName(Session.board.selectedCity.cityUID, newName);			
		}
		
		public function saveCity(city:City):void
		{
			refreshCitySmithsAndEquipments(city);
			getGameWrapper().saveCity(city);			
		}

		public function buildCity(city:City, merchant:Unit):void
		{
			refreshCitySmithsAndEquipments(city);
			
			currentUnitBeingSaved = merchant;
			cityBeingSaved = city;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.buildCity.addEventListener("result", unitSaved);
			gameWrapper.buildCity(city, merchant, Session.player.uralysUID);	
		}
		
		//--------------------------------------------------------------------------------//
		// pour que les appels AMF se fassent 1 par 1, on utilise le deleteNextMove, qui est appele à chaque 'moveDeleted'
		// (je ne peux pas appeler en meme temps gameWrapper.deleteMove plusieurs fois)
		
		private var movesBeingDeleted:Array;
		private function deleteAllMovesToBeDeleted ():void
		{
			trace("deleteAllMovesToBeDeleted");
			movesBeingDeleted = Session.movesToDelete.toArray();
			deleteNextMove(movesBeingDeleted.shift());
		}
		
		private function deleteNextMove (move:Move):void{
			trace("deleteNextMove");
			if(move == null)
				return;
			
			trace("gameWrapper.deleteMove");
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.deleteMove.addEventListener("result", moveDeleted);
			gameWrapper.deleteMove(move.moveUID);
		}

		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		
		private function itemsLoaded(event:ResultEvent):void{
			Session.ITEMS = event.result as ArrayCollection;
			Numbers.loadItemData();
			loginForceSteps();
		}
		
		
		private function casesLoaded(event:ResultEvent):void
		{
			trace("casesLoaded");
			
			//------------------------------------------------//
			
			Session.board.mapPositioner.removeAllElements();
			
			//------------------------------------------------//

			Session.CASES_LOADED = event.result as ArrayCollection;
			trace("received : " + Session.CASES_LOADED.length + " cases");
			
			//------------------------------------------------//
			// init du tableau
			
			Session.map = [];
			
			for(var i:int=0; i < Numbers.NB_HORIZONTAL_TILES_BY_LOADING; i++)
			{
				Session.map[Session.firstCaseX+i] = [];
				for(var j:int=0; j < Numbers.NB_VERTICAL_TILES_BY_LOADING; j++)
				{
					Session.map[Session.firstCaseX+i][Session.firstCaseY+j] = new Case(Session.firstCaseX+i,Session.firstCaseY+j);
				}
			}

			//------------------------------------------------//
			// affectation des vraies cases
			// on va appeler le case.refresh sur toutes les cases
			// ce qui va rafraichir les moves des units sur chaque case
			// et remplir Session.movesToDelete
			// et Session.allUnits
			// on affecte dans le forceRefresh le _move du actif sur la case. (ca suppose 1 seul pion visible par case) 
			
			for each(var _case:Case in Session.CASES_LOADED)
			{
				Session.map[_case.x][_case.y] = _case;
				(Session.map[_case.x][_case.y] as Case).forceRefresh(true);
			}

			//------------------------------------------------//
			
			// on enregistre tous les timers pour les moves des unites du plateau
			UnitMover.getInstance().refreshTimers();
			
			// on affiche tout, et on affecte les images aux pions des cases
			BoardDrawer.getInstance().refreshDisplay();
			
			// on refresh l'etat des armees dans les villes
			Session.board.refreshUnitsInCity();
			
			// on supprime maintenant tous les Session.movesToDelete
			deleteAllMovesToBeDeleted();
		}
		
		private var currentUnitBeingSaved:Unit;
		private var cityBeingSaved:City;
		private function unitSaved(event:ResultEvent):void
		{
			trace("--------");
			trace("unitSaved");
			
			currentUnitBeingSaved.isModified = false;
			Session.board.cityForm.currentState = Session.board.cityForm.normalState.name;
			
			if(event.result != null)
			{

				var casesAltered:ArrayCollection = event.result.casesAltered;
				var unitsAltered:ArrayCollection = event.result.unitsAltered;

				if(cityBeingSaved != null){
					cityBeingSaved.cityUID = event.result.cityUID as String;
					cityBeingSaved = null;
				}

				for each(var unitAltered:Unit in unitsAltered)
				{
					trace("unitAltered : " + unitAltered.unitUID);
					prepareUnitForClientSide(unitAltered);
					
					var unitInPlayer:Unit = Session.player.getUnit(unitAltered.unitUID);
					
					if(unitInPlayer == null)
						Session.player.units.addItem(unitAltered);
					else{
						Session.player.refreshUnit(unitAltered);
					}
						
					
					try{
						UnitMover.getInstance().addTimer(unitAltered.moves.toArray());
					}
					catch(e:Error){trace("not able to addTimer for this unit");};
				}
				
				for each(var caseAltered:Case in casesAltered)
				{
					trace("caseAltered : " + caseAltered.caseUID);
					if(caseAltered.challenger != null)
						trace("challengerUID : " + caseAltered.challenger.playerUID);
					trace("timeFromChallenging : " + caseAltered.timeFromChallenging);
					try{
						var caseInSession:Case = (Session.map[caseAltered.x][caseAltered.y] as Case);
						
						if(caseInSession != null){
							Session.map[caseAltered.x][caseAltered.y].recordedMoves = caseAltered.recordedMoves;
							Session.map[caseAltered.x][caseAltered.y].challenger = caseAltered.challenger;
							Session.map[caseAltered.x][caseAltered.y].timeFromChallenging = caseAltered.timeFromChallenging;
						}
						else
							Session.map[caseAltered.x][caseAltered.y] = caseAltered;
						
						Session.map[caseAltered.x][caseAltered.y].forceRefresh();
					}
					catch(e:Error){trace("not able to refresh this case");};
				}

				refreshStatusOfAllUnitsInSession();
				
				// on refresh les villes au cas ou le deplacement fait partir/arriver une unite de/dans une ville
				Session.board.refreshUnitsInCity();
			}

			trace("--------");
		}

		public function refreshStatusOfAllUnitsInSession():void
		{
			for each(var unit:Unit in Session.player.units)
			{
				if(unit.endTime != -1 && unit.endTime < new Date().getTime())
					unit.status = Unit.DESTROYED;
			}
		}

		private function moveDeleted(event:ResultEvent):void{
			if(movesBeingDeleted != null && movesBeingDeleted.length > 0)
				deleteNextMove(movesBeingDeleted.shift());
			else
				Session.movesToDelete = new ArrayCollection();
		}
	}
}