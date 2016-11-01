package com.qpark.lime.survey;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.qpark.lime.survey.model.mapper.Mapper;
import com.qpark.lime.survey.model.v25.ExportResponsesListEntryType;
import com.qpark.lime.survey.model.v25.ExportResponsesResponseType;
import com.qpark.lime.survey.model.v25.GetSessionKeyResponseType;
import com.qpark.lime.survey.model.v25.ListQuestionsResponseType;
import com.qpark.lime.survey.model.v25.ListSurveysResponseType;
import com.qpark.lime.survey.model.v25.PropertyType;
import com.qpark.lime.survey.model.v25.RequestType;

/**
 * Client calling the lime survey rest service.
 *
 * @author bhausen
 */
public class RestClient {
	/** The end point URL to call. */
	private final String endPointUrl;
	/** Content-type HTTP header. */
	private final HttpHeaders headers;
	/** The spring {@link RestTemplate}. */
	private final RestTemplate restTemplate;

	/**
	 * The REST service client implementation.
	 *
	 * @param restTemplate
	 *            the {@link RestTemplate}.
	 * @param endPointUrl
	 *            the end point URL to call.
	 */
	public RestClient(final RestTemplate restTemplate,
			final String endPointUrl) {
		this.restTemplate = restTemplate;
		this.endPointUrl = endPointUrl;
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
	}

	private String executeRequst(final RequestType request)
			throws JsonProcessingException {
		final HttpEntity<String> entity = new HttpEntity<String>(
				Mapper.getInstance().writeValueAsString(request), this.headers);
		final String value = this.restTemplate.postForObject(this.endPointUrl,
				entity, String.class);
		return value;
	}

	private RequestType getRequestType(final String method,
			final Object... params) {
		final RequestType value = new RequestType();
		value.setMethod(method);
		Arrays.asList(params).stream().forEach(p -> value.getParams().add(p));
		value.setId(1);
		return value;
	}

	/**
	 * Calls the operation <i>get_session_key</i>
	 *
	 * @param userName
	 * @param password
	 * @return the {@link Optional} of {@link GetSessionKeyResponseType}.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Optional<LimeSurveySession> getSessionKey(final String userName,
			final char[] password)
			throws JsonParseException, JsonMappingException, IOException {
		Optional<LimeSurveySession> value = Optional.empty();
		if (Objects.isNull(userName)) {
			throw new IllegalStateException("User name not provided.");
		} else if (Objects.isNull(password) || password.length == 0) {
			throw new IllegalStateException("User password not provided.");
		} else {
			final String method = "get_session_key";
			final RequestType request = this.getRequestType(method, userName,
					new String(password));
			final String response = this.executeRequst(request);
			if (Objects.nonNull(response)) {
				final GetSessionKeyResponseType session = Mapper.getInstance()
						.readValue(response.getBytes(),
								GetSessionKeyResponseType.class);
				if (Objects.nonNull(session.getResult())) {
					value = Optional.of(new LimeSurveySession());
					value.ifPresent(s -> {
						s.setSessionKey(session.getResult());
						s.setUserName(userName);
					});
				}
			}
		}
		return value;
	}

	/**
	 * Get the list of surveys.
	 *
	 * @param session
	 *            the {@link LimeSurveySession}.
	 * @return the {@link Optional} of {@link ListSurveysResponseType}.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Optional<ListSurveysResponseType> listSurveys(
			final LimeSurveySession session)
			throws JsonParseException, JsonMappingException, IOException {
		Optional<ListSurveysResponseType> value = Optional.empty();
		if (Objects.isNull(session)) {
			throw new IllegalStateException("Session not provided.");
		} else {
			final String method = "list_surveys";
			final RequestType request = this.getRequestType(method,
					session.getSessionKey(), session.getUserName());
			final String response = this.executeRequst(request);
			if (Objects.nonNull(response)) {
				value = Optional.ofNullable(Mapper.getInstance().readValue(
						response.getBytes(), ListSurveysResponseType.class));
			}
		}
		return value;
	}

	/**
	 * Calls the operation <i>get_survey_properties</i>.
	 *
	 * @param session
	 *            the {@link LimeSurveySession}.
	 * @param surveyId
	 *            the sid of the survey to get the properties for.
	 * @param propertyNames
	 *            the names of the properties to get.
	 * @throws JsonProcessingException
	 */
	public void getSurveyProperties(final LimeSurveySession session,
			final String surveyId, final List<String> propertyNames)
			throws JsonProcessingException {
		if (Objects.isNull(session)) {
			throw new IllegalStateException("Session not provided.");
		} else if (Objects.isNull(surveyId)) {
			throw new IllegalStateException("SurveyId not provided.");
		} else if (Objects.isNull(propertyNames) || propertyNames.size() == 0) {
			throw new IllegalStateException("No property names provided.");
		} else {
			final String method = "get_survey_properties";
			final RequestType request = this.getRequestType(method,
					session.getSessionKey(), surveyId,
					propertyNames.toArray(new String[propertyNames.size()]));
			final String response = this.executeRequst(request);
			if (Objects.nonNull(response)) {
				// value = Optional.ofNullable(Mapper.getInstance().readValue(
				// response.getBytes(), ListSurveysResponseType.class));
			}
		}
	}

	/**
	 * Calls the operation <i>list_questions</i>.
	 *
	 * @param session
	 *            the {@link LimeSurveySession}.
	 * @param surveyId
	 *            the sid of the survey to get the properties for.
	 * @return the {@link ListQuestionsResponseType}.
	 * @throws IOException
	 */
	public Optional<ListQuestionsResponseType> getListQuestions(
			final LimeSurveySession session, final String surveyId)
			throws IOException {
		Optional<ListQuestionsResponseType> value = Optional.empty();
		if (Objects.isNull(session)) {
			throw new IllegalStateException("Session not provided.");
		} else if (Objects.isNull(surveyId)) {
			throw new IllegalStateException("SurveyId not provided.");
		} else {
			final String method = "list_questions";
			final RequestType request = this.getRequestType(method,
					session.getSessionKey(), surveyId);
			final String response = this.executeRequst(request);
			if (Objects.nonNull(response)) {
				value = Optional.ofNullable(Mapper.getInstance().readValue(
						response.getBytes(), ListQuestionsResponseType.class));
			}
		}
		return value;
	}

	/**
	 * Calls the operation <i>list_questions</i>.
	 *
	 * @param session
	 *            the {@link LimeSurveySession}.
	 * @param surveyId
	 *            the sid of the survey to get the properties for.
	 * @param language
	 *            the language.
	 * @return the {@link ExportResponsesResponseType}.
	 * @throws IOException
	 */
	public Optional<ExportResponsesResponseType> getExportResponses(
			final LimeSurveySession session, final String surveyId,
			final Optional<String> language) throws IOException {
		Optional<ExportResponsesResponseType> value = Optional.empty();
		if (Objects.isNull(session)) {
			throw new IllegalStateException("Session not provided.");
		} else if (Objects.isNull(surveyId)) {
			throw new IllegalStateException("SurveyId not provided.");
		} else {
			final String method = "export_responses";
			final RequestType request = this.getRequestType(method,
					session.getSessionKey(), surveyId, "json",
					language.orElse("en"));
			final String response = this.executeRequst(request);
			if (Objects.nonNull(response)) {
				value = Optional.ofNullable(
						Mapper.getInstance().readValue(response.getBytes(),
								ExportResponsesResponseType.class));
				value.ifPresent(rr -> {
					if (Objects.nonNull(rr.getResult())) {
						try {
							final Map<String, Object> answer = Mapper
									.getInstance().readValue(rr.getResult(),
											new TypeReference<Map<String, Object>>() {
											});
							@SuppressWarnings({ "unchecked" })
							final List<Map<String, Map<String, Map<String, Object>>>> responses = (List<Map<String, Map<String, Map<String, Object>>>>) answer
									.get("responses");
							responses.stream().forEach(rxm -> {
								final ExportResponsesListEntryType en = new ExportResponsesListEntryType();
								rr.getResponses().add(en);
								rxm.values().stream().findFirst()
										.ifPresent(rm -> {
											en.setId(String
													.valueOf(rm.get("id")));
											en.setLastpage(String.valueOf(
													rm.get("lastpage")));
											en.setStartlanguage(String.valueOf(
													rm.get("startlanguage")));
											// submitdate
											// lastpage
											// token
											// startdate
											// datestamp
											// ipaddr
											// refurl
											rm.keySet().stream()
													.forEach(key -> {
														final PropertyType pt = new PropertyType();
														pt.setKey(key);
														pt.setValue(
																rm.get(key));
														en.getAnswers().add(pt);
														System.out.println(key);
													});
										});
							});
						} catch (final IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		}
		return value;
	}

	/**
	 * Calls the operation <i>release_session_key</i>.
	 *
	 * @param session
	 *            the {@link LimeSurveySession} to release.
	 * @throws JsonProcessingException
	 */
	public void releaseSessionKey(final LimeSurveySession session)
			throws JsonProcessingException {
		if (Objects.isNull(session)) {
			throw new IllegalStateException("Session not provided.");
		} else {
			final String method = "release_session_key";
			final RequestType request = this.getRequestType(method,
					session.getSessionKey(), session.getUserName());
			this.executeRequst(request);
		}
	}
}
