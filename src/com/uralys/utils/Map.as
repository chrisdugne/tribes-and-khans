package com.uralys.utils
{

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;
		
public class Map
{
	private var links:ArrayCollection = new ArrayCollection();
	
	public function put(key:Object, value:Object):void{
		links.addItem(new Mapping(key, value));
	}
		
	public function remove(key:Object):void{
		
		var indexToRemoveInLinks:int = -1;
		for each(var mapping:Mapping in links){
			if(mapping.key == key){
				indexToRemoveInLinks = links.getItemIndex(mapping);
				break;
			}
		}
		
		if(indexToRemoveInLinks >= 0)
			links.removeItemAt(indexToRemoveInLinks);
	}	
		
	public function get(key:Object):Object{
		for each(var mapping:Mapping in links){
			if(mapping.key == key)
				return mapping.value;	
		}
		
		// the key specified does not exist
		return null;
	}

	public function getItemAt(i:int):Object{
		return values().getItemAt(i);
	}
	
	public function getKey(value:Object):Object{
		for each(var mapping:Mapping in links){
			if(mapping.value == value)
				return mapping.key;	
		}
		
		// the value specified does not exist
		return null;
	}

	public function keySet():ArrayCollection{
		var keySet:ArrayCollection = new ArrayCollection();
		
		for each(var mapping:Mapping in links){
			keySet.addItem(mapping.key);	
		}
		
		return keySet;
	}

	public function values():ArrayCollection{
		var values:ArrayCollection = new ArrayCollection();
		
		for each(var mapping:Mapping in links){
			values.addItem(mapping.value);	
		}
		
		return values;
	}

	public function getNbEntries():int{
		var keySet:ArrayCollection = keySet();
		return keySet.length; 
	}
	
	/*
	 *	Sort the keys AND the links
	 */
	public function sortKeys(sortFields:SortField):void{
		
		var keySet:ArrayCollection = keySet();
		
		var sort:Sort = new Sort();
		sort.fields = [sortFields];
	
		keySet.sort = sort;
		keySet.refresh();
		
		var newMapping:ArrayCollection = new ArrayCollection();
		for each (var key:Object in keySet){
			for each (var mapping:Mapping in links){
				if(mapping.key == key){
					newMapping.addItem(mapping);
					break;
				}
			}
		}
		
		links = newMapping;
	}
	
	public function removeAll():void{
		links.removeAll();
	}
}

}
	
class Mapping{

	private var _key:Object;
	private var _value:Object;

	public function get key():Object {
		return _key;
	}
	
	public function set key(o:Object):void {
		_key = o;
	}
	
	public function get value():Object {
		return _value;
	}
	
	public function set value(o:Object):void {
		_value = o;
	}

	public function Mapping(key:Object, value:Object):void{
		this.key = key;
		this.value = value;
	}
}