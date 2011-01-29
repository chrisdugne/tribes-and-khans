package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Game;
	import com.uralys.tribes.entities.City;
	import com.uralys.tribes.entities.Player;
	
	import flash.display.Sprite;
	
	import mx.core.UIComponent;
	
	import spark.components.Group;
	import spark.components.Label;

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
	
		private static var instance : BoardDrawer = new BoardDrawer();
	
		public static function getInstance():BoardDrawer{
			return instance;
		}

		public function BoardDrawer(){}

		//=====================================================================================//
		
		public function redrawAllEntities(game:Game, board:Group):void{
			
			for each (var player:Player in game.players){
				for each (var city:City in player.cities){
					
					//------------------------------------------------------------//
					
					if(Session.DRAW_DETAILS){
						var circle:Sprite;
						circle = new Sprite(); 
						
						circle.graphics.beginFill(Numbers.BLUE, 0.5);
						circle.graphics.drawCircle(city.x, city.y, city.radius);
						circle.graphics.endFill();
						
						var c:UIComponent = new UIComponent();
						c.addChild(circle);
						c.toolTip = city.name;
						
						board.addElement(c);
					}

					//------------------------------------------------------------//

					if(Session.DRAW_TEXTS){
						var name:Label = new Label();
						name.text = city.name;
						name.x = city.x - city.radius - 20; 
						name.y = city.y - city.radius - 20; 
						
						board.addElement(name);
					}

					//------------------------------------------------------------//
					
					if(Session.DRAW_IMAGES){
						
					}
				}
			}
		}

		//==================================================================================================//
	}
}