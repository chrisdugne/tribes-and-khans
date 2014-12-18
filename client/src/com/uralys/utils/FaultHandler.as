package com.uralys.utils
{
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.rpc.Fault;
	
	import 	mx.core.FlexGlobals;
	
	public class FaultHandler
	{
		public function handleRemoteServiceFault(fault:Fault):void{
			Alert.show("Code erreur "+fault.faultCode+"\n\n"+fault.faultString);
		}
	}
}