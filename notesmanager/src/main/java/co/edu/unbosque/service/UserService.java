package co.edu.unbosque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.User;
import co.edu.unbosque.repository.UserRepository;

@Service
public class UserService implements CRUDOperation<User>{
	
	@Autowired
	private UserRepository userRepo;
	
	public UserService() {}

	@Override
	public int create(User data) {
		userRepo.save(data);
		return 0;
	}

	@Override
	public List<User> getAll() {
		return userRepo.findAll();
	}

	@Override
	public int deleteByID(Long id) {
		Optional<User> found= userRepo.findById(id);
		if(found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		}
		return 1;
	}

	@Override
	public int updateByID(Long id, User newData) {
		Optional<User> found = userRepo.findById(id);
		if(found.isPresent()) {
			User temp= found.get();
			temp.setName(newData.getName());
			temp.setNote1(newData.getNote1());
			temp.setNote2(newData.getNote2());
			temp.setNote3(newData.getNote3());
			userRepo.save(temp);
			return 0;
		}
		return 1;
	}

	@Override
	public long count() {
		return userRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return userRepo.existsById(id);
	}
	
	public User getById(Long id) {
		return userRepo.findById(id).get();
	}

}
