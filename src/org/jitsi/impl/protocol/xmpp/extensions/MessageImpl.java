package org.jitsi.impl.protocol.xmpp.extensions;

import net.java.sip.communicator.service.protocol.AbstractMessage;

/**
 * The Class MessageImpl.
 */
public class MessageImpl extends AbstractMessage {

	/**
	 * Instantiates a new message impl.
	 *
	 * @param content
	 *            the content
	 * @param contentType
	 *            the content type
	 * @param encoding
	 *            the encoding
	 * @param subject
	 *            the subject
	 */
	public MessageImpl(String content, String contentType, String encoding,
			String subject) {
		super(content, contentType, encoding, subject);
	}

}
