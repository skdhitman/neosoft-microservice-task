package com.neosoft.microservices.dept;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Department {

	@Id
	private long id;
	@Column(name="dept_name")
	private String deptName;
	@Column(name="manager")
	private String manager;
	@Column(name="technology")
	private String technology;
	
	private String enviornment;

	public Department(String deptName, String deptManager, String technology) {
		this.deptName = deptName;
		this.manager = deptManager;
		this.technology = technology;
	}

}
