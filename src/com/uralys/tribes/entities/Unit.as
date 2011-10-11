
package com.uralys.tribes.entities
{
	import com.uralys.tribes.commons.Numbers;
	import com.uralys.tribes.commons.Session;
	
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.uralys.tribes.entities.Unit")]
	public class Unit
	{	
		//==========================================================================//
				
		public static const TO_BE_CREATED:int = -1;
		public static const FREE:int = 1;
		public static const DESTROYED:int = 2;
		public static const FUTURE:int = 3;
		
		//==========================================================================//
		
		public function Unit(){}
		
		// on ne peut pas le mettre dans le constructeur, car BlazeDS l'utilise aussi

		
		public function initNewUnit(i:int = 0, j:int = 0):void{
			_currentCaseUID = (Session.map[i][j] as Cell).cellUID;
			_cellUIDExpectedForLand = _currentCaseUID;
			_unitUID = Session.player.uralysUID+"_"+(Session.player.units.length+1)+"_"+(new Date().getTime());
			_status = TO_BE_CREATED;
			_beginTime = new Date().getTime();
			_lastShotTime = new Date().getTime();
			_endTime = -1;
		}
		
		//==========================================================================//
		
		protected var _unitUID:String;
		protected var _type:int; // 1 armee,  2 marchand
		private var _player:Player;

		protected var _size:int;
		protected var _speed:int;

		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;

		protected var _bows:int;
		protected var _swords:int;
		protected var _armors:int;

		private var _beginTime:Number;
		private var _endTime:Number;
		private var _lastShotTime:Number;

		private var _cellUIDExpectedForLand:String;
		private var _unitMetUID:String;
		private var _unitNextUID:String;
		private var _messageUID:String;
		
		protected var _moves:ArrayCollection = new ArrayCollection();

		//==========================================================================//

		private var _status:int;
		private var _currentCaseUID:String;
		
		protected var _radius:int;
		protected var _value:int;
		
		
		//==========================================================================//
		
		public function get unitUID():String {
			return _unitUID;
		}
	
		public function set unitUID(o:String):void {
			isModified = true;
			_unitUID = o;
		}
	
		public function get size():int {
			return _size;
		}
	
		public function set size(o:int):void {
			isModified = true;
			_size = o;
			//refreshValue();
		}
	
		public function get currentCaseUID():String {
			return _currentCaseUID;
		}
	
		public function set currentCaseUID(o:String):void {
			_currentCaseUID = o;
		}
	
		public function get speed():int {
			return _speed;
		}
	
		public function set speed(o:int):void {
			isModified = true;
			_speed = o;
		}

		public function get type():int {
			return _type;
		}
	
		public function set type(o:int):void {
			_type = o;
		}
	
		public function set value(o:int):void {
			_value = o;
		}

		public function get value():int {
			return _value;
		}

		public function refreshValue():void 
		{
			if(type == MERCHANT)
				value = size/10;
			else{
				value = size 
						+ (bows > size ? size : bows) 
						+ (swords > size ? size : swords) *2 
						+ (armors > size ? size : armors) *3;
			}
		}
	
		
		public function get wheat():int {
			return _wheat;
		}
		
		public function set wheat(o:int):void {
			isModified = true;
			_wheat = o;
		}
		
		public function get wood():int {
			return _wood;
		}
		
		public function set wood(o:int):void {
			isModified = true;
			_wood = o;
		}
		
		public function get iron():int {
			return _iron;
		}
		
		public function set iron(o:int):void {
			isModified = true;
			_iron = o;
		}
		
		public function get gold():int {
			return _gold;
		}
		
		public function set gold(o:int):void {
			isModified = true;
			_gold = o;
		}
		
		public function get radius():int {
			return Math.sqrt(_size)/2 + 2; // min 2 pixels
		}
	
		public function set radius(o:int):void {
			isModified = true;
			_radius = o;
		}
		
		public function get moves():ArrayCollection {
			return _moves;
		}
		
		public function set moves(o:ArrayCollection):void {
			isModified = true;
			_moves = o;
		}
		
		public function get status():int {
			return _status;
		}
		
		public function set status(o:int):void {
			isModified = true;
			_status = o;
		}
		
		public function get player():Player {
			return _player;
		}
		
		public function set player(o:Player):void {
			isModified = true;
			_player = o;
		}
		
		public function get beginTime():Number
		{
			return _beginTime;
		}
		
		public function set beginTime(value:Number):void
		{
			_beginTime = value;
		}
		
		public function get endTime():Number
		{
			return _endTime;
		}
		
		public function set endTime(value:Number):void
		{
			_endTime = value;
		}
		
		public function get lastShotTime():Number
		{
			return _lastShotTime;
		}
		
		public function set lastShotTime(value:Number):void
		{
			_lastShotTime = value;
		}
		
		public function get cellUIDExpectedForLand():String
		{
			return _cellUIDExpectedForLand;
		}
		
		public function set cellUIDExpectedForLand(value:String):void
		{
			_cellUIDExpectedForLand = value;
		}
		
		public function get unitNextUID():String
		{
			return _unitNextUID;
		}
		
		public function set unitNextUID(value:String):void
		{
			_unitNextUID = value;
		}
		
		public function get unitMetUID():String
		{
			return _unitMetUID;
		}
		
		public function set unitMetUID(value:String):void
		{
			_unitMetUID = value;
		}

		public function get messageUID():String
		{
			return _messageUID;
		}
		
		public function set messageUID(value:String):void
		{
			_messageUID = value;
		}
		
		//==========================================================================//

		public function get bows():int{
			return _bows;
		}

		public function set bows(o:int):void{
			isModified = true;
			_bows = o;
			//refreshValue();
		}

		public function get swords():int{
			return _swords;
		}

		public function set swords(o:int):void{
			isModified = true;
			_swords = o;
			//refreshValue();
		}

		public function get armors():int{
			return _armors;
		}

		public function set armors(o:int):void{
			isModified = true;
			_armors = o;
			//refreshValue();
		}

		//==========================================================================//

		public function mayBuildAcity():Boolean{
			return type == MERCHANT 
				&& size >= Numbers.CITY_MERCHANT_BASE
				&& wheat >= Numbers.CITY_WHEAT_BASE_PRICE + size
				&& wood >= Numbers.CITY_WOOD_BASE_PRICE + size * 10
				&& iron >= Numbers.CITY_IRON_BASE_PRICE + size * 10
				&& gold >= Numbers.CITY_GOLD_BASE_PRICE + size;
		}
		
		//==========================================================================//
		
		public static const ARMY:int = 1;
		public static const MERCHANT:int = 2;
		
		public var landExpected:int = -1;
		//public var tmpLandSquare:Rect;
		
		//==========================================================================//

		private var _isModified:Boolean;
		
		public function get isModified():Boolean
		{
			return _isModified;
		}
		
		public function set isModified(value:Boolean):void
		{
			_isModified = value;
		}

		//==========================================================================//
		
		public static const PLAYER:int = 1;
		public static const ALLY:int = 2;
		public static const ENNEMY:int = 3;
		
		public var ownerStatus:int; //
		public var timeFromChallenging:Number = -1; // = -1 par defaut, set lorsquon a une cell.timeFromChallenging qui se refresh

		//==========================================================================//
		
		
		public function get lastMove():Move
		{
			// manipulation d'une unite : initialisation du deplacement
			var move:Move = moves.getItemAt(moves.length-1) as Move;
			return move;
		}
		
		
		public function removeLastMove():Move
		{
			var move:Move = moves.getItemAt(moves.length-1) as Move;
			moves.removeItemAt(moves.length-1);
			return move;
		}
		
		/*
			à appeler avant tout update (car update = replace des unites)
			les moves de l'unite vont etre recalcules.
			donc on enleve le move de la newUnit si il existe (car dans ce cas il a ete rattache en tant que lastMove de l'unite)
			et on declare le vrai lastMove comme etant move a fin indeterminee sur la derniere case
		*/ 
		public function refreshLastMoveBeforeReplacingUnit():void
		{
			var _realLastMove:Move = lastMove;
			removeLastMove();
			moves.addItem(_realLastMove);
				
			if(moves.length == 1 && (moves.getItemAt(0) as Move).timeTo > 0)
			{
				// ici on a une caravane dans la ville qui est enregistrée dans un conflit
				// on force le timeTo à -1 pour que l'algo qui va replacer l'autre unit du conflit trouve le recorded move et recalcule le conflit
				(moves.getItemAt(0) as Move).timeTo = -1; 			
			}
		}
	}
}
