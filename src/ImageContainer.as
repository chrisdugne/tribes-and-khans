package
{
	import flash.display.Bitmap;
	import flash.display.Loader;
	import flash.net.URLRequest;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	      	 
public final class ImageContainer
{
	//   ======================================================================//
	
	[ Embed(source="resources/embed/greenBar.swf") ]  
	[Bindable] public static var LOADING:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/flags/fr.png")]
	[Bindable] public static var FR_FLAG:Class;

	[Embed(source="resources/embed/flags/en.png")]
	[Bindable] public static var EN_FLAG:Class;

	[Embed(source="resources/embed/flags/us.png")]
	[Bindable] public static var US_FLAG:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/buttons/ville-left.png")]
	[Bindable] public static var CITY_BUTTON_LEFT:Class;

	[Embed(source="resources/embed/buttons/ville-right.png")]
	[Bindable] public static var CITY_BUTTON_RIGHT:Class;

	[Embed(source="resources/embed/buttons/ville-center.png")]
	[Bindable] public static var CITY_BUTTON_CENTER:Class;
	
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

	[Embed(source="resources/embed/icons/check.png")]
	[Bindable] public static var CHECK:Class;

	//   ======================================================================//

	[Embed(source="resources/embed/icons/epee.png")]
	[Bindable] public static var SWORD:Class;

	[Embed(source="resources/embed/icons/armure.png")]
	[Bindable] public static var ARMOR:Class;

	[Embed(source="resources/embed/icons/arc.png")]
	[Bindable] public static var BOW:Class;

	//   ======================================================================//

	[Embed(source="resources/embed/foret7.png")]
	[Bindable] public static var FORET7:Class;

	[Embed(source="resources/embed/sol_ville.png")]
	[Bindable] public static var SOL_VILLE:Class;

	//   ======================================================================//

	[Embed(source="resources/embed/icons/left.png")]
	[Bindable] public static var LEFT:Class;

	[Embed(source="resources/embed/icons/right.png")]
	[Bindable] public static var RIGHT:Class;

	[Embed(source="resources/embed/icons/top_left.png")]
	[Bindable] public static var TOP_LEFT:Class;

	[Embed(source="resources/embed/icons/top_right.png")]
	[Bindable] public static var TOP_RIGHT:Class;

	[Embed(source="resources/embed/icons/all_left.png")]
	[Bindable] public static var ALL_LEFT:Class;

	[Embed(source="resources/embed/icons/all_right.png")]
	[Bindable] public static var ALL_RIGHT:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/house1.png")]
	[Bindable] public static var HOUSE1:Class;
 
	[Embed(source="resources/embed/house2.png")]
	[Bindable] public static var HOUSE2:Class;
 
	[Embed(source="resources/embed/house3.png")]
	[Bindable] public static var HOUSE3:Class;
 
	//   ======================================================================//
	
	[Embed(source="resources/embed/merchant1.png")]
	[Bindable] public static var MERCHANT1:Class;
	
	[Embed(source="resources/embed/merchant2.png")]
	[Bindable] public static var MERCHANT2:Class;
 
	//   ======================================================================//
	
	[Embed(source="resources/embed/warrior1.png")]
	[Bindable] public static var WARRIOR1:Class;
	
	[Embed(source="resources/embed/warrior2.png")]
	[Bindable] public static var WARRIOR2:Class;

	//   ======================================================================//
	
	[Embed(source="resources/embed/question-mark.png")]
	[Bindable] public static var QUESTION:Class;

//	[Embed(source="resources/embed/conflit.png")]
//	[Bindable] public static var CONFLIT:Class;

	//   ======================================================================//

	[Bindable] public static var LOGO:Bitmap;
	[Bindable] public static var GROUPE_PERSO:Bitmap;
	[Bindable] public static var FERMIER:Bitmap;
	[Bindable] public static var FORGERON:Bitmap;
	[Bindable] public static var MARCHAND:Bitmap;
	[Bindable] public static var MARCHANDE:Bitmap;
	[Bindable] public static var GUERRIER:Bitmap;
	[Bindable] public static var MINI_LOGO:Bitmap;
	[Bindable] public static var MAP_BORDER_TOP:Bitmap;
	[Bindable] public static var MAP_BORDER_BOTTOM:Bitmap;
	[Bindable] public static var MAP_BORDER_LEFT:Bitmap;
	[Bindable] public static var MAP_BORDER_RIGHT:Bitmap;
	[Bindable] public static var HIGHLIGHT_BLEU:Bitmap;
	[Bindable] public static var HIGHLIGHT_BLANC:Bitmap;
	[Bindable] public static var HIGHLIGHT_VERT:Bitmap;
	[Bindable] public static var HIGHLIGHT_ROUGE:Bitmap;
	[Bindable] public static var MERCHANT_PLAYER:Bitmap;
	[Bindable] public static var MERCHANT_ALLY:Bitmap;
	[Bindable] public static var MERCHANT_ENNEMY:Bitmap;
	[Bindable] public static var ARMY_PLAYER:Bitmap;
	[Bindable] public static var ARMY_ALLY:Bitmap;
	[Bindable] public static var ARMY_ENNEMY:Bitmap;
	[Bindable] public static var EDIT:Bitmap;
	[Bindable] public static var FRONTIER_N:Bitmap;
	[Bindable] public static var FRONTIER_NE:Bitmap;
	[Bindable] public static var FRONTIER_NO:Bitmap;
	[Bindable] public static var FRONTIER_S:Bitmap;
	[Bindable] public static var FRONTIER_SE:Bitmap;
	[Bindable] public static var FRONTIER_SO:Bitmap;

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
		["webresources/images/logo.png",
		 "webresources/images/persos/persos.png",
		 "webresources/images/persos/fermier.png",
		 "webresources/images/persos/marchand.png",
		 "webresources/images/persos/marchande.png",
		 "webresources/images/persos/guerrier.png",
		 "webresources/images/persos/forgeron.png",
		 "webresources/images/minilogo.png",
		 "webresources/images/map/border-top.png",
		 "webresources/images/map/border-bottom.png",
		 "webresources/images/map/border-left.png",
		 "webresources/images/map/border-right.png",
		 "webresources/images/map/highlight_bleu.png",
		 "webresources/images/map/highlight_blanc.png",
		 "webresources/images/map/highlight_vert.png",
		 "webresources/images/map/highlight_rouge.png",
		 "webresources/images/persos/marchand_vert.png",
		 "webresources/images/persos/marchand_bleu.png",
		 "webresources/images/persos/marchand_rouge.png",
		 "webresources/images/persos/guerrier_vert.png",
		 "webresources/images/persos/guerrier_bleu.png",
		 "webresources/images/persos/guerrier_rouge.png",
		 "webresources/images/edit.png",
		 "webresources/images/map/frontier_n.png",
		 "webresources/images/map/frontier_ne.png",
		 "webresources/images/map/frontier_no.png",
		 "webresources/images/map/frontier_s.png",
		 "webresources/images/map/frontier_se.png",
		 "webresources/images/map/frontier_so.png"
		 ])
	
	public static function loadImages():void{
		loadNextImage();
	}

	private static function loadNextImage():void{
		loader = new Loader();
		request = new URLRequest(images.getItemAt(currentImage) as String);
		loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
		loader.load(request);
		
	}
	
	private static function onComplete(event:Event):void {
		switch(currentImage){
			case 0:
				LOGO = event.currentTarget.content;
				break;
			case 1:
				GROUPE_PERSO = event.currentTarget.content;
				break;
			case 2:
				FERMIER = event.currentTarget.content;
				break;
			case 3:
				MARCHAND = event.currentTarget.content;
				break;
			case 4:
				MARCHANDE = event.currentTarget.content;
				break;
			case 5:
				GUERRIER = event.currentTarget.content;
				break;
			case 6:
				FORGERON = event.currentTarget.content;
				break;
			case 7:
				MINI_LOGO = event.currentTarget.content;
				break;
			case 8:
				MAP_BORDER_TOP = event.currentTarget.content;
				break;
			case 9:
				MAP_BORDER_BOTTOM = event.currentTarget.content;
				break;
			case 10:
				MAP_BORDER_LEFT = event.currentTarget.content;
				break;
			case 11:
				MAP_BORDER_RIGHT = event.currentTarget.content;
				break;
			case 12:
				HIGHLIGHT_BLEU = event.currentTarget.content;
				break;
			case 13:
				HIGHLIGHT_BLANC = event.currentTarget.content;
				break;
			case 14:
				HIGHLIGHT_VERT = event.currentTarget.content;
				break;
			case 15:
				HIGHLIGHT_ROUGE = event.currentTarget.content;
				break;
			case 16:
				MERCHANT_PLAYER = event.currentTarget.content;
				break;
			case 17:
				MERCHANT_ALLY = event.currentTarget.content;
				break;
			case 18:
				MERCHANT_ENNEMY = event.currentTarget.content;
				break;
			case 19:
				ARMY_PLAYER = event.currentTarget.content;
				break;
			case 20:
				ARMY_ALLY = event.currentTarget.content;
				break;
			case 21:
				ARMY_ENNEMY = event.currentTarget.content;
				break;
			case 22:
				EDIT = event.currentTarget.content;
				break;
			case 23:
				FRONTIER_N = event.currentTarget.content;
				break;
			case 24:
				FRONTIER_NE = event.currentTarget.content;
				break;
			case 25:
				FRONTIER_NO = event.currentTarget.content;
				break;
			case 26:
				FRONTIER_S = event.currentTarget.content;
				break;
			case 27:
				FRONTIER_SE = event.currentTarget.content;
				break;
			case 28:
				FRONTIER_SO = event.currentTarget.content;
				break;
		}
		
		currentImage++;
		if(currentImage < 29)
			loadNextImage();
	}

	

}
}