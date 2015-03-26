package org.jitsi.impl.protocol.xmpp.extensions;

import net.java.sip.communicator.impl.protocol.jabber.extensions.AbstractPacketExtension;

/**
 * The Class PrivateCreateExt.
 */
public class PrivateCreateExt extends AbstractPacketExtension {
	/**
	 * XML namespace of this packet extension.
	 */
	public static final String NAMESPACE = "jabber:iq:private";

	/**
	 * XML element name of this packet extension.
	 */
	public static final String ELEMENT_NAME = "query";

	/** The focus id. */
	private String focusId;

	/** The room id. */
	private String roomId;

	/**
	 * Creates new instance of <tt>SessionInvalidPacketExtension</tt>.
	 */
	public PrivateCreateExt() {
		super(NAMESPACE, ELEMENT_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.sip.communicator.impl.protocol.jabber.extensions.
	 * AbstractPacketExtension#toXML()
	 */
	@Override
	public String toXML() {

		StringBuilder output = new StringBuilder();

		output.append("<iq type='set' to=")
				.append(getRoomId())
				.append(" from=")
				.append(getFocusId())
				.append(" id='11:sendIQ'>")
				.append("<recording xmlns='http://jitsi.org/protocol/jirecon' ")
				.append("action='VideoAudioConnected' ")
				.append("mucjid='call at conference.example.com' rid=''/></iq>");

		return output.toString();
	}

	/**
	 * Gets the focus id.
	 *
	 * @return the focus id
	 */
	public String getFocusId() {
		return focusId;
	}

	/**
	 * Sets the focus id.
	 *
	 * @param focusId
	 *            the new focus id
	 */
	public void setFocusId(String focusId) {
		this.focusId = focusId;
	}

	/**
	 * Gets the room id.
	 *
	 * @return the room id
	 */
	public String getRoomId() {
		return roomId;
	}

	/**
	 * Sets the room id.
	 *
	 * @param roomId
	 *            the new room id
	 */
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

}
