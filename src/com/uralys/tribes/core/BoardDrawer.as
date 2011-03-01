package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Army;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Utils;
	
	import flash.display.Sprite;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.core.UIComponent;
	import mx.graphics.GradientEntry;
	import mx.graphics.RadialGradientStroke;
	import mx.graphics.SolidColor;
	import mx.graphics.SolidColorStroke;
	
	import spark.components.Group;
	import spark.components.Label;
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
			moveGradient.push(new GradientEntry(0x00cccc));
			moveGradient.push(new GradientEntry(0x000000));
			radialGrad = new RadialGradientStroke();
			radialGrad.entries = moveGradient;
			radialGrad.interpolationMethod
		}

		public function setBoards(boardEntities:Group, boardImages:Group, boardTexts:Group):void{
			this.boardEntities = boardEntities;
			this.boardImages = boardImages;
			this.boardTexts = boardTexts;
		}

		public function setGame(game:Game):void{
			this.game = game;
		}

		//=====================================================================================//
		
		private var boardEntities:Group;
		private var boardImages:Group;
		private var boardTexts:Group;
		private var game:Game;
		
		//=====================================================================================//
		
		public function redrawAllEntities():void{
			
			try{
				boardEntities.removeAllElements();
				boardImages.removeAllElements();
				boardTexts.removeAllElements();
			}catch(e:Error){}
			
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
					
					//------------------------------------------------------------//
					
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
						
						boardEntities.addElement(armyCircle);
					}

					//------------------------------------------------------------//

					if(Session.DRAW_TEXTS){

					}

					//------------------------------------------------------------//
					
					if(Session.DRAW_IMAGES){
						
					}
				}

				//-----------------------------------------------------------------------------------------//
				// merchants

				for each (var merchant:Army in player.merchants){
					
					//------------------------------------------------------------//
					
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
						
						boardEntities.addElement(merchantCircle);
					}

					//------------------------------------------------------------//

					if(Session.DRAW_TEXTS){

					}

					//------------------------------------------------------------//
					
					if(Session.DRAW_IMAGES){
						
					}
				}
				
				//-----------------------------------------------------------------------------------------//
				// lands
				
				for each (var land:int in player.lands){
					
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
					
					boardEntities.addElement(landSquare);
				}
			}
		}

		//==================================================================================================//

		public function drawCity(city:City, isOpponent:Boolean):void{
		
			if(Session.DRAW_DETAILS){
				var cityCircle:Ellipse;
				cityCircle = new Ellipse();
				cityCircle.width = city.radius*2;
				cityCircle.height = city.radius*2;
				cityCircle.x = city.x - cityCircle.width/2;
				cityCircle.y = city.y - cityCircle.height/2;
				
				cityCircle.fill = new SolidColor(isOpponent ? Numbers.YELLOW : Numbers.BLUE);
				
				boardEntities.addElement(cityCircle);
			}
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_TEXTS){
				var name:Label = new Label();
				name.text = city.name;
				name.x = city.x - city.radius - 20; 
				name.y = city.y - city.radius - 20; 
				
				boardTexts.addElement(name);
			}
			
			//------------------------------------------------------------//
			
			if(Session.DRAW_IMAGES){
				var angle:int = 0;
				var distanceAuCentre:int = 0;
				var insideCircle:Boolean = true;
				
				var images:ArrayCollection = new ArrayCollection();
				
				while(insideCircle){
					var image:Image = new Image();
					image.source = "../images/house.png";
					
					image.x = (city.x - image.width - 10) + (Math.cos(angle)*(distanceAuCentre/2));
					image.y = (city.y - image.height - 10) + (Math.sin(angle)*(distanceAuCentre/2));
					
					if(distanceAuCentre > city.radius*2)
						insideCircle = false;
					else{
						images.addItem(image);
						distanceAuCentre = angle/360 * 40 + 30;
						angle += distanceAuCentre > 50 ? (distanceAuCentre > 100 ? 10 : 20) : 40;
					}
				}
				
				while(images.length > 0){
					var num:int = Utils.random(images.length) - 1;
					boardImages.addElement(images.removeItemAt(num) as Image);
				}
			}
		}

		//==================================================================================================//

		public function removeArmyFromBoard(army:Army):void{
			try{
				if(army.armyCircle)boardEntities.removeElement(army.armyCircle);
				if(army.lineTo)boardEntities.removeElement(army.lineTo);	
				if(army.ellipseTo)boardEntities.removeElement(army.ellipseTo);	
				if(army.tmpLandSquare)boardEntities.removeElement(army.tmpLandSquare);
				boardEntities.redrawRequested = true;

			}catch(e:Error){trace(e.message);}

		}

		// type 1 : armee, type 2 : merchant
		public function refreshArmyOnBoard(army:Army):void{
			try{
				boardEntities.removeElement(army.armyCircle);
			}catch(e:Error){}
			
			// army
			var armyCircle:Ellipse;
			armyCircle = new Ellipse();
			armyCircle.width = army.radius*2;
			armyCircle.height = army.radius*2;
			armyCircle.x = army.x - armyCircle.width/2;
			armyCircle.y = army.y - armyCircle.height/2;
			
			armyCircle.fill = new SolidColor(army.type == 1 ? Numbers.WHITE : Numbers.YELLOW);
			army.armyCircle = armyCircle;
			
			boardEntities.addElement(armyCircle);
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
			
			boardEntities.addElement(selectedArmyCircle);
		}

		public function removeArmySelection():void{
			try{
				Session.CURRENT_SELECTION_IS_ARMY = false;
				boardEntities.removeElement(selectedArmyCircle);
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
				boardEntities.removeElement(armyMoved.tmpLandSquare);	
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
				
				boardEntities.addElement(armyMoved.tmpLandSquare);
			}
			else
				armyMoved.landExpected = -1;
		}

	}
}