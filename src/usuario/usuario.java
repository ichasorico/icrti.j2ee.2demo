package usuario;

import java.util.ArrayList;
import java.util.List;

public class usuario {

	String idUsuario, nombre, sesionID;
	List<String> idRoles = new ArrayList <String> ();
	
	
	boolean isAdmin = false;
	
	public void setIdRol(String id){
		
		if(null!=id && !"".equalsIgnoreCase(id))
			idRoles.add(id);	
	}
	
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}	
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	/**
	 * DETERMINA SI UN USUARIO ESTÁ CORRECTAMENTE FORMADO. PARA ELLO VALIDA SI DISPONE DE 
	 * 		1.- NOMBRE E ID_USUARIO
	 * 		2.- ROLES ASIGNADOS
	 * @return
	 */
	public boolean isUserOk(){
		if(	null != nombre && !"".equalsIgnoreCase(nombre) &&
			null != idUsuario && !"".equalsIgnoreCase(idUsuario) &&
			null != idRoles && idRoles.size() > 0){
			
			System.out.println("usuario::isUserOk  --  Validación corecta objeto: nombre " + this.nombre + " idUsuario = " + this.idUsuario + " número de roles " + idRoles.size());
			return true;
			
			
		}else{
			System.out.println("usuario::isUserOk  --  ERRORRRR Validación objeto: nombre " + this.nombre + " idUsuario = " + this.idUsuario + " número de roles " + idRoles.size());
			return false;
		}
	}
	
	
	public void usuario(){
		this.nombre = "";
		this.sesionID= "";		
		this.idUsuario= "";
		
	}
	
	
	public String getSesionID() {
		return sesionID;
	}

	public void setSesionID(String sesionID) {
		this.sesionID = sesionID;
	}


}
