package com.uralys.tribes.core
{

import com.uralys.tribes.commons.Session;

import flash.events.Event;
import flash.media.Sound;
import flash.media.SoundChannel;
import flash.media.SoundTransform;
import flash.net.URLRequest;

import mx.core.FlexGlobals;

/**
The MusicPlayer manages the sounds
*/
public class MusicPlayer
{		
		//=====================================================================================//

		private static var instance : MusicPlayer = new MusicPlayer(); 
	
		public static function getInstance():MusicPlayer{
			return instance;
		}

		//=====================================================================================//
		
		public function MusicPlayer(){}

		//=====================================================================================//

		[Bindable]
		public static var volume:Number = 1;
		
		private var volumeRecorded:Number;
		private var music:Sound;
		private var channel:SoundChannel;
		private var soundTransform:SoundTransform = new SoundTransform();
		
		public function initMusic():void{
			if(!music){
				var request:URLRequest = new URLRequest("webresources/music/music1.mp3");
	            music = new Sound();
	            music.load(request);
				
				//volume = Session.player.musicOn ? 1 : 0;
				volume = 1
				playMusic();	
			}
			else{
				if(volume == 1 && !Session.player.musicOn)
					switchState();
			}
		}

		public function switchState():void{
			if(volume > 0){
				volume = 0;
				soundTransform.volume = 0;
				channel.soundTransform = soundTransform;
				
				if(Session.LOGGED_IN)
					Session.player.musicOn = false;
			}
			else{
				volume = volumeRecorded;
				soundTransform.volume = volumeRecorded;
				channel.soundTransform = soundTransform;	
				
				if(Session.LOGGED_IN)
					Session.player.musicOn = true;
			}
		}

		public function playMusic():void
		{	
			soundTransform.volume = volumeRecorded;
			
			channel = music.play();
			channel.soundTransform = soundTransform;
			
			channel.addEventListener(Event.SOUND_COMPLETE, nextMusic);
		}

		private function nextMusic(event:Event):void{			
			playMusic();
		}

		public function changeVolume():void
		{
			var pourcentageValue:int = FlexGlobals.topLevelApplication.volumeSlider.value;
			
			volumeRecorded = pourcentageValue/100;
			if(volume > 0){
				volume = pourcentageValue/100;
				soundTransform.volume = volume;
				channel.soundTransform = soundTransform;
			}
		}
}


}