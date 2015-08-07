package org.jitsi.impl.protocol.xmpp.extensions;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

/**
 * The Class PrivateIQProvider.
 */
public class PrivateIQProvider implements IQProvider {

	/**
	 * Creates new instance of <tt>ConferenceIqProvider</tt>.
	 */
	public PrivateIQProvider() {
		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addIQProvider(PrivateIQ.QUERY_ELEMENT_NAME,
				ConferenceIq.NAMESPACE, this);
	}

	/**
	 * Registers this IQ provider into given <tt>ProviderManager</tt>.
	 * 
	 * @param providerManager
	 *            the <tt>ProviderManager</tt> to which this instance will be
	 *            bound to.
	 */
	public void registerPrivateIQProvider(ProviderManager providerManager) {
		providerManager.addIQProvider(PrivateIQ.QUERY_ELEMENT_NAME,
				PrivateIQ.NAMESPACE, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		String namespace = parser.getNamespace();

		// Check the namespace
		if (!PrivateIQ.NAMESPACE.equals(namespace)) {
			return null;
		}

		PrivateIQ iq = new PrivateIQ();

		parser.require(XmlPullParser.START_TAG, null,
				PrivateIQ.QUERY_ELEMENT_NAME);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals(PrivateIQ.INFO_ELEMENT_NAME)) {
				String jabberid = parser.getAttributeValue("",
						PrivateIQ.ROUTING_ATTR_NAME);
				String action = parser.getAttributeValue("",
						PrivateIQ.ACTION_ATTR_NAME);
				String media = parser.getAttributeValue("",
						PrivateIQ.MEDIA_ATTR_NAME);

				iq.setJabberid(jabberid);
				iq.setAction(action);
				iq.setMedia(media);
			}
		}

		return iq;
	}
}
