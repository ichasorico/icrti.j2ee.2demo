package ServMain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sgbd.bbdd;
import usuario.usuario;

/**
 * Servlet implementation class ServMain
 */
@WebServlet(description = "Servlet principal", urlPatterns = { "/ServMain" })

public class ServMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<usuario> sesiones = new ArrayList <usuario>();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServMain() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		if(validarRequest(request)){
			
		
	        if(request.getParameter("operacion") != null){
	        	response.sendRedirect("index.jsp");
	        
	        }
		}else{
			response.sendRedirect("logOut.html");
		}
		//response.sendRedirect("index.jsp");
        //request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
/*	private void getSchemas(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		String redirect = "index.jsp";
		//DETERMINA SI VIENE DEL PROCESO ANTERIOR DE LOGIN
		HttpSession session = request.getSession(true);
		session.removeAttribute("tablasSchema");
		//DEBERÁ ENCONTRAR EN LA SESIÓN UNA ENTRADA DENOMINADA "Login" QUE CONTIENE USUARIO/PWD DADO EN LA REQUEST
		
		if(request.getParameter("usuario") != null && request.getParameter("password") != null ){
			//ENCONTRAMOS USUARIO Y PWD. NECESITAMOS VALIDARLO CONTRA EL DATO EN LA SESIÃ“N
			String usrSession = (String)session.getAttribute("Login");			
			if(null != usrSession && usrSession.equals(request.getParameter("usuario") + "/" + request.getParameter("password"))){
				//SUPERA LA VALIDACIÓN DE LA SESIÓN
				bbdd db = new bbdd();
				if (db.Login(request.getParameter("usuario").toString(),request.getParameter("password").toString())){
					List<String> tablas = db.getListaTablasDatabase(request.getParameter("squemas"));
					if(null != tablas && tablas.size()>0){
						String strTablas = "";
						for (int i = 0; i < tablas.size(); i++) {
							strTablas += tablas.get(i) + "/";
						}
						
	    				System.out.println("schemaTables de " +request.getParameter("squemas")+ " -- " + strTablas.substring(0, strTablas.length()-1));
	    				session.setAttribute("schemaSelected",request.getParameter("squemas"));
	    				session.setAttribute("tablasSchema", strTablas.substring(0, strTablas.length()-1));
					}else{
						
						//NO SE RECUPERAN TABLAS PARA SCHEMA DADO
						System.out.println("getSchemas:NO SE RECUPERAN TABLAS PARA schema pedido -> " + request.getParameter("squemas") + ".");
						redirect+="?loginError";	
					}
				}else{
					
					//LE ENVIAMOS A LA PANTALLA DE ERROR AL NO VALIDAR LA BBDD LAS CREDENCIALES
					System.out.println("getSchemas::BBDD NO VALIDA LOGIN. " + request.getParameter("usuario") + "-" + request.getParameter("password"));
					redirect+="?loginError";					
				}
			}else{
				
				//LE ENVIAMOS A LA PANTALLA DE ERROR AL NO VALIDAR LOS DATOS DE SESIÃ“N CON EL LOGIN DE LA REQUEST
				System.out.println("getSchemas::NO VALIDA LOGIN. Datos sesiÃ³n = " + usrSession + " datos request = " + request.getParameter("usuario") + "/" + request.getParameter("password"));
				redirect+="?loginError";				
			}
		}else{
			//LE ENVIAMOS A LA PANTALLA DE ERROR AL NO ENCONTRAR LOS PARÃ�METROS NECESARIOS
			System.out.println("getSchemas::No se dectecta usr + pwd.Login errorrrr");
			redirect+="?loginError";
		}
		response.sendRedirect(redirect);
	}
*/	
    private boolean login(HttpServletRequest request){
    	
    	System.out.println("ServMain::login SOLICITUD DE LOGIN");
    	
    	if(request.getParameter("usuario") != null && request.getParameter("password") != null ){

    		HttpSession session = request.getSession(true);
    		bbdd db = new bbdd();
    		usuario usr = db.Login(request.getParameter("usuario").toString(),request.getParameter("password").toString());    		
    		if (usr.isUserOk()){
    			HttpSession curSesion = request.getSession();
	        	String idSesion = curSesion.getId();
	        	usr.setSesionID(idSesion);
    			sesiones.add(usr);
        		System.out.println("ServMain::login  -- Login ok");
        		return true;

    		}else{
    			
    		}
    			
/*    			List<String> squemas = db.getListaSqchemas();
    			if(null != squemas && squemas.size() > 0){
    				String strSchemas = "";
    				for (int i = 0; i < squemas.size(); i++) {
						strSchemas+=squemas.get(i)+"/";
					}
    				System.out.println("schemas " + strSchemas.substring(0, strSchemas.length()-1));
    				session.setAttribute("schemas", strSchemas.substring(0, strSchemas.length()-1));
    			}
*/  				

		}else{
			//request.setAttribute("loginError","error");
			System.out.println("ServMain::login  -- Login errorrrr. FALTAN CREDENCIALES");
			//redirect+="?loginError";
		}		
    	
    	//response.sendRedirect(redirect);
    	return false;
    }
           
    /**
     * DETERMINAMOS SI EL USUARIO ESTÁ LOGADO. 
     * PARA ELLO DETERMINAMOS 
     * 		SI TIENE EN LA REQUEST EL PARÁMETRO USUARIO
     * 			BUSCAMOS EL USUARIO EN LA LISTA DE sesiones Y VALIDAMOS EL ID_SESIÓN
     * 		SI NO LO TIENE DETERMINAMOS SI NOS ESTÁ PIDIENDO LOGIN
     * 
     * @param request
     * @return
     */
    private boolean validarRequest(HttpServletRequest request){    	
    
    	
    	HttpSession curSesion = request.getSession();
    	String idSesion = curSesion.getId();
    	//BUSCAMOS EN sesiones OBJETOS CON ESTE ID_SESSIÓN
    	for (usuario u : sesiones){
    		if(u.getSesionID().equalsIgnoreCase(idSesion)){
	        	HttpSession session = request.getSession(true);
	        	session.setAttribute("isAdmin", u.isAdmin());
	        	System.out.println("ServMain::validarRequest  -- Se localiza la sesión con usuario "  + u.getNombre() + "("+u.getIdUsuario()+")");
    			return true;
    		}
    	}
    	
    	

    	//LLEGADOS A ESTE PUNTO NO SE ENCUENTRA AL USUARIO LOGADO POR SESIÓN.
    	//DETERMINAMOS SI NOS ESTÁ PIDIENDO LOGIN
    	System.out.println("ServMain::validarRequest  -- Acceso no logado. Se determinará si pide Login");
    	
    	if(request.getParameter("usuario") != null && !"".equalsIgnoreCase(request.getParameter("usuario"))){
    		String usuario = request.getParameter("usuario");

        	if(request.getParameter("operacion") != null && !"".equalsIgnoreCase(request.getParameter("operacion"))){
        		String operacion = request.getParameter("operacion");		        	
        		if(operacion.equals("login") || operacion.equals("login4Admin")){
        			return login(request);		        			
        		}else{
        			System.out.println("ServMain::validarRequest  -- Usuario No Logado y Request operación que no es de LOGIN");
        		}
        	}else{
	        	System.out.println("ServMain::validarRequest  -- Usuario No Logado y Request sin solicitud de LOGIN");
        	}	        	
    	  }else{
    		  System.out.println("ServMain::validarRequest  -- La request no contiene usuario");  
    	  }
    	  
	      return false;
    }
}
