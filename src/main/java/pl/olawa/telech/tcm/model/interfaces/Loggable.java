package pl.olawa.telech.tcm.model.interfaces;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.Modifier;
import pl.olawa.telech.tcm.utils.TUtils;

/*
 * Interfejs dla obiektów Dto, których zawartosć ma być logowana.
 */
public interface Loggable {

	public default String toFullString(int depth) {
		if(depth == 3)
			return TUtils.indent[depth] + "...\n";
		
		StringBuilder sb = new StringBuilder();
		List<Field> fields = new ArrayList<Field>();
		
		if(this.getClass().getSuperclass() != null)
			fields.addAll(Arrays.asList(this.getClass().getSuperclass().getDeclaredFields()));  // dodajemy z klasy nadrzędnej
		
		fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));						// dodajemy z klasy bieżącej
		
		for (Field field : fields) {
			try {
				if(Modifier.isStatic(field.getModifiers()))
					continue;
				
				field.setAccessible(true);
				Object obj = field.get(this);
				if (obj instanceof Loggable) {
					sb.append(TUtils.indent[depth] + field.getName() + ":\n");
					sb.append(((Loggable) obj).toFullString(depth + 1));
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

		if(depth == 0)
			sb.setLength(sb.length() - 1);
		
		return sb.toString();
	}
}
