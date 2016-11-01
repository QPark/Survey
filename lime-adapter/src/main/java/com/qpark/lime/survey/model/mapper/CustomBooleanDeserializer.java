package com.qpark.lime.survey.model.mapper;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Maps lime survey boolean <i>Y</i> to <code>true</code>, all other to
 * <code>false</code>.
 *
 * @author bhausen
 */
class CustomBooleanDeserializer extends JsonDeserializer<Boolean> {
	/**
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
	 *      com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public Boolean deserialize(final JsonParser jp,
			final DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Boolean value = null;
		System.out.println(jp);
		if (jp.getCurrentTokenId() == JsonTokenId.ID_STRING) {
			try {
				value = Boolean.valueOf(jp.getText())
						|| Objects.nonNull(jp.getText())
								&& jp.getText().trim().equals("Y");
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
