package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.dao.FeatureAttributeDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.FeatureAttributeLogic;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FeatureAttributeLogicImpl extends AbstractLogicImpl<FeatureAttribute> implements FeatureAttributeLogic {
		
	public FeatureAttributeLogicImpl(FeatureAttributeDAO dao) {
		super(dao);
	}
	
	@Override
	public FeatureAttribute create(FeatureAttribute _attribute) {
		validate(_attribute);
		
		var attribute = new FeatureAttribute();
		fillAuditData(attribute);
		attribute.setName(_attribute.getName());
		attribute.setType(_attribute.getType());
		attribute.setRequired(_attribute.isRequired());
		
		return attribute;
	}
	
	@Override
	public FeatureAttribute update(int id, FeatureAttribute _attribute) {
		validate(_attribute);
		
		FeatureAttribute attribute = loadById(id);
		TUtils.assertEntityExists(attribute);
		
		fillAuditData(attribute);
		attribute.setName(_attribute.getName());
		attribute.setType(_attribute.getType());
		attribute.setRequired(_attribute.isRequired());
		
		return attribute;
	}
	
	// #################################### PRIVATE ###################################################################################
	
	private void validate(FeatureAttribute attribute) {
		if(TUtils.isEmpty(attribute.getName())) {
			throw new TcmException("Attribute name is required.");
		}
		if(attribute.getType() == null) {
			throw new TcmException("Attribute type is required.");
		}
	}
	
}
