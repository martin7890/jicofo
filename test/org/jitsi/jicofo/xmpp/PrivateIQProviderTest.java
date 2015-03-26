package org.jitsi.jicofo.xmpp;

import static org.junit.Assert.*;

import org.jitsi.impl.protocol.xmpp.extensions.PrivateIQ;
import org.jitsi.impl.protocol.xmpp.extensions.PrivateIQProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * The Class PrivateIQProviderTest.
 * 
 * This is a test case for testing private iq creation.
 */
@RunWith(JUnit4.class)
public class PrivateIQProviderTest {

	/**
	 * Test parse iq.
	 * 
	 * Test case for testing private iq parsing
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testParseIq() throws Exception {
		String iqXml = "<iq to='t' from='f'>"
				+ "<query xmlns='jabber:iq:private'>"
				+ "<media xmlns='media:prefs'>"
				+ "<info jabberid='xyz@localhost/abc' media='AudioVideo' action='connected' />"
				+ "</media></query></iq>";

		PrivateIQProvider provider = new PrivateIQProvider();
		PrivateIQ privateIQ = (PrivateIQ) IQUtils.parse(iqXml, provider);

		assertEquals("f", privateIQ.getFrom());
		assertEquals("t", privateIQ.getTo());

		assertEquals("xyz@localhost/abc", privateIQ.getJabberid());
		assertEquals("AudioVideo", privateIQ.getMedia());
		assertEquals("connected", privateIQ.getAction());
	}

	/**
	 * Test to xml.
	 * 
	 * Test case for testing private iq xml creation.
	 */
	@Test
	public void testToXml() {
		PrivateIQ privateIQ = new PrivateIQ();

		privateIQ.setPacketID("123xyz");
		privateIQ.setTo("toJid");
		privateIQ.setFrom("fromJid");
		privateIQ.setJabberid("xyz@localhost/abc");

		privateIQ.setAudioSupport(true);
		privateIQ.setVideoSupport(true);
		privateIQ.setConnected(true);

		assertEquals(
				"<iq id=\"123xyz\" to=\"toJid\" from=\"fromJid\" type=\"get\">"
						+ "<query xmlns='jabber:iq:private'>"
						+ "<media xmlns='media:prefs'>"
						+ "<info jabberid='xyz@localhost/abc' media='AudioVideo' action='connected' />"
						+ "</media></query></iq>", privateIQ.toXML());
	}

}
