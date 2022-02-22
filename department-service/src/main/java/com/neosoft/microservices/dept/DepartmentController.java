package com.neosoft.microservices.dept;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {

	private static final String DEFAULT_MSG = "Calling \"/department{}\" endpoint";
	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private Environment environment;
	
	@GetMapping("/{id}")
	public Department getDept(
			@PathVariable(value="id") Long id) {
		String port = environment.getProperty("local.server.port");
		log.debug(DEFAULT_MSG, "/id");
		Department deptById = departmentService.getDept(id);
		deptById.setEnviornment(port);
		return deptById;
	}
	
	@GetMapping
	public List<Department> getDepartments(){
		log.debug(DEFAULT_MSG, "");
		return  departmentService.getAllDepartments();
	}
	
	@PostMapping
	public ResponseEntity<String> saveDept(
			@RequestBody Department dept) {
		log.debug(DEFAULT_MSG, "");
		
		Department deptSaveResponse = departmentService.saveDept(dept);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(String.format("Department with id: %d is saved successfully", deptSaveResponse.getId()));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateDept(
			@RequestBody Department dept, 
			@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MSG, "/id");
		
		Department deptUpdateResponse = departmentService.updateDept(dept, id);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(String.format("Department with id: %d is updated successfully", deptUpdateResponse.getId()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDept(@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MSG, "/id");
		Long deptDeleteResponse = departmentService.deleteDept(id);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(String.format("Department with id: %d is deleted successfully", deptDeleteResponse));
	}
}
