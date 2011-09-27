
package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.core.BoardDrawer;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.core.UnitMover;
	import com.uralys.tribes.entities.Ally;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Item;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.ObjectsAltered;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Stock;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.pages.Board;
	import com.uralys.tribes.renderers.Pawn;
	import com.uralys.utils.Utils;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.FlexGlobals;
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
		private var loadCellsResponder:CallResponder;

		// -  ================================================================================
		
		public function GameManager()
		{
			savePlayerResponder = new CallResponder();
			loadCellsResponder = new CallResponder();
		}
		
		private function getGameWrapper():RemoteObject
		{
			var gameWrapper:RemoteObject = new RemoteObject();
			gameWrapper.destination = "GameWrapper";
			gameWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
			
			return gameWrapper;
		}
		
		//=====================================================================================//
		// CONTROLS
		//============================================================================================//
		
		public function refreshPlayer(player:Player):void
		{
			trace("------------");
			trace("refreshPlayer");
			trace("------------");
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getPlayer.addEventListener("result", receivedPlayerToRefresh);
			gameWrapper.getPlayer(player.playerUID, false); // newConnection = false
		}
		
		private function receivedPlayerToRefresh(event:ResultEvent):void
		{
			trace("------------");
			trace("receivedPlayerToRefresh");
			trace("------------");
			var player:Player = event.result as Player
			Session.WAIT_FOR_SERVER = false;
			calculateSteps(player);
			
			if(Session.player.playerUID == player.playerUID){
				Session.player = player;
				Session.board.refreshUnits();
			}
			
			Session.board.reloadCurrentCells(true, true);
		}

		//============================================================================================//
		
		private function calculateSteps(player:Player):void 
		{
			trace("calculateSteps");
			
			// la liste est inversée quand on arrive ici..
			if(player.ally != null)
				player.ally.players.source.reverse();
			
			var now:Number = new Date().getTime();
			var timeSpentMillis:Number = now - Numbers.SERVER_START;
			
			var nbStepsSinceBeginning:int = timeSpentMillis/(Numbers.TIME_PER_STEP*60*1000);
			var nbStepsMissed:int = nbStepsSinceBeginning - player.lastStep;
			
			for each(var city:City in player.cities)
			{
				trace("MAJ des stocks : ("+city.stocks.length+" depots)");
				// recuperation des stocks
				for each(var stock:Stock in city.stocks)
				{
					// on peut avoir de la triche ici..
					// car on update la capacite du stock avant le calcul des stepMissed. 
					// et si le joueur a beaucoup de stepMissed, il va remplir le nouveau stock alors qu'il etait limité à ce moment là
					updateStockBuildingStatus(city, stock);
				}
			}
			
			//6*24 = 144 steps par jour
			//7 jours = 7 * 144 = 1008 steps par semaine : on limite à une absence de 7 jours pour les calculs.
			trace(nbStepsMissed + " missed");
			if(nbStepsMissed > 1000)
				nbStepsMissed = 1000;

			for(var i:int = 0; i < nbStepsMissed-1; i++){
				calculateStep(player, true);
			}
		
			// enregistre le statut lors du dernier step a rattrapper
			if(nbStepsMissed > 0){
				calculateStep(player);
			}

			var city:City = player.cities.getItemAt(0) as City;
			
			initMap(city.x, city.y);
			
			Session.LOGGED_IN_FORCE_STEPS_DONE = true;
		}
		
		private function refreshCityStock(city:City, stock:Stock):void
		{
			var stockName:String = Utils.getStockName(stock.stockUID);
			
			switch(stockName){
				case "_wheat_stock" :
					city.wheatStockCapacity = stock.stockCapacity;
					city.wheatStockBuilders = stock.peopleBuildingStock;
					city.wheatStockBeginTime = stock.stockBeginTime;
					city.wheatStockEndTime = stock.stockEndTime;
					city.wheatStockNextCapacity = stock.stockNextCapacity;
					break;
				case "_wood_stock" :
					city.woodStockCapacity = stock.stockCapacity;
					city.woodStockBuilders = stock.peopleBuildingStock;
					city.woodStockBeginTime = stock.stockBeginTime;
					city.woodStockEndTime = stock.stockEndTime;
					city.woodStockNextCapacity = stock.stockNextCapacity;
					break;
				case "_iron_stock" :
					city.ironStockCapacity = stock.stockCapacity;
					city.ironStockBuilders = stock.peopleBuildingStock;
					city.ironStockBeginTime = stock.stockBeginTime;
					city.ironStockEndTime = stock.stockEndTime;
					city.ironStockNextCapacity = stock.stockNextCapacity;
					break;
				case "_bow_stock" :
					city.bowStockCapacity = stock.stockCapacity;
					city.bowStockBuilders = stock.peopleBuildingStock;
					city.bowStockBeginTime = stock.stockBeginTime;
					city.bowStockEndTime = stock.stockEndTime;
					city.bowStockNextCapacity = stock.stockNextCapacity;
					city.bowsBeingBuilt = stock.itemsBeingBuilt;
					city.bowsBeingBuiltBeginTime = stock.itemsBeingBuiltBeginTime;
					city.bowsBeingBuiltEndTime = stock.itemsBeingBuiltEndTime;
					break;
				case "_sword_stock" :
					city.swordStockCapacity = stock.stockCapacity;
					city.swordStockBuilders = stock.peopleBuildingStock;
					city.swordStockBeginTime = stock.stockBeginTime;
					city.swordStockEndTime = stock.stockEndTime;
					city.swordStockNextCapacity = stock.stockNextCapacity;
					city.swordsBeingBuilt = stock.itemsBeingBuilt;
					city.swordsBeingBuiltBeginTime = stock.itemsBeingBuiltBeginTime;
					city.swordsBeingBuiltEndTime = stock.itemsBeingBuiltEndTime;
					break;
				case "_armor_stock" :
					city.armorStockCapacity = stock.stockCapacity;
					city.armorStockBuilders = stock.peopleBuildingStock;
					city.armorStockBeginTime = stock.stockBeginTime;
					city.armorStockEndTime = stock.stockEndTime;
					city.armorStockNextCapacity = stock.stockNextCapacity;
					city.armorsBeingBuilt = stock.itemsBeingBuilt;
					city.armorsBeingBuiltBeginTime = stock.itemsBeingBuiltBeginTime;
					city.armorsBeingBuiltEndTime = stock.itemsBeingBuiltEndTime;
					break;
			}
		}
		
		private var allCitiesAreFilled:Boolean = false; // pour le catchUpCalculation uniquement
		public function calculateStep(player:Player, catchUpCalculation:Boolean = false):void
		{
			(player.cities.getItemAt(0) as City).gold += player.nbLands;
			
			if(!catchUpCalculation || !allCitiesAreFilled)
				allCitiesAreFilled = calculateCitiesStep(player, catchUpCalculation);

			if(!catchUpCalculation){
				savePlayer(player);
			}
		}
		
		private function calculateCitiesStep(player:Player, catchUpCalculation:Boolean = false):void
		{
			trace("------------------------");
			trace("calculateCitiesStep");
			
			for each(var city:City in player.cities)
			{
				trace("--------------");
				trace("city : " + city.name);
				trace("wheatEarned : " + city.wheatEarned + " | city.wheatRequiredToFeed() : " + city.wheatRequiredToFeed());
				trace("woodEarned : " + city.woodEarned);
				trace("ironEarned : " + city.ironEarned);
				city.wheat += city.wheatEarned - city.wheatRequiredToFeed();
				city.wood += city.woodEarned;
				city.iron += city.ironEarned;
				
				// limitation des stocks
				
				if(city.wheat > city.wheatStockCapacity)
					city.wheat = city.wheatStockCapacity;
				if(city.wood > city.woodStockCapacity)
					city.wood = city.woodStockCapacity;
				if(city.iron > city.ironStockCapacity)
					city.iron = city.ironStockCapacity;
				
				var starvation:Boolean = false;
				
				if(city.wheat < 0){
					starvation = true;
					city.wheat = 0;
				}
				
				city.population = calculatePopulation(city.population, starvation);
				
				if(!catchUpCalculation)
					refreshStocksForServerSide(city);
				// else catchUpCalculation : on ne refresh surtout pas smith et equipment, ils sont necessaires pour updateCityWorkersEarningsAndSpendings
				
				refreshCityWorkersOnResources(true, city, starvation);
			}
		}
		
		public function updateStockBuildingStatus(city:City, stock:Stock):void
		{
			var now:Number = new Date().getTime();
			
			if(stock.stockBeginTime != -1 && now > stock.stockEndTime){
				stock.stockBeginTime = -1;
				stock.stockCapacity = stock.stockNextCapacity;
				stock.peopleBuildingStock = 0;
			}

			// init pour passage de 1.2.14 a 1.2.15 : toRemove apres
			if(stock.itemsBeingBuiltBeginTime == 0){
				stock.itemsBeingBuilt = 0;
				stock.itemsBeingBuiltBeginTime = -1;
				stock.itemsBeingBuiltEndTime = 1;
			}
			else	
				
			if(stock.itemsBeingBuiltBeginTime != -1 && now > stock.itemsBeingBuiltEndTime){
				stock.itemsBeingBuiltBeginTime = -1;
				
				switch(Utils.getItem(Utils.getStockItem(stock.stockUID)).name)
				{
					case "bow" :
						city.bows += stock.itemsBeingBuilt;
						city.bowWorkers = 0;
						break;
					case "sword" :
						city.swords += stock.itemsBeingBuilt;
						city.swordWorkers = 0;
						break;
					case "armor" :
						city.armors += stock.itemsBeingBuilt;
						city.armorWorkers = 0;
						break;
				}
				
				stock.itemsBeingBuilt = 0;
			}
			
			refreshCityStock(city,stock);
		}
		
		private function refreshStocksForServerSide(city:City):void
		{
			for each(var stock:Stock in city.stocks)
			{
				var stockName:String = Utils.getStockName(stock.stockUID);

				switch(stockName){
					case "_wheat_stock" :
						stock.peopleBuildingStock = city.wheatStockBuilders;
						stock.stockCapacity = city.wheatStockCapacity;
						stock.stockNextCapacity = city.wheatStockNextCapacity;
						stock.stockBeginTime = city.wheatStockBeginTime;
						stock.stockEndTime = city.wheatStockEndTime;
						break;
					case "_wood_stock" :
						stock.peopleBuildingStock = city.woodStockBuilders;
						stock.stockCapacity = city.woodStockCapacity;
						stock.stockNextCapacity = city.woodStockNextCapacity;
						stock.stockBeginTime = city.woodStockBeginTime;
						stock.stockEndTime = city.woodStockEndTime;
						break;
					case "_iron_stock" :
						stock.peopleBuildingStock = city.ironStockBuilders;
						stock.stockCapacity = city.ironStockCapacity;
						stock.stockNextCapacity = city.ironStockNextCapacity;
						stock.stockBeginTime = city.ironStockBeginTime;
						stock.stockEndTime = city.ironStockEndTime;
						break;
					case "_bow_stock" :
						stock.peopleBuildingStock = city.bowStockBuilders;
						stock.stockCapacity = city.bowStockCapacity;
						stock.stockNextCapacity = city.bowStockNextCapacity;
						stock.stockBeginTime = city.bowStockBeginTime;
						stock.stockEndTime = city.bowStockEndTime;
						stock.smiths = city.bowWorkers;
						stock.itemsBeingBuilt = city.bowsBeingBuilt;
						stock.itemsBeingBuiltBeginTime = city.bowsBeingBuiltBeginTime;
						stock.itemsBeingBuiltEndTime = city.bowsBeingBuiltEndTime;
						break;
					case "_sword_stock" :
						stock.peopleBuildingStock = city.swordStockBuilders;
						stock.stockCapacity = city.swordStockCapacity;
						stock.stockNextCapacity = city.swordStockNextCapacity;
						stock.stockBeginTime = city.swordStockBeginTime;
						stock.stockEndTime = city.swordStockEndTime;
						stock.smiths = city.swordWorkers;
						stock.itemsBeingBuilt = city.swordsBeingBuilt;
						stock.itemsBeingBuiltBeginTime = city.swordsBeingBuiltBeginTime;
						stock.itemsBeingBuiltEndTime = city.swordsBeingBuiltEndTime;
						break;
					case "_armor_stock" :
						stock.peopleBuildingStock = city.armorStockBuilders;
						stock.stockCapacity = city.armorStockCapacity;
						stock.stockNextCapacity = city.armorStockNextCapacity;
						stock.stockBeginTime = city.armorStockBeginTime;
						stock.stockEndTime = city.armorStockEndTime;
						stock.smiths = city.armorWorkers;
						stock.itemsBeingBuilt = city.armorsBeingBuilt;
						stock.itemsBeingBuiltBeginTime = city.armorsBeingBuiltBeginTime;
						stock.itemsBeingBuiltEndTime = city.armorsBeingBuiltEndTime;
						break;
				}
			}
		}
		
		//-------------------------------------------------------------------------//
		
		public function updatePlayerProfile():void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.updatePlayerProfile(Session.player.playerUID, Session.playerLoaded.profile);
		}

		public function updateAllyProfile():void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.updateAllyProfile(Session.allyLoaded.allyUID, Session.allyLoaded.profile);
		}

		public function saveAllyHierarchy():void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.saveAllyHierarchy(Session.player.ally);
		}

		//-------------------------------------------------------------------------//
		
		public function getPlayerInfo(playerUID:String):void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getPlayerInfo.addEventListener("result", receivedPlayerInfo);
			gameWrapper.getPlayerInfo(playerUID);
		}

		private function receivedPlayerInfo(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.playerLoaded = event.result as Player;
		}
		
		//-------------------------------------------------------------------------//
		
		public function getAlly(allyUID:String):void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getAlly.addEventListener("result", receivedAlly);
			gameWrapper.getAlly(allyUID);
		}

		private function receivedAlly(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.allyLoaded = event.result as Ally;
			
			if(Session.player.ally.allyUID == Session.allyLoaded.allyUID)
				Session.player.ally = Session.allyLoaded;
		}
		
		//-------------------------------------------------------------------------//
		
		public function createAlly(allyName:String):void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.createAlly.addEventListener("result", allyCreated);
			gameWrapper.createAlly(Session.player.playerUID, allyName);
		}
		
		private function allyCreated(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.player.ally = event.result as Ally;
			Session.playerLoaded.ally = event.result as Ally;
		}
		
		//-------------------------------------------------------------------------//
		
		public function inviteInAlly(playerUID:String, allyUID:String):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.inviteInAlly(playerUID, allyUID);
		}
		
		public function joinAlly(allyUID:String):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.joinAlly(Session.player.playerUID, allyUID);
		}

		public function removeFromAlly(playerUID:String, allyUID:String):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.removeFromAlly(playerUID, allyUID);
		}
		
		//-------------------------------------------------------------------------//

		public function createUnit(unit:Unit):void
		{
			if(unit == null)
				return;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.createUnit.addEventListener("result", unitSaved);
			gameWrapper.createUnit(Session.player.uralysUID, unit);
			currentUnitBeingSaved = unit;
			
			unit.status = Unit.FREE;
			
			for each(var move:Move in unit.moves){
				if(move.moveUID.indexOf("NEW_") != -1)
					move.moveUID = move.moveUID.substring(4);
			}
		}
		
		public function updateUnit(unit:Unit):void
		{
			trace("gameManager.updateUnit : " + unit.unitUID);
			
			if(unit == null)
				return;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.updateUnit.addEventListener("result", unitSaved);
			gameWrapper.updateUnit(unit);
			currentUnitBeingSaved = unit;
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
					trace("-----");
					trace("unitAltered : " + unitAltered.unitUID);
					if(unitAltered.player.playerUID != Session.player.playerUID)
						continue;
					
					var unitIsAlive:Boolean = prepareUnitForClientSide(unitAltered);
					if(!unitIsAlive)
						continue;
					
					var unitInPlayer:Unit = Session.player.getUnit(unitAltered.unitUID);
					
					if(unitInPlayer == null && unitAltered.status != Unit.DESTROYED)
						Session.player.units.addItem(unitAltered);
					else{
						Session.player.refreshUnit(unitAltered);
					}
				}
				
				for each(var cellAltered:Cell in casesAltered)
				{
					trace("cellAltered : " + cellAltered.cellUID);
					if(cellAltered.challenger != null)
						trace("challengerUID : " + cellAltered.challenger.playerUID);
					trace("timeFromChallenging : " + cellAltered.timeFromChallenging);
					try{
						var caseInSession:Cell = (Session.map[cellAltered.x][cellAltered.y] as Cell);
						
						if(caseInSession != null){
							//	Session.map[cellAltered.x][cellAltered.y].recordedMoves = cellAltered.recordedMoves;
							Session.map[cellAltered.x][cellAltered.y].challenger = cellAltered.challenger;
							Session.map[cellAltered.x][cellAltered.y].timeFromChallenging = cellAltered.timeFromChallenging;
							//	Session.map[cellAltered.x][cellAltered.y].units = cellAltered.units;
						}
						else
							Session.map[cellAltered.x][cellAltered.y] = cellAltered;
						
						Session.map[cellAltered.x][cellAltered.y].forceRefresh();
					}
					catch(e:Error){trace("----");trace(e.message);trace("----");trace("not able to refresh this case");};
				}
				
				refreshStatusOfAllUnitsInSession();
				
				// on refresh les villes au cas ou le deplacement fait partir/arriver une unite de/dans une ville
				Session.board.refreshUnitsInCity();
			}
			
			trace("--------");
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
				trace(" ====> Unit.DESTROYED");
				return false;
			}

			else if(unit.beginTime > now)
			{
				unit.status = Unit.FUTURE;
				trace(" ====> Unit.FUTURE");
				return false;
			}

			if(!UnitMover.getInstance().refreshMoves(unit)){
				unit.status = Unit.DESTROYED;
				trace("bug ! endTime etait != -1 et pourtant on a une Unit.DESTROYED (la case n'e contient pas de move pour cette unit, car il n'y a plus de move pour cette unit !)");
				return false;
			}
			
			unit.ownerStatus = Unit.PLAYER;
			unit.isModified = false;	
			
			// ici unit.isIntercepted a besoin que le currentCellUID soit set.
			if(unit.endTime != -1
				&& unit.isInterceptedOnThisCell)
			{
				trace(" ====> Unit.INTERCEPTED_ON_THIS_CASE");
				unit.status = Unit.INTERCEPTED_ON_THIS_CASE;
			}
				
			else{
				trace(" ====> Unit.FREE");
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
		
		public function refreshCityWorkersOnResources(forceCalculation:Boolean, city:City, starvation:Boolean):void
		{
			trace("--------");
			trace("refreshCityResources");
			if(!forceCalculation && city.calculationDone)
				return;
			
			//Session.board.cityForm.updateSmithsProgressBar();
	
			// attribution autoamtique des unemployed :  a mettre en place pour les comptes premium :p
//			if(!starvation)
//				city.peopleCreatingWheat += city.unemployed;
//	
//			if(city.peopleCreatingWheat < 0)city.peopleCreatingWheat = 0; // ca arrive que les unemployed soient plus nombreux que population à la connexion...on force au moins le 0 ca fera toujours moins faux que du negatif...
//			city.refreshUnemployed();
			
			// calcul des resources gagnees avec le precedent choix de peopleCreatingXXX

			city.woodEarned = Numbers.WOOD_EARNING_COEFF * city.peopleCreatingWood;
			city.ironEarned = Numbers.IRON_EARNING_COEFF * city.peopleCreatingIron;
			
			if(starvation){
				city.peopleCreatingWheat = city.population/5;
				city.woodEarned /= 2;
				city.ironEarned /= 2;
			}

			city.wheatEarned = Numbers.WHEAT_EARNING_COEFF * city.peopleCreatingWheat;

			city.refreshUnemployed();
			Session.board.cityForm.updateWorkersProgressBar();
			
			city.calculationDone = true;
		}
		
		
		private function calculatePopulation(population:int, starvation:Boolean):int 
		{
			trace("------");
			trace("calculatePopulation");
			
			if(starvation)
				return population - population/20;
			else if(population < 10000)
				return population + Utils.random(3);
			else{
				// au dela de 10000, on augmente de 1 la pop avec 1 chance sur 25  
				var i:int = Utils.random(25);
				
				if(i != 1)
					i = 0;
					
				return population + i;
			}
			
			
//			var maxPercentage:int = (12000 - population)/1000;
//			trace("maxPercentage : " + maxPercentage);
//			
//			var naturalEvolutionPercentage:int = Utils.random(maxPercentage)+1;
//			var armyPercentage:int = 100*armyRaised/population;
//			trace("naturalEvolutionPercentage : " + naturalEvolutionPercentage);
//			trace("armyPercentage : " + armyPercentage);
//			
//			var evolutionPercentage:int = naturalEvolutionPercentage - Math.sqrt(armyPercentage);
//			
//			if(starvation)
//				evolutionPercentage = -100;
//			trace("evolutionPercentage : " + evolutionPercentage);
//			
//			var populationEvolution:int =  population*evolutionPercentage/100;
//			trace("populationEvolution : " + populationEvolution);
//			
//			populationEvolution /= 20;
//			trace("populationEvolution final : " + populationEvolution);
//
//			if(population > 10000)
//				populationEvolution /= 10;
//
//			trace("populationEvolution final 2 : " + populationEvolution);
//			trace("population : " + (population + populationEvolution - armyRaised));
//			
//			return population + populationEvolution - armyRaised;
		}
		
		public function validateUnit(unit:Unit):void
		{
			if(unit.status == Unit.TO_BE_CREATED){
				createUnit(unit);
			}
			else{
				unit.refreshLastMoveBeforeReplacingUnit();
				updateUnit(unit);
			}
			
			BoardDrawer.getInstance().refreshUnits(Session.CURRENT_CELL_SELECTED);
		}
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		
		public function loadItems():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.loadItems.addEventListener("result", itemsLoaded);
			gameWrapper.loadItems();
		} 
		
		private function itemsLoaded(event:ResultEvent):void
		{
			Session.ITEMS = event.result as ArrayCollection;
			Numbers.loadItemData();
			
			if(Session.GAME_OVER){
				initMap(100, 100);
			}
			else
				calculateSteps(Session.player);
		}

		//============================================================================================//

		public function savePlayer(player:Player):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			savePlayerResponder.token = gameWrapper.savePlayer(player);
		}

		public function deleteUnit (unit:Unit):void
		{
			if(unit.status == Unit.TO_BE_CREATED){
				return;
			}
			
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
			refreshStocksForServerSide(city);
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.saveCity(city);			
		}

		public function buildCity(city:City, merchant:Unit):void
		{
			refreshStocksForServerSide(city);
			
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
			
			Session.board.reloadCurrentCells(true);
		}
		
		//----------------------------------------------------------------------//
		
		public function getCitiesBoard():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getCitiesBoard.addEventListener("result", citiesBoardReceived);
			gameWrapper.getCitiesBoard();
		}
	
		public function getArmiesBoard():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getArmiesBoard.addEventListener("result", armiesBoardReceived);
			gameWrapper.getArmiesBoard();
		}
		
		public function getPopulationBoard():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getArmiesBoard.addEventListener("result", populationBoardReceived);
			gameWrapper.getArmiesBoard();
		}
		
		public function getLandsBoard():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getLandsBoard.addEventListener("result", landsBoardReceived);
			gameWrapper.getLandsBoard();
		}
		
		//----------------------------------------------------------------------//
		
		public function getTopAlliesByCities():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getTopAlliesByCities.addEventListener("result", alliesCitiesBoardReceived);
			gameWrapper.getTopAlliesByCities();
		}
	
		public function getTopAlliesByArmies():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getTopAlliesByArmies.addEventListener("result", alliesArmiesBoardReceived);
			gameWrapper.getTopAlliesByArmies();
		}
		
		public function getTopAlliesByPopulation():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getTopAlliesByPopulation.addEventListener("result", alliesPopulationBoardReceived);
			gameWrapper.getTopAlliesByPopulation();
		}
		
		public function getTopAlliesByLands():void
		{
			Session.WAIT_FOR_SERVER = true;
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getTopAlliesByLands.addEventListener("result", alliesLandsBoardReceived);
			gameWrapper.getTopAlliesByLands();
		}
		
		//--------------------------------------------------------------------------------//
		
		private function citiesBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.playersCitiesBoard = Utils.sort(event.result as ArrayCollection, "nbCities");
		}
		
		private function armiesBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.playersArmiesBoard = Utils.sort(event.result as ArrayCollection, "nbArmies");
		}
		
		private function populationBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.playersPopulationBoard = Utils.sort(event.result as ArrayCollection, "nbPopulation");
		}
		
		private function landsBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.playersLandsBoard = Utils.sort(event.result as ArrayCollection, "nbLands");
		}
		
		//--------------------------------------------------------------------------------//
		
		private function alliesCitiesBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.alliesCitiesBoard = Utils.sort(event.result as ArrayCollection, "nbCities");
		}
		
		private function alliesArmiesBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.alliesArmiesBoard = Utils.sort(event.result as ArrayCollection, "nbArmies");
		}
		
		private function alliesPopulationBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.alliesPopulationBoard = Utils.sort(event.result as ArrayCollection, "nbPopulation");
		}
		
		private function alliesLandsBoardReceived(event:ResultEvent):void
		{
			Session.WAIT_FOR_SERVER = false;
			Session.alliesLandsBoard = Utils.sort(event.result as ArrayCollection, "nbLands");
		}
		
		//--------------------------------------------------------------------------------//
		
		public function sendMessage(message:String, recipientUID:String):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.sendMessage(Session.player.playerUID, recipientUID, message);
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

		//--------------------------------------------------------------------------------//
		
		public function initMap(centerX:int, centerY:int):void
		{
			Session.WAIT_FOR_SERVER = true;
			loadCells(centerX, centerY, true);
		}

		
		public function loadCells(centerX:int, centerY:int, refreshLandOwners:Boolean):void
		{
			Session.WAIT_FOR_SERVER = true;
			
			trace("-------------------------------------");
			
			var groups:Array = Utils.getGroups(centerX, centerY);
			groups.sort(Array.NUMERIC);
			
			Session.firstCellX = (groups[0]%27) * 15; // modulo 27
			Session.firstCellY = ((int)(groups[0]/27)) * 15; // divisé par 27
			
			
			trace("-------------------------------------");
			trace("loadCells center : [ " + centerX + " | " + centerY + " ]");
			trace(groups);
			trace("Session.firstCellX : " + Session.firstCellX);
			trace("Session.firstCellY : " + Session.firstCellY);
			
			var caseUIDs:ArrayCollection = new ArrayCollection();
			Session.nbTilesByEdge = Math.sqrt(groups.length) * 15;
			
			trace("nbTilesByEdge : " + Session.nbTilesByEdge);
			trace("-------------------------------------");
			
			Session.LEFT_LIMIT_LOADED  	 = Utils.getXPixel(Session.firstCellX) + Session.MAP_WIDTH/2;
			Session.RIGHT_LIMIT_LOADED	 = Utils.getXPixel(Session.firstCellX + Session.nbTilesByEdge) - Session.MAP_WIDTH/2;
			Session.TOP_LIMIT_LOADED 	 = Utils.getYPixel(Session.firstCellY) + Session.MAP_HEIGHT/2;
			Session.BOTTOM_LIMIT_LOADED	 = Utils.getYPixel(Session.firstCellY + Session.nbTilesByEdge) - Session.MAP_HEIGHT/2;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			loadCellsResponder.addEventListener("result", cellsLoaded);
			loadCellsResponder.token = gameWrapper.loadCells(groups, refreshLandOwners);
		}
		
		private function cellsLoaded(event:ResultEvent):void
		{
			trace("cellsLoaded");
			
			//------------------------------------------------//

			Session.CELLS_LOADED = event.result as ArrayCollection;
			trace("received : " + Session.CELLS_LOADED.length + " cases");
			
			//------------------------------------------------//
			// init du tableau
			
			Session.map = [];
			
			for(var i:int=0; i < Session.nbTilesByEdge; i++)
			{
				Session.map[Session.firstCellX+i] = [];
				for(var j:int=0; j < Session.nbTilesByEdge; j++)
				{
					Session.map[Session.firstCellX+i][Session.firstCellY+j] = new Cell(Session.firstCellX+i,Session.firstCellY+j);
				}
			}
			
			//------------------------------------------------//
			
			// on reset tous les timers pour les moves des unites du plateau
			UnitMover.getInstance().resetTimers();

			//------------------------------------------------//
			// affectation des vraies cases
			// on va appeler le case.refresh sur toutes les cases
			// ce qui va rafraichir les moves des units sur chaque case
			// et remplir Session.movesToDelete
			// et Session.allUnits
			// on affecte dans le forceRefresh le _move du actif sur la case. (ca suppose 1 seul pion visible par case)
			
			var citiesLoaded:ArrayCollection = new ArrayCollection();
			for each(var _cell:Cell in Session.CELLS_LOADED)
			{
				Session.map[_cell.x][_cell.y] = _cell;
				refreshCell(Session.map[_cell.x][_cell.y] as Cell);
				
				if(_cell.city != null)
					citiesLoaded.addItem(_cell.city);
			}

			// on en profite aussi pour rafraichir les villes en Session

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

			// on place le plateau au bon endroit
			Session.board.placeBoard(Session.CENTER_X, Session.CENTER_Y);
			
			// on affiche tout, et on affecte les images aux pions des cases
			BoardDrawer.getInstance().refreshDisplay();
			
			// on refresh l'etat des armees dans les villes
			Session.board.refreshUnitsInCity();
			
			// on supprime maintenant tous les Session.movesToDelete
			deleteAllMovesToBeDeleted();
			
		}
		
		//===================================================================================//
		
		public function refreshCell(cell:Cell):void
		{
			trace(cell.cellUID + " : unit : " + ObjectUtil.toString(cell.unit));
			cell.pawn.cell = cell;
			
			Utils.refreshUnit(cell.army);	
			Utils.refreshUnit(cell.caravan);	
			
			if(cell.challenger != null 
				&& ((cell.army != null && cell.army.ownerStatus == Unit.PLAYER) 
					|| (cell.caravan != null && cell.caravan.ownerStatus == Unit.PLAYER)))
			{
				trace("challenger : set timeTo : " + cell.timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS);
				cell.pawn.status = Pawn.CONQUERING_LAND;
				cell.pawn.timeTo = cell.timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS;
				
				cell.pawn.resetProgress();
			}
			else if(cell.unit != null){
				cell.pawn.status = Pawn.CLASSIC;
				cell.pawn.timeTo = cell.timeToChangeUnit;
				
				
				if(cell.unit.player.playerUID == Session.player.playerUID)
					cell.pawn.resetProgress();
				
				if(cell.timeFromChallenging > 0)
					UnitMover.getInstance().listenTo(cell);
			}
		}

		//===================================================================================//

		
		public function refreshCellFromServer(cell:Cell):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			loadCellsResponder.addEventListener("result", newCellsReceived);
			loadCellsResponder.token = gameWrapper.getNewCells(cell);
		}
		
		private function newCellsReceived(result:ResultEvent):void 
		{
			var newCells:ArrayCollection = new ArrayCollection();
			
			for each(var cell:Cell in newCells){
				refreshCell(cell);
				Session.map[cell.x][cell.y] = cell;
				BoardDrawer.getInstance().refreshUnits(Session.map[cell.x][cell.y]);
			}
		}
		
		//===================================================================================//
	

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