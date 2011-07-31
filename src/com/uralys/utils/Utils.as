
package com.uralys.utils
{

import com.uralys.tribes.commons.Numbers;

import flash.display.BitmapData;
import flash.display.Sprite;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

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
		
		public static function getXOnBoard(xForTheCell:int):int {
			return xForTheCell * (Numbers.LAND_WIDTH - Numbers.LAND_WIDTH/4);
		}

		public static function getYOnBoard(yForTheCell:int):int {
			return yForTheCell * (Numbers.LAND_HEIGHT - Numbers.LAND_HEIGHT/2);
		}

		//---------------------------------------------------------------------//
		
		public static function getXFromCaseUID(caseUID:String):int {
			
			var firstIndex:int = caseUID.indexOf("_");
			var secondIndex:int = caseUID.indexOf("_",firstIndex+1);
			var lengthBetweenIndexes:int = secondIndex - firstIndex;
			
			return parseInt(caseUID.substr(firstIndex+1, lengthBetweenIndexes));
		}
		
		public static function getYFromCaseUID(caseUID:String):int {
			
			var firstIndex:int = caseUID.indexOf("_");
			var secondIndex:int = caseUID.indexOf("_",firstIndex+1);
			
			return parseInt(caseUID.substr(secondIndex+1));
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
		
		
		
		/*
		* 0-404 * 0-404 = 27*27 groups
		* 15*15*27*27 = 405*405
		* 
		*/
		public static function getGroups(x:int, y:int):Array{
			
			var result:Array = [];
			
			// groupes de 15*15 sur un damier de 405*405
			// 27*27 groupes
			var groupX:int = x/15;
			var groupY:int = y/15;
			var group = groupX + groupY*27;
			
			// north : -1
			// south : +1
			// west : -1
			// east : +1
			var east_west:int = 1; // init : east
			var north_south:int = 1; // init : south
			var regionOnTheGroup:int;
			
			if(x - groupX*15 < 8){
				east_west = -1; // west
			}

			if(y - groupY*15 < 8){
				north_south = -1; // north
			}
			
			result = [group, 
					  group + east_west, 
					  group + (27*north_south), 
					  group + (27*north_south) + east_west]; 
			
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
}
}