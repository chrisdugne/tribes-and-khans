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
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.utils.Utils;

@SuppressWarnings("serial")
public class TribesAndKhansServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		String uralysUID = Utils.generateUID();
		ProfilDTO profil = new ProfilDTO();		
		Key key = KeyFactory.createKey(ProfilDTO.class.getSimpleName(), uralysUID);
		
		profil.setKey(KeyFactory.keyToString(key));
		profil.setUralysUID(uralysUID);
		
		
		String playerUID1 = Utils.generateUID();
		PlayerDTO player1 = new PlayerDTO();		
		Key key2 = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID1);
		String playerUID2 = Utils.generateUID();
		PlayerDTO player2 = new PlayerDTO();		
		Key key3 = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID2);
		
		player1.setKey(KeyFactory.keyToString(key2));
		player1.setPlayerUID(playerUID1);
		player1.setName(Utils.generatePassword());

		player2.setKey(KeyFactory.keyToString(key3));
		player2.setPlayerUID(playerUID2);
		player2.setName(Utils.generatePassword());
		
		
		profil.getPlayers().add(player1);
		profil.getPlayers().add(player2);
		
		pm.makePersistent(profil);
		pm.flush();
		pm.refreshAll();

		//===================================================================//
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
