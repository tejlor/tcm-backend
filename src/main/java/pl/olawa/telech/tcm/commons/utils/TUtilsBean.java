package pl.olawa.telech.tcm.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;

import pl.olawa.telech.tcm.commons.model.dto.AbstractDto;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

/*
 * Extension of Apache's BeanUtils with enum handling.
 * Bean is using during auto convertion Entity <-> Dto
 */
public class TUtilsBean extends BeanUtilsBean {

	/*
	 * Converts String to Enum. In oposite way works simple toString()  
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object convert(Object value, Class clazz) {
		if (clazz.isEnum())  			 
			return Enum.valueOf(clazz, value.toString());
		
		if(clazz == UUID.class)
			return value != null ? UUID.fromString((String)value) : null;
		
		return super.convert(value, clazz);
	}
	
	/*
	 * Skip entity objects, because there is no registerd converter Dto <-> Entity and exception causes termination od copying process.  
	 */
	@Override
	public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		if(value == null || value instanceof AbstractDto || value instanceof AbstractEntity)
			return;
		
		super.copyProperty(bean, name, value);
	}
}
