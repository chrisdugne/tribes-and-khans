
package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	import com.uralys.tribes.commons.Translations;
	
	import mx.messaging.management.Attribute;

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

		//----------------------------------------------

		public function get title():String
		{
			var victory:Boolean = nextUnit.ownerUID == Session.player.playerUID;
			var playerOwnsUnit1 = unit1.ownerUID ==  Session.player.playerUID;
			
			var _title:String;
			
			switch(reportType)
			{
				case Numbers.REPORT_GROUND_FIGHT:
					_title = (Translations.CONFLICT.getItemAt(Session.LANGUAGE) as String);
					break;

				case Numbers.REPORT_GROUND_GATHERING:
					_title = (Translations.GATHERING.getItemAt(Session.LANGUAGE) as String);
					break;

				case Numbers.REPORT_BOW_SHOT:
					_title = (Translations.BOW_SHOT.getItemAt(Session.LANGUAGE) as String);
					break;
					
				default:
					break;
			}
			
			if(reportType == Numbers.REPORT_GROUND_FIGHT)
			{
				if(victory){
					_title += " : " + (Translations.VICTORY.getItemAt(Session.LANGUAGE) as String);
					
					if(playerOwnsUnit1 && unit1.attackACity
					|| !playerOwnsUnit1 && unit1.defendACity){
						_title += " : " + (Translations.CITY_IS_TAKEN.getItemAt(Session.LANGUAGE) as String);
					}
				}
				else{
					_title += " : " + (Translations.DEFEAT.getItemAt(Session.LANGUAGE) as String);

					if(playerOwnsUnit1 && unit1.defendACity
					|| !playerOwnsUnit1 && unit1.attackACity){
						_title += " : " + (Translations.CITY_IS_LOST.getItemAt(Session.LANGUAGE) as String);
					}
				}
				
			}
			
			return _title;
		}


	}
		
}
