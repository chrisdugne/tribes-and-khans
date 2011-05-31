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
	import com.uralys.tribes.renderers.Pawn;
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
		
		private var currentUnitMoveHighLights:ArrayCollection = new ArrayCollection();;
		
		//=====================================================================================//

		/*
			Load les cases autour de cette case(centerX,centerY)
		*/
		public function refreshMap(centerX:int, centerY:int):void{
			Session.WAIT_FOR_SERVER = true;
			
			GameManager.getInstance().loadCases(centerX,centerY);
		}

		/**
		 * Lorsque les cases sont chargees, on refresh tout les display
		 */ 
		public function refreshDisplay():void
		{
			// draw city grounds
			for(var j:int=0; j < Numbers.NB_VERTICAL_TILES_BY_LOADING; j++)
			{
				for(var i:int=0; i < Numbers.NB_HORIZONTAL_TILES_BY_LOADING; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.type == 1)
						drawCase(_case); 
				}
			}
			
			for(var j:int=0; j < Numbers.NB_VERTICAL_TILES_BY_LOADING; j++)
			{
				for(var i:int=0; i < Numbers.NB_HORIZONTAL_TILES_BY_LOADING; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.type == 1)
						drawCity(_case.city);
				}

				// draw forests grounds
				for(var i:int=0; i < Numbers.NB_HORIZONTAL_TILES_BY_LOADING; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.type == 0){
						drawCase(_case);
					}
				}
				
			}

			for(var j:int=0; j < Numbers.NB_VERTICAL_TILES_BY_LOADING; j++)
			{
				// draw units
				for(var i:int=0; i < Numbers.NB_HORIZONTAL_TILES_BY_LOADING; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.caseUID.indexOf("case_") == -1)
						continue;
					
					refreshUnits(_case);
				}
			}

			redrawAllLands();
			Session.WAIT_FOR_SERVER = false;
		}
		
		public function redrawAllLands():void
		{
			Session.board.landLayer.removeAllElements();
				
			for(var j:int=0; j < Numbers.NB_VERTICAL_TILES_BY_LOADING; j++)
			{
				// draw lands
				for(var i:int=0; i < Numbers.NB_HORIZONTAL_TILES_BY_LOADING; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.caseUID.indexOf("case_") == -1)
						continue;
					
					
					drawLandAndBounds(_case);
				}
			}
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

		// ---------------------------------------------------------------------//
		// affichage des pions (armées-marchands)
		
		public function refreshUnits(_case:Case):void
		{
			try{
				Session.board.pawnLayer.removeElement(_case.pawn);	
			}
			catch(e:Error){}
			
			var imageUnit:Image;
			
			if(_case.army && _case.army.status != Unit.TO_BE_CREATED){
				imageUnit = new Image();
				
				switch(_case.army.ownerStatus){
					case Unit.PLAYER:
						imageUnit.source = ImageContainer.getImage(ImageContainer.ARMY_PLAYER);
						break;
					case Unit.ALLY:
						imageUnit.source = ImageContainer.getImage(ImageContainer.ARMY_ALLY);
						break;
					case Unit.ENNEMY:
						imageUnit.source = ImageContainer.getImage(ImageContainer.ARMY_ENNEMY);
						break;
				}
			}
			else if(_case.merchants && _case.merchants.status != Unit.TO_BE_CREATED){
				imageUnit = new Image();

				switch(_case.merchants.ownerStatus){
					case Unit.PLAYER:
						imageUnit.source = ImageContainer.getImage(ImageContainer.MERCHANT_PLAYER);
						break;
					case Unit.ALLY:
						imageUnit.source = ImageContainer.getImage(ImageContainer.MERCHANT_ALLY);
						break;
					case Unit.ENNEMY:
						imageUnit.source = ImageContainer.getImage(ImageContainer.MERCHANT_ENNEMY);
						break;
				}
			}
			else{
				return; // no unit
			}
			
			_case.pawn.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4) + 15;
			_case.pawn.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2) - 10;
			imageUnit.mouseEnabled = false;
			
			_case.pawn.addElement(imageUnit);
			
			Session.board.pawnLayer.addElement(_case.pawn);
		}
		
		
		public function drawCity(city:City):void
		{
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
				var cityPy:int = city.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2) + Numbers.LAND_HEIGHT/2;
				
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
				Session.board.mapPositioner.addElement(image);
			}
		}
		
		//------------------------------------------------------------------------------------//
		
		private function drawLandAndBounds(_case:Case):void
		{
			if(_case.landOwner != null)
			{
				var image:Image = new Image();
				
				image.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
				image.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
				
				if(_case.landOwner.playerUID == Session.player.playerUID)
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_VERT);
				else
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_ROUGE);
				
				Session.board.landLayer.addElement(image);
				
				//----------------//
				// frontier NO	
				
				if(Session.map[_case.x - 1][_case.y - 1] == null 
				|| Session.map[_case.x - 1][_case.y - 1].landOwner == null
				|| Session.map[_case.x - 1][_case.y - 1].landOwner.playerUID != _case.landOwner.playerUID)
				{
					var imageNO:Image = new Image();
					imageNO.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
					imageNO.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
					imageNO.source = ImageContainer.getImage(ImageContainer.FRONTIER_NO);
					
					Session.board.landLayer.addElement(imageNO);
				}
				
				//----------------//
				// frontier SO	
				
				if(Session.map[_case.x - 1][_case.y + 1] == null
				|| Session.map[_case.x - 1][_case.y + 1].landOwner == null
				|| Session.map[_case.x - 1][_case.y + 1].landOwner.playerUID != _case.landOwner.playerUID)
				{
					var imageSO:Image = new Image();
					imageSO.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
					imageSO.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
					imageSO.source = ImageContainer.getImage(ImageContainer.FRONTIER_SO);
					
					Session.board.landLayer.addElement(imageSO);
					
				}
				
				//----------------//
				// frontier N
				
				if(Session.map[_case.x][_case.y - 2] == null 
				|| Session.map[_case.x][_case.y - 2].landOwner == null
				|| Session.map[_case.x][_case.y - 2].landOwner.playerUID != _case.landOwner.playerUID)
				{
					var imageN:Image = new Image();
					imageN.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
					imageN.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
					imageN.source = ImageContainer.getImage(ImageContainer.FRONTIER_N);
					
					Session.board.landLayer.addElement(imageN);
				}
				
				//----------------//
				// frontier S
				
				if(Session.map[_case.x][_case.y + 2] == null 
				|| Session.map[_case.x][_case.y + 2].landOwner == null
				|| Session.map[_case.x][_case.y + 2].landOwner.playerUID != _case.landOwner.playerUID)
				{
					var imageS:Image = new Image();
					imageS.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
					imageS.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
					imageS.source = ImageContainer.getImage(ImageContainer.FRONTIER_S);
					
					Session.board.landLayer.addElement(imageS);
					
				}
				
				//----------------//
				// frontier NE
				
				if(Session.map[_case.x + 1][_case.y - 1] == null 
				|| Session.map[_case.x + 1][_case.y - 1].landOwner == null
				|| Session.map[_case.x + 1][_case.y - 1].landOwner.playerUID != _case.landOwner.playerUID)
				{
					var imageNE:Image = new Image();
					imageNE.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
					imageNE.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
					imageNE.source = ImageContainer.getImage(ImageContainer.FRONTIER_NE);
					
					Session.board.landLayer.addElement(imageNE);
					
				}
				
				//----------------//
				// frontier SE
				
				if(Session.map[_case.x + 1][_case.y + 1] == null 
				|| Session.map[_case.x + 1][_case.y + 1].landOwner == null
				|| Session.map[_case.x + 1][_case.y + 1].landOwner.playerUID != _case.landOwner.playerUID)
				{
					var imageSE:Image = new Image();
					imageSE.x = _case.x * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
					imageSE.y = _case.y * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
					imageSE.source = ImageContainer.getImage(ImageContainer.FRONTIER_SE);
					
					Session.board.landLayer.addElement(imageSE);
					
				}
				
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

//		private function drawCityText(city:City, isOpponent:Boolean):void{
//			var name:Label = new Label();
//			name.text = city.name;
//			name.x = city.x - city.radius - 20; 
//			name.y = city.y - city.radius - 20; 
//		}

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

		public function addMoveImages(move:com.uralys.tribes.entities.Move, xPreviousMove:int, yPreviousMove:int, displayProgress:Boolean):void{
			
			//------------------------------------------------------//

			var moveHighlight:MoveHighLight = new MoveHighLight();
			moveHighlight.x = Utils.getXOnBoard(Utils.getXFromCaseUID(move.caseUID));
			moveHighlight.y = Utils.getYOnBoard(Utils.getYFromCaseUID(move.caseUID));
			
			currentUnitMoveHighLights.addItem(moveHighlight);
			Session.board.highlighters.addElement(moveHighlight);
			
			//------------------------------------------------------//
			
			if(xPreviousMove >= 0){
				Session.board.highlighters.graphics.lineStyle(1,0x000000);
				Session.board.highlighters.graphics.beginFill(0x22ee22);
 
				GraphicsUtil.drawArrow(
					Session.board.highlighters.graphics,
					new Point(Utils.getXOnBoard(xPreviousMove)+Numbers.LAND_WIDTH/2,Utils.getYOnBoard(yPreviousMove)+Numbers.LAND_HEIGHT/2),
					new Point(Utils.getXOnBoard(Utils.getXFromCaseUID(move.caseUID))+Numbers.LAND_WIDTH/2,Utils.getYOnBoard(Utils.getYFromCaseUID(move.caseUID))+Numbers.LAND_HEIGHT/2)
				);
			}
		}
		
		public function removeAllUnitMovesImages():void
		{
			for each(var moveHighLight:MoveHighLight in currentUnitMoveHighLights)
			{
				Session.board.highlighters.removeElement(moveHighLight);
				Session.board.highlighters.graphics.clear();
			}	
			
			currentUnitMoveHighLights = new ArrayCollection();
		}
		
		
		//==================================================================================================//
		// old replay
		
		
//		private var conflictInDisplay:Conflict;
//		private var moves:ArrayCollection;
//		private var imagesForTheReplay:ArrayCollection;
		
//		public function displayConflict(conflict:Conflict):void{
//			
//			conflictInDisplay = conflict;
//			
//			FlexGlobals.topLevelApplication.hideconflicts.addEventListener(EffectEvent.EFFECT_END, windowClosed);
//			FlexGlobals.topLevelApplication.hideconflicts.play();
//		}
		
//		protected function windowClosed(event:EffectEvent):void{
//			FlexGlobals.topLevelApplication.hideconflicts.removeEventListener(EffectEvent.EFFECT_END, windowClosed);
//			
//			var mover:spark.effects.Move = new spark.effects.Move();
//			mover.target = Session.board.mapPositioner;
//			mover.xTo = 250 - conflictInDisplay.x;
//			mover.yTo = 250 - conflictInDisplay.y;
//			mover.duration = 800;
//			mover.addEventListener(EffectEvent.EFFECT_END, boardPlaced);
//			mover.play();
//		}

//		protected function boardPlaced(event:EffectEvent):void{
//			
//			moves = new ArrayCollection();
//			imagesForTheReplay = new ArrayCollection();
//			
//			for each(var moveConflict:MoveConflict in conflictInDisplay.moveAllies){
//				moves.addItem(moveArmyOnConflict(moveConflict, conflictInDisplay.x, conflictInDisplay.y));
//			}
//			
//			for each(var moveConflict:MoveConflict in conflictInDisplay.moveEnnemies){
//				moves.addItem(moveArmyOnConflict(moveConflict, conflictInDisplay.x, conflictInDisplay.y));
//			}
//			
//			// recupere le dernier move (on part de la fin et on chope le premier non null (null = armyStanding : pas de move))
//			var i:int = 1;
//			var lastMove:com.uralys.tribes.entities.Move = null;
//			while(lastMove == null){
//				lastMove = moves.getItemAt(moves.length-i) as com.uralys.tribes.entities.Move;
//				i++;
//			}
//			
//			//attend la fin du dernier move
//			lastMove.addEventListener(EffectEvent.EFFECT_END, moveConflictDone);
//		}

//		private function moveArmyOnConflict(moveConflict:MoveConflict, xTo:int, yTo:int):spark.effects.Move{
//			
//			var radius:int =  Math.sqrt(moveConflict.armySize)/2 + 2;
//			var images:Array = drawArmyImages(moveConflict.xFrom, moveConflict.yFrom, radius, 1);
//			
//			for(var i:int=0; i<images.length; i++){
//				imagesForTheReplay.addItem(images[i]);
//			}
//			
//			if(!moveConflict.armyStanding){
//				var mover:spark.effects.Move = new spark.effects.Move();
//				mover.targets = images;
//				mover.xFrom = moveConflict.xFrom;
//				mover.yFrom = moveConflict.yFrom;
//				mover.xTo = xTo-12;
//				mover.yTo = yTo-12;
//				mover.duration = 2000;
//				
//				mover.play();
//				return mover;
//			}
//			else
//				return null;
//		}
//		
//		
//		protected function moveConflictDone(event:EffectEvent):void{
//			
//			for each(var image:Image in imagesForTheReplay){
//				image.visible = false;
//				image = null;
//			}
//			
//			FlexGlobals.topLevelApplication.showconflicts.play();
//		}
		
	}
}