package com.uralys.tribes.core
{

import com.uralys.tribes.commons.*;

import mx.core.IVisualElement;
import mx.utils.ObjectUtil;

import spark.components.Group;

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
		
		protected var _window:Group;
		
		public function get window():Group {
			return _window;
		}
	
		public function set window(o:Group):void {
			_window = o;
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

		public function goToPage(PageType:Class, ...args):void{
			
			_currentPage = PageType;
			var page = new PageType(); 
			try{
				page.params = args;
			}catch(e:Error){}
				
			
			_window.removeAllElements();
			_window.addElement(page);
			
		}

		//==================================================================================================//
	
}


}