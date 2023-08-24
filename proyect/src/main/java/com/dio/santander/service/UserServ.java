package com.dio.santander.service;


import java.util.List;

import com.dio.santander.models.User;

public interface UserServ {

	public User findById(Long id);
	
	public User create(User user);
	
	public List<User> findAll();
	
	public void deleteById(Long id);
}
