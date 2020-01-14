package pl.olawa.telech.tcm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.dto.entity.EmployeeDto;
import pl.olawa.telech.tcm.logic.UserLogic;


@RestController
@RequestMapping("/Employees")
public class EmployeeController extends AbstractController {

	@Autowired
	private UserLogic employeeLogic;

	
	/*
	 * Wczytuje wszystkiech pracowników na listę Pracownicy
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<EmployeeDto> getAll() {
		
		return EmployeeDto.toDtoList(employeeLogic.loadAll());
	}


}