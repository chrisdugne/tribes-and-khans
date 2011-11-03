
package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.core.BoardClickAnalyser;
	import com.uralys.tribes.core.BoardDrawer;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.core.UnitMover;
	import com.uralys.tribes.entities.Ally;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Item;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.MoveResult;
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
		
		public function GameManager(){}
		
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
		
		public function getPlayer(player:Player):void
		{
			trace("------------");
			trace("refreshing : getPlayer");
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
			refreshPlayer(player);
		}
		
		public function refreshPlayer(player:Player):void
		{
			trace("------------");
			trace("refreshPlayer");
			trace("------------");
			Session.WAIT_FOR_SERVER = false;
			calculateSteps(player);
			
			if(Session.player.playerUID == player.playerUID){
				Session.player = player;
				refreshUnits();
			}
			
		}

		//============================================================================================//

		public function refreshUnits():void
		{
			var unitUIDsToDelete:ArrayCollection = new ArrayCollection();
			var uidsToRemove:ArrayCollection = new ArrayCollection();
			
			trace("refreshUnits : " + Session.player.units.length);
			
			for each(var unit:Unit in Session.player.units)
			{
				
				var unitExists:Boolean = refreshUnitStatus(unit);
				trace("status : " + unit.status);
				
				if(!unitExists){
					trace("unit is not prepared");
					if(unit.status == Unit.DESTROYED){
						trace("unit is to be deleted");				
						unitUIDsToDelete.addItem(unit.unitUID);
					}
					
					uidsToRemove.addItem(unit.unitUID);
				}
				else
					refreshUnit(unit);
			}
			
			for each(var unitToRemoveUID:String in uidsToRemove){
				trace("removing : " + unitToRemoveUID);
				var indexToRremove:int = -1;
				
				for each(var unit:Unit in Session.player.units){
					if(unit.unitUID == unitToRemoveUID){
						indexToRremove = Session.player.units.getItemIndex(unit);
						break;
					}
				}
				
				if(indexToRremove >= 0)
					Session.player.units.removeItemAt(indexToRremove);
			}
			
			deleteUnits(unitUIDsToDelete);
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
			
			Session.board.cityForm.refreshCity();

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
				city.wheat += city.wheatEarned - city.wheatRequiredToFeed;
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
				var moveResult:MoveResult = event.result as MoveResult;
				
				var cellDeparture:Cell = moveResult.cellDeparture;
				var unitAltered:Unit = moveResult.unit;
				
				trace("-----");
				trace("unitAltered : " + unitAltered.unitUID);

				refreshUnit(unitAltered);

				trace("----");
				trace("cellDeparture : " + cellDeparture.cellUID);
				trace("timeToChangeUnit : " + cellDeparture.timeToChangeUnit);
				trace("nextCellUID : " + cellDeparture.nextCellUID);
				
				if(cellDeparture.challenger != null){
					trace("challengerUID : " + cellDeparture.challenger.playerUID);
					trace("timeFromChallenging : " + cellDeparture.timeFromChallenging);
				}
				
				try{
					var cellInSession:Cell = (Session.map[cellDeparture.x][cellDeparture.y] as Cell);
					
					if(cellInSession != null){
						trace("found Cell in Session");
						cellInSession.challenger = cellDeparture.challenger;
						cellInSession.timeFromChallenging = cellDeparture.timeFromChallenging;
						cellInSession.timeToChangeUnit = cellDeparture.timeToChangeUnit;
						cellInSession.nextCellUID = cellDeparture.nextCellUID;
						cellInSession.army = cellDeparture.army;
						cellInSession.caravan = cellDeparture.caravan;
					}
					else
						Session.map[cellDeparture.x][cellDeparture.y] = cellDeparture;
					
					refreshCellDisplay(Session.map[cellDeparture.x][cellDeparture.y]);
				}
				catch(e:Error){trace("----");trace(e.message);trace("----");trace("not able to refresh this case");};
			
				//refreshStatusOfAllUnitsInSession();
				
				// on refresh les villes au cas ou le deplacement fait partir/arriver une unite de/dans une ville
				//refreshUnitsInCity();
			}
			else{
				// l'unite nexiste plus : elle a été supprimé entre temps
				// par exemple apres un tir d'arcs
				// on refresh le joueur, il recevra le message.
				getPlayer(Session.player);
				return;
			}
			
			// lorsqu'on sauvegarde une ville, on sauvegarde les unites 1 par 1
			tryToRecordNextUnitInCity();
			
			trace("--------");
		}
		
		public function refreshUnitStatus(unit:Unit):Boolean
		{
			var now:Number = new Date().getTime();
			
			trace("------------------");
			trace("refreshUnitStatus : " + unit.unitUID);
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

			unit.ownerStatus = Unit.PLAYER;
			unit.isModified = false;	
			
			trace(" ====> Unit.FREE");
			unit.status = Unit.FREE;
			
			return true;
		}
		
		//-------------------------------------------------------------------------------//

		/**
		 * utilisee lors d'un loadcases uniquement 
		 * le case.forceRefresh trouve toutes les unites
		 * et appelle ici pour les enregistrer au passage
		 * ensuite on va faire un addTimer sur chacune des unites
		 */
//		public function registerUnitInSession(unit:Unit):void
//		{
//			var foundInSession:Boolean = false;
//			
//			for each(var unitInSession:Unit in Session.allUnits){
//				if(unit.unitUID == unitInSession.unitUID){
//					foundInSession = true;
//					break;
//				}
//			}
//			
//			if(!foundInSession)
//				Session.allUnits.addItem(unit);
//		}
		
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
				city.peopleCreatingWheat = city.population/3;
				city.woodEarned /= 2;
				city.ironEarned /= 2;
			}

			city.wheatEarned = Numbers.WHEAT_EARNING_COEFF * city.peopleCreatingWheat;

			city.refreshUnemployed();
			//Session.board.cityForm.updateWorkersProgressBar();
			
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
		
		private function validateUnit(unit:Unit):void
		{
			if(unit.status == Unit.TO_BE_CREATED){
				createUnit(unit);
			}
			else{
				unit.refreshLastMoveBeforeReplacingUnit();
				updateUnit(unit);
			}
			
			// on vient de set Session.CURRENT_CELL_SELECTED.caravan ou army
			refreshCellDisplay(Session.CURRENT_CELL_SELECTED);
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
		}

		//============================================================================================//

		public function savePlayer(player:Player):void{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.savePlayer(player);
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
		
		//--------------------------------------------------------------------------------//

		private var unitsToRecord:Array;
		public function saveCity(city:City, unitsToRecord:Array = null):void
		{
			refreshStocksForServerSide(city);
			this.unitsToRecord = unitsToRecord;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.saveCity(city);
			gameWrapper.saveCity.addEventListener("result", tryToRecordNextUnitInCity);
		}
		
		private function tryToRecordNextUnitInCity(event:ResultEvent = null):void
		{
			if(unitsToRecord != null && unitsToRecord.length > 0){
				var unitToRecord:Unit = unitsToRecord.shift();
				validateUnit(unitToRecord);
			}
		}
		
		//--------------------------------------------------------------------------------//
		

		public function buildCity(unit:Unit):void
		{
			var city:City = new City();
			city.cityUID = "new";
			city.name = Translations.CITY_BUILDING_NAME.getItemAt(Session.LANGUAGE) as String;
			city.gold = unit.gold;
			city.wheat = unit.wheat;
			city.wood = unit.wood - (Numbers.CITY_WOOD_BASE_PRICE + unit.size * 10);
			city.iron = unit.iron - (Numbers.CITY_IRON_BASE_PRICE + unit.size * 10);
			city.population = unit.size;
			
			city.bows = unit.bows;
			city.swords = unit.swords;
			city.armors = unit.armors;
			
			city.x = Session.CURRENT_CELL_SELECTED.x;
			city.y = Session.CURRENT_CELL_SELECTED.y;
			
			city.beginTime = new Date().getTime() + Numbers.TIME_TO_BUILD_A_CITY;
			trace("city.beginTime : " + city.beginTime);
			
			try{
				Session.player.units.removeItemAt(Session.player.units.getItemIndex(unit));	
			}
			catch(e:Error){
				// cest qd on reclick sur buildcity une deuxieme fois : index -1 outofbounds
				// todo : empecher l'affichage du bouton build bordel
				return;
			}
			BoardDrawer.getInstance().drawCity(city);
			
			Session.player.cities.addItem(city);
			
			unit.gold = 0;
			unit.iron = 0;
			unit.wood = 0;
			unit.wheat = 0;
			unit.armors = 0;
			unit.bows = 0;
			unit.swords = 0;
			unit.endTime = 1;
			
			try{
				Session.CURRENT_CELL_SELECTED.caravan = null;
				refreshCell(Session.CURRENT_CELL_SELECTED);
			}
			catch(e:Error){}
			
			trace("build city on ["+city.x+"]["+city.y+"]");
			
			refreshStocksForServerSide(city);
			
			cityBeingSaved = city;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.buildCity.addEventListener("result", cityBuilt);	
			gameWrapper.buildCity(city, unit, Session.player.uralysUID);	
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
			
			reloadCurrentCells(true);
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
		
//		private var movesBeingDeleted:Array;
//		private function deleteAllMovesToBeDeleted ():void
//		{
//			trace("deleteAllMovesToBeDeleted");
//			movesBeingDeleted = Session.movesToDelete.toArray();
//			deleteNextMove(movesBeingDeleted.shift());
//		}
//		
//		private function deleteNextMove (move:Move):void{
//			trace("deleteNextMove");
//			if(move == null)
//				return;
//			
//			trace("gameWrapper.deleteMove");
//			var gameWrapper:RemoteObject = getGameWrapper();
//			gameWrapper.deleteMove.addEventListener("result", moveDeleted);
//			gameWrapper.deleteMove(move.moveUID);
//		}
		
		public function deleteMove(moveUID:String):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.deleteMove(moveUID);
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
			gameWrapper.loadCells.addEventListener("result", cellsLoaded);
			gameWrapper.loadCells(groups, refreshLandOwners);
		}
		
		private function cellsLoaded(event:ResultEvent):void
		{
			trace("-------------------------");
			trace("cellsLoaded");
			
			//------------------------------------------------//

			Session.CELLS_LOADED = event.result as ArrayCollection;
			trace("received : " + Session.CELLS_LOADED.length + " cases");
			trace("-------------------------");
			
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
			
			// si Session.CURRENT_CELL_SELECTED n'avait pas pu etre selectionné avant loadCells, on le peut ici.
			Session.CURRENT_CELL_SELECTED = Session.map[Session.CENTER_X][Session.CENTER_Y];
			
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

			// on refresh l'etat des armees dans les villes
			//refreshUnitsInCity();

			// on place le plateau au bon endroit
			Session.board.placeBoard(Session.CENTER_X, Session.CENTER_Y, false);
			
			// on affiche tout, et on affecte les images aux pions des cases
			//FlexGlobals.topLevelApplication.callLater(BoardDrawer.getInstance().refreshDisplay);
			BoardDrawer.getInstance().refreshDisplay();
			
			// on supprime maintenant tous les Session.movesToDelete
			//deleteAllMovesToBeDeleted();
			
			trace("-------------------------");
		}
		
		//===================================================================================//

		// appelee apres le BoardDrawer.refreshDisplay, qui raffraichit toutes les currentCaseUID des units de toutes les cases.
//		public function refreshUnitsInCity():void
//		{
//			trace("refreshUnitsInCity");
//			
//			for each(var city:City in Session.player.cities){
//				city.unitsToFeed = 0;
//			}
//			
//			for each(var unit:Unit in Session.player.units)
//			{
//				if(unit.status == Unit.DESTROYED != unit.status == Unit.FUTURE)
//					continue;
//				
//				if(unit.type == 2){
//					for each(var city:City in Session.player.cities)
//					{
//						if(Utils.getXFromCellUID(unit.currentCaseUID) == city.x && Utils.getYFromCellUID(unit.currentCaseUID) == city.y){
//							city.caravan = unit;
//							city.unitsToFeed += unit.size;
//							break;
//						}
//					}
//				}
//					
//				else if(unit.type == 1){
//					for each(var city:City in Session.player.cities)
//					{
//						if(Utils.getXFromCellUID(unit.currentCaseUID) == city.x && Utils.getYFromCellUID(unit.currentCaseUID) == city.y){
//							city.army = unit;
//							city.unitsToFeed += unit.size;
//							break;
//						}
//					}
//				}
//				
//			}
//			
//		}
		
		//===================================================================================//
		
		public function refreshCell(cell:Cell):void
		{
			cell.pawn.cell = cell;
			cell.pawn.timeTo = -1;
			
			refreshUnit(cell.army);	
			refreshUnit(cell.caravan);	
			
			if(cell.visibleUnit != null)
			{
				var cellIsToListen:Boolean = false;
				
				switch(cell.visibleUnit.type)
				{
					case Unit.ARMY :
					{
						if(cell.timeToChangeUnit > 0){
							cell.pawn.status = Pawn.CLASSIC;
							cell.pawn.timeTo = cell.visibleUnit.moves.getItemAt(0).timeTo;
							cellIsToListen = true;
							trace("move : set timeTo : " + cell.pawn.timeTo);
						}
						else if(cell.timeFromChallenging > 0){
							trace("challenger : set timeTo : " + (cell.timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS));
							cell.pawn.status = Pawn.CONQUERING_LAND;
							cell.pawn.timeTo = cell.timeFromChallenging + Numbers.BASE_TIME_FOR_LAND_CONQUEST_MILLIS;
							cell.visibleUnit.timeFromChallenging = cell.timeFromChallenging;
							cellIsToListen = true;
						}
						
						break;
					}
					case Unit.MERCHANT :
					{
						if(cell.timeToChangeUnit > 0){
							cell.pawn.status = Pawn.CLASSIC;
							cell.pawn.timeTo = cell.visibleUnit.moves.getItemAt(0).timeTo;
							cellIsToListen = true;
							trace("move : set timeTo : " + cell.pawn.timeTo);
						}
						
						break;
					}
				}				
				
				if(cellIsToListen){
					UnitMover.getInstance().listenTo(cell);
				}

				refreshInSession(cell.visibleUnit);
			}
		}

		//===================================================================================//

		public function refreshCellFromServer(cell:Cell):void
		{
			trace("----------");
			trace("refreshCellFromServer : " + cell.cellUID + " | " + cell.nextCellUID);

			if(Utils.getCellInSession(cell.nextCellUID).city != null){
				trace("nextCellUID is a city : refreshPlayer");
				getPlayer(Session.player);
				return;	
			}
			
			BoardDrawer.getInstance().resetCellDisplay(cell);
	
			if(cell.nextCellUID != null)
				BoardDrawer.getInstance().resetCellDisplay(Utils.getCellInSession(cell.nextCellUID));
			
			cell.pawn = null;
			
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.getNewCells.addEventListener("result", newCellsReceived);
			gameWrapper.getNewCells(cell);
		}
		
		private function newCellsReceived(event:ResultEvent):void 
		{
			trace("----------");
			trace("newCellsReceived");
			var newCells:ArrayCollection = event.result as ArrayCollection;
			
			for each(var cell:Cell in newCells){
				trace("cell : " + cell.cellUID);
				refreshCellDisplay(cell);
				Session.map[cell.x][cell.y] = cell;
			}
			
			BoardClickAnalyser.getInstance().simulateClick(); // rafraichit l'information sur la case courante.
		}
		
		//===================================================================================//
		
		public function refreshUnit(unit:Unit):void
		{
			if(unit != null)
			{
				trace("refreshUnit : " + unit.unitUID);
				unit.refreshValue();
				
				if(unit.player.playerUID == Session.player.playerUID)
					unit.ownerStatus = Unit.PLAYER;
					
				else if(unit.player.ally != null 
					&& Session.player.ally != null
					&& unit.player.ally.allyUID == Session.player.ally.allyUID)
					unit.ownerStatus = Unit.ALLY;
					
				else
					unit.ownerStatus = Unit.ENNEMY;
				
				unit.currentCaseUID = (unit.moves.getItemAt(0) as Move).cellUID;
				
				refreshInSession(unit);
				UnitMover.getInstance().refreshMoves(unit);
			}
			
		}
		
		private function refreshInSession(unit:Unit):void
		{
			if(unit.player.playerUID != Session.player.playerUID)
				return;

			var unitInPlayer:Unit = Session.player.getUnit(unit.unitUID);
			
			if(unitInPlayer == null)
				Session.player.units.addItem(unit);
			else{
				Session.player.refreshUnit(unit);
			}
		}

		//---------------------------------------------------------------------------------------------------//
		
		public function reloadCurrentCells(force:Boolean = false, refreshLandOwners:Boolean = false):Boolean{
			return checkToLoadCases(Utils.getXPixel(Session.CENTER_X), Utils.getYPixel(Session.CENTER_Y), force, refreshLandOwners);
		}
		
		private function checkToLoadCases(x:int, y:int, force:Boolean = false, refreshLandOwners = false):Boolean
		{
			//				trace("-------------------------------------");
			//				trace("checkToLoadCases | force : " + force);
			//				trace("------");
			//				trace("x : " + x);
			//				trace("y : " + y);
			//				trace("------");
			//				trace("Session.LEFT_LIMIT_LOADED : " + Session.LEFT_LIMIT_LOADED);
			//				trace("Session.RIGHT_LIMIT_LOADED : " + Session.RIGHT_LIMIT_LOADED);
			//				trace("Session.TOP_LIMIT_LOADED : " + Session.TOP_LIMIT_LOADED);
			//				trace("Session.BOTTOM_LIMIT_LOADED : " + Session.BOTTOM_LIMIT_LOADED);
			//				trace("------");
			//				trace("Session.CENTER_X : " + Session.CENTER_X);
			//				trace("Session.CENTER_Y : " + Session.CENTER_Y);
			//				trace("------");
			
			if(x < Session.LEFT_LIMIT_LOADED
				|| x > Session.RIGHT_LIMIT_LOADED 
				|| y < Session.TOP_LIMIT_LOADED 
				|| y > Session.BOTTOM_LIMIT_LOADED
				|| force)
			{
				trace("-------------------------------------");
				trace("checkToLoadCases  ==>  LOAD");
				loadCells(Session.CENTER_X, Session.CENTER_Y, refreshLandOwners);
				return true;
			}
			
			return false;
		}
		
		//---------------------------------------------------------------------//		
		
		public function refreshCellDisplay(cell:Cell):void
		{
			trace("refreshCellDisplay");
			BoardDrawer.getInstance().resetCellDisplay(cell);
			refreshCell(cell);
			BoardDrawer.getInstance().refreshCellDisplay(cell);
		}

		//---------------------------------------------------------------------//		
		
		public function updateWorkersProgressBar():void
		{
			Session.board.cityForm.cityHeader.updateWorkersProgressBar();
		}
		
		//---------------------------------------------------------------------//		

		public function shoot(shooter:Unit, target:Unit, cellUID:String):void
		{
			var gameWrapper:RemoteObject = getGameWrapper();
			gameWrapper.shoot(shooter, target, cellUID);
		}
	}
}