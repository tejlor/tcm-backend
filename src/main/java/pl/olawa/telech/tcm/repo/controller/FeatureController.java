package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.logic.FeatureLogicImpl;
import pl.olawa.telech.tcm.repo.model.dto.FeatureDto;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;


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
	 * Returns all features for table.
	 */
	@RequestMapping(value = "/table", method = GET)
	public TableDataDto<FeatureDto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);		
		Pair<List<Feature>, Integer> result = featureLogic.loadTable(tableParams); 	
		var table = new TableDataDto<FeatureDto>(tableParams);
		table.setRows(FeatureDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Export all users to xlsx.
	 */
	@RequestMapping(value = "/xlsx", method = GET)
	public ResponseEntity<ByteArrayResource> exportToXlsx() {
		
         byte[] bytes = featureLogic.exportToXlsx().toByteArray();
         var resource = new ByteArrayResource(bytes);
         return ResponseEntity.ok().body(resource);
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
