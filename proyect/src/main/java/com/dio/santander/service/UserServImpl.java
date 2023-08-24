package com.dio.santander.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dio.santander.models.User;
import com.dio.santander.repo.UserRepo;


@Service
public class UserServImpl implements UserServ {

	@Autowired
	public UserRepo userRepo;
	

	@Override
	@Transactional(readOnly = true)
	public User findById(Long id) {
		return userRepo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public User create(User user) {
		return userRepo.save(user);
	}

	@Override
	public List<User> findAll() {
		return (List<User>)userRepo.findAll();
	}

	@Override
	public void deleteById(Long id) {
		userRepo.deleteById(id);
		
	}


}
