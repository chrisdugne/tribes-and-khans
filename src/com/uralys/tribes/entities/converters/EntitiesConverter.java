package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;

public class EntitiesConverter {
	
	public static Move convertMoveDTO(MoveDTO moveDTO) {
		
		Move move = new Move();
		
		move.setMoveUID(moveDTO.getMoveUID());
		move.setxFrom(moveDTO.getxFrom());
		move.setxTo(moveDTO.getxTo());
		move.setyFrom(moveDTO.getyFrom());
		move.setyTo(moveDTO.getyTo());
		
		return move;
	}
	
	public static Player convertPlayerDTO(PlayerDTO playerDTO) {
		
		Player player = new Player();
		
		player.setUralysUID(playerDTO.getUralysUID());
		player.setEmail(playerDTO.getEmail());
		player.setPassword(playerDTO.getPassword());
		player.setName(playerDTO.getName());
		
		List<Move> moves = new ArrayList<Move>();
		
		for(MoveDTO moveDTO : playerDTO.getMoves()){
			moves.add(convertMoveDTO(moveDTO));
		}
		
		player.setMoves(moves);
		
		return player;
	}
	
}
