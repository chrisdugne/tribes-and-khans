
package com.uralys.tribes.entities
{

	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Report")]
	public class Report
	{	
		private var _reportType:int;
		private var _cellUID:String;
		private var _unit1:UnitReport;
		private var _unit2:UnitReport;
		private var _nextUnit:UnitReport;

		public function get reportType():int
		{
			return _reportType;
		}

		public function set reportType(value:int):void
		{
			_reportType = value;
		}

		public function get unit1():UnitReport
		{
			return _unit1;
		}

		public function set unit1(value:UnitReport):void
		{
			_unit1 = value;
		}

		public function get nextUnit():UnitReport
		{
			return _nextUnit;
		}

		public function set nextUnit(value:UnitReport):void
		{
			_nextUnit = value;
		}

		public function get unit2():UnitReport
		{
			return _unit2;
		}

		public function set unit2(value:UnitReport):void
		{
			_unit2 = value;
		}

		public function get cellUID():String
		{
			return _cellUID;
		}

		public function set cellUID(value:String):void
		{
			_cellUID = value;
		}


	}
		
}
