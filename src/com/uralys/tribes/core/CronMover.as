package com.uralys.tribes.core
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.entities.Move;
	import com.uralys.utils.Map;
	
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.utils.Timer;

	public class CronMover
	{
		public function CronMover(){}
		
		//  - instance ================================================================================
		
		private static var instance : CronMover = new CronMover(); 
		
		public static function getInstance():CronMover{
			return instance;
		}
		
		// ============================================================================================

		private static var loop:Number = 0;
		private static var timers:Map = new Map();
		
		// ============================================================================================
		
		public function addTimer(move:Move):void{
			
			for each (var moveListened:Move in timers.values()){
				if(move.moveUID == moveListened.moveUID){
					trace(move.moveUID + " est deja ecouté");
					return;
				}
			}
			
			trace("move.timeTo " + move.timeTo);
			var timeToWait:Number = move.timeTo - new Date().getTime();
			trace("timeToWait " + timeToWait);
			var t:Timer = new Timer(timeToWait, 1);
			t.addEventListener(TimerEvent.TIMER_COMPLETE, timerComplete);
			t.start();
			
			timers.put(t, move);
		}
		
		// ============================================================================================
		
		private function timerComplete(e:TimerEvent):void{
			trace("-----");
			trace("un move se produit !")
			
			var moveToPerform:Move = timers.get(e.currentTarget) as Move;
			trace(moveToPerform.moveUID);
			
			timers.remove(e.currentTarget);
			
			trace("-----");
		}

		// ============================================================================================
	}
}