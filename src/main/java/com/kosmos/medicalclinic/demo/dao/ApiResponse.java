package com.kosmos.medicalclinic.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

	private String status;

	private String message;
	
	private T data;

}
