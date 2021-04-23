package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.builder.feature.FeatureAttributeBuilder;
import pl.olawa.telech.tcm.repo.builder.feature.FeatureBuilder;
import pl.olawa.telech.tcm.repo.model.dto.FeatureAttributeDto;
import pl.olawa.telech.tcm.repo.model.dto.FeatureDto;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.enums.FeatureAttributeType;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class FeatureControllerTest extends BaseTest {

	@Autowired
	FeatureController featureController;	

	@Test
	@Transactional
	public void getAll() {
		// given
		Feature feature1 = setupFeature("Important invoice", "invoice", 
						setupFeatureAttribute("Number", FeatureAttributeType.STRING),
						setupFeatureAttribute("Amount", FeatureAttributeType.DEC),
						setupFeatureAttribute("Payment date", FeatureAttributeType.DATE));
		Feature feature2 = setupFeature("Photo", "photo", 
				setupFeatureAttribute("City", FeatureAttributeType.STRING),
				setupFeatureAttribute("Date", FeatureAttributeType.DATE),
				setupFeatureAttribute("Rating", FeatureAttributeType.INT));
		flush();	
		// when
		List<FeatureDto> result = featureController.getAll();
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertFeature(result.get(0), feature1);
		assertFeature(result.get(1), feature2);
	}
	
	@Test
	@Transactional
	public void create() {
		// given
		FeatureDto featureDto = setupFeatureDto("Important invoice", "invoice", 
				setupFeatureAttributeDto("Number", FeatureAttributeType.STRING),
				setupFeatureAttributeDto("Amount", FeatureAttributeType.DEC),
				setupFeatureAttributeDto("Payment date", FeatureAttributeType.DATE));
		flush();	
		// when
		FeatureDto result = featureController.create(featureDto);
		flushAndClear();
		// then
		assertFeatureDto(result, featureDto);
		
		Feature createdFeature = load(Feature.class, result.getId());
		assertFeature(result, createdFeature);
	}
	
	@Test
	@Transactional
	public void update() {
		// given
		Feature feature = setupFeature("Important invoice", "invoice", 
				setupFeatureAttribute("Number", FeatureAttributeType.STRING),
				setupFeatureAttribute("Amount", FeatureAttributeType.DEC),
				setupFeatureAttribute("Payment date", FeatureAttributeType.DATE));
		flush();	
		// when
		FeatureDto featureDto = new FeatureDto(feature);
		featureDto.setCode("new_code");
		FeatureAttributeDto attributeDto = setupFeatureAttributeDto("Client", FeatureAttributeType.STRING);
		featureDto.getAttributes().add(attributeDto);
		FeatureDto result = featureController.update(feature.getId(), featureDto);
		flushAndClear();
		// then
		assertFeatureDto(featureDto, result);	
		
		Feature createdFeature = load(Feature.class, feature.getId());
		assertThat(createdFeature).isNotNull();
		assertThat(createdFeature.getCode()).isNotEqualTo("invoice").isEqualTo("new_code");
		assertThat(createdFeature.getAttributes()).hasSize(4);
	}
	
	@Test
	@Transactional
	public void remove() {
		// given
		Feature feature = setupFeature("Important invoice", "invoice", 
				setupFeatureAttribute("Number", FeatureAttributeType.STRING),
				setupFeatureAttribute("Amount", FeatureAttributeType.DEC),
				setupFeatureAttribute("Payment date", FeatureAttributeType.DATE));
		flush();	
		// when
		featureController.remove(feature.getId());
		flushAndClear();
		// then		
		Feature deletedFeature = load(Feature.class, feature.getId());
		assertThat(deletedFeature).isNull();
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertFeature(FeatureDto featureDto, Feature feature) {
		assertThat(featureDto.getId()).isEqualTo(feature.getId());
		assertThat(featureDto.getCreatedByName()).isEqualTo(feature.getCreatedBy().calcFirstLastName());
		assertThat(featureDto.getCreatedTime()).isEqualTo(feature.getCreatedTime());
		assertThat(featureDto.getName()).isEqualTo(feature.getName());
		assertThat(featureDto.getCode()).isEqualTo(feature.getCode());
		assertThat(featureDto.getAttributes()).hasSameSizeAs(feature.getAttributes());
		
		Map<String, FeatureAttribute> attributes = feature.getAttributes().stream()
				.collect(Collectors.toMap(a -> a.getName(), a -> a));
		for(FeatureAttributeDto attributeDto: featureDto.getAttributes()) {
			assertFeatureAttribute(attributeDto, attributes.get(attributeDto.getName()));
		}
	}
	
	private void assertFeatureDto(FeatureDto feature1, FeatureDto feature2) {
		assertThat(feature1.getName()).isEqualTo(feature2.getName());
		assertThat(feature1.getCode()).isEqualTo(feature2.getCode());
		assertThat(feature1.getAttributes()).hasSameSizeAs(feature2.getAttributes());
		
		Map<String, FeatureAttributeDto> attributes = feature2.getAttributes().stream()
				.collect(Collectors.toMap(a -> a.getName(), a -> a));
		for(FeatureAttributeDto attribute: feature1.getAttributes()) {
			assertFeatureAttributeDto(attribute, attributes.get(attribute.getName()));
		}
	}
	
	private void assertFeatureAttribute(FeatureAttributeDto attributeDto, FeatureAttribute attribute) {
		assertThat(attributeDto.getId()).isEqualTo(attribute.getId());
		assertThat(attributeDto.getCreatedByName()).isEqualTo(attribute.getCreatedBy().calcFirstLastName());
		assertThat(attributeDto.getCreatedTime()).isEqualTo(attribute.getCreatedTime());
		assertThat(attributeDto.getName()).isEqualTo(attribute.getName());
		assertThat(attributeDto.getType()).isEqualTo(attribute.getType().name());
		assertThat(attributeDto.isRequired()).isEqualTo(attribute.isRequired());
	}
	
	private void assertFeatureAttributeDto(FeatureAttributeDto attribute1, FeatureAttributeDto attribute2) {
		assertThat(attribute1.getName()).isEqualTo(attribute2.getName());
		assertThat(attribute1.getType()).isEqualTo(attribute2.getType());
		assertThat(attribute1.isRequired()).isEqualTo(attribute2.isRequired());
	}
	
	private Feature setupFeature(String name, String code, FeatureAttribute... attributes) {
		return new FeatureBuilder()
				.name(name)
				.code(code)
				.attributes(Arrays.stream(attributes).collect(Collectors.toSet()))
				.saveAndReload(entityManager);
	}
	
	private FeatureDto setupFeatureDto(String name, String code, FeatureAttributeDto... attributes) {
		return new FeatureDto(new FeatureBuilder()
				.name(name)
				.code(code)
				.attributes(Arrays.stream(attributes).map(a -> a.toModel()).collect(Collectors.toSet()))
				.build());
	}
	
	private FeatureAttribute setupFeatureAttribute(String name, FeatureAttributeType type) {
		return new FeatureAttributeBuilder()
				.name(name)
				.type(type)
				.saveAndReload(entityManager);
	}
	
	private FeatureAttributeDto setupFeatureAttributeDto(String name, FeatureAttributeType type) {
		return new FeatureAttributeDto(new FeatureAttributeBuilder()
				.name(name)
				.type(type)
				.build());
	}
}
