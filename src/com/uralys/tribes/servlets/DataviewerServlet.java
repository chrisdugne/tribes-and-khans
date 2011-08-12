package com.uralys.tribes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.impl.UniversalDAO;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.GatheringDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MessageDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ServerDataDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
import com.uralys.tribes.entities.dto.StockDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.utils.Utils;


public class DataviewerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String PASSWORD = "3bc7c708f8d865b506ffd1acde3b47f61af9445d";
	private String VERSION = "1.1.2";


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGet (HttpServletRequest req,
			HttpServletResponse res)
	throws ServletException, IOException
	{
		PrintWriter out = res.getWriter();

		if(!req.getRemoteHost().equals("127.0.0.1") && !req.getRequestURL().toString().startsWith("https")){
			out.println("<meta http-equiv=\"refresh\" content=\"0.1; URL=https://tribes-and-khans.appspot.com/dataviewer\"/>");	
			out.close();
			return;
		}
			

		if(!req.getRemoteHost().equals("127.0.0.1") && (req.getParameter("pwd")==null || !Utils.SHA1(req.getParameter("pwd")).equals(PASSWORD))){
			out.println("<html><body>");
			out.println("<center><span style=\"color:#009933\"><h2>Tribes And Khans Dataviewer - "+VERSION+" (Off)</h2></center>");
			out.println("</body></html>");
			out.close();
			return;
		}
		
//		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
//		InitDAO initDao = (InitDAO) appContext.getBean("initDao");
//		initDao.createRio("324567");
//		initDao.createRio("3245674");
//		initDao.createRio("3245677");
//		initDao.createRio("3245679");
//		initDao.createRio("3245671");
//		initDao.createRio("32456123");
//		initDao.createRio("3245234");
//		initDao.createRio("327867");
//		initDao.createRio("324587967");
//		initDao.createRio("327897");
//		initDao.createRio("388724567");
//		initDao.createRio("39624567");

//		initDao.updateFools();

		UniversalDAO universalDao = UniversalDAO.getInstance();

		out.println("<html>" +
					"<head>" +
					"<SCRIPT TYPE=\"text/javascript\">" +
					"<!--" +
					"function dropdown(mySel)" +
					"{" +
					"var myWin, myVal;" +
					"myVal = mySel.options[mySel.selectedIndex].value;" +
					"if(myVal)" +
					"   {" +
					"   if(mySel.form.target)myWin = parent[mySel.form.target];" +
					"   else myWin = window;" +
					"   if (! myWin) return true;" +
					"   myWin.location = myVal;" +
					"   }" +
					"return false;" +
					"}" +

					"//-->" +
					"</SCRIPT>" +

					
					"<STYLE TYPE=\"text/css\">" +
					
					"tr.title" +
					"{" +
					"	font-weight: bold ;" +
					"	background: #c5d7ef ;" +
					"}" +
					"" +
					"tr" +
					"{" +
					"	padding: .25em 1.5em .5em .5em;" +
					"}" +
					"" +
					"td" +
					"{" +
					"	text-align: center;" +
					"}" +
					"</style>" +
					
					
					"" +
					"</head>" +
					"<body>");
		out.println("<center><span style=\"color:#009933\"><h2>Tribes And Khans Dataviewer - "+VERSION+"</h2></center>");

		out.println("<hr>");
		out.println("<br>");
		
		String dropdown = "<FORM " +
				" METHOD=GET onSubmit=\"return dropdown(this.dto)\">" +
				" <SELECT NAME=\"dto\" onChange=\"this.form.submit()\">" +
				" <OPTION VALUE=\"___\">Choose a DTO..." +

				//-----------------------------------------------------------------------------------//				// - HERE : add a line for every new DTO
				
				" <OPTION VALUE=\"player\">PlayerDTO" +
				" <OPTION VALUE=\"ally\">AllyDTO" +
				" <OPTION VALUE=\"city\">CityDTO" +
				" <OPTION VALUE=\"stock\">StockDTO" +
				" <OPTION VALUE=\"unit\">UnitDTO" +
				" <OPTION VALUE=\"smith\">SmithDTO" +
				" <OPTION VALUE=\"equipment\">EquipmentDTO" +
				" <OPTION VALUE=\"case\">CaseDTO" +
				" <OPTION VALUE=\"move\">MoveDTO" +
				" <OPTION VALUE=\"gathering\">GatheringDTO" +
				" <OPTION VALUE=\"message\">MessageDTO" +
				" <OPTION VALUE=\"item\">ItemDTO" +
				" <OPTION VALUE=\"serverdata\">ServerDataDTO" +
				
				//-----------------------------------------------------------------------------------//
								" </SELECT>" +
				" <INPUT TYPE=\"text\" name=\"pwd\" value=\""+req.getParameter("pwd")+"\"/>" +
				"</FORM>";
		out.println(dropdown);
		out.println("<br>");
		out.println("<input type=\"button\" value=\"Refresh\" onclick=\"window.location.reload();\">");

		
		//-----------------------------------------------------------------------------------//
		// - edit				
		if(req.getParameter("action") != null && req.getParameter("action").equals("confirmRemoveAll")){
			out.println("<input type=\"button\" value=\"Confirm Remove All\" onclick=\"window.location = 'dataviewer?action=removeAll&pwd="+req.getParameter("pwd")+"&entity="+req.getParameter("entity")+"'\">");
			out.println("<input type=\"button\" value=\"Cancel\" onclick=\"window.location = 'dataviewer?pwd="+req.getParameter("pwd")+"&dto="+req.getParameter("entity")+"'\">");
			out.println("<h4>"+getDTOClass(req.getParameter("entity"))+"</h4>");
			
		}
		else if(req.getParameter("action") != null && req.getParameter("action").equals("removeAll")){
			Class selectedDTO = getDTOClass(req.getParameter("entity"));
			
			universalDao.deleteAll(selectedDTO);
			out.println("<meta http-equiv=\"refresh\" content=\"0.1; URL=/dataviewer?pwd="+req.getParameter("pwd")+"&dto="+req.getParameter("entity")+"\">");	
		}
		else if(req.getParameter("action") != null && req.getParameter("action").equals("edit")){

			
			Class selectedDTO = getDTOClass(req.getParameter("entity"));
			
			Object o = universalDao.getObjectDTO(req.getParameter("___uid"), selectedDTO);
			Field[] fields = o.getClass().getDeclaredFields();
			
			// [0] : key |  [1] : objectUID
			String idName = fields[1].getName();

			StringBuffer editTable = new StringBuffer();
			StringBuffer parametersToSave= new StringBuffer();

			editTable.append("<table>");
			
			for(Field field : fields){
				
				if(field.getName().startsWith("key") || field.getName().startsWith("jdo"))
					continue;
			
				if(field.getType().getName().equals("com.google.appengine.api.datastore.Text"))
					continue;
				
				if(field.getType().getName().equals("java.util.Date"))
					continue;
				
				try {
					field.setAccessible(true);
					editTable.append("<tr>");
					
					parametersToSave.append(field.getName() + "="+field.getName()+".value,");
					
					if(field.getName().equals(idName))
						editTable.append("<td><b>"+field.getName() + "</b> </td><td>" + field.get(o) + "</td>");
					else if(field.getType().getName().equals("java.lang.Boolean"))
						editTable.append("<td>"+field.getName() + "</td><td><select name="+field.getName()+"><option value=" + field.get(o) + ">"+ field.get(o) +"<option value=" + !(Boolean)field.get(o) + ">"+ !(Boolean)field.get(o) +"</td>");
					else if(field.getType().getName().equals("java.util.List"))
						editTable.append("<td>"+field.getName() + "</td><td><textarea style=\"width: 280px;\" rows=\""+((List)field.get(o)).size()+"\" name=\""+field.getName()+"\">" + field.get(o) + "</textarea></td>");
					else
						editTable.append("<td>"+field.getName() + "</td><td><input type=\"text\" name=\""+field.getName()+"\" value=\"" + field.get(o) + "\" /></td>");
					editTable.append("</tr>");
				} 
				catch (IllegalArgumentException e) {} 
				catch (IllegalAccessException e) {}
			}

			out.println("<form METHOD=GET onSubmit=\"document.window.href='/dataviewer?action=save&entity="+req.getParameter("entity")+"&uid="+req.getParameter("uid")+"'\">");
			out.println(editTable.toString());
			out.println("<input type=\"button\" value=\"Save\" onClick=\"this.form.submit();\"/>");
			out.println("<input type=\"hidden\" value=\""+req.getParameter("entity")+"\" name=\"entity\"/>");
			out.println("<input type=\"hidden\" value=\""+req.getParameter("___uid")+"\" name=\"___uid\"/>");
			out.println("<input type=\"hidden\" value=\""+req.getParameter("pwd")+"\" name=\"pwd\"/>");
			out.println("<input type=\"hidden\" value=\"save\" name=\"action\"/>");
			out.println("</form>");
		}

		//-----------------------------------------------------------------------------------//
		// - edit		
		else if(req.getParameter("action") != null && req.getParameter("action").equals("create")){
			
			
			Class selectedDTO = getDTOClass(req.getParameter("entity"));
			
			Object o = new Object();
			try {
				o = selectedDTO.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
			Field[] fields = o.getClass().getDeclaredFields();
			
			// [0] : key |  [1] : objectUID
			String idName = fields[1].getName();
			
			StringBuffer editTable = new StringBuffer();
			StringBuffer parametersToSave= new StringBuffer();
			
			editTable.append("<table>");
			editTable.append("<td>specify uid ? </td><td><input type=\"text\" name=\"UIDSpecified\" value=\"null\" /></td>");
			
			for(Field field : fields){
				
				if(field.getName().startsWith("key") || field.getName().startsWith("jdo"))
					continue;
				
				if(field.getType().getName().equals("com.google.appengine.api.datastore.Text"))
					continue;
				
				if(field.getType().getName().equals("java.util.Date"))
					continue;
				
				try {
					field.setAccessible(true);
					editTable.append("<tr>");
					
					parametersToSave.append(field.getName() + "="+field.getName()+".value,");
					
					if(field.getName().equals(idName))
						editTable.append("<td><b>"+field.getName() + "</b> </td><td>" + field.get(o) + "</td>");
					else if(field.getType().getName().equals("java.lang.Boolean"))
						editTable.append("<td>"+field.getName() + "</td><td><select name="+field.getName()+"><option value=true>true<option value=false>false</td>");
					else if(field.getType().getName().equals("java.util.List"))
						editTable.append("<td>"+field.getName() + "</td><td><textarea style=\"width: 280px;\" rows=\""+((List)field.get(o)).size()+"\" name=\""+field.getName()+"\">" + field.get(o) + "</textarea></td>");
					else
						editTable.append("<td>"+field.getName() + "</td><td><input type=\"text\" name=\""+field.getName()+"\" value=\"" + field.get(o) + "\" /></td>");
					editTable.append("</tr>");
				} 
				catch (IllegalArgumentException e) {} 
				catch (IllegalAccessException e) {}
			}
			
			out.println("<form METHOD=GET onSubmit=\"document.window.href='/dataviewer?action=save&entity="+req.getParameter("entity")+"&uid="+req.getParameter("uid")+"'\">");
			out.println(editTable.toString());
			out.println("<input type=\"button\" value=\"Save\" onClick=\"this.form.submit();\"/>");
			out.println("<input type=\"hidden\" value=\""+req.getParameter("entity")+"\" name=\"entity\"/>");
			out.println("<input type=\"hidden\" value=\""+req.getParameter("___uid")+"\" name=\"___uid\"/>");
			out.println("<input type=\"hidden\" value=\""+req.getParameter("pwd")+"\" name=\"pwd\"/>");
			out.println("<input type=\"hidden\" value=\"save\" name=\"action\"/>");
			out.println("</form>");
		}
		
		//-----------------------------------------------------------------------------------//
		// - delete
		
		else if(req.getParameter("action") != null && req.getParameter("action").equals("delete")){
			Class selectedDTO = getDTOClass(req.getParameter("entity"));
			
			universalDao.delete(selectedDTO, req.getParameter("___uid"));
			out.println("<meta http-equiv=\"refresh\" content=\"0.1; URL=/dataviewer?pwd="+req.getParameter("pwd")+"&dto="+req.getParameter("entity")+"\">");	
		}
		
		//-----------------------------------------------------------------------------------//
		// - save the edition
		
		else if(req.getParameter("action") != null && req.getParameter("action").equals("save")){
			
			boolean newInstanceIsCreated = req.getParameter("___uid").equals("null");
			
			String instanceUID = null;
			Key key = null;
			
			Class selectedDTO = getDTOClass(req.getParameter("entity"));
			
			Object instance = new Object();
			
			if(newInstanceIsCreated){
				if(!req.getParameter("UIDSpecified").equals("null"))
					instanceUID = req.getParameter("UIDSpecified");
				else
					instanceUID = Utils.generateUID();
				
				key = KeyFactory.createKey(selectedDTO.getSimpleName(), instanceUID);
			
				try {
					instance = selectedDTO.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace(System.out);
				} catch (IllegalAccessException e) {
					e.printStackTrace(System.out);
				}
			}
			else
			 instance = universalDao.getObjectDTO(req.getParameter("___uid"), selectedDTO);

			for(Field field : instance.getClass().getDeclaredFields()){

				field.setAccessible(true);
				
				if(newInstanceIsCreated){
					if(field.getName().endsWith("UID") 
					&& field.getName().toLowerCase().startsWith(req.getParameter("entity").toLowerCase())){
						try {
							field.set(instance, instanceUID);
						} catch (Exception e) {
							e.printStackTrace(System.out);
						}
						
						continue;
					}
					
					if(field.getName().startsWith("key")){
						try {
							field.set(instance, KeyFactory.keyToString(key));
						} catch (IllegalArgumentException e) {
							e.printStackTrace(System.out);
						} catch (IllegalAccessException e) {
							e.printStackTrace(System.out);
						}
						
						continue;
					}
						
					
				}
				
				if(field.getName().startsWith("key") || field.getName().startsWith("jdo"))
					continue;

				
				try {
					String value = req.getParameter(field.getName());

					if(value == null)
						continue;

					if(value.equals("null") || value.equals("") )
						value = null;
					
					if(value != null && value.startsWith("["))
						field.set(instance, createList(value));
					else if(field.getType().getName().equals("java.lang.Integer") || field.getType().getName().equals("int"))
						field.set(instance, value == null ? null : Integer.parseInt(value));
					else if(field.getType().getName().equals("java.lang.Boolean") || field.getType().getName().equals("boolean"))
						field.set(instance, value == null ? null : Boolean.parseBoolean(value));
					else if(field.getType().getName().equals("java.lang.Float") || field.getType().getName().equals("float"))
						field.set(instance, value == null ? null : Float.parseFloat(value));
					else if(field.getType().getName().equals("java.lang.Double") || field.getType().getName().equals("double"))
						field.set(instance, value == null ? null : Double.parseDouble(value));
					else if(field.getType().getName().equals("java.lang.Long") || field.getType().getName().equals("long"))
						field.set(instance, value == null ? null : Long.parseLong(value));
					else{
						// String
						field.set(instance, value);
					}
						
					
				} catch (SecurityException e) {
					e.printStackTrace(System.out);
					break;
				} catch (IllegalAccessException e) {
					e.printStackTrace(System.out);
					break;
				}
			}

			if(newInstanceIsCreated)
				universalDao.makePersistent(instance);
			else
				universalDao.update(instance, req.getParameter("___uid"));
			
			
			
			out.println("<meta http-equiv=\"refresh\" content=\"0.1; URL=/dataviewer?pwd="+req.getParameter("pwd")+"&dto="+req.getParameter("entity")+"\">");	
		}
		
		//-----------------------------------------------------------------------------------//		// - Display
		
		else if(req.getParameter("dto") != null && !req.getParameter("dto").equals("___")){

			Class selectedDTO = getDTOClass(req.getParameter("dto"));
			out.println("<input type=\"button\" value=\"Create new\" onclick=\"window.location = 'dataviewer?action=create&pwd="+req.getParameter("pwd")+"&entity="+req.getParameter("dto")+"'\">");
			out.println("<input type=\"button\" value=\"Remove All!\" onclick=\"window.location = 'dataviewer?action=confirmRemoveAll&pwd="+req.getParameter("pwd")+"&entity="+req.getParameter("dto")+"'\">");
			out.println("<h4>"+selectedDTO.getSimpleName()+"</h4>");
			
			int from = 1;
			int to = 100;
			
			if(req.getParameter("from") != null)
				from = Integer.parseInt(req.getParameter("from"));
			if(req.getParameter("to") != null)
				to = Integer.parseInt(req.getParameter("to"));
				
			List<Object> list = universalDao.getListDTO(selectedDTO, from, to);
			
			String table = " <table class=\"entities\" cellpadding=\"2\">" +
							"<tr class=\"title\"> ";
			
			table += "<td style=\"min-width:150px; background:#BCE954\"> Actions </td>";
			for(Field field : selectedDTO.getDeclaredFields()){
				if(field.getName().startsWith("key") || field.getName().startsWith("jdo"))
					continue;
				table += "<td> "+field.getName()+" </td>";
			}
			table += "</tr>";


			for(Object o : list)
			{
				Field[] fields = o.getClass().getDeclaredFields();
				fields[1].setAccessible(true);
				String objectUID = "";
				
				try {
					objectUID = (String) fields[1].get(o);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				table += "<tr>";
				table += "<td>" +
				" <a href=\"/dataviewer?pwd="+req.getParameter("pwd")+"&entity="+req.getParameter("dto")+"&action=edit&___uid="+objectUID+"\">Edit</a>" +
				" &nbsp;&nbsp;&nbsp;" +
				" <a href=\"/dataviewer?pwd="+req.getParameter("pwd")+"&entity="+req.getParameter("dto")+"&action=delete&___uid="+objectUID+"\">Delete</a>" +
				" </td>";
				for(Field field : fields){
					if(field.getName().startsWith("key") || field.getName().startsWith("jdo"))
						continue;
					try {
						field.setAccessible(true);
						if(field.getName().contains("Millis")) // Millis : disp date
							table += "<td> "+new Date((Long)field.get(o))+" </td>";
						else // all other fields
							table += "<td> "+field.get(o)+" </td>";
					} catch (IllegalArgumentException e) {
						table += "<td> ___PROBLEM___ </td>";
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						table += "<td> ___PROBLEM___ </td>";
					}
				}
			
				table += "</tr>";
			}
			table += "</table>";
			
			out.println(table);
		}

		out.println("</body></html>");
		out.close();
	}


	/**
	 * convert [a,b,c,d]
	 * to List<String> {a,b,c,d}
	 */
	private List<String> createList(String s) {
		
		if(s.equals("[]"))
			return null;
		 
		List<String> list = new ArrayList<String>();
		
		//removing the '[' and ']'
		s = s.substring(1, s.length()-1);
		String[] values = s.split(",");
		
		for(String value : values){
			list.add(value.trim());
		}
		
		return list;
	}


	@SuppressWarnings({"rawtypes" })
	private Class getDTOClass(String dto) {
		
		//-----------------------------------------------------------------------------------//
		// - HERE : add a condition for every new DTO

		if(dto.equals("player"))
			return PlayerDTO.class;
		else if(dto.equals("ally"))
			return AllyDTO.class;
		else if(dto.equals("city"))
			return CityDTO.class;
		else if(dto.equals("stock"))
			return StockDTO.class;
		else if(dto.equals("smith"))
			return SmithDTO.class;
		else if(dto.equals("unit"))
			return UnitDTO.class;
		else if(dto.equals("case"))
			return CaseDTO.class;
		else if(dto.equals("equipment"))
			return EquipmentDTO.class;
		else if(dto.equals("item"))
			return ItemDTO.class;
		else if(dto.equals("move"))
			return MoveDTO.class;
		else if(dto.equals("gathering"))
			return GatheringDTO.class;
		else if(dto.equals("message"))
			return MessageDTO.class;
		else if(dto.equals("serverdata"))
			return ServerDataDTO.class;
		
		//never
		return null;

		//-----------------------------------------------------------------------------------//
	}	
}

