package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	import com.uralys.tribes.entities.Cell;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Player;
	import com.uralys.tribes.entities.Unit;
	import com.uralys.tribes.managers.GameManager;
	import com.uralys.tribes.pages.Board;
	import com.uralys.utils.Map;
	import com.uralys.utils.Utils;
	
	import flash.events.MouseEvent;
	import flash.ui.Mouse;
	
	import mx.core.FlexGlobals;
	import mx.events.ItemClickEvent;
	import mx.utils.ObjectUtil;
	
	import spark.components.Group;

	/**
		BoardClickListener
	*/
	public class BoardClickAnalyser
	{	
		//=====================================================================================//
		
		private static var instance : BoardClickAnalyser = new BoardClickAnalyser();
	
		public static function getInstance():BoardClickAnalyser{
			return instance;
		}

		public function BoardClickAnalyser(){}

		//=====================================================================================//
		
		public function clickOnCell(_cell:Cell):void
		{
			if(_cell.city != null 
			&& _cell.city.cityUID != "new")
				clickOnCity(_cell.city);
		}
		
		
		private function clickOnCity(city:City):void
		{
			if(Session.MOVE_A_UNIT)
				return;
			
			Session.board.showEnterCity(city);
		}
			
		//==================================================================================================//
		// 		map scrolling
		
		public static var isDragging:Boolean = false;
		private var mayDrag:Boolean = false;
		private var draggingImageXFrom:int;
		private var draggingImageYFrom:int;
		
		public function mouseDown(event:MouseEvent):void
		{
			mayDrag = true;
			draggingImageXFrom = event.currentTarget.mouseX; // le x de la souris sur le mapContainer
			draggingImageYFrom = event.currentTarget.mouseY; // le y de la souris sur le mapContainer
			
			if(Session.MOVE_A_UNIT){
				// manipulation d'une unite
			}
		}
		
		public function mouseMove(event:MouseEvent):void
		{
			Session.COORDINATE_X = Utils.getXCoordinate(event.currentTarget.mouseX);
			Session.COORDINATE_Y = Utils.getYCoordinate(event.currentTarget.mouseY);		
			
			if(Math.abs(Session.COORDINATE_X - Session.COORDINATE_Y)%2 !=0){
				Session.COORDINATE_Y--;
			}
			
			if(mayDrag)
				isDragging = true;
			
			if(Session.MOVE_A_UNIT){
				// manipulation d'une unite
			}
			
			if(isDragging)
			{
				// on decale l'image du nombre de pixels du dernier mouvement
				// donc elle suit bien la souris et event.localXY a bien lieu au meme endroit sur l'image apres chaque mouseMove
				Session.board.mapPositioner.x += (event.currentTarget.mouseX - draggingImageXFrom);
				Session.board.mapPositioner.y += (event.currentTarget.mouseY - draggingImageYFrom);
				
				Session.CENTER_X = Utils.getXCoordinate(Session.MAP_WIDTH/2 - Session.board.mapPositioner.x);
				Session.CENTER_Y = Utils.getYCoordinate(Session.MAP_HEIGHT/2 - Session.board.mapPositioner.y);
				
				if(Math.abs(Session.CENTER_X-Session.CENTER_Y)%2 !=0){
					// la difference entre x et y n'est pas paire
					// c'est une 'non case' : un hexagone intermediaire qui ne fait pas partie du plateau
					// on prend l'hexagone du dessus
					Session.CENTER_Y -= 1;
				}
			}
		}
		
		public function simulateClick():void{
			mouseUp();
		}
		
		public function mouseUp(event:MouseEvent = null):void
		{
			mayDrag = false;
			
			//Session.board.rightPanel.cellContent.enterCityForm.visible = false;
			//Session.board.rightPanel.cellContent.enterCityForm.includeInLayout = false;

			if(Session.MOVE_A_UNIT){
				UnitMover.getInstance().recordMove();
			}
			else if(Session.SHOOTING){
				shoot();
			}
			else{
				try{
					Session.CURRENT_SELECTION_X = Session.COORDINATE_X;
					Session.CURRENT_SELECTION_Y = Session.COORDINATE_Y;
					
					Session.CURRENT_CELL_SELECTED = Session.map[Session.CURRENT_SELECTION_X][Session.CURRENT_SELECTION_Y];
					clickOnCell(Session.CURRENT_CELL_SELECTED);
					
					GameManager.getInstance().reloadCurrentCells();
				}
				catch(e:Error){
					// au chargement Session.map peut etre null, et la souris etre sur une image de bord
				}
			}
			
			isDragging = false;
			Mouse.show();
		}
		
		//---------------------------------------------------------------------------------------------------//
		
		private function shoot():void
		{
			var shootInAGoodCell:Boolean = false;
			
			if(Session.COORDINATE_X == Session.CURRENT_CELL_SELECTED.x
				&& Session.COORDINATE_Y == Session.CURRENT_CELL_SELECTED.y - 2)
				shootInAGoodCell = true;
			
			if(Session.COORDINATE_X == Session.CURRENT_CELL_SELECTED.x
				&& Session.COORDINATE_Y == Session.CURRENT_CELL_SELECTED.y + 2)
				shootInAGoodCell = true;
			
			if(Session.COORDINATE_X == Session.CURRENT_CELL_SELECTED.x + 1
				&& Session.COORDINATE_Y == Session.CURRENT_CELL_SELECTED.y - 1)
				shootInAGoodCell = true;
			
			if(Session.COORDINATE_X == Session.CURRENT_CELL_SELECTED.x - 1
				&& Session.COORDINATE_Y == Session.CURRENT_CELL_SELECTED.y - 1)
				shootInAGoodCell = true;
			
			if(Session.COORDINATE_X == Session.CURRENT_CELL_SELECTED.x - 1
				&& Session.COORDINATE_Y == Session.CURRENT_CELL_SELECTED.y + 1)
				shootInAGoodCell = true;
			
			if(Session.COORDINATE_X == Session.CURRENT_CELL_SELECTED.x + 1
				&& Session.COORDINATE_Y == Session.CURRENT_CELL_SELECTED.y + 1)
				shootInAGoodCell = true;
			
			if(!shootInAGoodCell)
				FlexGlobals.topLevelApplication.message(Translations.WRONG_SHOOT.getItemAt(Session.LANGUAGE));
			else{
				UnitMover.getInstance().shoot();
			}
		}		
		
		//---------------------------------------------------------------------------------------------------//
		// click sur une ville dans la barre laterale
		public function onCityClick(event:ItemClickEvent):void
		{
			var city:City = event.currentTarget.selectedItem;
			
			Session.board.placeBoard(city.x, city.y);
			var cellBeingLoaded:Boolean = GameManager.getInstance().reloadCurrentCells();
			
			if(!cellBeingLoaded){
				clickOnCell(Session.map[city.x][city.y]);
			}
			
			return;
		}
		
		// click sur une unité dans la barre laterale
		public function onMyUnitClick(event:ItemClickEvent):void
		{
			var unit:Unit = event.currentTarget.selectedItem;
			unit.currentCaseUID = (unit.moves.getItemAt(0) as com.uralys.tribes.entities.Move).cellUID;
			
			Session.board.placeBoard(Utils.getXFromCellUID(unit.currentCaseUID), Utils.getYFromCellUID(unit.currentCaseUID));
			var caseBeingLoaded:Boolean = GameManager.getInstance().reloadCurrentCells();
			
			if(!caseBeingLoaded){
				clickOnCell(Session.map[Utils.getXFromCellUID(unit.currentCaseUID)][Utils.getYFromCellUID(unit.currentCaseUID)]);
			}
			
			return;
		}
	}
}