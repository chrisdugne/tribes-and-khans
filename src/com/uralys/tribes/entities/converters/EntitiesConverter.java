package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Profil;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;

public class EntitiesConverter {
	
	public static Move convertMoveDTO(MoveDTO moveDTO) {
		
		if(moveDTO == null)
			return null;
		
		Move move = new Move();
		
		move.setMoveUID(moveDTO.getMoveUID());
		move.setxFrom(moveDTO.getxFrom());
		move.setxTo(moveDTO.getxTo());
		move.setyFrom(moveDTO.getyFrom());
		move.setyTo(moveDTO.getyTo());
		
		return move;
	}
	
	public static Player convertPlayerDTO(PlayerDTO playerDTO) {
		
		if(playerDTO == null)
			return null;
		
		Player player = new Player();
		
		player.setPlayerUID(playerDTO.getPlayerUID());
		player.setName(playerDTO.getName());
		player.setGameName(playerDTO.getGameName());
		player.setGameUID(playerDTO.getGameUID());
		
		List<Move> moves = new ArrayList<Move>();
		
		for(MoveDTO moveDTO : playerDTO.getMoves()){
			moves.add(convertMoveDTO(moveDTO));
		}
		
		player.setMoves(moves);
		
		return player;
	}
	
	public static Game convertGameDTO(GameDTO gameDTO) {

		if(gameDTO == null)
			return null;
		
		Game game = new Game();
		
		game.setGameUID(gameDTO.getGameUID());
		game.setName(gameDTO.getName());
		game.setStatus(gameDTO.getStatus());
		game.setCurrentTurn(gameDTO.getCurrentTurn());
		game.setAutoEndTurnTime(gameDTO.getAutoEndTurnPeriod());
		
		List<Player> players = new ArrayList<Player>();
		
		for(PlayerDTO playerDTO : gameDTO.getPlayers()){
			players.add(convertPlayerDTO(playerDTO));
		}
		
		game.setPlayers(players);

		return game;
	}

	public static Profil convertProfilDTO(ProfilDTO profilDTO) {
		
		if(profilDTO == null)
			return null;
		
		Profil profil = new Profil();
		
		profil.setUralysUID(profilDTO.getUralysUID());
		
		List<Player> players = new ArrayList<Player>();
		
		for(PlayerDTO playerDTO : profilDTO.getPlayers()){
			players.add(convertPlayerDTO(playerDTO));
		}
		
		profil.setPlayers(players);
		
		return profil;
	}
}
