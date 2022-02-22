package com.neosoft.microservices.dept;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neosoft.microservices.dept.errors.ContentNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentService {

	private static final String NOT_FOUND_MSG = "Department with Id: %s was not found!";
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	public List<Department> getAllDepartments(){
		log.debug("Get Departments");
		
		return departmentRepository.findAll();
	}
	
	public Department getDept(Long id) {
		log.debug("Get Department with Id: {}", id);
		return departmentRepository.findById(id).orElseThrow(
				() -> new ContentNotFoundException(404, String.format(NOT_FOUND_MSG, id)));
	}
	
	public Department saveDept(Department dept) {
		log.debug("Create Department with Id: {}", dept.getId());
		return departmentRepository.save(dept);
	}
	
	public Department updateDept(Department dept,Long id) {
		log.debug("Update Department with Id: {}", id);
		
		Department existingDept = departmentRepository.findById(id)
				.orElseThrow(() -> new ContentNotFoundException(404, String.format(NOT_FOUND_MSG, id)));
		if(existingDept.getId() == id)
			log.debug("Employee with id: {} is found", id);
		dept.setId(id);
		departmentRepository.save(dept);

		return dept;
	}
	
	public Long deleteDept(Long id) {
		log.debug("Delete Department with Id: {}", id);
		if(departmentRepository.findById(id).isPresent()) {
			departmentRepository.deleteById(id);
			return id;
		}else {
			log.error("Could not delete the Department. Exception occurred!");
			throw new ContentNotFoundException(404, String.format(NOT_FOUND_MSG, id));
		}
	}
}
