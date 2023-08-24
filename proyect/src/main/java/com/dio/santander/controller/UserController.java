package com.dio.santander.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dio.santander.models.User;
import com.dio.santander.service.UserServ;
@RestController
@RequestMapping("/user")
public class UserController{
	
	@Autowired
	private UserServ service;

	@GetMapping("/all")
	public List<User> listaUsers(){
		return service.findAll();
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		User user = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			user = service.findById(id);
		}catch(DataAccessException e){
			response.put("message", "Erro ao consultar o banco de dados");
			response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(user== null) {
			response.put("message", "o ID do cliente".concat(id.toString().concat("não existe no banco de dados")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<?>create(@RequestBody User user, BindingResult result){
		
		User userNew = null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err-> "O campo" + err.getField() + " " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", result);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			userNew = service.create(user);
		}catch(DataAccessException e) {
			response.put("message",  "Erro ao consultar o banco de dados");
			response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		response.put("message", "O cliente foi criado com sucesso");
		response.put("user", userNew);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> update( @RequestBody User user, BindingResult result, @PathVariable Long id){
		
		User userActual = service.findById(id);
		User userNew = null;
		
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err-> "O campo" + err.getField() + " " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", result);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(user== null) {
			response.put("message", "Erro, não foi possível editar o ID do cliente".concat(id.toString().concat("não existe no banco de dados")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			userActual.setNome(user.getNome());
			userActual.setCartao(user.getCartao());
			userActual.setConta(user.getConta());
			userActual.setFeatures(user.getFeatures());
			userActual.setNews(user.getNews());
			
			userNew = service.create(userActual);
		}catch(DataAccessException e) {
			response.put("message",  "Erro ao atualizar o cliente para o banco de dados");
			response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "O cliente foi atualizado com sucesso");
		response.put("user",userNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?>delete(@PathVariable Long id){
		
		Map<String, Object> response= new HashMap<>();
		
		try {
			service.deleteById(id);
		}catch(DataAccessException e){
			response.put("message", "Erro ao consultar o banco de dados");
			response.put("message", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "O cliente foi removido com sucesso do banco de dados.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}
	
}
