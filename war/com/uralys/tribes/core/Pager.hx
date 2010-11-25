package com.skooairs.core
{

import com.skooairs.constants.*;

import mx.containers.Canvas;
import mx.containers.ViewStack;
import mx.controls.Image;

import windows.Tutorial;
	
/**

The Pager manages the main Canvas of the application as pages.
The application contains one child : the window
the Pager select the canva to add as a child to this window

then every content is a page, constructed using components 
*/
public class Pager
{	
		//=====================================================================================//
	
		private static var instance : Pager = new Pager();
	
		public static function getInstance():Pager{
			return instance;
		}

		public function Pager(){trace("Pager initialized")}

		//=====================================================================================//
		
		protected var _window:Canvas;
		protected var _viewstack:ViewStack;
		protected var _tutorial:Tutorial;
		
		public function get window():Canvas {
			return _window;
		}
	
		public function set window(o:Canvas):void {
			_window = o;
		}
		
		[Bindable]
		public function get tutorial():Tutorial {
			return _tutorial;
		}
	
		public function set tutorial(o:Tutorial):void {
			_tutorial = o;
		}

		public function get viewstack():ViewStack {
			return _viewstack;
		}
	
		public function set viewstack(o:ViewStack):void {
			_viewstack = o;
		}
		//=====================================================================================//
		
		protected var _currentPage:Class;
		

		public function get currentPage():Class {
			return _currentPage;
		}
	
		public function set currentPage(o:Class):void {
			_currentPage = o;
		}
		
		//======================================================================================//

		public function goToPage(Page:Class):void{
			
			_currentPage = Page;
			
			//child at 0 is the background image
			if(_window.numChildren > 1)
				_window.removeChildAt(1);
			_window.addChild(new Page());
			
		}
		
		//======================================================================================//

		public function goToView(view:int):void{
			_viewstack.selectedIndex = view;
		}

		//======================================================================================//

		public function setBackground(image:String):void{
			
			var _image:Image = new Image();
			_image.source = image;
			_image.percentWidth = 100;
			
			_window.addChildAt(_image,0);
		}	
		
		//==================================================================================================//
	
}


}