package com.skooairs.utils
{

import com.skooairs.constants.Session;
import com.skooairs.constants.Numbers;

import mx.collections.ArrayCollection;

public class Utils
{
		
		// return 1-i or 0
		public static function random(i:int):int {
			if(i == 0)
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
		
/*
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
		
}
}