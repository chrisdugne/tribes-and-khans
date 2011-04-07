package com.uralys.tribes.core
{

import com.uralys.tribes.commons.Numbers;
import com.uralys.tribes.commons.Session;
import com.uralys.tribes.commons.Translations;
import com.uralys.tribes.managers.GameManager;

import flash.events.Event;

import mx.core.FlexGlobals;

import spark.components.Group;
		
public class CronListener
{
		//  - instance ================================================================================

		private static var instance : CronListener = new CronListener(); 
		public static function getInstance():CronListener{
			return instance;
		}
	
		// -  ================================================================================

		private static var loop:int = 0;
				
		public function CronListener(){}

		public function init(window:Group):void{
			window.addEventListener(Event.ENTER_FRAME, cronLoop);
		}
		
		/*
			1 sec = 24 frames
		*/
		private static var FPS:int = 24;
		
		private function cronLoop(e:Event):void{
			loop ++;

			if(loop%(FPS*60*Numbers.TIME_PER_STEP) == 0){

				GameManager.getInstance().saveStep();
				
				// message 'resources recues'
				FlexGlobals.topLevelApplication.message(Translations.RESOURCES_UPDATED.getItemAt(Session.LANGUAGE), 3);
			}
		}
				
}
}