package com.qpark.lime.survey.model.mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * Maps lime responses to model objects.
 *
 * @author bhausen
 */
public class Mapper {
	/** The instance of the Mapper. */
	private static Mapper instance = new Mapper();

	/**
	 * @return the {@link Mapper} instance.
	 */
	public static Mapper getInstance() {
		return instance;
	}

	/** The {@link ObjectMapper}. */
	private final ObjectMapper mapper;

	/**
	 * @return the mapper
	 */
	public ObjectMapper getMapper() {
		return this.mapper;
	}

	/** Creates the {@link Mapper} instance. */
	public Mapper() {
		this.mapper = new ObjectMapper();
		this.mapper.configure(
				DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
				true);
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		final SimpleModule module = new SimpleModule();
		module.addDeserializer(boolean.class, new CustomBooleanDeserializer());
		this.mapper.registerModule(module);
		this.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		this.mapper.setAnnotationIntrospector(
				new JaxbAnnotationIntrospector(this.mapper.getTypeFactory()));

	}

	/**
	 * Calls {@link ObjectMapper#readValue(byte[], Class)}
	 *
	 * @param src
	 *            the byte array.
	 * @param valueType
	 *            the model {@link Class} to parse the value in.
	 * @return an object of {@link Class} valueType.
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	public <T> T readValue(final byte[] src, final Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		return this.mapper.readValue(src, valueType);
	}

	/**
	 * Calls {@link ObjectMapper#readValue(byte[], TypeReference)}
	 *
	 * @param src
	 *            the byte array.
	 * @param valueTypeRef
	 *            the model {@link TypeReference} to parse the value in.
	 * @return an object of {@link TypeReference} valueType.
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	public <T> T readValue(final byte[] src, final TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		return this.mapper.readValue(src, valueTypeRef);
	}

	/**
	 * Calls {@link ObjectMapper}{@link #writeValueAsString(Object)}.
	 *
	 * @param value
	 *            the value to write.
	 * @return the resulting {@link String}.
	 * @throws JsonProcessingException
	 */
	public String writeValueAsString(final Object value)
			throws JsonProcessingException {
		return this.mapper.writeValueAsString(value);
	}
}
