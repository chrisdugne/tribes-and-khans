
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.UnitReport")]
	public class UnitReport
	{	
		//--------------------------------------------------------------//
		
		protected var _unitUID:String;
		protected var _ownerUID:String;
		protected var _ownerName:String;
		protected var _type:int;
		protected var _size:int;
		protected var _bows:int;
		protected var _armors:int;
		protected var _swords:int;
		protected var _value:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _attackACity:Boolean;
		protected var _defendACity:Boolean;
		
		//--------------------------------------------------------------//
		
		
		public function get unitUID():String {
			return _unitUID;
		}
		
		public function set unitUID(o:String):void {
			_unitUID = o;
		}
		
		public function get ownerUID():String {
			return _ownerUID;
		}
		
		public function set ownerUID(o:String):void {
			_ownerUID = o;
		}
		
		public function get ownerName():String {
			return _ownerName;
		}
		
		public function set ownerName(o:String):void {
			_ownerName = o;
		}
		
		public function get size():int {
			return _size;
		}
		
		public function set size(o:int):void {
			_size = o;
		}
		
		public function get bows():int {
			return _bows;
		}
		
		public function set bows(o:int):void {
			_bows = o;
		}
		
		public function get armors():int {
			return _armors;
		}
		
		public function set armors(o:int):void {
			_armors = o;
		}
		
		public function get swords():int {
			return _swords;
		}
		
		public function set swords(o:int):void {
			_swords = o;
		}
		
		public function get value():int {
			return _value;
		}
		
		public function set value(o:int):void {
			_value = o;
		}
		
		public function get wheat():int {
			return _wheat;
		}
		
		public function set wheat(o:int):void {
			_wheat = o;
		}
		
		public function get wood():int {
			return _wood;
		}
		
		public function set wood(o:int):void {
			_wood = o;
		}
		
		public function get iron():int {
			return _iron;
		}
		
		public function set iron(o:int):void {
			_iron = o;
		}
		
		public function get attackACity():Boolean {
			return _attackACity;
		}
		
		public function set attackACity(o:Boolean):void {
			_attackACity = o;
		}
		
		public function get defendACity():Boolean {
			return _defendACity;
		}
		
		public function set defendACity(o:Boolean):void {
			_defendACity = o;
		}

		public function get type():int
		{
			return _type;
		}

		public function set type(value:int):void
		{
			_type = value;
		}

		public function get gold():int
		{
			return _gold;
		}

		public function set gold(value:int):void
		{
			_gold = value;
		}
		


	}
}
