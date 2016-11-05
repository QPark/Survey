package com.qpark.survey.lime.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.qpark.survey.lime.RestClient;
import com.qpark.survey.lime.model.mapper.CustomBooleanDeserializer;
import com.qpark.survey.lime.model.mapper.Mapper;

/**
 * The spring configuration bean of the lime adapter.
 *
 * @author bhausen
 */
@Configuration
public class SpringConfig {
	/**
	 * @return the {@link ObjectMapper} of the lime adapter.
	 */
	@Bean(name = "limeObjectMapper")
	public ObjectMapper limeObjectMapper() {
		final ObjectMapper bean = new ObjectMapper();
		bean.configure(
				DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
				true);
		bean.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		final SimpleModule module = new SimpleModule();
		module.addDeserializer(boolean.class, new CustomBooleanDeserializer());
		bean.registerModule(module);
		bean.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		bean.setAnnotationIntrospector(
				new JaxbAnnotationIntrospector(bean.getTypeFactory()));
		return bean;
	}

	/**
	 * @return the {@link Mapper} of the lime adapter.
	 */
	@Bean
	public Mapper limeMapper() {
		final Mapper bean = new Mapper();
		return bean;
	}

	/**
	 * @return the {@link RestTemplate} of the lime adapter.
	 */
	@Bean(name = "limeRestTemplate")
	public RestTemplate limeRestTemplate() {
		final RestTemplate bean = new RestTemplate();
		return bean;
	}

	/**
	 * @return the {@link RestClient} of the lime adapter.
	 */
	@Bean(name = "limeRestClient")
	public RestClient limeRestClient() {
		final RestClient bean = new RestClient();
		return bean;
	}
}
