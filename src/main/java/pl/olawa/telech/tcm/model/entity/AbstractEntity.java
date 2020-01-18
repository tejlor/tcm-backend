package pl.olawa.telech.tcm.model.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import pl.olawa.telech.tcm.utils.TUtils;

/*
 * Klasa nadrzędna dla wszystkich obiektów encyjnych.
 */
@Getter @Setter
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;							// id obiektu
	
	
	public AbstractEntity() {
		
	}
	
	public AbstractEntity(Integer id) {
		this.id = id;
	}
	
	public String toFullString(int depth) {
		if(depth == 2)
			return "...";
		
		StringBuilder sb = new StringBuilder();
		List<Field> fields = new ArrayList<Field>();
		
		if(this.getClass().getSuperclass() != null)
			fields.addAll(Arrays.asList(this.getClass().getSuperclass().getDeclaredFields()));  // dodajemy z klasy nadrzędnej
		
		fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));						// dodajemy z klasy bieżącej
		
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object obj = field.get(this);
				if (obj instanceof AbstractEntity) {
					sb.append(TUtils.indent[depth] + field.getName() + ":\n");
					sb.append(((AbstractEntity) obj).toFullString(depth + 1));
				}
				else if(obj instanceof List){
					sb.append(TUtils.indent[depth] + field.getName() + ": ");
					sb.append("list of " + ((List<?>)obj).size() + " elements\n");
				}
				else {
					sb.append(TUtils.indent[depth] + field.getName() + ": " + obj + "\n");
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return id.intValue();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (this.getClass() != other.getClass())
			return false;

		AbstractEntity o = (AbstractEntity) other;
		return id.equals(o.id);
	}
}
