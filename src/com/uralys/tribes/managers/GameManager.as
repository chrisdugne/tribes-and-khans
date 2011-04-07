package com.uralys.tribes.managers {
	
	import com.uralys.tribes.commons.Names;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.core.BoardDrawer;
	import com.uralys.tribes.core.Pager;
	import com.uralys.tribes.entities.Case;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Equipment;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Smith;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Utils;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
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
		
		private var gameWrapper:RemoteObject;

		// -  ================================================================================
		
		public function GameManager(){
			gameWrapper = new RemoteObject();
			gameWrapper.destination = "GameWrapper";
			gameWrapper.endpoint = Names.SERVER_AMF_ENDPOINT;
		}
		
		//=====================================================================================//
		
		public function setBoard(board:Board):void{
			this.board = board;
		}

		public function tracetest():void{
			trace("city.woodSpent : " + board.selectedCity.woodSpent);
			trace("city.woodEarned : " + board.selectedCity.woodEarned);
			trace("city.wood : " + board.selectedCity.wood);
		}
		
		//=====================================================================================//
		
		private var board:Board;
		
		//============================================================================================//
		// CONTROLS
		//============================================================================================//
		
		private function loginForceSteps():void {
			trace("-------------");
			trace("loginForceSteps");
			
			var now:Number = new Date().getTime();
			var timeSpentMillis:Number = now - Numbers.SERVER_START;
			
			var nbStepsSinceBeginning:int = timeSpentMillis/(Numbers.TIME_PER_STEP*60*1000);
			var nbStepsMissed:int = nbStepsSinceBeginning - Session.player.lastStep;
			
			trace("Session.player.lastStep : " + Session.player.lastStep);
			trace("nbStepsMissed : " + nbStepsMissed);
		
			
			for(var i:int = 0; i < nbStepsMissed-1; i++){
				if(i%100 == 0)
					trace(i);
				saveStep(true);
			}
			
			// enregistre le statut lors du dernier step
			trace("lastStep");
			saveStep(false, true);
		}
		
		public function saveStep(loginCatchUp:Boolean = false, loadCases:Boolean = false){
			for each(var city:City in Session.player.cities){
				
				city.wheat += city.wheatEarned - city.wheatSpent;
				city.wood += city.woodEarned - city.woodSpent;
				city.iron += city.ironEarned - city.ironSpent;

				var starvation:Boolean = false;
				
				if(city.wheat < 0){
					starvation = true;
					city.wheat = 0;
				}
				
				var armyRaised:int = city.armyReleased - city.armyRaised;
				
				city.population = calculatePopulation(city.population, starvation, armyRaised);

				for each(var equipment:Equipment in city.equipmentStock){
					switch(equipment.item.name){
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
				
				for each(var smith:Smith in city.smiths){
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
				
				city.reset();
				refreshCity(city, starvation);
			}
			
			for each(var unit:Unit in Session.player.units){
				if(unit.unitUID == "new"){
					var bows:Equipment = new Equipment();
					var swords:Equipment = new Equipment();
					var armors:Equipment = new Equipment();
					
					bows.size = unit.bows;
					swords.size = unit.swords;
					armors.size = unit.armors;
					
					for each(var equipment:Equipment in city.equipmentStock){
						switch(equipment.item.name){
							case "bow" :
								bows.item = equipment.item;
								break;
							case "sword" :
								swords.item = equipment.item;
								break;
							case "armor" :
								armors.item = equipment.item;
								break;
						}
					}
					
					unit.equipments.addItem(bows);
					unit.equipments.addItem(swords);
					unit.equipments.addItem(armors);
				}
				else{
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
				
				unit.armyCircle = null;
				unit.ellipseTo = null;
				unit.lineTo = null;
				unit.tmpLandSquare = null;
			}
			
			(Session.player.cities.getItemAt(0) as City).gold += Session.player.nbLands;
			if(!loginCatchUp)
				savePlayer(loadCases);
		}
		
		public function refreshCity(city:City, starvation:Boolean):void{
			
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
			
			return population + populationEvolution;
		}
		
		//============================================================================================//
		// DATA MANAGEMENT
		//============================================================================================//
		//  ASKING SERVER
		
		public function loadItems():void{
			gameWrapper.loadItems.addEventListener("result", itemsLoaded);
			gameWrapper.loadItems();
		}

		public function loadCases(centerX:int, centerY:int):void{

			var caseUIDs:ArrayCollection = new ArrayCollection();
			
			for(var i:int = 0; i < 30; i++){
				for(var j:int = 0; j < 30; j++){
					caseUIDs.addItem("case_"+(centerX-15+i)+"_"+(centerY-15+j));
				}
			}
			
			gameWrapper.loadCases.addEventListener("result", casesLoaded);
			gameWrapper.loadCases(caseUIDs);
		}

		public function savePlayer(loadCases:Boolean):void{
			if(loadCases)
				gameWrapper.savePlayer.addEventListener("result", readyToLoadCases);
			
			gameWrapper.savePlayer(Session.player);
		}
		
		//============================================================================================//
		//  RESULTS FROM SERVER	
		
		
		private function readyToLoadCases(event:ResultEvent):void{
			var city:City = Session.player.cities.getItemAt(0) as City;
			BoardDrawer.getInstance().refreshMap(city.x, city.y);
		}

		private function itemsLoaded(event:ResultEvent):void{
			Session.ITEMS = event.result as ArrayCollection;
			Numbers.loadItemData();
			loginForceSteps();
		}
		
		private function casesLoaded(event:ResultEvent):void{
			Session.CASES_LOADED = event.result as ArrayCollection;
			
			//------------------------------------------------//
			// init du tableau 29x29
			
			Session.map = [];
			
			for(var i:int=Session.centerX-15; i < Session.centerX+15; i++){
				Session.map[i] = [];
				for(var j:int=Session.centerY-15; j < Session.centerY+15; j++){
					Session.map[i][j] = new Case(i,j);
				}
			}

			//------------------------------------------------//
			// affectationdes vraies cases
			
			for each(var _case:Case in Session.CASES_LOADED){
				Session.map[_case.x][_case.y] = _case;
			}

			//------------------------------------------------//
			
			BoardDrawer.getInstance().refreshDisplay();
		}
	}
}