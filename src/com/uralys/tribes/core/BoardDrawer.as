package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Army;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Conflict;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.MoveConflict;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Map;
	import com.uralys.utils.Utils;
	
	import flash.display.Sprite;
	
	import mx.collections.ArrayCollection;
	import mx.collections.SortField;
	import mx.controls.Image;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.EffectEvent;
	import mx.graphics.GradientEntry;
	import mx.graphics.RadialGradientStroke;
	import mx.graphics.SolidColor;
	import mx.graphics.SolidColorStroke;
	import mx.utils.ObjectUtil;
	
	import spark.components.Group;
	import spark.components.Label;
	import spark.effects.Move;
	import spark.primitives.Ellipse;
	import spark.primitives.Line;
	import spark.primitives.Rect;

	/**
		BoardDrawer draws all the entities of the game on the board.
		 - circles
		 - lines
		 - images
		 - texts
	*/
	public class BoardDrawer
	{	
		//=====================================================================================//
		
		private var moveGradient:Array;
		private var radialGrad:RadialGradientStroke;
		
		//=====================================================================================//
	
		private static var instance : BoardDrawer = new BoardDrawer();
	
		public static function getInstance():BoardDrawer{
			return instance;
		}

		public function BoardDrawer(){
			moveGradient = [];
			moveGradient.push(new GradientEntry(0xFFFFFF));
			moveGradient.push(new GradientEntry(0x11dd11));
			moveGradient.push(new GradientEntry(0x000000));
			radialGrad = new RadialGradientStroke();
			radialGrad.entries = moveGradient;
			radialGrad.interpolationMethod
		}

		public function setBoards(board:Board):void{
			this.board = board;
		}

		public function setGame(game:Game):void{
			this.game = game;
		}

		//=====================================================================================//
		
		private var board:Board;
		private var game:Game;
		
		//=====================================================================================//

		public function redrawAllEntities():void{
			
			try{
				board.mapTiles.removeAllElements();
				board.boardEntities.removeAllElements();
				board.boardImages.removeAllElements();
				board.boardTexts.removeAllElements();
			}catch(e:Error){}
			
			drawMap();
			drawConflicts();
			
			for each (var player:Player in game.players){
				
				// il faudra revoir ca pour les alliances
				var isOpponent:Boolean = Session.currentPlayer.playerUID != player.playerUID;
				
				//-----------------------------------------------------------------------------------------//
				// cities
				
				for each (var city:City in player.cities){
					drawCity(city, isOpponent);
				}

				//-----------------------------------------------------------------------------------------//
				// armies

				for each (var army:Army in player.armies){					
					drawArmy(army, isOpponent);
				}

				//-----------------------------------------------------------------------------------------//
				// merchants

				for each (var merchant:Army in player.merchants){
					drawMerchant(merchant, isOpponent);
				}
				
				//-----------------------------------------------------------------------------------------//
				// lands
				
				for each (var land:int in player.lands){
					drawLand(land, isOpponent);					
				}
			}
		}

		//=====================================================================================//
		
		public function drawConflicts():void{
			for each(var conflict:Conflict in Session.currentPlayer.conflicts){
				var image:Image = new Image();
				
				image.source = ImageContainer.CONFLIT;
					
				image.x = conflict.x-25;
				image.y = conflict.y-25;
				
				board.mapTiles.addElement(image);
			}
				
		}
		
		public function drawMap():void{
			
			//------------------------------------------------//
			// init du tableau 29x29
			
			Session.map = [];
			
			for(var i:int=0; i < board.image.width/Numbers.LAND_WIDTH; i++){
				Session.map[i] = [];
				for(var j:int=0; j < board.image.height/Numbers.LAND_HEIGHT; j++){
					Session.map[i][j] = Numbers.NOTHING;
				}
			}
			
			//------------------------------------------------//
			// city floors
			
			for each (var player:Player in game.players){
				for each (var city:City in player.cities){
					
					// de 0 a 29
					var cityLandXmin:int = Math.floor((city.x - city.radius)/Numbers.LAND_WIDTH);	
					var cityLandXmax:int = Math.floor((city.x + city.radius)/Numbers.LAND_WIDTH);	
					var cityLandYmin:int = Math.floor((city.y - city.radius)/Numbers.LAND_HEIGHT);	
					var cityLandYmax:int = Math.floor((city.y + city.radius)/Numbers.LAND_HEIGHT);
					
					
					for(var i:int=cityLandXmin; i <= cityLandXmax; i++){
						for(var j:int=cityLandYmin; j <= cityLandYmax; j++){
							
							var image:Image = new Image();
							image.source = ImageContainer.SOL_VILLE;
							
							image.x = i * Numbers.LAND_WIDTH;
							image.y = j * Numbers.LAND_HEIGHT;
							
							Session.map[i][j] = Numbers.PLAIN;
							
							board.mapTiles.addElement(image);
						}
					}
				}
			}
			
			//------------------------------------------------//
			// lacs (3x3 cases)
			
			var lacsX:Array = [8, 18, 8, 18];
			var lacsY:Array = [8, 8, 18, 18];
			
			for(i = 0; i < lacsX.length; i++){
				var image:Image = new Image();
				
				switch(Utils.random(2)){
					case 1:
						image.source = ImageContainer.LAC1;
						break;
					case 2:
						image.source = ImageContainer.LAC2;
						break;
					
				}
				
				image.x = lacsX[i] * Numbers.LAND_WIDTH;
				image.y = lacsY[i] * Numbers.LAND_HEIGHT;
				
				// 3x3 cases en lac
				for(var k:int = 0; k < 3; k++){
					for(var l:int = 0; l < 3; l++){
						Session.map[lacsX[i] + k][lacsY[i] + l] = Numbers.LAKE;
					}
				}
				
				board.mapTiles.addElement(image);
			}
			
			//------------------------------------------------//
			// rochers (2x2 cases)
			
			var rocsX:Array = [9, 19, 9, 19];
			var rocsY:Array = [2, 2, 27, 27];
			
			for(i = 0; i < lacsX.length; i++){
				var image:Image = new Image();
				
				switch(Utils.random(2)){
					case 1:
						image.source = ImageContainer.ROCHE1;
						break;
					case 2:
						image.source = ImageContainer.ROCHE2;
						break;
					
				}
				
				image.x = rocsX[i] * Numbers.LAND_WIDTH;
				image.y = rocsY[i] * Numbers.LAND_HEIGHT;
				
				// 3x3 cases en lac
				for(var k:int = 0; k < 2; k++){
					for(var l:int = 0; l < 2; l++){
						Session.map[rocsX[i] + k][rocsY[i] + l] = Numbers.ROCK;
					}
				}
				
				board.mapTiles.addElement(image);
			}
			
			//------------------------------------------------//
			// forests
			
			for(var i:int=0; i < board.image.width/Numbers.LAND_WIDTH; i++){
				for(var j:int=0; j < board.image.height/Numbers.LAND_HEIGHT; j++){
					if(Session.map[i][j] == Numbers.NOTHING){
						var image:Image = new Image();
						switch(Utils.random(2)){
							case 1:
								image.source = ImageContainer.FORET1;
								break;
							case 2:
								image.source = ImageContainer.FORET2;
								break;
							
						}
						
						image.x = i * Numbers.LAND_WIDTH - 35;
						image.y = j * Numbers.LAND_HEIGHT - 35;
						
						board.mapTiles.addElement(image);
					}
				}
			}
			
		}

		//=====================================================================================//

		public function redrawTexts():void{
			
			try{
				board.boardTexts.removeAllElements();
			}catch(e:Error){}
			
			for each (var player:Player in game.players){
				
				var isOpponent:Boolean = Session.currentPlayer.playerUID != player.playerUID;
				if(!isOpponent)
					player = Session.currentPlayer; // toutes les modifs sont a jour dans Session.currentPlayer, et non dans 'game'
				
				//-----------------------------------------------------------------------------------------//
				// cities
				
				for each (var city:City in player.cities){
					drawCityText(city, isOpponent);
				}
				
				//-----------------------------------------------------------------------------------------//
				// armies
				
				for each (var army:Army in player.armies){					
					//drawArmyText(army, isOpponent);
				}
				
				//-----------------------------------------------------------------------------------------//
				// merchants
				
				for each (var merchant:Army in player.merchants){
					//drawMerchantText(merchant, isOpponent);
				}
				
			}
		}
	
		//==================================================================================================//
		
		public function drawCity(city:City, isOpponent:Boolean):void{
			
			if(isOpponent && city.creationTurn == game.currentTurn)
				return;
			
			if(Session.DRAW_DETAILS){
				var cityCircle:Ellipse;
				cityCircle = new Ellipse();
				cityCircle.width = city.radius*2;
				cityCircle.height = city.radius*2;
				cityCircle.x = city.x - cityCircle.width/2;
				cityCircle.y = city.y - cityCircle.height/2;
				
				cityCircle.fill = new SolidColor(isOpponent ? Numbers.YELLOW : Numbers.BLUE);
				
				board.boardEntities.addElement(cityCircle);
			}
			
			var cityMiniCircle:Rect;
			cityMiniCircle = new Rect();
			cityMiniCircle.width = city.radius*2/10;
			cityMiniCircle.height = city.radius*2/10;
			cityMiniCircle.x = (city.x - cityCircle.width/2)/15;
			cityMiniCircle.y = (city.y - cityCircle.height/2)/15;
			
			
			cityMiniCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.BLUE);
			
			board.minimap.addElement(cityMiniCircle);
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_TEXTS){
				drawCityText(city, isOpponent);
			}
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_IMAGES){
				var angle:int = 0;
				var distanceAuCentre:int = 0;
				var insideCircle:Boolean = true;
				
				var images:Map = new Map();
				
				while(insideCircle){
					var image:Image = new Image();
					
					switch(Utils.random(3)){
						case 1:
							image.source = ImageContainer.HOUSE1;
							break;
						case 2:
							image.source = ImageContainer.HOUSE2;
							break;
						case 3:
							image.source = ImageContainer.HOUSE3;
							break;
						
					}
					
					image.x = (city.x - image.width - 10) + (Math.cos(angle)*(distanceAuCentre/2));
					image.y = (city.y - image.height - 10) + (Math.sin(angle)*(distanceAuCentre/2));
					
					if(distanceAuCentre > city.radius*2)
						insideCircle = false;
					else{
						images.put(image.y, image);
						distanceAuCentre = angle/360 * 25;
						angle += distanceAuCentre > 50 ? (distanceAuCentre > 100 ? 10 : 20) : 40;
					}
				}

				images.sortKeys(new SortField(null, true));
				
				for each(var image:Image in images.values()){
					//var num:int = Utils.random(images.length) - 1;
					//boardImages.addElement(images.removeItemAt(num) as Image);
					board.boardImages.addElement(image);
				}
			}
		}

		private function drawCityText(city:City, isOpponent:Boolean):void{
			var name:Label = new Label();
			name.text = city.name;
			name.x = city.x - city.radius - 20; 
			name.y = city.y - city.radius - 20; 
			
			board.boardTexts.addElement(name);
		}

		//==================================================================================================//

		public function drawArmy(army:Army, isOpponent:Boolean):void{
			
			if(Session.DRAW_DETAILS){
				
				// army
				var armyCircle:Ellipse;
				armyCircle = new Ellipse();
				armyCircle.width = army.radius*2;
				armyCircle.height = army.radius*2;
				armyCircle.x = army.x - armyCircle.width/2;
				armyCircle.y = army.y - armyCircle.height/2;
				
				armyCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.WHITE);
				army.armyCircle = armyCircle;
				
				board.boardEntities.addElement(armyCircle);
			}
			
			var armyMiniCircle:Ellipse;
			armyMiniCircle = new Ellipse();
			armyMiniCircle.width = army.radius*2/4;
			armyMiniCircle.height = army.radius*2/4;
			armyMiniCircle.x = (army.x - armyCircle.width/2)/15;
			armyMiniCircle.y = (army.y - armyCircle.height/2)/15;
			
			
			armyMiniCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.BLUE);
			
			board.minimap.addElement(armyMiniCircle);
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_TEXTS){
				
			}
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_IMAGES){
				drawArmyImages(army.x, army.y, army.radius, 1);
			}
		}
		
		//==================================================================================================//

		public function drawMerchant(merchant:Army, isOpponent:Boolean):void{
			
			if(Session.DRAW_DETAILS){
				
				// army
				var merchantCircle:Ellipse;
				merchantCircle = new Ellipse();
				merchantCircle.width = merchant.radius*2;
				merchantCircle.height = merchant.radius*2;
				merchantCircle.x = merchant.x - merchantCircle.width/2;
				merchantCircle.y = merchant.y - merchantCircle.height/2;
				
				merchantCircle.fill = new SolidColor(isOpponent ? Numbers.BLACK : Numbers.YELLOW);
				merchant.armyCircle = merchantCircle;
				
				board.boardEntities.addElement(merchantCircle);
			}
			
			var merchantMiniCircle:Ellipse;
			merchantMiniCircle = new Ellipse();
			merchantMiniCircle.width = merchant.radius*2/4;
			merchantMiniCircle.height = merchant.radius*2/4;
			merchantMiniCircle.x = (merchant.x - merchantCircle.width/2)/15;
			merchantMiniCircle.y = (merchant.y - merchantCircle.height/2)/15;
			
			
			merchantMiniCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.BLUE);
			
			board.minimap.addElement(merchantMiniCircle);
		
			//------------------------------------------------------------//
			
			if(Session.DRAW_TEXTS){
				
			}
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_IMAGES){
				drawArmyImages(merchant.x, merchant.y, merchant.radius, 2);
			}
		}

		//==================================================================================================//
		
		// type : 1 army | 2 merchant
		// return la liste des images de la troupe (Array)
		private function drawArmyImages(x:int, y:int, radius:int, type:int):Array{
			var angle:int = 0;
			var distanceAuCentre:int = 0;
			var insideCircle:Boolean = true;
			
			var images:Map = new Map();
			
			while(insideCircle){
				var image:Image = new Image();
				
				switch(Utils.random(2)){
					case 1:
						image.source = type == 1 ? ImageContainer.WARRIOR1 : ImageContainer.MERCHANT1;
						break;
					case 2:
						image.source = type == 1 ? ImageContainer.WARRIOR2 : ImageContainer.MERCHANT2;
						break;
				}
				
				image.x = (x - image.width - 15) + (Math.cos(angle)*(distanceAuCentre/2));
				image.y = (y - image.height - 17) + (Math.sin(angle)*(distanceAuCentre/2));
				
				if(distanceAuCentre > radius*2)
					insideCircle = false;
				else{
					images.put(image.y, image);
					distanceAuCentre = angle/360 * 20;
					angle += distanceAuCentre > 50 ? (distanceAuCentre > 100 ? 10 : 20) : 40;
				}
			}
			
			images.sortKeys(new SortField(null, true));
			
			for each(var image:Image in images.values()){
				//var num:int = Utils.random(images.length) - 1;
				//boardImages.addElement(images.removeItemAt(num) as Image);
				board.boardImages.addElement(image);
			}
			
			return images.values().toArray();
		}
		
		//==================================================================================================//

		public function drawLand(land:int, isOpponent:Boolean):void{
			
			var i:int = land;
			var j:int = 0;
			
			while(land > 30){
				land -= 30;
				j++;
			}
			
			i = land;
			
			var landSquare:Rect = new Rect();
			landSquare.width = Numbers.LAND_WIDTH;
			landSquare.height = Numbers.LAND_HEIGHT;
			landSquare.x = i*Numbers.LAND_WIDTH;
			landSquare.y = j*Numbers.LAND_HEIGHT;
			landSquare.fill = new SolidColor(isOpponent ? Numbers.YELLOW : Numbers.BLUE);
			landSquare.alpha = 0.2;
			
			board.boardEntities.addElement(landSquare);
		}

		//==================================================================================================//

		public function removeArmyFromBoard(army:Army):void{
			try{
				if(army.armyCircle)board.boardEntities.removeElement(army.armyCircle);
				if(army.lineTo)board.boardEntities.removeElement(army.lineTo);	
				if(army.ellipseTo)board.boardEntities.removeElement(army.ellipseTo);	
				if(army.tmpLandSquare)board.boardEntities.removeElement(army.tmpLandSquare);
				board.boardEntities.redrawRequested = true;

			}catch(e:Error){trace(e.message);}

		}

		public function refreshArmyOnBoard(army:Army):void{
			try{
				board.boardEntities.removeElement(army.armyCircle);
			}catch(e:Error){}
			
			drawArmy(army, false);
		}

		public function refreshMerchantOnBoard(merchant:Army):void{
			try{
				board.boardEntities.removeElement(merchant.armyCircle);
			}catch(e:Error){}
			
			drawMerchant(merchant, false);
		}
		
		
		//==================================================================================================//
		
		var selectedArmyCircle:Ellipse = new Ellipse();
		public function drawArmySelection(army:Army):void{
			
			removeArmySelection();
			
			Session.CURRENT_SELECTION_IS_ARMY = true;
			
			selectedArmyCircle = new Ellipse();
			selectedArmyCircle.width = army.radius*2 + 10;
			selectedArmyCircle.height = army.radius*2 + 10;
			selectedArmyCircle.x = army.x - selectedArmyCircle.width/2;
			selectedArmyCircle.y = army.y - selectedArmyCircle.height/2;
			
			selectedArmyCircle.stroke = new SolidColorStroke(army.type == 1 ? Numbers.WHITE : Numbers.YELLOW);
			
			board.boardEntities.addElement(selectedArmyCircle);
		}

		public function removeArmySelection():void{
			try{
				Session.CURRENT_SELECTION_IS_ARMY = false;
				board.boardEntities.removeElement(selectedArmyCircle);
			}
			catch(e:Error){}
		}

		//==================================================================================================//

		// verifie si la case est accessible pour etre rajoutee aux contrees
		public function testLand(armyMoved:Army):void{
			
			if(armyMoved.type == 2)
				return;
			
			var boardX:int = armyMoved.lineTo.xTo;
			var boardY:int = armyMoved.lineTo.yTo;

			try{
				board.boardEntities.removeElement(armyMoved.tmpLandSquare);	
			}catch(error:Error){}
			
			var landX:int = Math.floor(boardX/Numbers.LAND_WIDTH);	
			var landY:int = Math.floor(boardY/Numbers.LAND_HEIGHT);
			
			armyMoved.landExpected = landY*30+landX;
			var landIsAccessible:Boolean = false;
			var landIsExpectedYet:Boolean = false;
			
			
			// contree deja prevue comme conquise par une autre armee ce tour ci
			for each(var army:Army in Session.currentPlayer.armies){
				if(army.armyUID != armyMoved.armyUID
				&& army.landExpected == armyMoved.landExpected){
					landIsExpectedYet = true;
					break;
				}
			}
			
			// cest deja une contree enregistree
			if(Session.currentPlayer.lands.contains(armyMoved.landExpected))
				landIsExpectedYet = true;
			
			// si les tests precedents sont ok, on regarde si la contree touche le royaume
			if(!landIsExpectedYet){
			
				for each(var land:int in Session.currentPlayer.lands){
					if(land == armyMoved.landExpected+1
					|| land == armyMoved.landExpected-1
					|| land == armyMoved.landExpected-30
					|| land == armyMoved.landExpected+30){
						landIsAccessible = true;
						break;
					}
				}
			}
			
			if(landIsAccessible){
				armyMoved.tmpLandSquare = new Rect();
				armyMoved.tmpLandSquare.width = Numbers.LAND_WIDTH;
				armyMoved.tmpLandSquare.height = Numbers.LAND_HEIGHT;
				armyMoved.tmpLandSquare.x = landX*Numbers.LAND_WIDTH;
				armyMoved.tmpLandSquare.y = landY*Numbers.LAND_HEIGHT;
				armyMoved.tmpLandSquare.fill = new SolidColor(Numbers.BLUE);
				armyMoved.tmpLandSquare.alpha = 0.12;
				
				board.boardEntities.addElement(armyMoved.tmpLandSquare);
			}
			else
				armyMoved.landExpected = -1;
		}
		
		//==================================================================================================//
		
		private var conflictInDisplay:Conflict;
		private var moves:ArrayCollection;
		private var imagesForTheReplay:ArrayCollection;
		
		public function displayConflict(conflict:Conflict):void{
			
			conflictInDisplay = conflict;
			
			FlexGlobals.topLevelApplication.hideconflicts.addEventListener(EffectEvent.EFFECT_END, windowClosed);
			FlexGlobals.topLevelApplication.hideconflicts.play();
		}
		
		protected function windowClosed(event:EffectEvent):void{
			FlexGlobals.topLevelApplication.hideconflicts.removeEventListener(EffectEvent.EFFECT_END, windowClosed);
			
			var mover:Move = new Move();
			mover.target = board.image;
			mover.xTo = 250 - conflictInDisplay.x;
			mover.yTo = 250 - conflictInDisplay.y;
			mover.duration = 800;
			mover.addEventListener(EffectEvent.EFFECT_END, boardPlaced);
			mover.play();
		}

		protected function boardPlaced(event:EffectEvent):void{
			
			moves = new ArrayCollection();
			imagesForTheReplay = new ArrayCollection();
			
			for each(var moveConflict:MoveConflict in conflictInDisplay.moveAllies){
				moves.addItem(moveArmyOnConflict(moveConflict, conflictInDisplay.x, conflictInDisplay.y));
			}
			
			for each(var moveConflict:MoveConflict in conflictInDisplay.moveEnnemies){
				moves.addItem(moveArmyOnConflict(moveConflict, conflictInDisplay.x, conflictInDisplay.y));
			}
			
			// recupere le dernier move (on part de la fin et on chope le premier non null (null = armyStanding : pas de move))
			var i:int = 1;
			var lastMove:Move = null;
			while(lastMove == null){
				lastMove = moves.getItemAt(moves.length-i) as Move;
				i++;
			}
			
			//attend la fin du dernier move
			lastMove.addEventListener(EffectEvent.EFFECT_END, moveConflictDone);
		}

		private function moveArmyOnConflict(moveConflict:MoveConflict, xTo:int, yTo:int):Move{
			
			var radius:int =  Math.sqrt(moveConflict.armySize)/2 + 2;
			var images:Array = drawArmyImages(moveConflict.xFrom, moveConflict.yFrom, radius, 1);
			
			for(var i:int=0; i<images.length; i++){
				imagesForTheReplay.addItem(images[i]);
			}
			
			if(!moveConflict.armyStanding){
				var mover:Move = new Move();
				mover.targets = images;
				mover.xFrom = moveConflict.xFrom;
				mover.yFrom = moveConflict.yFrom;
				mover.xTo = xTo-12;
				mover.yTo = yTo-12;
				mover.duration = 2000;
				
				mover.play();
				return mover;
			}
			else
				return null;
		}
		
		
		protected function moveConflictDone(event:EffectEvent):void{
			
			for each(var image:Image in imagesForTheReplay){
				image.visible = false;
				image = null;
			}
			
			FlexGlobals.topLevelApplication.showconflicts.play();
		}
	}
}