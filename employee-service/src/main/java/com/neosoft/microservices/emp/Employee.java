package com.neosoft.microservices.emp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
	
	@Id
	private long id;
	@Column(name="emp_name")
	private String empName;
	@Column(name="date_of_joining")
	private String dateOfJoining;
	@Column(name="sal")
	private long salary;
	@Column(name="dept_id")
	private long deptId;
	private String environment;
	@Transient
	private DepartmentDetails deptDetails;

	public Employee(long id, String empName, long salary, String dateOfJoining, long deptId,
			DepartmentDetails deptDetails) {
		this.id = id;
		this.empName = empName;
		this.salary = salary;
		this.dateOfJoining = dateOfJoining;
		this.deptId = deptId;
		this.deptDetails = deptDetails;
	}
	
//	public Employee(long id, long deptId, String empName, String dateOfJoining, long salary, String environment) {
//		this.id = id;
//		this.empName = empName;
//		this.salary = salary;
//		this.dateOfJoining = dateOfJoining;
//		this.deptId = deptId;
//		this.environment = environment;
//	}
	
}
