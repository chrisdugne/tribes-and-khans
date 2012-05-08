package com.uralys.tribes.core
{
	import com.dncompute.graphics.GraphicsUtil;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Move;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.main.ImageContainer;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.tribes.renderers.MoveHighLight;
	import com.uralys.utils.Utils;
	
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.core.FlexGlobals;
	import mx.graphics.GradientEntry;
	import mx.graphics.RadialGradientStroke;

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

		[Bindable] public var scale:Number = 1;
		
		//=====================================================================================//
	
		private static var instance : BoardDrawer = new BoardDrawer();
	
		public static function getInstance():BoardDrawer{
			return instance;
		}

		public function BoardDrawer()
		{
			moveGradient = [];
			moveGradient.push(new GradientEntry(0xFFFFFF));
			moveGradient.push(new GradientEntry(0x11dd11));
			moveGradient.push(new GradientEntry(0x000000));
			radialGrad = new RadialGradientStroke();
			radialGrad.entries = moveGradient;
			radialGrad.interpolationMethod
		}

		//=====================================================================================//
		
		private var currentUnitMoveHighLights:ArrayCollection = new ArrayCollection();
		
		//=====================================================================================//

		/**
		 * Lorsque les cases sont chargees, on refresh tout le display
		 */ 
		private var timer:Timer = new Timer(1000,1);
		public function refreshDisplay(event:TimerEvent = null):void
		{
			Session.board.mapLayer.removeAllElements();
			Session.board.pawnLayer.removeAllElements();
			
			if(!ImageContainer.IMAGES_LOADED){
				FlexGlobals.topLevelApplication.message(Translations.LOADING_IMAGES.getItemAt(Session.LANGUAGE));
				timer.addEventListener(TimerEvent.TIMER_COMPLETE, refreshDisplay);
				timer.start();
				return;
			}
			else if(timer.running){
				timer.removeEventListener(TimerEvent.TIMER_COMPLETE, refreshDisplay);
				timer.stop();
			}
			
			
			// de haut en bas, de gauche à droite, ligne apres ligne
			for(var j:int=0; j < Session.nbTilesByEdge; j++)
			{
				for(var i:int=0; i < Session.nbTilesByEdge; i++)
				{
					var _cell:Cell = Session.map[Session.firstCellX+i][Session.firstCellY+j];
					
					drawNorthFrontiers(_cell); 
					drawCell(_cell); 
					drawLands(_cell); 
					
					// draw cell content
					if(_cell.cellUID.indexOf("cell_") != -1)
						refreshCellDisplay(_cell);

					drawSouthFrontiers(_cell); 
					
				}
			}

			Session.WAIT_FOR_SERVER = false;
		}
		
		private function drawCell(_cell:Cell):void
		{
			var image:Image = new Image();
			var placeCity:Boolean = false;
			
			switch(_cell.type)
			{
				case 0:
					// forest

					switch(scale){
						case 1:
							image.source = ImageContainer.FORET_1;
							break;
						case 0.5:
							image.source = ImageContainer.FORET_05;
							break;
						case 0.25:
							image.source = ImageContainer.FORET_025;
							break;
					}

					break;

				case 1:
					//city
					placeCity = true;

					switch(scale){
						case 1:
							image.source = ImageContainer.SOL_VILLE_1;
							break;
						case 0.5:
							image.source = ImageContainer.SOL_VILLE_05;
							break;
						case 0.25:
							image.source = ImageContainer.SOL_VILLE_025;
							break;
					}

					break;
			}
			
			
			image.x = Utils.getXPixel(_cell.x);
			image.y = Utils.getYPixel(_cell.y);
			
			// decalage des tuiles ville de 22 vers le haut
			if(placeCity){
				image.y -= 22 * scale;
			}

			Session.board.mapLayer.addElement(image);
		}

		// ---------------------------------------------------------------------//
		// affichage des pions (armées-marchands)
		
		public function resetCellDisplay(cell:Cell):void
		{
			if(cell == null)
				return;
			
			try{
				cell.pawn.removeElementAt(1);				
				Session.board.pawnLayer.removeElement(cell.pawn);
			}
			catch(e:Error){}
		}

		
		public function refreshCellDisplay(cell:Cell):void
		{
			if(cell == null)
				return;
			
			if(cell.visibleUnit == null){
				return; // no unit
			}
			
			var imageUnit:Image;
			
			switch(cell.visibleUnit.type)
			{
				case Unit.ARMY :
				{
					imageUnit = new Image();
					
					switch(cell.army.ownerStatus){
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
					
					break;
				}
				case Unit.MERCHANT :
				{
					imageUnit = new Image();
					
					switch(cell.caravan.ownerStatus){
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
					
					break;
				}
			}
			
			cell.pawn.x = (Utils.getXPixel(cell.x) + 15*scale);
			cell.pawn.y = (Utils.getYPixel(cell.y) - 120*scale);
			
			imageUnit.scaleX = scale;
			imageUnit.scaleY = scale;
			imageUnit.mouseEnabled = false;
			
			cell.pawn.addElement(imageUnit);
			
			Session.board.pawnLayer.addElement(cell.pawn);
		}
		
		public function drawCity(city:City):void
		{
			var image:Image = new Image();
			
			switch(scale){
				case 1:
					image.source = ImageContainer.SOL_VILLE_1;
					break;
				case 0.5:
					image.source = ImageContainer.SOL_VILLE_05;
					break;
				case 0.25:
					image.source = ImageContainer.SOL_VILLE_025;
					break;
			}
			
			var cityPx:int = Utils.getXPixel(city.x);
			var cityPy:int = Utils.getYPixel(city.y);
			
			image.x = cityPx;
			image.y = cityPy;
			image.scaleX = scale;
			image.scaleY = scale;
			
			Session.board.pawnLayer.addElement(image);
		}
		
		//------------------------------------------------------------------------------------//

		private function drawNorthFrontiers(_cell:Cell):void
		{
			if(_cell.landOwner != null)
			{
				//----------------//
				// frontier NO	
				
				try{
					if(Session.map[_cell.x - 1][_cell.y - 1] != null 
						&& (Session.map[_cell.x - 1][_cell.y - 1].landOwner == null
							|| Session.map[_cell.x - 1][_cell.y - 1].landOwner.playerUID != _cell.landOwner.playerUID))
					{
						var imageNO:Image = new Image();
						imageNO.x = Utils.getXPixel(_cell.x) - 13*scale;
						imageNO.y = Utils.getYPixel(_cell.y) - 23*scale;
						imageNO.source = ImageContainer.getImage(ImageContainer.FRONTIER_NO);
						imageNO.scaleX = scale;
						imageNO.scaleY = scale;
						
						Session.board.mapLayer.addElement(imageNO);
					}
				}
				catch(e:Error){}
				
				
				//----------------//
				// frontier N
				
				try{
					if(Session.map[_cell.x][_cell.y - 2] != null 
						&& (Session.map[_cell.x][_cell.y - 2].landOwner == null
							|| Session.map[_cell.x][_cell.y - 2].landOwner.playerUID != _cell.landOwner.playerUID))
					{
						var imageN:Image = new Image();
						imageN.x = Utils.getXPixel(_cell.x) - 13*scale;
						imageN.y = Utils.getYPixel(_cell.y) - 23*scale;
						imageN.source = ImageContainer.getImage(ImageContainer.FRONTIER_N);
						imageN.scaleX = scale;
						imageN.scaleY = scale;
						
						Session.board.mapLayer.addElement(imageN);
					}
				}
				catch(e:Error){}
				
				
				//----------------//
				// frontier NE
				
				
				try{
					if(Session.map[_cell.x + 1][_cell.y - 1] != null 
						&& (Session.map[_cell.x + 1][_cell.y - 1].landOwner == null
							|| Session.map[_cell.x + 1][_cell.y - 1].landOwner.playerUID != _cell.landOwner.playerUID))
					{
						var imageNE:Image = new Image();
						imageNE.x = Utils.getXPixel(_cell.x) - 13*scale;
						imageNE.y = Utils.getYPixel(_cell.y) - 23*scale;
						imageNE.source = ImageContainer.getImage(ImageContainer.FRONTIER_NE);
						imageNE.scaleX = scale;
						imageNE.scaleY = scale;
						
						Session.board.mapLayer.addElement(imageNE);
					}
				}
				catch(e:Error){}
				
			}
		}

		private function drawSouthFrontiers(_cell:Cell):void
		{
			if(_cell.landOwner != null)
			{
				//----------------//
				// frontier SO	
				
				try{
					if(Session.map[_cell.x - 1][_cell.y + 1] != null
						&& (Session.map[_cell.x - 1][_cell.y + 1].landOwner == null
							|| Session.map[_cell.x - 1][_cell.y + 1].landOwner.playerUID != _cell.landOwner.playerUID))
					{
						var imageSO:Image = new Image();
						imageSO.x = Utils.getXPixel(_cell.x) - 13*scale;
						imageSO.y = Utils.getYPixel(_cell.y) - 23*scale;
						imageSO.source = ImageContainer.getImage(ImageContainer.FRONTIER_SO);
						imageSO.scaleX = scale;
						imageSO.scaleY = scale;
						
						Session.board.mapLayer.addElement(imageSO);
					}
				}
				catch(e:Error){}
				
				//----------------//
				// frontier S
				
				try{
					if(Session.map[_cell.x][_cell.y + 2] != null 
						&& (Session.map[_cell.x][_cell.y + 2].landOwner == null
							|| Session.map[_cell.x][_cell.y + 2].landOwner.playerUID != _cell.landOwner.playerUID))
					{
						var imageS:Image = new Image();
						imageS.x = Utils.getXPixel(_cell.x) - 13*scale;
						imageS.y = Utils.getYPixel(_cell.y) - 23*scale;
						imageS.source = ImageContainer.getImage(ImageContainer.FRONTIER_S);
						imageS.scaleX = scale;
						imageS.scaleY = scale;
						
						Session.board.mapLayer.addElement(imageS);
					}
				}
				catch(e:Error){}

				//----------------//
				// frontier SE
				
				try{
					if(Session.map[_cell.x + 1][_cell.y + 1] != null 
						&& (Session.map[_cell.x + 1][_cell.y + 1].landOwner == null
							|| Session.map[_cell.x + 1][_cell.y + 1].landOwner.playerUID != _cell.landOwner.playerUID))
					{
						var imageSE:Image = new Image();
						imageSE.x = Utils.getXPixel(_cell.x) - 13*scale;
						imageSE.y = Utils.getYPixel(_cell.y) - 23*scale;
						imageSE.source = ImageContainer.getImage(ImageContainer.FRONTIER_SE);
						imageSE.scaleX = scale;
						imageSE.scaleY = scale;
						
						Session.board.mapLayer.addElement(imageSE);
					}
				}
				catch(e:Error){}
				
			}
		}
		
		
		//------------------------------------------------------------------------------------//
		
		private function drawLands(_cell:Cell):void
		{
			if(_cell.landOwner != null)
			{
				var image:Image = new Image();
				
				image.x = Utils.getXPixel(_cell.x);
				image.y = Utils.getYPixel(_cell.y);
				
				if(_cell.landOwner.playerUID == Session.player.playerUID)
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_VERT);

				else if(Session.player.ally != null && Utils.containsPlayer(Session.player.ally.players, _cell.landOwner))
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_BLEU);
				
				else
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_ROUGE);
				
				image.scaleX = scale;
				image.scaleY = scale;
				
				Session.board.mapLayer.addElement(image);
			}
		}
		
		//==================================================================================================//

		public function addMoveImages(move:com.uralys.tribes.entities.Move, xPreviousMove:int, yPreviousMove:int, displayProgress:Boolean):void
		{
			//------------------------------------------------------//

			var moveHighlight:MoveHighLight = new MoveHighLight();
			moveHighlight.x = Utils.getXPixel(Utils.getXFromCellUID(move.cellUID));
			moveHighlight.y = Utils.getYPixel(Utils.getYFromCellUID(move.cellUID));
			moveHighlight.image.scaleX = scale;
			moveHighlight.image.scaleY = scale;
			
			currentUnitMoveHighLights.addItem(moveHighlight);
			Session.board.highlighters.addElement(moveHighlight);
			
			//------------------------------------------------------//
			
			if(xPreviousMove >= 0){
				Session.board.highlighters.graphics.lineStyle(1,0x000000);
				Session.board.highlighters.graphics.beginFill(0x22ee22);
 
				GraphicsUtil.drawArrow(
					Session.board.highlighters.graphics,
					new Point((Utils.getXPixel(xPreviousMove)+Utils.getLandWidth()/2), (Utils.getYPixel(yPreviousMove)+Utils.getLandHeight()/2)),
					new Point((Utils.getXPixel(Utils.getXFromCellUID(move.cellUID))+Utils.getLandWidth()/2), (Utils.getYPixel(Utils.getYFromCellUID(move.cellUID))+Utils.getLandHeight()/2))
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
		
		private var currentBowsToShoot:ArrayCollection = new ArrayCollection();
		
		public function removeBowsToShoot():void
		{
			for each(var image:Image in currentBowsToShoot){
				Session.board.pawnLayer.removeElement(image);
			}
			
			currentBowsToShoot.removeAll();
		}

		public function drawBowsToShoot():void
		{
			currentBowsToShoot = new ArrayCollection();
			
			var cell:Cell = Session.CURRENT_CELL_SELECTED;
			
			var nbdraws:int = 0;
			
			nbdraws += tryToDrawBowCell(cell.x, cell.y + 2);
			nbdraws += tryToDrawBowCell(cell.x + 1, cell.y - 1);
			nbdraws += tryToDrawBowCell(cell.x - 1, cell.y - 1);
			nbdraws += tryToDrawBowCell(cell.x + 1, cell.y + 1);
			nbdraws += tryToDrawBowCell(cell.x - 1, cell.y + 1);
			nbdraws += tryToDrawBowCell(cell.x, cell.y - 2);
			
			if(nbdraws == 0)
				FlexGlobals.topLevelApplication.message(Translations.NO_TARGET.getItemAt(Session.LANGUAGE));
			
		}
		
		private function tryToDrawBowCell(x:int, y:int):int
		{
			trace("tryToDrawBowCell : " + x + " | " + y);
			
			try{
				var px:int = Utils.getXPixel(x);
				var py:int = Utils.getYPixel(y);
				
				if(Session.map[x][y].army || Session.map[x][y].caravan)
				{
					drawBowTarget(px, py);
					return 1;
				}
			}
			catch(e:Error){} // cell doesnt exist():void
				return 0;
		}
		
		private function drawBowTarget(x:int, y:int):void
		{
			trace("drawBowTarget : " + x + " | " + y);
			var image:Image = new Image();			
			image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_BOW_SHOOT);
			
			image.x = x;
			image.y = y;
			image.scaleX = scale;
			image.scaleY = scale;
			
			Session.board.pawnLayer.addElement(image);
			currentBowsToShoot.addItem(image);
		}
		
		//==================================================================================================//

		public function zoom():void
		{
			switch(scale)
			{
				case 0.25:
					scale = 0.5;
					applyZoom();
					break;

				case 0.5:
					scale = 1;
					applyZoom();
					break;

			}
		}

		public function unzoom():void
		{
			switch(scale)
			{
				case 1:
					scale = 0.5;
					applyZoom();
					break;
				
				case 0.5:
					scale = 0.25;
					applyZoom();
					break;
			}
		}

		private function applyZoom():void
		{
			GameManager.getInstance().loadCells(Session.CENTER_X, Session.CENTER_Y, false);
		}
	}
}