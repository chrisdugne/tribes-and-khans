package com.uralys.tribes.domain;

import com.uralys.tribes.entities.MoveResult;
import com.uralys.tribes.entities.Unit;

public interface IMovesManager {

	//-----------------------------------------------------------------------------------//
	
	public MoveResult refreshUnitMoves(Unit unit);
	
	//-----------------------------------------------------------------------------------//
	
}
