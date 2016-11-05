package com.qpark.survey.lime.test;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qpark.survey.lime.RestClient;
import com.qpark.survey.lime.model.v25.ListSurveysResponseType;

/**
 * Sample implementation.
 *
 * @author bhausen
 */
@Configuration
@Import({ com.qpark.survey.lime.config.SpringConfig.class })
public class Sample implements InitializingBean {
	@Autowired
	private RestClient client;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final ConfigurableApplicationContext ac = SpringApplication
				.run(Sample.class, args);
	}

	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory.getLogger(Sample.class);

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.run();
	}

	private void run() throws Exception {
		final String endPoint = "http://server/index.php/admin/remotecontrol";
		final String userName = "userName";
		final String password = "password";
		this.client.setEndPointUrl(endPoint);
		this.client.getSessionKey(userName, password.toCharArray())
				.ifPresent(session -> {
					try {
						this.logger.info("logged in");
						final Optional<ListSurveysResponseType> listSurveys = this.client
								.listSurveys(session);
						listSurveys.ifPresent(surveys -> surveys.getResult()
								.stream().filter(survey -> survey.isActive())
								.forEach(survey -> {
									this.logger.info(
											"###########################################");
									this.logger.info("Survey: {}",
											survey.getSurveylsTitle());
									try {
										this.client
												.getListQuestions(session,
														survey.getSid())
												.ifPresent(qs -> qs.getResult()
														.stream().forEach(
																q -> this.logger
																		.info("Question: {}",
																				q.getQuestion())));
									} catch (final IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									try {
										this.client
												.getExportResponses(session,
														survey.getSid(),
														Optional.of("en"))
												.ifPresent(rs -> rs
														.getResponses().stream()
														.forEach(r -> {
															this.logger.info(
																	"Answer: {}",
																	r.getAnswers());
														}));
									} catch (final IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}));
					} catch (final Exception e) {
						e.printStackTrace();
					} finally {
						try {
							this.client.releaseSessionKey(session);
						} catch (final JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
}
