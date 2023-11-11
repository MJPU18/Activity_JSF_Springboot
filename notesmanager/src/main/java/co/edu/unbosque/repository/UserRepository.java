package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
