package co.edu.unbosque.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.model.User;
import co.edu.unbosque.service.UserService;
import co.edu.unbosque.util.AESUtil;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/usernote")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "*" })
@Transactional
public class UserController {
	
	@Autowired
	private UserService userServ;
	
	@PostMapping(path = "/createuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createUser(@RequestBody User newUser){
		User encryptUser=new User(AESUtil.encrypt(newUser.getName()),AESUtil.encrypt(newUser.getNote1()), AESUtil.encrypt(newUser.getNote2()), AESUtil.encrypt(newUser.getNote3()));
		encryptUser.setNoteFinal(AESUtil.encrypt(Double.toString(encryptUser.calculateNoteFinal())));
		userServ.create(encryptUser);
		return new ResponseEntity<String>("User create successfully.",HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/getnote")
	public ResponseEntity<String> getNoteFinal(){
		if(userServ.getAll().size()>0) {
			String noteFinal=AESUtil.decrypt(userServ.getAll().get(userServ.getAll().size()-1).getNoteFinal());
			return new ResponseEntity<String>(noteFinal,HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("Data base empty.",HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/getnotebyid/{id}")
	public ResponseEntity<String> getNoteFinal(@PathVariable Long id){
		if(userServ.exist(id)) {
			String noteFinal=AESUtil.decrypt(userServ.getById(id).getNoteFinal());
			return new ResponseEntity<String>(noteFinal,HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("Data base empty.",HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(path = "/getall")
	public ResponseEntity<List<User>> getAll(){
		List<User> users=userServ.getAll();
		List<User> decriptUsers=new ArrayList<>();
		if(users.isEmpty())return new ResponseEntity<List<User>>(users,HttpStatus.NO_CONTENT);
		for(User user:users) {
			User decyptUser=new User(AESUtil.decrypt(user.getName()), AESUtil.decrypt(user.getNote1()), AESUtil.decrypt(user.getNote2()), AESUtil.decrypt(user.getNote3()));
			decyptUser.setId(user.getId());
			decyptUser.setNoteFinal(AESUtil.decrypt(user.getNoteFinal()));
			decriptUsers.add(decyptUser);
		}
		return new ResponseEntity<List<User>>(decriptUsers,HttpStatus.ACCEPTED);
	}
}
