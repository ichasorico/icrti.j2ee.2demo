package sgbd;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import sgbd.dbcrypt.BCrypt;
import usuario.usuario;

public class bbdd {

	
	  InitialContext ctx = null;
	  DataSource ds = null;
	  Connection conn = null;
	  boolean conexion = false;
	  boolean loginOK = false;



	public bbdd(){
		try{
			ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/sqlLocal");    	    
		    conn = ds.getConnection();
		    conexion=true;
		    System.out.println("bbdd::INIT-Conexión a BBDD realizado");
		}catch(Exception e){
			System.out.println("bbdd::INIT-ERRORRRR al intentar Conexión a BBDD " + e.toString());
		}	    
	  }
	  
	public List<String> getListaTablasDatabase(String database){
		List<String> tablas = new ArrayList<String>();
		String sql = "SELECT * FROM INFORMATION_SCHEMA.tables WHERE TABLE_SCHEMA='" + database + "'";
		try {
			Statement sentencia = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultado = sentencia.executeQuery(sql);
			while (resultado.next()) {
				tablas.add(resultado.getString("TABLE_NAME"));
			}
            return tablas;
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	  public usuario Login(String user, String pwd){
		  
		  usuario u = new usuario();
		  
		  if (conexion){
			  
			  try {				  
			  System.out.println("bbdd:Login() --  Intento Login usuario="+user + " pwd="+pwd);	    	    
				if(validarUsuario(user,pwd)){
					// CUMPLIMENTAR OBJETO USUARIO
					u.setNombre(user);
					cumplimentarObjetoUSuario(u);					
					
					
				}else{
					// DEVOLVER USUARIO VACÍO
					
				}				
			  }catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
		  }else{
			  System.out.println("bbdd::Login("+user+","+pwd+")  --  NO HAY CONEXIÓN A LA BBDD");
		  }
	  	return u;
	  }
	  
	  
	  public List<String> getListaSqchemas(){
		  
		List<String> squemas = new ArrayList<String>();
		try {
			ResultSet rs = conn.getMetaData().getCatalogs();
			while (rs.next()) {
				System.out.println("Recupera schema " + rs.getString("TABLE_CAT"));
				squemas.add(rs.getString("TABLE_CAT"));
			}
			return squemas;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		  
		return null;
		 		  
	  }

	
	private boolean validarUsuario(String u, String p){
		

		String sql = "SELECT * FROM usuarios where nombre = '" + u + "'";
		try {
			Statement sentencia = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultado = sentencia.executeQuery(sql);
			String storedPassword = "";
			if (resultado.first()){
				
				do {
					//System.out.println("bbdd::validarUsuario("+u+" , "+p+")  -- Encuentra usuario en bbdd");
					storedPassword = resultado.getString("pwd");
					//String generatedSecuredPasswordHash = BCrypt.hashpw(p, BCrypt.gensalt(12));
					//System.out.println("bbdd::validarUsuario generatedSecuredPasswordHash + recoveredSecuredPasswordHash");
					//System.out.println(generatedSecuredPasswordHash);
					//System.out.println(storedPassword);				
					//System.out.println("bbdd::validarUsuario LongitudPasswordHash="+generatedSecuredPasswordHash.length());
					//boolean matched = BCrypt.checkpw(p, generatedSecuredPasswordHash);
					//System.out.println(matched);
					
					loginOK = BCrypt.checkpw(p, storedPassword);
					System.out.println("bbdd::validarUsuario("+u+" , "+p+")  -- Recupera storedPassword = " + storedPassword + " Validación " + loginOK);
				}while (resultado.next());
			}else{
				System.out.println("bbdd::validarUsuario("+u+" , "+p+")  -- No se encuentra el usuario. LOGIN ERROR  ");
				loginOK = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}catch (Exception e){
			e.printStackTrace();
		}
		return loginOK;
	}
	
	private void cumplimentarObjetoUSuario(usuario u){
		

		String sql = "SELECT * FROM usuarios where nombre = '" + u.getNombre() + "'";
		try {
			Statement sentencia = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultado = sentencia.executeQuery(sql);
			while (resultado.next()) {
				u.setIdUsuario(resultado.getString("idUsuario")); 
			}            
			
			sql = "SELECT * FROM userroles where idUsuario = '" + u.getIdUsuario() + "'";
			Statement sentencia2 = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultado2 = sentencia2.executeQuery(sql);
			while (resultado2.next()) {
				u.setIdUsuario(resultado2.getString("idRol"));
				sql = "SELECT * FROM roles where idRoles = '" + resultado2.getString("idRol") + "'";				
				ResultSet res2 = sentencia.executeQuery(sql);
				while (res2.next()) {
					u.setIdRol(res2.getString("idroles"));
					if("1".equalsIgnoreCase(res2.getString("admin"))) u.setAdmin(true);
				}
			}		
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}	
	
}
