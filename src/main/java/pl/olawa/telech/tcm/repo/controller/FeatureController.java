package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.logic.FeatureLogicImpl;
import pl.olawa.telech.tcm.repo.model.dto.FeatureDto;


@RestController
@RequestMapping("/features")
@FieldDefaults(level = PRIVATE)
public class FeatureController extends AbstractController {

	@Autowired
	FeatureLogicImpl featureLogic;

	/*
	 * Returns all features.
	 */
	@RequestMapping(value = "", method = GET)
	public List<FeatureDto> getAll() {

		return FeatureDto.toDtoList(featureLogic.loadAll());
	}
	
	/*
	 * Returns feature values by element id and feature code.
	 */
	@RequestMapping(value = "", method = GET)
	public List<FeatureDto> getAttributeValues() {

		return FeatureDto.toDtoList(featureLogic.loadAll());
	}
	
	/*
	 * Creates new feature.
	 */
	@RequestMapping(value = "", method = POST)
	public FeatureDto create(
			@RequestBody FeatureDto feature) {
		
		return new FeatureDto(featureLogic.create(feature.toModel()));
	}
	
	/*
	 * Updates feature.
	 */
	@RequestMapping(value = "/{id:" + ID +"}", method = PUT)
	public FeatureDto update(
			@PathVariable int id,
			@RequestBody FeatureDto feature) {
		
		TUtils.assertDtoId(id, feature);		
		return new FeatureDto(featureLogic.update(id, feature.toModel()));
	}
	
	
	/*
	 * Deletes feature.
	 */
	@RequestMapping(value = "/{id:" + ID +"}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remove(
			@PathVariable int id) {
		
		featureLogic.delete(id);
	}
}
