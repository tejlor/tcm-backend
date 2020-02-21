package pl.olawa.telech.tcm.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;

import pl.olawa.telech.tcm.dto.entity.AbstractDto;
import pl.olawa.telech.tcm.model.entity.AbstractEntity;

/*
 * Rozszerzenie implementacji Apachowego BeanUtils o obsługę enumów.
 * Bean jest używany podczas automatycznej konwersji Entity <-> Dto.
 */
public class TUtilsBean extends BeanUtilsBean {

	/*
	 * Automatycznie konwertujemy String na Enum (Dto -> Entity), w drugą stronę działa zwykłe toString().
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
	 * Omijamy obiekty encyjne, bo nie ma zarejestrowanego konwertera Dto <-> Entity i wyjątek powoduje przerwanie procesu kopiowania. 
	 */
	@Override
	public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		if(value == null || value instanceof AbstractDto || value instanceof AbstractEntity)
			return;
		
		super.copyProperty(bean, name, value);
	}
	
}
