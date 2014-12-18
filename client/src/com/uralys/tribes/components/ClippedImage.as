package com.uralys.tribes.components 
{
	import flash.events.Event;
	import flash.geom.Rectangle;
	
	import mx.controls.Image;
	import mx.core.UIComponent;
	
	//--------------------------------------
	//  Events
	//--------------------------------------
	
	[Event(name="complete", type="flash.events.Event")]
	
	public class ClippedImage extends UIComponent {
		
		//--------------------------------------------------------------------------
		//
		//  Constructor
		//
		//--------------------------------------------------------------------------
		
		public function ClippedImage() {
			image = new Image();
			image.addEventListener(Event.COMPLETE,image_completeHandler);
			addChild(image);
		}
		
		//--------------------------------------------------------------------------
		//
		//  Variables
		//
		//--------------------------------------------------------------------------
		
		private var image:Image;
		
		//--------------------------------------------------------------------------
		//
		//  Properties
		//
		//--------------------------------------------------------------------------
		
		//----------------------------------
		//  source
		//----------------------------------
		
		public function get source():Object {
			return image.source;
		}
		
		public function set source(value:Object):void {
			image.source = value;
		}
		
		//--------------------------------------------------------------------------
		//
		//  Overridden methods
		//
		//--------------------------------------------------------------------------
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var imageMeasuredWidth:Number = image.measuredWidth;
			var imageMeasuredHeight:Number = image.measuredHeight;
			var imageAspectRatio:Number = imageMeasuredWidth / imageMeasuredHeight;
			
			var imageWidth:Number;
			var imageHeight:Number;
			
			// try setting image according to width first
			imageWidth = unscaledWidth;
			imageHeight = imageWidth / imageAspectRatio;
			
			// if a gap exists vertically, set image according to height
			if (imageHeight < unscaledHeight) {
				imageHeight = unscaledHeight;
				imageWidth = imageAspectRatio * imageHeight;
			}
			
			image.setLayoutBoundsSize(imageWidth, imageHeight);
			
			// set image x and y
			var imagex:Number = (unscaledWidth - imageWidth) / 2;
			var imagey:Number = (unscaledHeight - imageHeight) / 2;
			
			image.setLayoutBoundsPosition(imagex, imagey);
			
			scrollRect = new Rectangle(0, 0, unscaledWidth, unscaledHeight);
		}
		
		//--------------------------------------------------------------------------
		//
		//  Event handlers
		//
		//--------------------------------------------------------------------------
		
		private function image_completeHandler(event:Event):void {
			dispatchEvent(event);
		}
	}
}
