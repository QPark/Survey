package com.qpark.lime.survey;

/**
 * @author bhausen
 */
public class LimeSurveySession {
	private String sessionKey;
	private String userName;

	/**
	 * @return the sessionKey
	 */
	public String getSessionKey() {
		return this.sessionKey;
	}

	/**
	 * @param sessionKey
	 *            the sessionKey to set
	 */
	public void setSessionKey(final String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}
}
