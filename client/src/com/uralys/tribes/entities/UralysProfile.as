package com.uralys.tribes.entities
{

[Bindable]
[RemoteClass(alias="com.uralys.tribes.entities.UralysProfile")]
public class UralysProfile
{
	public function UralysProfile(){}



	public function get uralysUID():String {
		return _uralysUID;
	}

	public function set uralysUID(o:String):void {
		_uralysUID = o;
	}

	public function get surname():String {
		return _surname;
	}

	public function set surname(o:String):void {
		_surname = o;
	}

	public function get email():String {
		return _email;
	}

	public function set email(o:String):void {
		_email = o;
	}
	public function get language():int {
		return _language;
	}

	public function set language(o:int):void {
		_language = o;
	}


	public function get lastLog():Number {
		return _lastLog;
	}

	public function set lastLog(o:Number):void {
		_lastLog = o;
	}
	
	public function get facebookUID():String {
		return _facebookUID;
	}
	
	public function set facebookUID(o:String):void {
		_facebookUID = o;
	}
	
	protected var _uralysUID:String;
	protected var _facebookUID:String;
	protected var _surname:String;
	protected var _email:String;
	protected var _language:int;
	protected var _lastLog:Number;

}
}

