package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.dao.FeatureAttributeValueDAO;
import pl.olawa.telech.tcm.repo.dao.FeatureDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FeatureLogic;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FeatureLogicImpl extends AbstractLogicImpl<Feature> implements FeatureLogic {
	
	private static final Pattern pCode = Pattern.compile("[a-zA-Z_\\-]");
	
	@Autowired
	FeatureAttributeValueDAO featureAttributeValueDAO;
	
	@Autowired
	ElementLogic elementLogic;
	
	
	public FeatureLogicImpl(FeatureDAO dao) {
		super(dao);
	}
	
	@Override
	public Set<Feature> loadByElement(UUID elementRef) {
		Element element = elementLogic.loadByRef(elementRef);
		return element.getFeatures();
	}
	
	@Override
	public List<FeatureAttributeValue> loadValuesByElementFeature(UUID elementRef, String featureCode) {
		Element element = elementLogic.loadByRef(elementRef);
		return featureAttributeValueDAO.loadByElementAndFeature(element.getId(), featureCode);
	}
	
	@Override
	public Feature create(Feature _feature) {
		validate(_feature);
		
		var feature = new Feature();
		fillCreatable(feature);
		
		feature.setName(_feature.getName());
		feature.setCode(_feature.getCode());
		
		for(FeatureAttribute _attribute : _feature.getAttributes()) {
			validate(_attribute);
			
			var attribute = new FeatureAttribute();
			attribute.setName(_attribute.getName());
			attribute.setType(_attribute.getType());
			attribute.setRequired(_attribute.isRequired());
			
			feature.addAttribute(attribute);
		}
		
		return save(feature);
	}
	
	@Override
	public Feature update(int id, Feature _feature) {
		validate(_feature);
		
		Feature feature = loadById(id);
		TUtils.assertEntityExists(feature);
		
		fillModifiable(feature);
		
		feature.setName(_feature.getName());
		feature.setCode(_feature.getCode());
		feature.getAttributes().clear();
		
		for(FeatureAttribute _attribute : _feature.getAttributes()) {
			validate(_attribute);
			
			var attribute = new FeatureAttribute();
			attribute.setName(_attribute.getName());
			attribute.setType(_attribute.getType());
			attribute.setRequired(_attribute.isRequired());
			
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
		
		delete(id);
	}
	
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
	
	private void validate(FeatureAttribute attribute) {
		if(TUtils.isEmpty(attribute.getName())) {
			throw new TcmException("Attribute name is required.");
		}
		if(attribute.getType() == null) {
			throw new TcmException("Attribute type is required.");
		}
	}
	
}
