package com.neosoft.microservices.emp;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="department")
public interface DepartmentServiceProxy {

	@GetMapping("/department/{id}")
	public DepartmentDetails getDepartmentByEmployeeId(@PathVariable("id") Long id);
}
