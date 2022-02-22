package com.neosoft.microservices.emp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neosoft.microservices.emp.errors.ContentNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	private static final String NOT_FOUND_MSG = "Employee with Id: %s was not found!";
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<Employee> getAllEmps(){
		log.debug("Get all employees");
		return employeeRepository.findAll();
	}
	public Employee getEmpById(Long id) {
		log.debug("Get Employee with Id: {}",id);
		return employeeRepository.findById(id).orElseThrow(
				() -> new ContentNotFoundException(404, String.format(NOT_FOUND_MSG, id)));
	}
	
	public Employee saveEmployee(Employee emp) {
		log.debug("Create Employee with Id: {}", emp.getId());
		return employeeRepository.save(emp);
	}
	
	public Employee updateEmployee(Employee emp, Long id) {
		log.debug("Update Employee with ID: {}",id);
		Employee existingEmp = employeeRepository.findById(id)
				.orElseThrow(() -> new ContentNotFoundException(404, String.format(NOT_FOUND_MSG, id)));
		if(existingEmp.getId() == id)
			log.debug("Employee with id: {} is found", id);
		emp.setId(id);
		employeeRepository.save(emp);

		return emp;
	}
	
	public Long deleteEmployee(Long id) {
		log.debug("Delete Employee with Id: {}",id);
		if(employeeRepository.findById(id).isPresent()) {
			employeeRepository.deleteById(id);
			return id;
		}else {
			log.error("Employee Delete operation couldn't be performed!");
			throw new ContentNotFoundException(404, String.format(NOT_FOUND_MSG, id));
		}
	}
}
