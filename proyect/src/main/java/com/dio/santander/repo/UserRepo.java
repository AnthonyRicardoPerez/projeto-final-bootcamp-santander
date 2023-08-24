package com.dio.santander.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dio.santander.models.User;

public interface UserRepo extends JpaRepository<User, Long>{

}
