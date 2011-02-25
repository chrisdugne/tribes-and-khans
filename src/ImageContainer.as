package
{
	      	 
public final class ImageContainer
{
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

	[Embed(source="resources/embed/icons/epee.png")]
	[Bindable] public static var SWORD:Class;

	[Embed(source="resources/embed/icons/armure.png")]
	[Bindable] public static var ARMOR:Class;

	[Embed(source="resources/embed/icons/arc.png")]
	[Bindable] public static var BOW:Class;

}
}