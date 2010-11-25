package com.uralys.tribes.events
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	public class AccountEvent extends Event
	{
		public static const REGISTER:String = "REGISTER";
		public static const LOGIN:String = "LOGIN";
		
		public var email:String;							
		public var password:String;
		
		public function AccountEvent(type:String, bubbles:Boolean = true, cancelable:Boolean = true){
			super(type, bubbles, cancelable);
		}
	}
}