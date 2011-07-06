
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
		public static const INTERCEPTED_ON_THIS_CASE:int = 1;
		public static const FREE:int = 2;
		public static const DESTROYED:int = 3;
		public static const FUTURE:int = 4;
		
		//==========================================================================//
		
		public function Unit(){}
		
		// on ne peut pas le mettre dans le constructeur, car BlazeDS l'utilise aussi
		public function initNewUnit(i:int = 0, j:int = 0):void{
			_currentCaseUID = (Session.map[i][j] as Case).caseUID;
			_finalCaseUIDExpected = _currentCaseUID;
			_unitUID = Session.player.uralysUID+"_"+(Session.player.units.length+1)+"_"+(new Date().getTime());
			_status = TO_BE_CREATED;
			_beginTime = new Date().getTime();
			_endTime = -1;
		}
		
		//==========================================================================//
		
		private var _beginTime:Number;
		private var _endTime:Number;
		private var _gatheringUIDExpected:String;
		private var _conflictUIDExpected:String;
		private var _finalCaseUIDExpected:String;
		
		private var _status:int;
		private var _currentCaseUID:String;
		private var _player:Player;
		
		protected var _unitUID:String;
		protected var _size:int;
		protected var _speed:int;
		protected var _value:int;
		protected var _wheat:int;
		protected var _wood:int;
		protected var _iron:int;
		protected var _gold:int;
		protected var _radius:int;
		
		protected var _equipments:ArrayCollection = new ArrayCollection();
		protected var _moves:ArrayCollection = new ArrayCollection();
		protected var _conflicts:ArrayCollection = new ArrayCollection();
		
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
			refreshValue();
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
	
		public function get value():int {
			return _value;
		}

		public function refreshValue():void {
			value = size 
					+ (bows > size ? size : bows) 
					+ (swords > size ? size : swords) *2 
					+ (armors > size ? size : armors) *3;
			
			if(type == MERCHANT)
				value = value/10;
		}
	
		public function set value(o:int):void {
			isModified = true;
			_value = o;
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
		
		public function get equipments():ArrayCollection {
			return _equipments;
		}
		
		public function set equipments(o:ArrayCollection):void {
			isModified = true;
			_equipments = o;
		}
		
		public function get moves():ArrayCollection {
			return _moves;
		}
		
		public function set moves(o:ArrayCollection):void {
			isModified = true;
			_moves = o;
		}
		
		public function get conflicts():ArrayCollection {
			return _conflicts;
		}
		
		public function set conflicts(o:ArrayCollection):void {
			isModified = true;
			_conflicts = o;
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
		
		public function get gatheringUIDExpected():String
		{
			return _gatheringUIDExpected;
		}
		
		public function set gatheringUIDExpected(value:String):void
		{
			_gatheringUIDExpected = value;
		}
		
		public function get conflictUIDExpected():String
		{
			return _conflictUIDExpected;
		}
		
		public function set conflictUIDExpected(value:String):void
		{
			_conflictUIDExpected = value;
		}
		
		public function get finalCaseUIDExpected():String
		{
			return _finalCaseUIDExpected;
		}
		
		public function set finalCaseUIDExpected(value:String):void
		{
			_finalCaseUIDExpected = value;
		}
		
		//==========================================================================//

		private var _bows:int;
		private var _swords:int;
		private var _armors:int;

		
		public function get bows():int{
			return _bows;
		}

		public function set bows(o:int):void{
			isModified = true;
			_bows = o;
			refreshValue();
		}

		public function get swords():int{
			return _swords;
		}

		public function set swords(o:int):void{
			isModified = true;
			_swords = o;
			refreshValue();
		}

		public function get armors():int{
			return _armors;
		}

		public function set armors(o:int):void{
			isModified = true;
			_armors = o;
			refreshValue();
		}

		//==========================================================================//

		public function mayBuildAcity():Boolean{
			trace('mayBuildAcity ? ' + type);
			return type == 2 
				&& size >= 250
				&& wheat >= Numbers.CITY_WHEAT_BASE_PRICE + size
				&& wood >= Numbers.CITY_WOOD_BASE_PRICE + size * 10
				&& iron >= Numbers.CITY_IRON_BASE_PRICE + size * 10
				&& gold >= Numbers.CITY_GOLD_BASE_PRICE;
		}
		
		//==========================================================================//
		
		public static const ARMY:int = 1;
		public static const MERCHANT:int = 2;
		
		public var type:int; // 1 armee,  2 marchand
		
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

		//==========================================================================//
		
		// memes commentaires que pour getLastMove
		// une interception c'est un conflit sur la case sur laquelle on est actuellement. On est Unit.FREE si le conflit est sur une case plus loin.
		public function get isInterceptedOnThisCase():Boolean
		{
			trace("test isIntercepted");
			trace("trace nbMoves : " + moves.length);
			trace("currentCaseUID : " + currentCaseUID);
			
			var lastMove:Move = moves.getItemAt(moves.length-1) as Move;
			trace("lastMove.caseUID : " + lastMove.caseUID);
			
			if(lastMove.caseUID != currentCaseUID){
				trace("unité en mouvement : pas d'interception");
				return false;
			}
			
			var moveOfTheNewUnitResultingFromTheInterception:Move = null;
			trace("lastMove.moveUID : " + lastMove.moveUID);
			
			if(lastMove.moveUID.indexOf(unitUID) == -1){
				trace("move d'une future newUnit liée : on check la case pour savoir si cest une interception");
				moveOfTheNewUnitResultingFromTheInterception = moves.getItemAt(moves.length-1) as Move;
				lastMove = moves.getItemAt(moves.length-2) as Move;
			}
			
			if(moveOfTheNewUnitResultingFromTheInterception == null)
				return false;
			else if(moveOfTheNewUnitResultingFromTheInterception.caseUID == lastMove.caseUID){
				trace("moveOfTheNewUnitResultingFromTheInterception is on the same case : INTERCEPTED_ON_THIS_CASE");
				return true;
			}
			else{
				trace("moveOfTheNewUnitResultingFromTheInterception is NOT on the same case : NOT INTERCEPTED_ON_THIS_CASE");
				return false;
			}
		}
		
		public function get lastMove():Move
		{
			// manipulation d'une unite : initialisation du deplacement
			var move:Move = moves.getItemAt(moves.length-1) as Move;
			
			// raison du test :
			//		on test si le move est bien celui de l'unite, et non le move rajoute de la newUnit resultant du gathering avec une autre armee
			// 		(auquel cas ce move ne compte pas dans le nouveau calcul du deplacement)
			// comment on test : 
			//		les moveUIDs de l'unite continennement unitUID, contrairement à newUnitUID
			// resultat :
			// 		si on trouve le move d'une newUnit, on prend celui d'avant (qui sera le move precedent le gathering et donc bien le dernier move de l'unite)
			if(move.moveUID.indexOf(unitUID) == -1){
				trace("move d'une future newUnit liée : on le zappe");
				move = moves.getItemAt(moves.length-2) as Move;
			}
			
			return move;
		}
		
		
		// memes commentaires que pour getLastMove
		// retourne le dernier move reel de l'unité
		public function removeLastMove():Move
		{
			var needToRemoveHiddenMove:Boolean = false;
			var move:Move = moves.getItemAt(moves.length-1) as Move;
			
			if(move.moveUID.indexOf(unitUID) == -1){
				trace("move d'une future newUnit liée : il faut aussi l'enlever");
				needToRemoveHiddenMove = true;
			}
			
			moves.removeItemAt(moves.length-1);
			if(needToRemoveHiddenMove)
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
