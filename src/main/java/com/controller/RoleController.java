package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bean.RoleBean;
import com.repository.RoleRepository;

@RestController
public class RoleController {
	
	@Autowired
	RoleRepository roleRepo;

	@PostMapping("/role")
	public ResponseEntity<?> addRole(@RequestBody RoleBean role){
		try {
			roleRepo.save(role);
		}catch(Exception e) {
			return ResponseEntity.unprocessableEntity().build();
		}
		return ResponseEntity.ok(role);
	}
	
	@DeleteMapping("/role/{roleId}")
	public ResponseEntity<?> deleteRole(@PathVariable("roleId") Integer roleId){
		try {
		roleRepo.deleteById(roleId);
		}catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok("Role Deleted");
	}
	
	@GetMapping("/roles")
	public ResponseEntity<?> getAllRoles(){
		return ResponseEntity.ok(roleRepo.findAll());
	}
}
