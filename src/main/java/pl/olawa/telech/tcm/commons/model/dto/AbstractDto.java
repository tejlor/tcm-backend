package pl.olawa.telech.tcm.commons.model.dto;

import static lombok.AccessLevel.PROTECTED;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.commons.model.interfaces.Loggable;
import pl.olawa.telech.tcm.commons.utils.TUtilsBean;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PROTECTED)
public abstract class AbstractDto implements Loggable {

	private static final BeanUtilsBean beanUtils = new TUtilsBean();
	
	Integer id; 
	
	public AbstractDto(AbstractEntity model) {
		try {
			beanUtils.copyProperties(this, model);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract AbstractEntity toModel();

	protected void fillModel(AbstractEntity model) {
		try {
			beanUtils.copyProperties(model, this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static <M extends AbstractEntity, T extends AbstractDto> List<T> toDtoList(Class<M> modelType, Class<T> dtoType, List<M> list) {
		if (list == null) {
			return new ArrayList<T>();
		}
		
		List<T> newList = new ArrayList<>();

		try {
			Constructor<T> cons = dtoType.getDeclaredConstructor(modelType);
			for (M model : list) {
				newList.add(cons.newInstance(model));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return newList;
	}

	@SuppressWarnings("unchecked")
	protected static <M extends AbstractEntity, T extends AbstractDto> List<M> toModelList(List<T> list) {
		List<M> newList = new ArrayList<M>();
		if (list != null) {
			for (T dto : list) {
				newList.add((M) dto.toModel());
			}
		}
		return newList;
	}
}
