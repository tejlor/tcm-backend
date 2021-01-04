package pl.olawa.telech.tcm.commons.tests;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reflections.Reflections;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class PropertyNameTest extends BaseTest {

	private static final String PREFIX = "PROP_";
	private static final String UPPERCASE_WITH_UNDERSCORES = "[A-Z][A-Z0-9]*(_[A-Z0-9]*)*";
	
	@Test
	@Transactional
	public void propertyConstants() {
		var reflections = new Reflections("pl.olawa.telech.tcm");
		Set<Class<?>> classes = new HashSet<>();
		classes.addAll(reflections.getTypesAnnotatedWith(Entity.class));
		classes.addAll(reflections.getTypesAnnotatedWith(MappedSuperclass.class));
		classes.addAll(reflections.getTypesAnnotatedWith(Embeddable.class));

		for(Class<?> clazz : classes) {
			Set<Field> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toSet());
			fields.stream()
				.filter(f -> f.getType() == String.class)
				.filter(f -> f.getName().startsWith(PREFIX))
				.map(f -> Pair.of(f, f.getModifiers()))
				.filter(p -> Modifier.isStatic(p.getRight()))
				.filter(p -> Modifier.isFinal(p.getRight()))
				.map(p -> p.getLeft())
				.forEach(field -> {
					assertFieldNameMatchesItsValue(field);
					assertFieldValueMatchesClassField(field, clazz, fields);
				});
		}
	}

	@SneakyThrows
	private void assertFieldNameMatchesItsValue(Field field) {
		String deprefixedFieldName = withoutPrefix(field.getName());
		assertThat(deprefixedFieldName).matches(UPPERCASE_WITH_UNDERSCORES);
		
		String fieldValue = (String) field.get(null); // static field
		String fieldValueInUpperCase = camelCaseToUpperCaseWithUnderscores(fieldValue);
		assertThat(fieldValueInUpperCase).isEqualTo(deprefixedFieldName);
	}

	@SneakyThrows
	private void assertFieldValueMatchesClassField(Field field, Class<?> clazz, Set<Field> fields) {
		String fieldValue = (String) field.get(null); // static field
		if(!fields.contains(field)) {
			Assert.fail("Class [" + clazz.getSimpleName() + "] does not declare field [" + fieldValue + "]");
		}
	}

	private String withoutPrefix(String fieldName) {
		return fieldName.substring(PREFIX.length());
	}

	private String camelCaseToUpperCaseWithUnderscores(String camelCaseString) {
		return camelCaseString.replaceAll("[A-Z]+", "_$0").toUpperCase();
	}
}
