package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.exception.ValidateException;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.dao.FeatureAttributeValueDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FeatureAttributeLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FeatureAttributeValueLogic;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;
import pl.olawa.telech.tcm.repo.model.enums.FeatureAttributeType;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FeatureAttributeValueLogicImpl extends AbstractLogicImpl<FeatureAttributeValue> implements FeatureAttributeValueLogic {
		
	@SuppressWarnings("unused")
	FeatureAttributeValueDAO dao;
	
	@Autowired
	ElementLogic elementLogic;
	@Autowired
	FeatureAttributeLogic featureAttributeLogic;
	
	public FeatureAttributeValueLogicImpl(FeatureAttributeValueDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public List<FeatureAttributeValue> save(UUID elementRef, List<FeatureAttributeValue> featureValues) {
		Element element = elementLogic.loadByRef(elementRef);	
		
		List<FeatureAttributeValue> result = new ArrayList<>();
		for(FeatureAttributeValue _value : featureValues) {
			validate(_value, element);
			
			FeatureAttributeValue value = loadOrCreate(_value.getId());
			TUtils.assertEntityExists(value);
			
			fillAuditData(value);
			value.setElement(_value.getElement());
			value.setFeatureAttribute(_value.getFeatureAttribute());
			value.setValue(_value.getTransientValue());
			
			result.add(save(value));
		}
		return result;
	}
	
	// #################################### PRIVATE ###################################################################################
	
	private void validate(FeatureAttributeValue value, Element element) {
		if(value.getFeatureAttributeId() == null) {
			throw new ValidateException("Attribute id is required.");
		}
		if(value.getElementId() == null) {
			throw new ValidateException("Element id is required.");
		}
		if(!value.getElement().equals(element)) {
			throw new ValidateException("Element id is incorrect.");
		}
		
		FeatureAttribute attribute = featureAttributeLogic.loadById(value.getFeatureAttributeId());
		String typedPropertyName = attribute.getType().getTypedPropertyName();
		for(String propertyName : FeatureAttributeType.getTypedPropertyNames()) {
			if(propertyName.equals(typedPropertyName))
				continue;
			
			try {
				if(PropertyUtils.getSimpleProperty(attribute, propertyName) != null) {
					throw new ValidateException("Property " + propertyName + " cannot be filled for the type " + attribute.getType());
				}
			}
			catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
	
}
