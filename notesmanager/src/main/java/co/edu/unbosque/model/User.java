package co.edu.unbosque.model;

import co.edu.unbosque.util.AESUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usernote")
public class User {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String name;
	private String note1;
	private String note2;
	private String note3;
	private String noteFinal;
	
	public User() {}

	
	
	public User(String name, String note1, String note2, String note3) {
		this.name = name;
		this.note1 = note1;
		this.note2 = note2;
		this.note3 = note3;
	}

	public double calculateNoteFinal() {
		double noteOne=Double.parseDouble(AESUtil.decryptAll(note1));
		double noteTwo=Double.parseDouble(AESUtil.decryptAll(note2));
		double noteThree=Double.parseDouble(AESUtil.decryptAll(note3));
		double average=(noteOne*30/5)+(noteTwo*30/5)+(noteThree*40/5);
		double noteFinal=average*5/100;
		return noteFinal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote1() {
		return note1;
	}

	public void setNote1(String note1) {
		this.note1 = note1;
	}

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	public String getNote3() {
		return note3;
	}

	public void setNote3(String note3) {
		this.note3 = note3;
	}

	public String getNoteFinal() {
		return noteFinal;
	}

	public void setNoteFinal(String noteFinal) {
		this.noteFinal = noteFinal;
	}

	
}
