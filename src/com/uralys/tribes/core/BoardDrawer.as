package com.uralys.tribes.core
{
	import com.dncompute.graphics.GraphicsUtil;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Case;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Conflict;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.MoveConflict;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.tribes.pages.Board;
	import com.uralys.tribes.renderers.MoveHighLight;
	import com.uralys.utils.Map;
	import com.uralys.utils.Utils;
	
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.collections.SortField;
	import mx.controls.Image;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.EffectEvent;
	import mx.events.ToolTipEvent;
	import mx.graphics.GradientEntry;
	import mx.graphics.RadialGradientStroke;
	import mx.graphics.SolidColor;
	import mx.graphics.SolidColorStroke;
	import mx.utils.ObjectUtil;
	
	import spark.components.Button;
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

		//=====================================================================================//
		
		private var currentUnitMoveHighLights:ArrayCollection;
		
		//=====================================================================================//

		/*
			Load les cases autour de cette case(centerX,centerY)
		*/
		public function refreshMap(centerX:int, centerY:int):void{
			Session.WAIT_FOR_SERVER = true;
			Session.centerX = centerX;
			Session.centerY = centerY;
			
			GameManager.getInstance().loadCases(centerX,centerY);
		}

		/**
		 * Lorsque les cases sont chargees, on refresh tout les display
		 */ 
		public function refreshDisplay():void{
			
			try{
				//board.boardEntities.removeAllElements();
				//board.boardImages.removeAllElements();
				//board.boardTexts.removeAllElements();
			}catch(e:Error){}
			
			var offset:int = Numbers.NB_TILES_ON_EDGE_BY_LOADING/2;
			
			// draw city grounds
			for(var j:int=Session.centerY-offset; j < Session.centerY+offset; j++){
				for(var i:int=Session.centerX-offset; i < Session.centerX+offset; i++){
					var _case:Case = Session.map[i][j];
					if(_case.type == 1)
						drawCase(_case); 
				}
			}
			
			for(var j:int=Session.centerY-offset; j < Session.centerY+offset; j++)
			{
				// draw city houses
				for(var i:int=Session.centerX-offset; i < Session.centerX+offset; i++)
				{
					var _case:Case = Session.map[i][j];
					if(_case.type == 1)
						drawCity(_case.city);
				}

				// draw forests grounds
				for(var i:int=Session.centerX-offset; i < Session.centerX+offset; i++)
				{
					var _case:Case = Session.map[i][j];
					if(_case.type == 0){
						drawCase(_case);
					}
				}

				// draw units
				for(var i:int=Session.centerX-offset; i < Session.centerX+offset; i++)
				{
					var _case:Case = Session.map[i][j];
					
					if(_case.refresh())
						drawUnits(_case);
				}
			}
			
			Session.board.refreshUnitsInCity();
			Session.WAIT_FOR_SERVER = false;
		}
		

		private function drawCase(_case:Case):void{

			var image:Image = new Image();
			var placeForet:Boolean = false;
			
			switch(_case.type){
				case 0:
					// forest
					placeForet = true;
					var image:Image = new Image();
					switch(Utils.random(6)){
						case 1:
							image.source = ImageContainer.FORET7;
							break;
						case 2:
							image.source = ImageContainer.FORET7;
							break;
						case 3:
							image.source = ImageContainer.FORET7;
							break;
						case 4:
							image.source = ImageContainer.FORET7;
							break;
						case 5:
							image.source = ImageContainer.FORET7;
							break;
						default:
							image.source = ImageContainer.FORET7;
							break;
						
					}

					break;
				case 1:
					//city
					image.source = ImageContainer.SOL_VILLE;
					break;
			}
			
			image.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
			image.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
			
			// decalage des tuiles forets
			if(placeForet){
				image.x -=10;	
				image.y -=9;	
			}
			
			image.data = _case;
			image.addEventListener(MouseEvent.CLICK, tileIsCLicked);
			image.addEventListener(MouseEvent.ROLL_OVER, tileIsRolledOn);
			
			Session.board.mapPositioner.addElement(image);
		}

		public function drawUnits(_case:Case):void{
			
			// ------------------//
			// affichage des pions (armées-marchands)
			
			var imageUnit:Image = new Image();
			
			if(_case.armies.length > 0){
				imageUnit.source = ImageContainer.getImage(ImageContainer.ARMY_PLAYER);
			}
			else if(_case.merchants.length > 0){
				imageUnit.source = ImageContainer.getImage(ImageContainer.MERCHANT_PLAYER);				
			}
			
			imageUnit.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4) + 15;
			imageUnit.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2) - 10;
			imageUnit.mouseEnabled = false;
			
			_case.imageUnit = imageUnit; 
			
			Session.board.mapPositioner.addElement(imageUnit);
		}
		
		
		public function drawCity(city:City):void{
			
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
				
				
				var cityPx:int = city.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4) + Numbers.LAND_WIDTH/2;
				var cityPy:int = city.x * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2) + Numbers.LAND_HEIGHT/2;
				
				image.x = (cityPx - image.width - 10) + (Math.cos(angle)*(distanceAuCentre/2));
				image.y = (cityPy - image.height - 10) + (Math.sin(angle)*(distanceAuCentre/2));
				
				image.data = city;
				image.addEventListener(MouseEvent.CLICK, cityIsCLicked);
				image.addEventListener(MouseEvent.ROLL_OVER, cityIsRolledOn);
				
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
				Session.board.mapPositioner.addElement(image);
			}
		}
		
		//==================================================================================================//
		// click et roll-over sur les tuiles-images
		
		protected function tileIsCLicked(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().clickOnCase(event.currentTarget.data as Case);
		}
		
		protected function cityIsCLicked(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().clickOnHouse(event.currentTarget.data as City);
		}
		
		protected function tileIsRolledOn(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().rollOnCase(event.currentTarget.data as Case);
		}

		protected function cityIsRolledOn(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().rollOnCity(event.currentTarget.data as City);
		}
		
		//==================================================================================================//
		
//			
//		public function redrawAllEntities():void{
//			
//			try{
//				board.mapTiles.removeAllElements();
//				board.boardEntities.removeAllElements();
//				board.boardImages.removeAllElements();
//				board.boardTexts.removeAllElements();
//			}catch(e:Error){}
//			
//			drawMap();
//			drawConflicts();
//			
////			for each (var player:Player in game.players){
////				
////				// il faudra revoir ca pour les alliances
////				var isOpponent:Boolean = Session.player.uralysUID != player.uralysUID;
////				
////				//-----------------------------------------------------------------------------------------//
////				// cities
////				
////				for each (var city:City in player.cities){
////					drawCity(city, isOpponent);
////				}
////
////				//-----------------------------------------------------------------------------------------//
////				// armies
////
////				for each (var army:Unit in player.units){					
////					drawArmy(army, isOpponent);
////				}
////
////				//-----------------------------------------------------------------------------------------//
////				// lands
////				
////				for each (var land:int in player.lands){
////					drawLand(land, isOpponent);					
////				}
////			}
//		}
//
//		//=====================================================================================//
//		
//		public function drawConflicts():void{
//			for each(var conflict:Conflict in Session.player.conflicts){
//				var image:Image = new Image();
//				
//				image.source = ImageContainer.CONFLIT;
//					
//				image.x = conflict.x-25;
//				image.y = conflict.y-25;
//				
//				board.mapTiles.addElement(image);
//			}
//				
//		}
//		
//		
//		public function drawMap():void{
//			
//			//------------------------------------------------//
//			// init du tableau 29x29
//			
//			Session.map = [];
//			
//			for(var i:int=0; i < board.mapPositioner.width/Numbers.LAND_WIDTH; i++){
//				Session.map[i] = [];
//				for(var j:int=0; j < board.mapPositioner.height/Numbers.LAND_HEIGHT; j++){
//					Session.map[i][j] = Numbers.NOTHING;
//				}
//			}
//			
			//------------------------------------------------//
			// city floors
			
//			for each (var player:Player in game.players){
//				for each (var city:City in player.cities){
//					
//					// de 0 a 29
//					var cityLandXmin:int = Math.floor((city.x - city.radius)/Numbers.LAND_WIDTH);	
//					var cityLandXmax:int = Math.floor((city.x + city.radius)/Numbers.LAND_WIDTH);	
//					var cityLandYmin:int = Math.floor((city.y - city.radius)/Numbers.LAND_HEIGHT);	
//					var cityLandYmax:int = Math.floor((city.y + city.radius)/Numbers.LAND_HEIGHT);
//					
//					
//					for(var i:int=cityLandXmin; i <= cityLandXmax; i++){
//						for(var j:int=cityLandYmin; j <= cityLandYmax; j++){
//							
//							var image:Image = new Image();
//							image.source = ImageContainer.SOL_VILLE;
//							
//							image.x = i * Numbers.LAND_WIDTH;
//							image.y = j * Numbers.LAND_HEIGHT;
//							
//							Session.map[i][j] = Numbers.PLAIN;
//							
//							board.mapTiles.addElement(image);
//						}
//					}
//				}
//			}
			
			//------------------------------------------------//
			// lacs (3x3 cases)
//			
//			var lacsX:Array = [8, 18, 8, 18];
//			var lacsY:Array = [8, 8, 18, 18];
//			
//			for(i = 0; i < lacsX.length; i++){
//				var image:Image = new Image();
//				
//				switch(Utils.random(2)){
//					case 1:
//						image.source = ImageContainer.LAC1;
//						break;
//					case 2:
//						image.source = ImageContainer.LAC2;
//						break;
//					
//				}
//				
//				image.x = lacsX[i] * Numbers.LAND_WIDTH;
//				image.y = lacsY[i] * Numbers.LAND_HEIGHT;
//				
//				// 3x3 cases en lac
//				for(var k:int = 0; k < 3; k++){
//					for(var l:int = 0; l < 3; l++){
//						Session.map[lacsX[i] + k][lacsY[i] + l] = Numbers.LAKE;
//					}
//				}
//				
//				board.mapTiles.addElement(image);
//			}
//			
//			//------------------------------------------------//
//			// rochers (2x2 cases)
//			
//			var rocsX:Array = [9, 19, 9, 19];
//			var rocsY:Array = [2, 2, 27, 27];
//			
//			for(i = 0; i < lacsX.length; i++){
//				var image:Image = new Image();
//				
//				switch(Utils.random(2)){
//					case 1:
//						image.source = ImageContainer.ROCHE1;
//						break;
//					case 2:
//						image.source = ImageContainer.ROCHE2;
//						break;
//					
//				}
//				
//				image.x = rocsX[i] * Numbers.LAND_WIDTH;
//				image.y = rocsY[i] * Numbers.LAND_HEIGHT;
//				
//				// 3x3 cases en lac
//				for(var k:int = 0; k < 2; k++){
//					for(var l:int = 0; l < 2; l++){
//						Session.map[rocsX[i] + k][rocsY[i] + l] = Numbers.ROCK;
//					}
//				}
//				
//				board.mapTiles.addElement(image);
//			}
//			
//			//------------------------------------------------//
//			// forests
//			
//			for(var i:int=0; i < board.mapPositioner.width/Numbers.LAND_WIDTH; i++){
//				for(var j:int=0; j < board.mapPositioner.height/Numbers.LAND_HEIGHT; j++){
//					if(Session.map[i][j] == Numbers.NOTHING){
//						var image:Image = new Image();
//						switch(Utils.random(2)){
//							case 1:
//								image.source = ImageContainer.FORET1;
//								break;
//							case 2:
//								image.source = ImageContainer.FORET2;
//								break;
//							
//						}
//						
//						image.x = i * Numbers.LAND_WIDTH - 35;
//						image.y = j * Numbers.LAND_HEIGHT - 35;
//						
//						board.mapTiles.addElement(image);
//					}
//				}
//			}
//			
//		}

		//=====================================================================================//

		public function redrawTexts():void{
//			
//			try{
//				board.boardTexts.removeAllElements();
//			}catch(e:Error){}
			
//			for each (var player:Player in game.players){
//				
//				var isOpponent:Boolean = Session.player.uralysUID != player.uralysUID;
//				if(!isOpponent)
//					player = Session.player; // toutes les modifs sont a jour dans Session.currentPlayer, et non dans 'game'
//				
//				//-----------------------------------------------------------------------------------------//
//				// cities
//				
//				for each (var city:City in player.cities){
//					drawCityText(city, isOpponent);
//				}
//				
//			}
		}
	
		//==================================================================================================//
		

		//==================================================================================================//
//			
//		public function drawCity(city:City, isOpponent:Boolean):void{
//			
//			if(isOpponent)
//				return;
//			
//			if(Session.DRAW_DETAILS){
//				var cityCircle:Ellipse;
//				cityCircle = new Ellipse();
//				cityCircle.width = city.radius*2;
//				cityCircle.height = city.radius*2;
//				cityCircle.x = city.x - cityCircle.width/2;
//				cityCircle.y = city.y - cityCircle.height/2;
//				
//				cityCircle.fill = new SolidColor(isOpponent ? Numbers.YELLOW : Numbers.BLUE);
//				
//				board.boardEntities.addElement(cityCircle);
//			}
//			
//			var cityMiniCircle:Rect;
//			cityMiniCircle = new Rect();
//			cityMiniCircle.width = city.radius*2/10;
//			cityMiniCircle.height = city.radius*2/10;
//			cityMiniCircle.x = (city.x - cityCircle.width/2)/15;
//			cityMiniCircle.y = (city.y - cityCircle.height/2)/15;
//			
//			
//			cityMiniCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.BLUE);
//			
//			board.minimap.addElement(cityMiniCircle);
//			
//			//------------------------------------------------------------//
//			
//			if(Session.DRAW_TEXTS){
//				drawCityText(city, isOpponent);
//			}
//			
//			//------------------------------------------------------------//
//			
//			if(Session.DRAW_IMAGES){
//				var angle:int = 0;
//				var distanceAuCentre:int = 0;
//				var insideCircle:Boolean = true;
//				
//				var images:Map = new Map();
//				
//				while(insideCircle){
//					var image:Image = new Image();
//					
//					switch(Utils.random(3)){
//						case 1:
//							image.source = ImageContainer.HOUSE1;
//							break;
//						case 2:
//							image.source = ImageContainer.HOUSE2;
//							break;
//						case 3:
//							image.source = ImageContainer.HOUSE3;
//							break;
//						
//					}
//					
//					image.x = (city.x - image.width - 10) + (Math.cos(angle)*(distanceAuCentre/2));
//					image.y = (city.y - image.height - 10) + (Math.sin(angle)*(distanceAuCentre/2));
//					
//					if(distanceAuCentre > city.radius*2)
//						insideCircle = false;
//					else{
//						images.put(image.y, image);
//						distanceAuCentre = angle/360 * 25;
//						angle += distanceAuCentre > 50 ? (distanceAuCentre > 100 ? 10 : 20) : 40;
//					}
//				}
//
//				images.sortKeys(new SortField(null, true));
//				
//				for each(var image:Image in images.values()){
//					//var num:int = Utils.random(images.length) - 1;
//					//boardImages.addElement(images.removeItemAt(num) as Image);
//					board.boardImages.addElement(image);
//				}
//			}
//		}

		private function drawCityText(city:City, isOpponent:Boolean):void{
			var name:Label = new Label();
			name.text = city.name;
			name.x = city.x - city.radius - 20; 
			name.y = city.y - city.radius - 20; 
			
//			board.boardTexts.addElement(name);
		}

		//==================================================================================================//

		public function drawArmy(army:Unit, isOpponent:Boolean):void{
			
			if(Session.DRAW_DETAILS){
				
				// army
//				var armyCircle:Ellipse;
//				armyCircle = new Ellipse();
//				armyCircle.width = army.radius*2;
//				armyCircle.height = army.radius*2;
//				armyCircle.x = army.x - armyCircle.width/2;
//				armyCircle.y = army.y - armyCircle.height/2;
//				
//				armyCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.WHITE);
//				army.armyCircle = armyCircle;
//				
//				board.boardEntities.addElement(armyCircle);
			}
			
//			var armyMiniCircle:Ellipse;
//			armyMiniCircle = new Ellipse();
//			armyMiniCircle.width = army.radius*2/4;
//			armyMiniCircle.height = army.radius*2/4;
//			armyMiniCircle.x = (army.currentCase.x - army.radius/2)/15;
//			armyMiniCircle.y = (army.currentCase.y - army.radius/2)/15;
//			
//			
//			armyMiniCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.BLUE);
//			
//			Session.board.minimap.addElement(armyMiniCircle);
			
			//------------------------------------------------------------//
//			
//			if(Session.DRAW_TEXTS){
//				
//			}
			
			//------------------------------------------------------------//
			
//			if(Session.DRAW_IMAGES){
//				drawArmyImages(army.currentCase.x, army.currentCase.y, army.radius, 1);
//			}
		}
		
		//==================================================================================================//

		public function drawMerchant(merchant:Unit, isOpponent:Boolean):void{
			
//			if(Session.DRAW_DETAILS){
//				
//				// army
//				var merchantCircle:Ellipse;
//				merchantCircle = new Ellipse();
//				merchantCircle.width = merchant.radius*2;
//				merchantCircle.height = merchant.radius*2;
//				merchantCircle.x = merchant.x - merchantCircle.width/2;
//				merchantCircle.y = merchant.y - merchantCircle.height/2;
//				
//				merchantCircle.fill = new SolidColor(isOpponent ? Numbers.BLACK : Numbers.YELLOW);
//				merchant.armyCircle = merchantCircle;
//				
//				board.boardEntities.addElement(merchantCircle);
//			}
			
//			var merchantMiniCircle:Ellipse;
//			merchantMiniCircle = new Ellipse();
//			merchantMiniCircle.width = merchant.radius*2/4;
//			merchantMiniCircle.height = merchant.radius*2/4;
//			merchantMiniCircle.x = (merchant.currentCase.x - merchant.radius/2)/15;
//			merchantMiniCircle.y = (merchant.currentCase.y - merchant.radius/2)/15;
//			
//			
//			merchantMiniCircle.fill = new SolidColor(isOpponent ? Numbers.RED : Numbers.BLUE);
//			
//			Session.board.minimap.addElement(merchantMiniCircle);
		
			//------------------------------------------------------------//
//			
//			if(Session.DRAW_TEXTS){
//				
//			}
			
			//------------------------------------------------------------//
			
//			if(Session.DRAW_IMAGES){
//				drawArmyImages(merchant.currentCase.x, merchant.currentCase.y, merchant.radius, 2);
//			}
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
//				board.boardImages.addElement(image);
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
			
//			board.boardEntities.addElement(landSquare);
		}

		//==================================================================================================//

		public function removeArmyFromBoard(army:Unit):void{
			try{
				//if(army.armyCircle)board.boardEntities.removeElement(army.armyCircle);
				//if(army.lineTo)board.boardEntities.removeElement(army.lineTo);	
				//if(army.ellipseTo)board.boardEntities.removeElement(army.ellipseTo);	
				//if(army.tmpLandSquare)board.boardEntities.removeElement(army.tmpLandSquare);
//				board.boardEntities.redrawRequested = true;

			}catch(e:Error){trace(e.message);}

		}

		public function refreshArmyOnBoard(army:Unit):void{
			try{
				//board.boardEntities.removeElement(army.armyCircle);
			}catch(e:Error){}
			
			drawArmy(army, false);
		}

		public function refreshMerchantOnBoard(merchant:Unit):void{
			try{
				//board.boardEntities.removeElement(merchant.armyCircle);
			}catch(e:Error){}
			
			drawMerchant(merchant, false);
		}

		//==================================================================================================//
		
		public function addAllUnitMovesImages(unit:Unit):void{
			
			currentUnitMoveHighLights = new ArrayCollection();
			
			var xPreviousMove:int = -1; 
			var yPreviousMove:int = -1; 
			
			var numOfTheMove:int = 0;
			for each(var unitMove:com.uralys.tribes.entities.Move in unit.moves)
			{
				//------------------------------------------------------//

				numOfTheMove++;
				
				//------------------------------------------------------//

				var moveHighlight:MoveHighLight = new MoveHighLight();
				moveHighlight.x = Utils.getXOnBoard(Utils.getXFromCaseUID(unitMove.caseUID));
				moveHighlight.y = Utils.getYOnBoard(Utils.getYFromCaseUID(unitMove.caseUID));
				moveHighlight._move = unitMove;
				
				currentUnitMoveHighLights.addItem(moveHighlight);
				Session.board.highlighters.addElement(moveHighlight);
				
				//------------------------------------------------------//
				
				if(xPreviousMove >= 0){
					Session.board.highlighters.graphics.lineStyle(1,0x000000);
					Session.board.highlighters.graphics.beginFill(0x22ee22);
 
					GraphicsUtil.drawArrow(
						Session.board.highlighters.graphics,
						new Point(Utils.getXOnBoard(xPreviousMove)+Numbers.LAND_WIDTH/2,Utils.getYOnBoard(yPreviousMove)+Numbers.LAND_HEIGHT/2),
						new Point(Utils.getXOnBoard(Utils.getXFromCaseUID(unitMove.caseUID))+Numbers.LAND_WIDTH/2,Utils.getYOnBoard(Utils.getYFromCaseUID(unitMove.caseUID))+Numbers.LAND_HEIGHT/2)
					);
				}
				
				// recorded a second move : wait for timeTo of the first move
				if(numOfTheMove == 2){
					trace("on doit enregistrer un move a ecouter !");
					var _moveToListen:com.uralys.tribes.entities.Move = unit.moves.getItemAt(0) as com.uralys.tribes.entities.Move;
					trace(_moveToListen.moveUID);
						
					CronMover.getInstance().addTimer(_moveToListen);
				}
				
				//------------------------------------------------------//

				xPreviousMove = Utils.getXFromCaseUID(unitMove.caseUID);
				yPreviousMove = Utils.getYFromCaseUID(unitMove.caseUID);
			}
		}
		
		public function removeAllUnitMovesImages():void{
			for each(var moveHighLight:MoveHighLight in currentUnitMoveHighLights){
				Session.board.highlighters.removeElement(moveHighLight);
				Session.board.highlighters.graphics.clear();
			}	
		}
		
		
		//==================================================================================================//
		
		var selectedArmyCircle:Ellipse = new Ellipse();
		public function drawArmySelection(army:Unit):void{
//			
//			removeArmySelection();
//			
//			Session.CURRENT_SELECTION_IS_ARMY = true;
//			
//			selectedArmyCircle = new Ellipse();
//			selectedArmyCircle.width = army.radius*2 + 10;
//			selectedArmyCircle.height = army.radius*2 + 10;
//			selectedArmyCircle.x = army.currentCase.x - selectedArmyCircle.width/2;
//			selectedArmyCircle.y = army.currentCase.y - selectedArmyCircle.height/2;
//			
//			selectedArmyCircle.stroke = new SolidColorStroke(army.type == 1 ? Numbers.WHITE : Numbers.YELLOW);
			
//			board.boardEntities.addElement(selectedArmyCircle);
		}

		public function removeArmySelection():void{
			try{
//				Session.CURRENT_SELECTION_IS_ARMY = false;
//				board.boardEntities.removeElement(selectedArmyCircle);
			}
			catch(e:Error){}
		}

		//==================================================================================================//

		// verifie si la case est accessible pour etre rajoutee aux contrees
		public function testLand(armyMoved:Unit):void{
			
//			if(armyMoved.type == 2)
//				return;
//			
//			var boardX:int = armyMoved.lineTo.xTo;
//			var boardY:int = armyMoved.lineTo.yTo;
//
//			try{
//				board.boardEntities.removeElement(armyMoved.tmpLandSquare);	
//			}catch(error:Error){}
//			
//			var landX:int = Math.floor(boardX/Numbers.LAND_WIDTH);	
//			var landY:int = Math.floor(boardY/Numbers.LAND_HEIGHT);
//			
//			armyMoved.landExpected = landY*30+landX;
//			var landIsAccessible:Boolean = false;
//			var landIsExpectedYet:Boolean = false;
//			
//			
//			// contree deja prevue comme conquise par une autre armee ce tour ci
//			for each(var army:Unit in Session.player.units){
//				if(army.unitUID != armyMoved.unitUID
//				&& army.landExpected == armyMoved.landExpected){
//					landIsExpectedYet = true;
//					break;
//				}
//			}
//			
			
			// cest deja une contree enregistree
			//if(Session.player.lands.contains(armyMoved.landExpected))
			//	landIsExpectedYet = true;
			
			// si les tests precedents sont ok, on regarde si la contree touche le royaume
//			if(!landIsExpectedYet){
//			
//				for each(var land:int in Session.player.lands){
//					if(land == armyMoved.landExpected+1
//					|| land == armyMoved.landExpected-1
//					|| land == armyMoved.landExpected-30
//					|| land == armyMoved.landExpected+30){
//						landIsAccessible = true;
//						break;
//					}
//				}
//			}
			
//			if(landIsAccessible){
//				armyMoved.tmpLandSquare = new Rect();
//				armyMoved.tmpLandSquare.width = Numbers.LAND_WIDTH;
//				armyMoved.tmpLandSquare.height = Numbers.LAND_HEIGHT;
//				armyMoved.tmpLandSquare.x = landX*Numbers.LAND_WIDTH;
//				armyMoved.tmpLandSquare.y = landY*Numbers.LAND_HEIGHT;
//				armyMoved.tmpLandSquare.fill = new SolidColor(Numbers.BLUE);
//				armyMoved.tmpLandSquare.alpha = 0.12;
//				
//				board.boardEntities.addElement(armyMoved.tmpLandSquare);
//			}
//			else
//				armyMoved.landExpected = -1;
		}
		
		//==================================================================================================//
		// old replay
		
		
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
			
			var mover:spark.effects.Move = new spark.effects.Move();
			mover.target = Session.board.mapPositioner;
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
			var lastMove:com.uralys.tribes.entities.Move = null;
			while(lastMove == null){
				lastMove = moves.getItemAt(moves.length-i) as com.uralys.tribes.entities.Move;
				i++;
			}
			
			//attend la fin du dernier move
			lastMove.addEventListener(EffectEvent.EFFECT_END, moveConflictDone);
		}

		private function moveArmyOnConflict(moveConflict:MoveConflict, xTo:int, yTo:int):spark.effects.Move{
			
			var radius:int =  Math.sqrt(moveConflict.armySize)/2 + 2;
			var images:Array = drawArmyImages(moveConflict.xFrom, moveConflict.yFrom, radius, 1);
			
			for(var i:int=0; i<images.length; i++){
				imagesForTheReplay.addItem(images[i]);
			}
			
			if(!moveConflict.armyStanding){
				var mover:spark.effects.Move = new spark.effects.Move();
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