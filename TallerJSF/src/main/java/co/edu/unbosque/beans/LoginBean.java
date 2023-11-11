package co.edu.unbosque.beans;

import java.util.ArrayList;
import java.util.regex.Pattern;

import co.edu.unbosque.controller.HttpClientSynchronous;
import co.edu.unbosque.util.AESUtil;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class LoginBean {

	private String nombre;
	private double note1;
	private double note2;
	private double note3;
	private double average;
	private ArrayList<String[]> listUsers;
	

	public LoginBean() {
		nombre = "";
		listUsers=users();
	}

	// https://mkyong.com/java/how-to-send-http-request-getpost-in-java/
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getNote1() {
		return note1;
	}

	public void setNote1(double note1) {
		this.note1 = note1;
	}

	public double getNote2() {
		return note2;
	}

	public void setNote2(double note2) {
		this.note2 = note2;
	}

	public double getNote3() {
		return note3;
	}

	public void setNote3(double note3) {
		this.note3 = note3;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}
	
	public ArrayList<String[]> getListUsers() {
		loadUsers();
		return listUsers;
	}
	
	public void setListUsers(ArrayList<String[]> listUsers) {
		this.listUsers = listUsers;
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
	
	
	public void showSticky() {

		if(average >= 3.1) {
		
		FacesContext.getCurrentInstance().addMessage("sticky-key",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Your notes doesnt have problem"));
		}
		else if(average < 3.1 && average>= 3) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "WARNING", "Your average is 3, becareful"));
		}
		else if(average < 3) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "DANGER", "You are losing the semester"));
		}else {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sticky Message", "Something is wrong"));
		}
	}

	public void validate() {
		showSticky();
	}
	
	public void createUser() {
		HttpClientSynchronous.doPost("http://localhost:8081/usernote/createuser",
				"{\"name\": \""+AESUtil.encrypt(nombre)+"\",\"note1\":\""+AESUtil.encrypt(Double.toString(note1))+"\",\"note2\": \""+AESUtil.encrypt(Double.toString(note2))+"\",\"note3\": \""+AESUtil.encrypt(Double.toString(note3))+"\"}");
		
		average = Double.parseDouble(HttpClientSynchronous.doGet("http://localhost:8081/usernote/getnote"));
		showSticky();
	}
	
	public void loadUsers() {
		listUsers=users();
	}
	
	public ArrayList<String[]> users(){
		String response=HttpClientSynchronous.doGet("http://localhost:8081/usernote/getall");
		if(response.isEmpty()) return new ArrayList<String[]>();
		String json=response.replaceAll("[{\n ]", "");
		json=json.replaceAll("["+Pattern.quote("[")+Pattern.quote("]")+"]", "");
		json=json.substring(0,json.length()-1);
		String[] rows=json.split("\\}");
		ArrayList<String[]> users=new ArrayList<>();
		for(String aux: rows) {
			if(aux.charAt(0)==',')aux=aux.substring(1);
			String[] attributes=aux.split(",");
			String[] all=new String[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				if(i==0) {
					all[i]=attributes[i].substring(attributes[i].indexOf(":")+1);
					continue;
				}
				else if(i==attributes.length-1) {
					attributes[i]=attributes[i].replace("\"", "");
					all[i]=attributes[i].substring(attributes[i].indexOf(":")+1);
					continue;
				}
				attributes[i]=attributes[i].replace("\"", "");
				all[i]=AESUtil.decrypt(attributes[i].substring(attributes[i].indexOf(":")+1));
			}
			users.add(all);
		}
		return users;
	}
	
}
