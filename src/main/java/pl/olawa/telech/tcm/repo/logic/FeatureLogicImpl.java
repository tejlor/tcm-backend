package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.logic.helper.ExcelExporter;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.dao.FeatureAttributeValueDAO;
import pl.olawa.telech.tcm.repo.dao.FeatureDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FeatureAttributeLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FeatureLogic;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FeatureLogicImpl extends AbstractLogicImpl<Feature> implements FeatureLogic {
	
	private static final Pattern pCode = Pattern.compile("[a-zA-Z_\\-]+");
	
	FeatureDAO dao;
	
	@Autowired
	FeatureAttributeValueDAO featureAttributeValueDAO;
	
	@Autowired
	ElementLogic elementLogic;
	@Autowired
	FeatureAttributeLogic featureAttributeLogic;
	
	
	public FeatureLogicImpl(FeatureDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public Pair<List<Feature>, Integer> loadTable(TableParams tableParams){
		return dao.findAll(tableParams);
	} 
	
	@Override 
	public ByteArrayOutputStream exportToXlsx() {
		return new ExcelExporter<Feature>()
				.title("Element Features")
				.column(new ExcelExporter.Column<Feature>("Id", Feature::getId))
				.column(new ExcelExporter.Column<Feature>("Name", Feature::getName))
				.column(new ExcelExporter.Column<Feature>("Code", Feature::getCode))
				.dataSet(loadAllOrderById())
				.create();
	}
	
	@Override
	public Set<Feature> loadByElement(UUID elementRef) {
		Element element = elementLogic.loadByRef(elementRef);
		return element.getFeatures();
	}
	
	@Override
	public List<FeatureAttributeValue> loadValuesByElementFeature(UUID elementRef, int featureId) {
		Element element = elementLogic.loadByRef(elementRef);
		TUtils.assertEntityExists(element);
		
		Feature feature = loadById(featureId);
		TUtils.assertEntityExists(feature);
		
		List<FeatureAttributeValue> attributeValues = featureAttributeValueDAO.loadByElementAndFeature(element.getId(), feature.getId());
		Set<Integer> attributeWithValuesIds = attributeValues.stream()
				.map(v -> v.getFeatureAttributeId())
				.collect(Collectors.toSet());
		
		for(FeatureAttribute attribute : feature.getAttributes()) {
			if(!attributeWithValuesIds.contains(attribute.getId())) {
				var value = new FeatureAttributeValue();
				value.setElement(element);
				value.setFeatureAttribute(attribute);
				attributeValues.add(value);
			}
		}
		
		Collections.sort(attributeValues, (a, b) -> a.getFeatureAttribute().getId().compareTo(b.getFeatureAttribute().getId()));
		
		return attributeValues;
	}
	
	@Override
	public List<FeatureAttributeValue> loadValuesByElementFeature(UUID elementRef, String featureCode) {
		Feature feature = dao.findByCode(featureCode);
		TUtils.assertEntityExists(feature);
		return loadValuesByElementFeature(elementRef, feature.getId());
	}
	
	@Override
	public Feature create(Feature _feature) {
		validate(_feature);
		
		var feature = new Feature();
		fillAuditData(feature);
		feature.setName(_feature.getName());
		feature.setCode(_feature.getCode());
		
		for(FeatureAttribute _attribute : _feature.getAttributes()) {
			feature.addAttribute(featureAttributeLogic.create(_attribute));
		}
		
		return save(feature);
	}
	
	@Override
	public Feature update(int id, Feature _feature) {
		validate(_feature);
		
		Feature feature = loadById(id);
		TUtils.assertEntityExists(feature);
		
		fillAuditData(feature);
		feature.setName(_feature.getName());
		feature.setCode(_feature.getCode());
		feature.getAttributes().clear();
		
		for(FeatureAttribute _attribute : _feature.getAttributes()) {
			FeatureAttribute attribute = _attribute.getId() == null 
					? featureAttributeLogic.create(_attribute)
					: featureAttributeLogic.update(_attribute.getId(), _attribute);					
			feature.addAttribute(attribute);
		}
		
		return save(feature);
	}
	
	public void delete(int id) {
		Feature feature = loadById(id);
		String elementRefs = feature.getElements().stream()
			.map(e -> e.getRef().toString())
			.limit(5)
			.collect(Collectors.joining(", "));
		if(!elementRefs.isEmpty()) {
			throw new TcmException("Cannot delete feature used in " + feature.getElements().size() + " elements, e.g: " 
					+ elementRefs + ".");
		}
		
		delete(feature);
	}
	
	// #################################### PRIVATE ###################################################################################
	
	private void validate(Feature feature) {
		if(TUtils.isEmpty(feature.getName())) {
			throw new TcmException("Feature name is required.");
		}
		if(TUtils.isEmpty(feature.getCode())) {
			throw new TcmException("Feature code is required.");
		}
		if(!pCode.asMatchPredicate().test(feature.getCode())) {
			throw new TcmException("Feature code should contains only letters and - or _.");
		}
	}
		
}
