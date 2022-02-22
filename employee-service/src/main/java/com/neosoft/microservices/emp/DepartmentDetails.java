package com.neosoft.microservices.emp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDetails {

	private String deptName;
	private String manager;
	private String technology;
	private String enviornment;

	public DepartmentDetails(String deptName, String manager, String technology) {
		this.deptName = deptName;
		this.manager = manager;
		this.technology = technology;
	}

}
