package com.qpark.survey.lime.model.mapper;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Maps lime responses to model objects.
 *
 * @author bhausen
 */
public class Mapper {
	/** The {@link ObjectMapper}. */
	@Autowired
	private ObjectMapper mapper;

	/**
	 * @return the mapper
	 */
	public ObjectMapper getMapper() {
		return this.mapper;
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
