
package com.uralys.utils
{

import com.uralys.tribes.commons.Numbers;
import com.uralys.tribes.commons.Session;
import com.uralys.tribes.core.BoardDrawer;
import com.uralys.tribes.core.UnitMover;
import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Message;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

import flash.display.BitmapData;
import flash.display.Sprite;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;
import mx.utils.ObjectUtil;

public class Utils
{
		
		// return 1-i or 0
		public static function random(i:int):int {
			if(i <= 0)
				return 0;
			
			var rand:int = Math.round(Math.random()*i);		
			if(rand > 0)
				return rand;
			else
				return random(i);
		}
	
		public static function isOdd(num:Number):Boolean {
			var odd:Boolean;
    		num % 2 ? odd = true : odd = false;
   			return odd;
		}

		public static function isInt(entry:String):Boolean {
			var intExpression:RegExp = /^\d+$/;
    		return intExpression.test(entry);
		}
		
		public static function isValidEmail(email:String):Boolean {
    		var emailExpression:RegExp = /^[a-z][\w.-]+@\w[\w.-]+\.[\w.-]*[a-z][a-z]$/i;
    		return emailExpression.test(email);
		}
		
		public static function round(n:Number, p:int):Number {
		   return Math.round( n * Math.pow(10, p)) / Math.pow(10, p);
		}

		//---------------------------------------------------------------------//
		// fonction pour recuperer les coordonnees sur le mappositionner lorsquon connait les coordonnees de la case.
		
		public static function getXPixel(xCoordinate:int):int {
			return xCoordinate * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4) * BoardDrawer.getInstance().scale;
		}

		public static function getYPixel(yCoordinate:int):int {
			return yCoordinate * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2) * BoardDrawer.getInstance().scale;
		}

		//---------------------------------------------------------------------//
		// fonctions inverses
		
		public static function getXCoordinate(xPixel:int):int {
			return xPixel / (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4) / BoardDrawer.getInstance().scale;
		}

		public static function getYCoordinate(yPixel:int):int {
			return yPixel / (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2) / BoardDrawer.getInstance().scale;
		}
		
		//---------------------------------------------------------------------//

		public static function getLandWidth():int {
			return Numbers.LAND_WIDTH * BoardDrawer.getInstance().scale;
		}

		public static function getLandHeight():int {
			return Numbers.LAND_HEIGHT * BoardDrawer.getInstance().scale;
		}

		//---------------------------------------------------------------------//
		
		public static function getXFromCellUID(cellUID:String):int {
			
			var firstIndex:int = cellUID.indexOf("_");
			var secondIndex:int = cellUID.indexOf("_",firstIndex+1);
			var lengthBetweenIndexes:int = secondIndex - firstIndex;
			
			return parseInt(cellUID.substr(firstIndex+1, lengthBetweenIndexes));
		}
		
		public static function getYFromCellUID(cellUID:String):int {
			
			var firstIndex:int = cellUID.indexOf("_");
			var secondIndex:int = cellUID.indexOf("_",firstIndex+1);
			
			return parseInt(cellUID.substr(secondIndex+1));
		}
		
		
		//---------------------------------------------------------------------//

		/*
		public static function getPositionName(p:int, gender:int):String {
			
			//gender : 1 male 
			//         2 female
			
			if(Session.LANGUAGE == 0){
				if(p == 1)
					return gender == 1 ? "er" : "ère";
				else	
					return "ème";
			}
			else if(Session.LANGUAGE == 1 || Session.LANGUAGE == 2){
				switch(p){
					case 1:
					case 21:
					case 31:
					case 41:
					case 51:
					case 61:
					case 71:
					case 81:
					case 91:
					case 101:
						return "st";
						break;
					case 2:
					case 22:
					case 32:
					case 42:
					case 52:
					case 62:
					case 72:
					case 82:
					case 92:
					case 102:
						return "nd";
						break;
					case 3:
					case 23:
					case 33:
					case 43:
					case 53:
					case 63:
					case 73:
					case 83:
					case 93:
					case 103:
						return "rd";
						break;
					default:
						return "th";
				}
				
			}
			
			//never
			return "";
		}

		public static function getPlayerFirstName(playerUID:String):String{
			
			if(Session.fbook == null)
				return playerUID;
			
			if(playerUID == Session.player.playerUID)
				return Session.user.first_name;
				
			for each(var friend:FacebookUser in Session.friends){
				if(friend.uid == playerUID)
					return friend.first_name;
			}
			
			// for someone you dont know
			return "";
		}
		
*/

		public static function wasAtLeastYesterday(time:Number):Boolean {

           var limit:Date = new Date();

           // set to 00h00:00
           limit.hours = 0 ;
           limit.minutes = 0 ;
           limit.seconds = 0 ;

               
           return time < limit.getTime();
		}
		
		
		
		/**
		 * 0-404 * 0-404 = 27*27 groups
		 * 15*15*27*27 = 405*405
		 * 
		 * scale 2    : 1 group
		 * scale 1    : 9 groups
		 * scale 0.5  : 25 groups
		 * scale 0.25 : 49 groups
		 * 
		 */
		public static function getGroups(x:int, y:int):Array
		{
			var result:Array = [];
			
			// groupes de 15*15 sur un damier de 405*405
			// 27*27 groupes
			var groupX:int = x/15;
			var groupY:int = y/15;
			var group = groupX + groupY*27;
			
			trace("group : " + group);
			
			var offset:int = 0;
			
			switch(BoardDrawer.getInstance().scale)
			{
				case 2 :
					offset = 0;
					break;
				case 1 :
					offset = 1;
					break;
				case 0.5 :
					offset = 2;
					break;
				case 0.25 :
					offset = 2;
					break;
			}

			trace("offset : " + offset);
			
			for(var i:int = -offset; i <= offset ; i++){
				for(var j:int = -offset; j <= offset ; j++){
					result.push(group + i + (27*j));
				}
			}
			
			return result;
		}
		
		
		/**
		 * Creates a sprite object out of bitmapData.
		 * The returned sprite can will cover all non transparent areas of the
		 * given picture.
		 * 
		 * @param bitmapData:BirmapData - The BitmapData for the sprite creation
		 * 
		 * @param grainSize:uint - Defines how accurate the sprite will get drawn. 
		 * A high grainSize will result in a better performance (especially when 
		 * dealing with large images).
		 * 
		 * @return Sprite - a sprite object that covers all non-transparent areas of
		 * the given bitmapData
		 * 
		 * */                  
		public static function createHitArea(bitmapData:BitmapData, grainSize:uint=1):Sprite{
			var _hitarea:Sprite = new Sprite();
			_hitarea.graphics.beginFill(0x000000, 1.0);            
			for(var x:uint=0;x<bitmapData.width;x+=grainSize) {
				for(var y:uint=grainSize;y<bitmapData.height;y+=grainSize) {                    
					if(x<=bitmapData.width && y<=bitmapData.height && bitmapData.getPixel(x,y)!=0) {
						_hitarea.graphics.drawRect(x,y,grainSize,grainSize);                        
					}                    
				}
			}            
			_hitarea.graphics.endFill();                        
			return _hitarea;
		}
		
		
		/*
			To sort an ArrayCollection
		*/
		public static function sort(collection:ArrayCollection, field:String, descending:Boolean = true):ArrayCollection
		{
			/* Select on which field the list will be sorted on */
			var dataSortField:SortField = new SortField();
			dataSortField.name = field;
			dataSortField.descending = descending;
			
			/* Create the Sort object and add the SortField object created earlier to the array of fields to sort on. */
			var labelSort:Sort = new Sort();
			labelSort.fields = [dataSortField];
			
			/* Set the ArrayCollection object's sort property to our custom sort, and refresh the ArrayCollection. */
			collection.sort = labelSort;
			collection.refresh();
			
			return collection;
		}
		
		//----------------------------------------------------------------------------------------------------
		
		public static function getStockName(stockUID:String):String
		{
			return stockUID.substr(stockUID.indexOf("__")+1); 
		}

		public static function getStockItem(stockUID:String):String
		{
			var stockName:String = getStockName(stockUID);
			var start:int = stockName.indexOf("_")+1;
			var end:int = stockName.indexOf("_",2);
			trace(start);
			trace(end);
			trace("getStockItem " + stockName + " " + stockName.substring(start, end));
			return stockName.substring(start, end);
		}

		//----------------------------------------------------------------------------------------------------
		
		public static function containsPlayer(collection:ArrayCollection, player:Player):Boolean
		{
			for each(var playerInSource:Player in collection){
				if(player.playerUID == playerInSource.playerUID)
					return true;
			}
			
			return false;
		}

		
		//----------------------------------------------------------------------------------------------------
		
		public static function isClassicMessage(message:Message):Boolean
		{
			return !isReportMessage(message) && !isInviteMessage(message);
		}

		public static function isInviteMessage(message:Message):Boolean
		{
			return !isReportMessage(message) && message.content.indexOf("____allyInvitation") != -1;
		}

		public static function isReportMessage(message:Message):Boolean
		{
			return message.report != null; 
		}

		//----------------------------------------------------------------------------------------------------

		public static function createDummyAlly(content:String):Ally{
			return new Ally(content);
		}
		
		
		public static function getItem(itemName:String):Item
		{
			for each(var item:Item in Session.ITEMS){
				if(item.name == itemName)
					return item;
			}
			
			// wheat - wood - iron 
			return new Item();
		}
		
		public static function getCellInSession(cellUID:String):Cell
		{
			return Session.map[getXFromCellUID(cellUID)][getYFromCellUID(cellUID)];
		}
}
}