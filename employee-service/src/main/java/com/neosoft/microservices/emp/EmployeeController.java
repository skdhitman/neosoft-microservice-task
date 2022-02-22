package com.neosoft.microservices.emp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
	
	private static final String ENDPOINT_ID = "/{id}";
	private static final String DEFAULT_MESSAGE = "Calling /employee{} endpoint: {}";
	private static final String DATA_SAVE_THROTTLER_SERVICE = "dataSaveRateLimitThrottler";
	
	@Value("${department-microservice-base-url}")
	private String deptMicroservicebaseUrl;
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private Environment environment;
	@Autowired 
	private DepartmentServiceProxy departmentProxy;

	@GetMapping
	public List<Employee> getEmployees(){
		log.debug(DEFAULT_MESSAGE, "", new Date());
		return employeeService.getAllEmps();
	}
	
	@GetMapping(ENDPOINT_ID)
	public ResponseEntity<Employee> getEmp(
			@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MESSAGE, ENDPOINT_ID, new Date());
		Employee empResponse = employeeService.getEmpById(id);
		empResponse.setEnvironment(environment.getProperty("local.server.port"));
		return ResponseEntity.status(HttpStatus.OK).body(empResponse);
	}
	
	@GetMapping("/dept/{id}")
	public ResponseEntity<Employee> getEmpDetails(
			@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MESSAGE, "/dept/{id}", new Date());
		Employee empDetails = employeeService.getEmpById(id);
		HashMap<String ,Long> deptIdMap = new HashMap<>();
		deptIdMap.put("id", empDetails.getDeptId());
		ResponseEntity<DepartmentDetails> departmentDetailsResponse
			= new RestTemplate().getForEntity(deptMicroservicebaseUrl+ENDPOINT_ID,
				DepartmentDetails.class, deptIdMap);
		DepartmentDetails departmentDetails = departmentDetailsResponse.getBody();
		if(departmentDetails != null) {
			empDetails.setDeptDetails(
					new DepartmentDetails(
							departmentDetails.getDeptName(),
							departmentDetails.getManager(),
							departmentDetails.getTechnology()));
		}
		empDetails.setEnvironment(environment.getProperty("local.server.port"));
		return ResponseEntity.status(HttpStatus.OK).body(empDetails);
	}
	
	@GetMapping("/dept-feign/{id}")
	public ResponseEntity<Employee> getEmpDetailsFeignClient(
			@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MESSAGE, "/dept-feign/{id}", new Date());
		Employee emp = employeeService.getEmpById(id);
		DepartmentDetails departmentDetails = departmentProxy.getDepartmentByEmployeeId(emp.getDeptId());
		emp.setDeptDetails(
				new DepartmentDetails(
						departmentDetails.getDeptName(),
						departmentDetails.getManager(),
						departmentDetails.getTechnology()));
		String env = departmentDetails.getEnviornment();
		emp.setEnvironment(env+" feign client");
		return ResponseEntity.status(HttpStatus.OK).body(emp);
	}
	
	//@RateLimiter(name=DATA_SAVE_THROTTLER_SERVICE, fallbackMethod = "dataSaveRateLimiterFallback")
	@PostMapping
	public ResponseEntity<String> saveEmployee(
			@RequestBody Employee emp) {
		log.debug(DEFAULT_MESSAGE, "", new Date());
		Employee empSaveResponse = employeeService.saveEmployee(emp);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(String.format("Employee with id: %d is saved successfully", empSaveResponse.getId()));
	}
	
	@PutMapping(ENDPOINT_ID)
	public ResponseEntity<String> updateEmployee(
			@RequestBody Employee emp, 
			@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MESSAGE, ENDPOINT_ID, new Date());
		Employee empUpdateResponse = employeeService.updateEmployee(emp, id);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(String.format("Employee with id: %d is updated successfully", empUpdateResponse.getId()));
	}
	
	@DeleteMapping(ENDPOINT_ID)
	public ResponseEntity<String> deleteEmployee(
			@PathVariable(value="id") Long id) {
		log.debug(DEFAULT_MESSAGE, ENDPOINT_ID, new Date());
		Long empDeleteResponse = employeeService.deleteEmployee(id);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(String.format("Employee with id: %d is deleted successfully", empDeleteResponse));
	}
	
	
    // Rate Limiter(Throttler) FALLBACK method
	/*
	 * public ResponseEntity<String> dataSaveRateLimiterFallback( Employee emp,
	 * RequestNotPermitted exception) {
	 * log.error("Max request rate limit fallback triggered. Exception: ",
	 * exception);
	 * 
	 * return ResponseEntity.status( HttpStatus.TOO_MANY_REQUESTS)
	 * .body("Too many requests made! Retry after some time"); }
	 */
}
