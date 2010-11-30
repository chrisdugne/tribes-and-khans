package com.uralys.tribes.events
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	public class GameEvent extends Event
	{
		public static const GET_GAMES:String = "GameEvent.GET_GAMES";
		
		public var playerUID:String;
		public var gameName:String;
		
		public function GameEvent(type:String, bubbles:Boolean = true, cancelable:Boolean = true){
			super(type, bubbles, cancelable);
		}
	}
}