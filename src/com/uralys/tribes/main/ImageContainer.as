package com.uralys.tribes.main
{
	import flash.display.Bitmap;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.net.URLRequest;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	      	 
public final class ImageContainer
{
	//   ======================================================================//
	
	[ Embed(source="resources/embed/greenBar.swf") ]  
	[Bindable] public static var LOADING:Class;

	[ Embed(source="resources/embed/borders.png") ]  
	[Bindable] public static var BORDERS:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/mail.png")]
	[Bindable] public static var MAIL_BOX:Class;

	[Embed(source="resources/embed/logo.png")]
	[Bindable] public static var LOGO:Class;

	//   ======================================================================//

	[Embed(source="resources/embed/zoom_in.png")]
	[Bindable] public static var ZOOM_IN:Class;

	[Embed(source="resources/embed/zoom_out.png")]
	[Bindable] public static var ZOOM_OUT:Class;

	[Embed(source="resources/embed/refresh.png")]
	[Bindable] public static var REFRESH:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/flags/fr.png")]
	[Bindable] public static var FR_FLAG:Class;

	[Embed(source="resources/embed/flags/en.png")]
	[Bindable] public static var EN_FLAG:Class;

	[Embed(source="resources/embed/flags/us.png")]
	[Bindable] public static var US_FLAG:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/icons/volume.png")]
	[Bindable] public static var HP:Class;

//	[Embed(source="resources/embed/buttons/ville-left.png")]
//	[Bindable] public static var CITY_BUTTON_LEFT:Class;
//
//	[Embed(source="resources/embed/buttons/ville-right.png")]
//	[Bindable] public static var CITY_BUTTON_RIGHT:Class;
//
//	[Embed(source="resources/embed/buttons/ville-center.png")]
//	[Bindable] public static var CITY_BUTTON_CENTER:Class;
	
	//   ======================================================================//
	
	[Embed(source="resources/embed/icons/wheat.png")]
	[Bindable] public static var WHEAT:Class;

	[Embed(source="resources/embed/icons/wood.png")]
	[Bindable] public static var WOOD:Class;

	[Embed(source="resources/embed/icons/iron.png")]
	[Bindable] public static var IRON:Class;

	[Embed(source="resources/embed/icons/gold.png")]
	[Bindable] public static var GOLD:Class;

	//   ======================================================================//

	[Embed(source="resources/embed/icons/cross.png")]
	[Bindable] public static var CROSS:Class;

	[Embed(source="resources/embed/clock.png")]
	[Bindable] public static var CLOCK:Class;  

	[Embed(source="resources/embed/upgrade_arrow.png")]
	[Bindable] public static var UPGRADE_ARROW:Class;  

	[Embed(source="resources/embed/arrow_promote.png")]
	[Bindable] public static var ARROW_PROMOTE:Class;  

	[Embed(source="resources/embed/arrow_demote.png")]
	[Bindable] public static var ARROW_DEMOTE:Class;  

	[Embed(source="resources/embed/arrow_left.png")]
	[Bindable] public static var ARROW_LEFT:Class;  

	[Embed(source="resources/embed/arrow_right.png")]
	[Bindable] public static var ARROW_RIGHT:Class;  

	//   ======================================================================//

	[Embed(source="resources/embed/pions.open.png")]
	[Bindable] public static var PIONS_OPEN:Class;  

	[Embed(source="resources/embed/pions.close.png")]
	[Bindable] public static var PIONS_CLOSE:Class;  

	//   ======================================================================//

	[Embed(source="resources/embed/icons/epee.png")]
	[Bindable] public static var SWORD:Class;

	[Embed(source="resources/embed/icons/armure.png")]
	[Bindable] public static var ARMOR:Class;

	[Embed(source="resources/embed/icons/arc.png")]
	[Bindable] public static var BOW:Class;

	[Embed(source="resources/embed/icons/people.png")]
	[Bindable] public static var PEOPLE:Class;

	//   ======================================================================//

	[Embed(source="resources/embed/foret1.png")]
	[Bindable] public static var FORET_1:Class;

	[Embed(source="resources/embed/foret05.png")]
	[Bindable] public static var FORET_05:Class;

	[Embed(source="resources/embed/foret025.png")]
	[Bindable] public static var FORET_025:Class;

	[Embed(source="resources/embed/sol_ville_1.png")]
	[Bindable] public static var SOL_VILLE_1:Class;

	[Embed(source="resources/embed/sol_ville_05.png")]
	[Bindable] public static var SOL_VILLE_05:Class;

	[Embed(source="resources/embed/sol_ville_025.png")]
	[Bindable] public static var SOL_VILLE_025:Class;

	//   ======================================================================//

//	[Embed(source="resources/embed/merchant1.png")]
//	[Bindable] public static var MERCHANT1:Class;
//	
//	[Embed(source="resources/embed/merchant2.png")]
//	[Bindable] public static var MERCHANT2:Class;
//	
//	[Embed(source="resources/embed/merchant3.png")]
//	[Bindable] public static var MERCHANTS:Class;
// 
//	//   ======================================================================//
//	
//	[Embed(source="resources/embed/warrior1.png")]
//	[Bindable] public static var WARRIOR1:Class;
//	
//	[Embed(source="resources/embed/warrior2.png")]
//	[Bindable] public static var WARRIOR2:Class;

	//   ======================================================================//
	
	[Embed(source="resources/embed/persos/fermier.png")]
	[Bindable] public static var FERMIER:Class;

	[Embed(source="resources/embed/persos/forgeron.png")]
	[Bindable] public static var FORGERON:Class;

	[Embed(source="resources/embed/persos/marchand.png")]
	[Bindable] public static var MARCHAND:Class;

	[Embed(source="resources/embed/persos/marchande.png")]
	[Bindable] public static var MARCHANDE:Class;

	[Embed(source="resources/embed/persos/guerrier.png")]
	[Bindable] public static var GUERRIER:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/question-mark.png")]
	[Bindable] public static var GREEN_QUESTION:Class;

	[Embed(source="resources/embed/resizeHandler.png")]
	[Bindable] public static var RESIZER:Class;

//	[Embed(source="resources/embed/conflit.png")]
//	[Bindable] public static var CONFLIT:Class;

	//   ======================================================================//

	[Bindable] public static var BACKGROUND:Bitmap;
	[Bindable] public static var FRONTIER_N:Bitmap;
	[Bindable] public static var FRONTIER_NE:Bitmap;
	[Bindable] public static var FRONTIER_NO:Bitmap;
	[Bindable] public static var FRONTIER_S:Bitmap;
	[Bindable] public static var FRONTIER_SE:Bitmap;
	[Bindable] public static var FRONTIER_SO:Bitmap;
	[Bindable] public static var HIGHLIGHT_BLEU:Bitmap;
	[Bindable] public static var HIGHLIGHT_BLANC:Bitmap;
	[Bindable] public static var HIGHLIGHT_VERT:Bitmap;
	[Bindable] public static var HIGHLIGHT_ROUGE:Bitmap;
	[Bindable] public static var MINI_LOGO:Bitmap;
	[Bindable] public static var MAP_BORDER_TOP:Bitmap;
	[Bindable] public static var MAP_BORDER_BOTTOM:Bitmap;
	[Bindable] public static var MAP_BORDER_LEFT:Bitmap;
	[Bindable] public static var MAP_BORDER_RIGHT:Bitmap;
	[Bindable] public static var MERCHANT_PLAYER:Bitmap;
	[Bindable] public static var MERCHANT_ALLY:Bitmap;
	[Bindable] public static var MERCHANT_ENNEMY:Bitmap;
	[Bindable] public static var ARMY_PLAYER:Bitmap;
	[Bindable] public static var ARMY_ALLY:Bitmap;
	[Bindable] public static var ARMY_ENNEMY:Bitmap;
	[Bindable] public static var EDIT:Bitmap;
	[Bindable] public static var VILLE:Bitmap;
	[Bindable] public static var FLEX:Bitmap;
	[Bindable] public static var APPENGINE:Bitmap;
	[Bindable] public static var ICONS:Bitmap;
	[Bindable] public static var FONTS:Bitmap;
	[Bindable] public static var URALYS_MINI_LOGO:Bitmap;
	[Bindable] public static var UH:Bitmap;
	[Bindable] public static var EDIT_WHITE:Bitmap;
	[Bindable] public static var LEFT_ARROW:Bitmap;
	[Bindable] public static var RIGHT_ARROW:Bitmap;
	[Bindable] public static var HIGHLIGHT_BOW_SHOOT:Bitmap;

	//   ======================================================================//

	// permet de faire des copies pour avoir plein d'image a partir du meme bitmap
	public static function getImage(bitmap:Bitmap):Bitmap{
		return new Bitmap(bitmap.bitmapData);
	}
	
	//   ======================================================================//
	
	private static var loader:Loader;
	private static var request:URLRequest;
	private static var currentImage:int = 0;
		
	public static var images:ArrayCollection = new ArrayCollection(
		[
		 "webresources/images/background/background.jpg",
		 "webresources/images/map/frontier_n.png",
		 "webresources/images/map/frontier_ne.png",
		 "webresources/images/map/frontier_no.png",
		 "webresources/images/map/frontier_s.png",
		 "webresources/images/map/frontier_se.png",
		 "webresources/images/map/frontier_so.png",
		 "webresources/images/map/highlight_bleu.png",
		 "webresources/images/map/highlight_blanc.png",
		 "webresources/images/map/highlight_vert.png",
		 "webresources/images/map/highlight_rouge.png",
		 "webresources/images/minilogo.png",
		 "webresources/images/persos/merchant_player.png",
		 "webresources/images/persos/merchant_ally.png",
		 "webresources/images/persos/merchant_ennemy.png",
		 "webresources/images/persos/army_player.png",
		 "webresources/images/persos/army_ally.png",
		 "webresources/images/persos/army_ennemy.png",
		 "webresources/images/edit.png",
		 "webresources/images/map/ville-moyenne.png",
		 "webresources/images/logos/utopian-hedonism-logo.jpg",
		 "webresources/images/edit_white.png",
		 "webresources/images/icons/left.png",
		 "webresources/images/icons/right.png",
		 "webresources/images/map/highlight_bow_shoot.png"
		 ]);
	
	public static var IMAGES_LOADED:Boolean = false;
	public static function loadImages():void{
		IMAGES_LOADED = false;
		loadNextImage();
	}

	private static function loadNextImage():void{
		loader = new Loader();
		request = new URLRequest(images.getItemAt(currentImage) as String);
		loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
		loader.load(request);
		
	}
	
	private static function onComplete(event:Event):void 
	{
		switch(currentImage)
		{
			case 0:
				BACKGROUND = event.currentTarget.content;
				break;
			case 1:
				FRONTIER_N = event.currentTarget.content;
				break;
			case 2:
				FRONTIER_NE = event.currentTarget.content;
				break;
			case 3:
				FRONTIER_NO = event.currentTarget.content;
				break;
			case 4:
				FRONTIER_S = event.currentTarget.content;
				break;
			case 5:
				FRONTIER_SE = event.currentTarget.content;
				break;
			case 6:
				FRONTIER_SO = event.currentTarget.content;
				break;
			case 7:
				HIGHLIGHT_BLEU = event.currentTarget.content;
				break;
			case 8:
				HIGHLIGHT_BLANC = event.currentTarget.content;
				break;
			case 9:
				HIGHLIGHT_VERT = event.currentTarget.content;
				break;
			case 10:
				HIGHLIGHT_ROUGE = event.currentTarget.content;
				break;
			case 11:
				MINI_LOGO = event.currentTarget.content;
				break;
			case 12:
				MERCHANT_PLAYER = event.currentTarget.content;
				break;
			case 13:
				MERCHANT_ALLY = event.currentTarget.content;
				break;
			case 14:
				MERCHANT_ENNEMY = event.currentTarget.content;
				break;
			case 15:
				ARMY_PLAYER = event.currentTarget.content;
				break;
			case 16:
				ARMY_ALLY = event.currentTarget.content;
				break;
			case 17:
				ARMY_ENNEMY = event.currentTarget.content;
				break;
			case 18:
				EDIT = event.currentTarget.content;
				break;
			case 19:
				VILLE = event.currentTarget.content;
				break;
			case 20:
				UH = event.currentTarget.content;
				break;
			case 21:
				EDIT_WHITE = event.currentTarget.content;
				break;
			case 22:
				LEFT_ARROW = event.currentTarget.content;
				break;
			case 23:
				RIGHT_ARROW = event.currentTarget.content;
				break;
			case 24:
				HIGHLIGHT_BOW_SHOOT = event.currentTarget.content;
				break;
		}
		
		currentImage++;
		
		if(currentImage < 25)
			loadNextImage();
		else
			IMAGES_LOADED = true;
	}
}
}