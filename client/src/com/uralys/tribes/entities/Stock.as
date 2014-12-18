package com.uralys.tribes.entities
{
	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Stock")]
	public class Stock
	{
		public function Stock(){}
		
		//--------------------------------------------------------------//
		
		protected var _stockUID:String;
		
		protected var _stockCapacity:Number;
		
		protected var _peopleBuildingStock:int;
		protected var _stockBeginTime:Number;
		protected var _stockEndTime:Number;
		protected var _stockNextCapacity:int;
		
		protected var _smiths:int;
		protected var _itemsBeingBuilt:int;
		protected var _itemsBeingBuiltBeginTime:Number;
		protected var _itemsBeingBuiltEndTime:Number;
		
		//--------------------------------------------------------------//
		
		
		public function get stockUID():String {
			return _stockUID;
		}
		
		public function set stockUID(o:String):void {
			_stockUID = o;
		}
		
		public function get stockCapacity():Number {
			return _stockCapacity;
		}
		
		public function set stockCapacity(o:Number):void {
			_stockCapacity = o;
		}
		
		public function get peopleBuildingStock():int {
			return _peopleBuildingStock;
		}
		
		public function set peopleBuildingStock(o:int):void {
			_peopleBuildingStock = o;
		}
		
		public function get stockBeginTime():Number {
			return _stockBeginTime;
		}
		
		public function set stockBeginTime(o:Number):void {
			_stockBeginTime = o;
		}
		
		public function get stockEndTime():Number {
			return _stockEndTime;
		}
		
		public function set stockEndTime(o:Number):void {
			_stockEndTime = o;
		}
		
		public function get stockNextCapacity():int {
			return _stockNextCapacity;
		}
		
		public function set stockNextCapacity(o:int):void {
			_stockNextCapacity = o;
		}

		public function get smiths():int
		{
			return _smiths;
		}

		public function set smiths(value:int):void
		{
			_smiths = value;
		}

		public function get itemsBeingBuilt():int
		{
			return _itemsBeingBuilt;
		}

		public function set itemsBeingBuilt(value:int):void
		{
			_itemsBeingBuilt = value;
		}

		public function get itemsBeingBuiltBeginTime():Number
		{
			return _itemsBeingBuiltBeginTime;
		}

		public function set itemsBeingBuiltBeginTime(value:Number):void
		{
			_itemsBeingBuiltBeginTime = value;
		}

		public function get itemsBeingBuiltEndTime():Number
		{
			return _itemsBeingBuiltEndTime;
		}

		public function set itemsBeingBuiltEndTime(value:Number):void
		{
			_itemsBeingBuiltEndTime = value;
		}
		

	}
}