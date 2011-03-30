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
	
	[Embed(source="resources/embed/loading.swf")]
	[Bindable] public static var LOADING:Class;
	
	//   ======================================================================//

	[Embed(source="resources/embed/flags/fr.png")]
	[Bindable] public static var FR_FLAG:Class;

	[Embed(source="resources/embed/flags/en.png")]
	[Bindable] public static var EN_FLAG:Class;

	[Embed(source="resources/embed/flags/us.png")]
	[Bindable] public static var US_FLAG:Class;
	
	//   ======================================================================//
	
	[Embed(source="resources/embed/map.jpg")]
	[Bindable] public static var MAP:Class;

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

	[Embed(source="resources/embed/foret1.png")]
	[Bindable] public static var FORET1:Class;

	[Embed(source="resources/embed/foret2.png")]
	[Bindable] public static var FORET2:Class;

	[Embed(source="resources/embed/solville.png")]
	[Bindable] public static var SOL_VILLE:Class;

	[Embed(source="resources/embed/lac2.png")]
	[Bindable] public static var LAC2:Class;

	[Embed(source="resources/embed/lac1.png")] 
	[Bindable] public static var LAC1:Class;

	[Embed(source="resources/embed/roche1.png")]
	[Bindable] public static var ROCHE1:Class;

	[Embed(source="resources/embed/roche2.png")]
	[Bindable] public static var ROCHE2:Class;

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

	[Embed(source="resources/embed/conflit.png")]
	[Bindable] public static var CONFLIT:Class;

	//   ======================================================================//

	[Bindable] public static var GROUPE_PERSO:Bitmap;
	[Bindable] public static var FERMIER:Bitmap;
	[Bindable] public static var FORGERON:Bitmap;
	[Bindable] public static var MARCHAND:Bitmap;
	[Bindable] public static var MARCHANDE:Bitmap;
	[Bindable] public static var GUERRIER:Bitmap;

	//   ======================================================================//
	
	private static var loader:Loader;
	private static var request:URLRequest;
	private static var currentImage:int = 0;
	
	public static var images:ArrayCollection = new ArrayCollection(
		["webresources/images/persos/persos.png",
		 "webresources/images/persos/fermier.png",
		 "webresources/images/persos/marchand.png",
		 "webresources/images/persos/marchande.png",
		 "webresources/images/persos/guerrier.png",
		 "webresources/images/persos/forgeron.png"]);
	
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
				GROUPE_PERSO = event.currentTarget.content;
				break;
			case 1:
				FERMIER = event.currentTarget.content;
				break;
			case 2:
				MARCHAND = event.currentTarget.content;
				break;
			case 3:
				MARCHANDE = event.currentTarget.content;
				break;
			case 4:
				GUERRIER = event.currentTarget.content;
				break;
			case 5:
				FORGERON = event.currentTarget.content;
				break;
		}
		
		currentImage++;
		if(currentImage < 6)
			loadNextImage();
	}

	
}
}