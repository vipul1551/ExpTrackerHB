package com.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.LoginBean;
import com.bean.ResponseBean;
import com.bean.RoleBean;
import com.bean.UserBean;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import com.service.TokenService;

@RestController
@RequestMapping("/public")
public class SessionController {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	BCryptPasswordEncoder bCrypt; // BCrypt Password encoder for encrypting password
	
	@PostMapping("/signup")
	public ResponseEntity<ResponseBean<UserBean>> signup(@RequestBody UserBean user){
		
		UserBean dbUser = userRepo.findByEmail(user.getEmail());
		ResponseBean<UserBean> res = new ResponseBean<>();
		
		if(dbUser==null) {
			
			RoleBean role = roleRepo.findByRoleName("user");
			user.setRole(role);
			
			// BCrypt Password encoder for encrypting password
			String encPassword = bCrypt.encode(user.getPassword());
			user.setPassword(encPassword);
			
			userRepo.save(user);
			res.setData(user);
			res.setMsg("Signup Done.......");
			return ResponseEntity.ok(res);
		}
		else{
			res.setData(user);
			res.setMsg("Email already Taken");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody LoginBean login){
		UserBean dbUser = userRepo.findByEmail(login.getEmail());
				
		if(dbUser == null || bCrypt.matches(login.getPassword(), dbUser.getPassword()) == false) {
			ResponseBean<LoginBean> res = new ResponseBean<>();
			res.setData(login);
			res.setMsg("Invalid Credentials");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
		}else {
			String authToken = tokenService.generateToken(16);
			dbUser.setAuthToken(authToken);
			userRepo.save(dbUser);
			
			ResponseBean<Map<String,Object>> res = new ResponseBean<>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user",dbUser);
			res.setData(data);
			res.setMsg("Login Done.....");
			return ResponseEntity.ok(res);
		}
	}
}
