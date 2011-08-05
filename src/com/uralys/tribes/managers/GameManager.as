
package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
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
		
		private function loginForceSteps():void 
		{
			trace("loginForceSteps");
			var now:Number = new Date().getTime();
			var timeSpentMillis:Number = now - Numbers.SERVER_START;
			
			var nbStepsSinceBeginning:int = timeSpentMillis/(Numbers.TIME_PER_STEP*60*1000);
			var nbStepsMissed:int = nbStepsSinceBeginning - Session.player.lastStep;
			
			for each(var city:City in Session.player.cities)
			{
				trace("MAJ des stocks de la forge");
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
			}
			
			
			trace(nbStepsMissed + " missed");
			for(var i:int = 0; i < nbStepsMissed-1; i++){
				saveStep(true);
			}
		
			// enregistre le statut lors du dernier step a rattrapper
			if(nbStepsMissed > 0){
				saveStep();
			}

			var city:City = Session.player.cities.getItemAt(0) as City;
			
			GameManager.getInstance().initMap(city.x, city.y);
			
			Session.LOGGED_IN_FORCE_STEPS_DONE = true;
		}
		
		public function saveStep(loginCatchUp:Boolean = false)
		{
			for each(var city:City in Session.player.cities)
			{
				city.wheat += city.wheatEarned - city.wheatSpent;
				city.wood += city.woodEarned - city.woodSpent;
				city.iron += city.ironEarned - city.ironSpent;
				
				// petite correction, avec les arrondis on arrive parfois a etre negatif de peu. (a verifier)
				// en tout cas on force le zero, sinon dans city.reset ca casse tout.
				
				if(city.wheat < 0)
					city.wheat = 0;
				if(city.wood < 0)
					city.wood = 0;
				if(city.iron < 0)
					city.iron = 0;

				var starvation:Boolean = false;
				
				if(city.wheat < 0){
					starvation = true;
					city.wheat = 0;
				}
				
				var armyRaised:int = city.armyRaised - city.armyReleased;
				city.population = calculatePopulation(city.population, starvation, armyRaised);

				if(!loginCatchUp)
				{
					refreshCitySmithsAndEquipments(city, true);
				}
				// else loginCatchUp : on ne refresh surtout pas smith et equipment, ils sont necessaires pour calculateCityData

				city.reset();
				calculateCityData(loginCatchUp, true, city, starvation);
			}
			
			(Session.player.cities.getItemAt(0) as City).gold += Session.player.nbLands;
			
			if(!loginCatchUp){
				savePlayer();
			}
		}
		
		
		private function refreshCitySmithsAndEquipments(city:City, needPoduction:Boolean):void
		{
			trace("refreshCitySmithsAndEquipments");
			for each(var equipment:Equipment in city.equipmentStock)
			{
				switch(equipment.item.name)
				{
					case "bow" :
						equipment.size = city.bowStock
						+ city.bowsRestored
						- city.bowsEquiped;
						
						if(needPoduction)
							equipment.size += city.bowWorkers * equipment.item.peopleRequired;
						
						break;
					case "sword" :
						equipment.size = city.swordStock
						+ city.swordsRestored
						- city.swordsEquiped
						
						if(needPoduction)
							equipment.size += city.swordWorkers * equipment.item.peopleRequired;
						
						break;
					case "armor" :
						equipment.size = city.armorStock
						+ city.armorsRestored
						- city.armorsEquiped
						
						if(needPoduction)
							equipment.size += city.armorWorkers * equipment.item.peopleRequired;
						
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
		
		public function updatePlayerProfile():void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.updatePlayerProfile(Session.player.playerUID, Session.playerLoaded.profile);
		}

		//-------------------------------------------------------------------------//
		
		public function getPlayerInfo(playerUID:String):void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getPlayerInfo.addEventListener("result", receivedPlayerInfo);
			gameWrapper.getPlayerInfo(playerUID);
		}

		public function receivedPlayerInfo(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.playerLoaded = event.result as Player;
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
			
			//unit.status = Unit.FREE;
			
			for each(var equipment:Equipment in unit.equipments){
				if(equipment.equimentUID.indexOf("NEW_") != -1)
					equipment.equimentUID = equipment.equimentUID.substring(4);
			}
			
			for each(var move:Move in unit.moves){
				if(move.moveUID.indexOf("NEW_") != -1)
					move.moveUID = move.moveUID.substring(4);
			}
		}
		
		public function updateUnit(unit:Unit, cityUID:String = null):void
		{
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
			trace("------------------");
			trace("prepareUnitForClientSide : " + unit.unitUID);
			trace("unit.playerUID : " + unit.player.playerUID);
			trace("unit.endTime : " + unit.endTime);
			trace("now : " + now);
			
			

			if(unit.endTime != -1 && unit.endTime < now)
			{
				unit.status = Unit.DESTROYED;
				trace("Unit.DESTROYED");
				return false;
			}

			else if(unit.beginTime > now)
			{
				unit.status = Unit.FUTURE;
				trace("Unit.FUTURE");
				return false;
			}

			if(!UnitMover.getInstance().refreshMoves(unit)){
				unit.status = Unit.DESTROYED;
				trace("bug ! endTime etait != -1 et pourtant on a une Unit.DESTROYED (la case n'e contient pas de move pour cette unit, car il n'y a plus de move pour cette unit !)");
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


			unit.ownerStatus = Unit.PLAYER;
			unit.isModified = false;	
			
			// ici unit.isIntercepted a besoin que le currentCaseUID soit set.
			if(unit.endTime != -1
				&& unit.isInterceptedOnThisCase)
			{
				trace("Unit.INTERCEPTED_ON_THIS_CASE");
				unit.status = Unit.INTERCEPTED_ON_THIS_CASE;
			}
				
			else{
				trace("Unit.FREE");
				unit.status = Unit.FREE;
			}
			return true;
		}
		
		//-------------------------------------------------------------------------------//

		/**
		 * utilisee lors d'un loadcases uniquement 
		 * le case.forceRefresh trouve toutes les unites
		 * et appelle ici pour les enregistrer au passage
		 * ensuite on va faire un addTimer sur chacune des unites
		 */
		public function registerUnitInSession(unit:Unit):void
		{
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
		
		public function calculateCityData(loginCatchUp:Boolean, forceCalculation:Boolean, city:City, starvation:Boolean):void
		{
			trace("calculateCityData");
			if(!forceCalculation && city.calculationDone)
				return;
			
			// recuperations des workers pour chaque item de la forge
			// et verification si on peut laisser autant de smith (si les stocks sont suffisants)
			var woodSpendingForThisStep:int;
			var ironSpendingForThisStep:int;
			
			for each(var smith:Smith in city.smiths)
			{
				switch(smith.item.name){
					case "bow" :
						trace("------");
						trace("Bows");
						trace("smith.people : " + smith.people);
						trace("depense en bois : " + (Numbers.BOW_WOOD * smith.people) + " | city.wood : " + city.wood);
						trace("depense en fer : " + (Numbers.BOW_IRON * smith.people) + " | city.iron : " + city.iron);
						
						city.bowWorkers = -1;
						// la depense en bois pour les arcs est plus grande que le stock de bois
						if(Numbers.BOW_WOOD * smith.people > city.wood)
							city.bowWorkers = Math.floor(city.wood/Numbers.BOW_WOOD);
						
						// la depense en fer pour les arcs est plus grande que le stock de fer
						if(Numbers.BOW_IRON * smith.people > city.iron)
							city.bowWorkers = Math.floor(city.iron/Numbers.BOW_IRON);
						
						trace("city.bowWorkers : " + city.bowWorkers);
						
						if(city.bowWorkers == -1) // stock suffisant
							city.bowWorkers = smith.people;

						trace("updating bowStock");
						city.bowStock += city.bowWorkers;
						
						trace("final : city.bowWorkers : " + city.bowWorkers);
						trace("final : city.bowStock : " + city.bowStock);
						
						// on rajoute les depenses pour ce step
						woodSpendingForThisStep += Numbers.BOW_WOOD * city.bowWorkers;
						ironSpendingForThisStep += Numbers.BOW_IRON * city.bowWorkers;

						break;
					case "sword" :		
						
						var woodRemaining:int = city.wood - woodSpendingForThisStep;
						var ironRemaining:int = city.iron - ironSpendingForThisStep;
						
						trace("------");
						trace("Swords");
						trace("smith.people : " + smith.people);
						trace("depense en bois : " + (Numbers.SWORD_WOOD * smith.people) + " | woodRemaining : " + woodRemaining);
						trace("depense en fer : " + (Numbers.SWORD_IRON * smith.people) + " | ironRemaining : " + ironRemaining);
						
						city.swordWorkers = -1;
						
						// la depense en bois pour les epees est plus grande que le stock de bois
						if(Numbers.SWORD_WOOD * smith.people > woodRemaining)
							city.swordWorkers = Math.floor(woodRemaining/Numbers.SWORD_WOOD);
						
						// la depense en fer pour les epees est plus grande que le stock de fer
						if(Numbers.SWORD_IRON * smith.people > ironRemaining)
							city.swordWorkers = Math.floor(ironRemaining/Numbers.SWORD_IRON);
						
						trace("city.swordWorkers : " + city.swordWorkers);
						
						if(city.swordWorkers == -1) // stock suffisant
							city.swordWorkers = smith.people;
						
						trace("updating swordStock");
						city.swordStock += city.swordWorkers;
						
						trace("final : city.swordWorkers : " + city.swordWorkers);
						trace("final : city.swordStock : " + city.swordStock);
						
						// on rajoute les depenses pour ce step
						woodSpendingForThisStep += Numbers.SWORD_WOOD * city.swordWorkers;
						ironSpendingForThisStep += Numbers.SWORD_IRON * city.swordWorkers;
						
						break;
					case "armor" :				
						
						var woodRemaining:int = city.wood - woodSpendingForThisStep;
						var ironRemaining:int = city.iron - ironSpendingForThisStep;

						trace("------");
						trace("Armors");
						trace("smith.people : " + smith.people);
						trace("depense en bois : " + (Numbers.ARMOR_WOOD * smith.people) + " | woodRemaining : " + woodRemaining);
						trace("depense en fer : " + (Numbers.ARMOR_IRON * smith.people) + " | ironRemaining : " + ironRemaining);
						
						city.armorWorkers = -1;
						
						// la depense en bois pour les armoures est plus grande que le stock de bois
						if(Numbers.ARMOR_WOOD * smith.people > woodRemaining)
							city.armorWorkers = Math.floor(woodRemaining/Numbers.ARMOR_WOOD);
						
						// la depense en fer pour les armoures est plus grande que le stock de fer
						if(Numbers.ARMOR_IRON * smith.people > ironRemaining)
							city.armorWorkers = Math.floor(ironRemaining/Numbers.ARMOR_IRON);
						
						trace("city.armorWorkers : " + city.armorWorkers);
						if(city.armorWorkers == -1) // stock suffisant
							city.armorWorkers = smith.people;
						
						trace("updating armorStock");
						city.armorStock += city.armorWorkers;
						
						trace("final : city.armorWorkers : " + city.armorWorkers);
						trace("final : city.armorStock : " + city.armorStock);
						
						// on rajoute les depenses pour ce step
						woodSpendingForThisStep += Numbers.ARMOR_WOOD * city.armorWorkers;
						ironSpendingForThisStep += Numbers.ARMOR_IRON * city.armorWorkers;
						
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
			city.wheatSpent += city.unitsToFeed * Numbers.FEED_COEFF;
			
			city.refreshUnemployed();
			city.peopleCreatingWheat += city.unemployed;
			if(city.peopleCreatingWheat < 0)city.peopleCreatingWheat = 0; // ca arrive que les unemployed soient plus nombreux que population à la connexion...on force au moins le 0 ca fera toujours moins faux que du negatif...
			city.refreshUnemployed();
			
			// calcul des resources gagnees avec le precedent choix de peopleCreatingXXX
			city.wheatEarned = Numbers.WHEAT_EARNING_COEFF * city.peopleCreatingWheat;
			city.woodEarned = Numbers.WOOD_EARNING_COEFF * city.peopleCreatingWood;
			city.ironEarned = Numbers.IRON_EARNING_COEFF * city.peopleCreatingIron;
			
			city.calculationDone = true;
		}
		
		
		private function calculatePopulation(population:int, starvation:Boolean, armyRaised:int):int 
		{
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
		
		public function validateMerchants(city:City):void
		{
			if(city.merchant.status == Unit.TO_BE_CREATED)
				createUnit(city.merchant, city.cityUID);
			else{
				city.merchant.refreshLastMoveBeforeReplacingUnit();
				updateUnit(city.merchant, city.cityUID);
			}
			
			BoardDrawer.getInstance().refreshUnits(Session.CURRENT_CASE_SELECTED);
		}
		
		public function validateArmy(city:City):void
		{
			if(city.army.status == Unit.TO_BE_CREATED)
				createUnit(city.army, city.cityUID);
			else{
				city.army.refreshLastMoveBeforeReplacingUnit();
				updateUnit(city.army, city.cityUID);
			}
			
			BoardDrawer.getInstance().refreshUnits(Session.CURRENT_CASE_SELECTED);
		}
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function loadItems():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.loadItems.addEventListener("result", itemsLoaded);
			gameWrapper.loadItems();
		} 

		public function initMap(centerX:int, centerY:int):void
		{
			Session.WAIT_FOR_SERVER = true;
			loadCases(centerX, centerY, true);
		}
		
		public function loadCases(centerX:int, centerY:int, refreshLandOwners:Boolean):void
		{
			Session.WAIT_FOR_SERVER = true;
			
			trace("-------------------------------------");

			var groups:Array = Utils.getGroups(centerX, centerY);
			groups.sort(Array.NUMERIC);
			
			Session.firstCaseX = (groups[0]%27) * 15; // modulo 27
			Session.firstCaseY = ((int)(groups[0]/27)) * 15; // divisé par 27
			
			
			trace("-------------------------------------");
			trace("loadCases center : [ " + centerX + " | " + centerY + " ]");
			trace(groups);
			trace("Session.firstCaseX : " + Session.firstCaseX);
			trace("Session.firstCaseY : " + Session.firstCaseY);
			
			var caseUIDs:ArrayCollection = new ArrayCollection();
			Session.nbTilesByEdge = Math.sqrt(groups.length) * 15;

			trace("nbTilesByEdge : " + Session.nbTilesByEdge);
			trace("-------------------------------------");
			trace("Utils.getLandWidth() : " + Utils.getLandWidth());
			trace("15 * Utils.getLandWidth() : " + (15 * Utils.getLandWidth()));
			trace("Utils.getXPixel(Session.firstCaseX + 15 * Utils.getLandWidth()) : " + (Utils.getXPixel(Session.firstCaseX + 15 * Utils.getLandWidth())));
			
			
			Session.LEFT_LIMIT_LOADED  	 = Utils.getXPixel(Session.firstCaseX);
			Session.RIGHT_LIMIT_LOADED	 = Utils.getXPixel(Session.firstCaseX) + Session.nbTilesByEdge * Utils.getLandWidth();
			Session.TOP_LIMIT_LOADED 	 = Utils.getYPixel(Session.firstCaseY);
			Session.BOTTOM_LIMIT_LOADED	 = Utils.getYPixel(Session.firstCaseY) + Session.nbTilesByEdge * Utils.getLandHeight();
			
			var gameWrapper:RemoteObject = getGameWrapper();
			loadCasesResponder.addEventListener("result", casesLoaded);
			loadCasesResponder.token = gameWrapper.loadCases(groups, refreshLandOwners);
		}

		public function savePlayer():void{
			var gameWrapper:RemoteObject = getGameWrapper();
			savePlayerResponder.token = gameWrapper.savePlayer(Session.player);
		}

		public function deleteUnit (unit:Unit):void
		{
			if(unit.status == Unit.TO_BE_CREATED)
				return;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.deleteUnit(Session.player.uralysUID, unit);
		}

		public function deleteUnits (unitUIDs:ArrayCollection):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.deleteUnits(Session.player.uralysUID, unitUIDs);
		}

		public function changeName():void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.changeName(Session.player.uralysUID, Session.player.name);			
		}

		public function changeCityName(newName:String):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.changeCityName(Session.board.selectedCity.cityUID, newName);			
		}
		

		public function saveCity(city:City):void
		{
			refreshCitySmithsAndEquipments(city, false);
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.saveCity(city);			
		}

		public function buildCity(city:City, merchant:Unit):void
		{
			refreshCitySmithsAndEquipments(city, false);
			
			cityBeingSaved = city;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.buildCity.addEventListener("result", cityBuilt);	
			gameWrapper.buildCity(city, merchant, Session.player.uralysUID);	
		}
		
		private function cityBuilt(event:ResultEvent):void
		{
			for each(var city:City in Session.player.cities)
			{
				if(city.cityUID == "new"){
					city.cityUID = event.result as String;
					break;
				}
			}
			
			Session.board.reloadCurrentCases(true);
		}
		
		//----------------------------------------------------------------------//
		
		public function getCitiesBoard(forceRefresh:Boolean = false):void
		{
			if(!forceRefresh && Session.citiesBoard != null)
				return;
			
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getCitiesBoard.addEventListener("result", citiesBoardReceived);
			gameWrapper.getCitiesBoard();
		}
	
		public function getArmiesBoard(forceRefresh:Boolean = false):void
		{
			if(!forceRefresh && Session.armiesBoard != null)
				return;
			
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getArmiesBoard.addEventListener("result", armiesBoardReceived);
			gameWrapper.getArmiesBoard();
		}
		
		public function getPopulationBoard(forceRefresh:Boolean = false):void
		{
			if(!forceRefresh && Session.populationBoard != null)
				return;
			
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getArmiesBoard.addEventListener("result", populationBoardReceived);
			gameWrapper.getArmiesBoard();
		}
		
		public function getLandsBoard(forceRefresh:Boolean = false):void
		{
			if(!forceRefresh && Session.landsBoard != null)
				return;
			
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getLandsBoard.addEventListener("result", landsBoardReceived);
			gameWrapper.getLandsBoard();
		}
		
		//--------------------------------------------------------------------------------//
		
		private function citiesBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.citiesBoard = Utils.sort(event.result as ArrayCollection, "nbCities");
		}
		
		private function armiesBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.armiesBoard = Utils.sort(event.result as ArrayCollection, "nbArmies");
		}
		
		private function populationBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.populationBoard = Utils.sort(event.result as ArrayCollection, "nbPopulation");
		}
		
		private function landsBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.landsBoard = Utils.sort(event.result as ArrayCollection, "nbLands");
		}
		
		//--------------------------------------------------------------------------------//
		
		public function sendMessage(message:String):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.sendMessage(Session.player.playerUID, Session.playerLoaded.playerUID, message);
		}

		public function markAsRead(messageUIDs:ArrayCollection):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.markAsRead(messageUIDs);
		}

		public function archiveMessages(messageUIDs:ArrayCollection):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.archiveMessages(messageUIDs);
		}

		public function deleteMessages(messageUIDs:ArrayCollection):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.deleteMessages(Session.player.playerUID, messageUIDs);
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
		
		
		private function itemsLoaded(event:ResultEvent):void
		{
			Session.ITEMS = event.result as ArrayCollection;
			Numbers.loadItemData();
			
			if(Session.GAME_OVER){
				initMap(100, 100);
			}
			else
				loginForceSteps();
		}
		
		
		private function casesLoaded(event:ResultEvent):void
		{
			trace("casesLoaded");
			
			//------------------------------------------------//

			Session.CASES_LOADED = event.result as ArrayCollection;
			trace("received : " + Session.CASES_LOADED.length + " cases");
			
			//------------------------------------------------//
			// init du tableau
			
			Session.map = [];
			
			for(var i:int=0; i < Session.nbTilesByEdge; i++)
			{
				Session.map[Session.firstCaseX+i] = [];
				for(var j:int=0; j < Session.nbTilesByEdge; j++)
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
			// on en profite aussi pour rafraichir les villes en Session
			
			var citiesLoaded:ArrayCollection = new ArrayCollection();
			for each(var _case:Case in Session.CASES_LOADED)
			{
				Session.map[_case.x][_case.y] = _case;
				(Session.map[_case.x][_case.y] as Case).forceRefresh(true);
				
				if(_case.city != null)
					citiesLoaded.addItem(_case.city);
			}

			for each(var _cityLoaded:City in citiesLoaded)
			{
				for each(var _city:City in Session.player.cities)
				{
					if(_city.cityUID == _cityLoaded.cityUID)
					{
						_city = _cityLoaded;
						break;
					}
				}
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
					if(unitAltered.player.playerUID != Session.player.playerUID)
						continue;
					
					var unitIsAlive:Boolean = prepareUnitForClientSide(unitAltered);
					if(!unitIsAlive)
						continue;
					
					var unitInPlayer:Unit = Session.player.getUnit(unitAltered.unitUID);
					
					if(unitInPlayer == null && unitAltered.player.playerUID == Session.player.playerUID && unitAltered.status != Unit.DESTROYED)
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
					catch(e:Error){trace("----");trace(e.message);trace("----");trace("not able to refresh this case");};
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