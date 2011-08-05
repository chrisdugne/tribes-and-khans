package com.uralys.tribes.core
{
	import com.dncompute.graphics.GraphicsUtil;
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
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
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.utils.Timer;
	
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

		[Bindable] public var scale:Number = 1;
		
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

		/**
		 * Lorsque les cases sont chargees, on refresh tout les display
		 */ 
		private var timer:Timer = new Timer(1000,1);
		public function refreshDisplay(event:TimerEvent = null):void
		{
			Session.board.mapPositioner.removeAllElements();
			Session.board.pawnLayer.removeAllElements();
			Session.board.landLayer.removeAllElements();
			
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
			
			// draw city grounds
			for(var j:int=0; j < Session.nbTilesByEdge; j++)
			{
				for(var i:int=0; i < Session.nbTilesByEdge; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.type == 1)
						drawCase(_case); 
				}
			}
			
			for(var j:int=0; j < Session.nbTilesByEdge; j++)
			{
				for(var i:int=0; i < Session.nbTilesByEdge; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.type == 1)
						drawCity(_case.city);
				}

				// draw forests grounds
				for(var i:int=0; i < Session.nbTilesByEdge; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.type == 0){
						drawCase(_case);
					}
				}
				
			}

			for(var j:int=0; j < Session.nbTilesByEdge; j++)
			{
				// draw units
				for(var i:int=0; i < Session.nbTilesByEdge; i++)
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
				
			for(var j:int=0; j < Session.nbTilesByEdge; j++)
			{
				// draw lands
				for(var i:int=0; i < Session.nbTilesByEdge; i++)
				{
					var _case:Case = Session.map[Session.firstCaseX+i][Session.firstCaseY+j];
					
					if(_case.caseUID.indexOf("case_") == -1)
						continue;
					
					
					drawLandAndBounds(_case);
				}
			}
		}

			
		private function drawCase(_case:Case):void
		{
			var image:Image = new Image();
			var placeForet:Boolean = false;
			
			switch(_case.type){
				case 0:
					// forest
					placeForet = true;
					image.source = ImageContainer.FORET7;

//					switch(Utils.random(6)){
//						case 1:
//							image.source = ImageContainer.FORET7;
//							break;
//						case 2:
//							image.source = ImageContainer.FORET7;
//							break;
//						case 3:
//							image.source = ImageContainer.FORET7;
//							break;
//						case 4:
//							image.source = ImageContainer.FORET7;
//							break;
//						case 5:
//							image.source = ImageContainer.FORET7;
//							break;
//						default:
//							image.source = ImageContainer.FORET7;
//							break;
//						
//					}

					break;
				case 1:
					//city
					image.source = ImageContainer.SOL_VILLE;
					break;
			}
			
			image.x = Utils.getXPixel(_case.x);
			image.y = Utils.getYPixel(_case.y);
			image.scaleX = scale;
			image.scaleY = scale;
			
			// decalage des tuiles forets
			if(placeForet){
				image.x -= 10 * scale;	
				image.y -= 9 * scale;	
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
			
			if(_case.army){
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
			else if(_case.merchants){
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
			
			_case.pawn.x = (Utils.getXPixel(_case.x) + 15*scale);
			_case.pawn.y = (Utils.getYPixel(_case.y) - 10*scale);
			imageUnit.scaleX = scale;
			imageUnit.scaleY = scale;
			imageUnit.mouseEnabled = false;
			
			_case.pawn.addElement(imageUnit);
			
			Session.board.pawnLayer.addElement(_case.pawn);
		}
		
		public function drawCity(city:City):void
		{
			var image:Image = new Image();
			image.source = ImageContainer.getImage(ImageContainer.VILLE);
			
			var cityPx:int = Utils.getXPixel(city.x);
			var cityPy:int = Utils.getYPixel(city.y);
			
			image.x = cityPx;
			image.y = cityPy;
			image.scaleX = scale;
			image.scaleY = scale;
			
			Session.board.pawnLayer.addElement(image);
		}
		
		//------------------------------------------------------------------------------------//
		
		private function drawLandAndBounds(_case:Case):void
		{
			if(_case.landOwner != null)
			{
				var image:Image = new Image();
				
				image.x = Utils.getXPixel(_case.x);
				image.y = Utils.getYPixel(_case.y);
				
				if(_case.landOwner.playerUID == Session.player.playerUID)
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_VERT);
				else
					image.source = ImageContainer.getImage(ImageContainer.HIGHLIGHT_ROUGE);
				
				image.scaleX = scale;
				image.scaleY = scale;
				
				Session.board.landLayer.addElement(image);
				
				//----------------//
				// frontier NO	
				
				try{
					if(Session.map[_case.x - 1][_case.y - 1] != null 
					&& (Session.map[_case.x - 1][_case.y - 1].landOwner == null
					|| Session.map[_case.x - 1][_case.y - 1].landOwner.playerUID != _case.landOwner.playerUID))
					{
						var imageNO:Image = new Image();
						imageNO.x = Utils.getXPixel(_case.x);
						imageNO.y = Utils.getYPixel(_case.y);
						imageNO.source = ImageContainer.getImage(ImageContainer.FRONTIER_NO);
						imageNO.scaleX = scale;
						imageNO.scaleY = scale;
						
						Session.board.landLayer.addElement(imageNO);
					}
				}
				catch(e:Error){}
				
				
				//----------------//
				// frontier SO	
				
				try{
					if(Session.map[_case.x - 1][_case.y + 1] != null
					&& (Session.map[_case.x - 1][_case.y + 1].landOwner == null
					|| Session.map[_case.x - 1][_case.y + 1].landOwner.playerUID != _case.landOwner.playerUID))
					{
						var imageSO:Image = new Image();
						imageSO.x = Utils.getXPixel(_case.x);
						imageSO.y = Utils.getYPixel(_case.y);
						imageSO.source = ImageContainer.getImage(ImageContainer.FRONTIER_SO);
						imageSO.scaleX = scale;
						imageSO.scaleY = scale;
						
						Session.board.landLayer.addElement(imageSO);
					}
				}
				catch(e:Error){}
					
				//----------------//
				// frontier N
				
				try{
					if(Session.map[_case.x][_case.y - 2] != null 
					&& (Session.map[_case.x][_case.y - 2].landOwner == null
					|| Session.map[_case.x][_case.y - 2].landOwner.playerUID != _case.landOwner.playerUID))
					{
						var imageN:Image = new Image();
						imageN.x = Utils.getXPixel(_case.x);
						imageN.y = Utils.getYPixel(_case.y);
						imageN.source = ImageContainer.getImage(ImageContainer.FRONTIER_N);
						imageN.scaleX = scale;
						imageN.scaleY = scale;
						
						Session.board.landLayer.addElement(imageN);
					}
				}
				catch(e:Error){}
				
				//----------------//
				// frontier S
				
				try{
					if(Session.map[_case.x][_case.y + 2] != null 
					&& (Session.map[_case.x][_case.y + 2].landOwner == null
					|| Session.map[_case.x][_case.y + 2].landOwner.playerUID != _case.landOwner.playerUID))
					{
						var imageS:Image = new Image();
						imageS.x = Utils.getXPixel(_case.x);
						imageS.y = Utils.getYPixel(_case.y);
						imageS.source = ImageContainer.getImage(ImageContainer.FRONTIER_S);
						imageS.scaleX = scale;
						imageS.scaleY = scale;
						
						Session.board.landLayer.addElement(imageS);
					}
				}
				catch(e:Error){}
				
				//----------------//
				// frontier NE
				
				
				try{
					if(Session.map[_case.x + 1][_case.y - 1] != null 
					&& (Session.map[_case.x + 1][_case.y - 1].landOwner == null
						|| Session.map[_case.x + 1][_case.y - 1].landOwner.playerUID != _case.landOwner.playerUID))
					{
						var imageNE:Image = new Image();
						imageNE.x = Utils.getXPixel(_case.x);
						imageNE.y = Utils.getYPixel(_case.y);
						imageNE.source = ImageContainer.getImage(ImageContainer.FRONTIER_NE);
						imageNE.scaleX = scale;
						imageNE.scaleY = scale;
						
						Session.board.landLayer.addElement(imageNE);
					}
				}
				catch(e:Error){}
				
				//----------------//
				// frontier SE
				
					
				try{
					if(Session.map[_case.x + 1][_case.y + 1] != null 
					&& (Session.map[_case.x + 1][_case.y + 1].landOwner == null
					|| Session.map[_case.x + 1][_case.y + 1].landOwner.playerUID != _case.landOwner.playerUID))
					{
						var imageSE:Image = new Image();
						imageSE.x = Utils.getXPixel(_case.x);
						imageSE.y = Utils.getYPixel(_case.y);
						imageSE.source = ImageContainer.getImage(ImageContainer.FRONTIER_SE);
						imageSE.scaleX = scale;
						imageSE.scaleY = scale;
						
						Session.board.landLayer.addElement(imageSE);
					}
				}
				catch(e:Error){}
				
			}
		}
		
		//==================================================================================================//
		// click et roll-over sur les tuiles-images
		
		protected function tileIsCLicked(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().clickOnCase(event.currentTarget.data as Case);
		}
		
		protected function tileIsRolledOn(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().rollOnCase(event.currentTarget.data as Case);
		}

		protected function cityIsRolledOn(event:MouseEvent):void{
			BoardClickAnalyser.getInstance().rollOnCity(event.currentTarget.data as City);
		}
		
		//==================================================================================================//

		public function addMoveImages(move:com.uralys.tribes.entities.Move, xPreviousMove:int, yPreviousMove:int, displayProgress:Boolean):void
		{
			//------------------------------------------------------//

			var moveHighlight:MoveHighLight = new MoveHighLight();
			moveHighlight.x = Utils.getXPixel(Utils.getXFromCaseUID(move.caseUID));
			moveHighlight.y = Utils.getYPixel(Utils.getYFromCaseUID(move.caseUID));
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
					new Point((Utils.getXPixel(Utils.getXFromCaseUID(move.caseUID))+Utils.getLandWidth()/2), (Utils.getYPixel(Utils.getYFromCaseUID(move.caseUID))+Utils.getLandHeight()/2))
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

				case 1:
					scale = 2;
					applyZoom();
					break;
				
			}
		}

		public function unzoom():void
		{
			switch(scale)
			{
				case 2:
					scale = 1;
					applyZoom();
					break;
				
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
			GameManager.getInstance().loadCases(Session.CENTER_X, Session.CENTER_Y, false);
			Session.board.placeBoard(Session.CENTER_X, Session.CENTER_Y);
		}
	}
}