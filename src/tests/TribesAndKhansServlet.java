package tests;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.impl.PMF;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.utils.Utils;

@SuppressWarnings("serial")
public class TribesAndKhansServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		
		if(req.getParameter("createProfil") != null){
			String uralysUID = Utils.generateUID();
			ProfilDTO profil = new ProfilDTO();		
			Key key = KeyFactory.createKey(ProfilDTO.class.getSimpleName(), uralysUID);
			
			profil.setKey(KeyFactory.keyToString(key));
			profil.setUralysUID(uralysUID);
			
			pm.makePersistent(profil);
			pm.close();
		}
		else if(req.getParameter("createPlayer") != null){
			
			String playerUID = Utils.generateUID();
			PlayerDTO player = new PlayerDTO();		
			Key key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID);
			
			player.setKey(KeyFactory.keyToString(key));
			player.setPlayerUID(playerUID);
			player.setName(req.getParameter("createPlayer"));
			
			ProfilDTO profil = pm.getObjectById(ProfilDTO.class, "128998298816601660674803");
			profil.getPlayers().add(player);
			
			pm.flush();
			pm.refreshAll();
			
		}
		else if(req.getParameter("createMoves") != null){
			
			String moveUID = Utils.generateUID();
			MoveDTO move = new MoveDTO();		
			Key key = KeyFactory.createKey(MoveDTO.class.getSimpleName(), moveUID);
			
			//org.datanucleus.store.appengine.query.StreamingQueryResult cannot be cast to com.uralys.tribes.entities.dto.PlayerDTO
			Query q = pm.newQuery("select from " + PlayerDTO.class.getName() + " where name == 'coco'");
			PlayerDTO player = (PlayerDTO) q.execute();
			
			move.setKey(KeyFactory.keyToString(key));
			move.setPlayer(player);
			move.setxFrom(10);
			move.setxTo(22);
			move.setyFrom(10);
			move.setyTo(12);
			
			
			pm.makePersistent(move);
			
		}
		else if(req.getParameter("players") != null){
			PrintWriter out = resp.getWriter();
			
			Query q = pm.newQuery("select from " + ProfilDTO.class.getName());

			@SuppressWarnings("unchecked")
			List<ProfilDTO> profils = (List<ProfilDTO>) q.execute();
			for(ProfilDTO p : profils){
				out.println("profil : " + p.getUralysUID() + "<br/>");
				for(PlayerDTO pa : p.getPlayers()){
					out.println("player : " + pa.getPlayerUID() + " " + pa.getName()  + "<br/>");				
				}
			}
			out.close();
			
			pm.close();
		}
	}
}
