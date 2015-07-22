/*
 * Jicofo, the Jitsi Conference Focus.
 *
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.jicofo;

import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.service.protocol.event.*;
import net.java.sip.communicator.service.shutdown.*;
import net.java.sip.communicator.util.*;
import net.java.sip.communicator.util.Logger;

import org.jitsi.jicofo.log.*;
import org.jitsi.protocol.*;
import org.jitsi.protocol.xmpp.*;
import org.jitsi.service.configuration.*;
import org.jitsi.util.*;
import org.jitsi.videobridge.eventadmin.*;
import org.jivesoftware.smack.provider.*;

import java.util.*;

/**
 * Manages {@link JitsiMeetConference} on some server. Takes care of creating
 * and expiring conference focus instances. Manages focus XMPP connection.
 *
 * @author Pawel Domas
 */
public class FocusManager
    implements JitsiMeetConference.ConferenceListener,
               RegistrationStateChangeListener
{
    /**
     * The logger used by this instance.
     */
    private final static Logger logger = Logger.getLogger(FocusManager.class);

    /**
     * Name of configuration property for focus idle timeout.
     */
    public static final String IDLE_TIMEOUT_PROP_NAME
        = "org.jitsi.focus.IDLE_TIMEOUT";

    /**
     * Default amount of time for which the focus is being kept alive in idle
     * mode(no peers in the room).
     */
    public static final long DEFAULT_IDLE_TIMEOUT = 15000;

    /**
     * The name of configuration property that specifies server hostname to
     * which the focus user will connect to.
     */
    public static final String HOSTNAME_PNAME = "org.jitsi.jicofo.HOSTNAME";

    /**
     * The name of configuration property that specifies XMPP domain that hosts
     * the conference and will be used in components auto-discovery. This is the
     * domain on which the jitsi-videobridge runs.
     */
    public static final String XMPP_DOMAIN_PNAME
        = "org.jitsi.jicofo.XMPP_DOMAIN";

    /**
     * The name of configuration property that specifies XMPP domain of
     * the focus user.
     */
    public static final String FOCUS_USER_DOMAIN_PNAME
        = "org.jitsi.jicofo.FOCUS_USER_DOMAIN";

    /**
     * The name of configuration property that specifies the user name used by
     * the focus to login to XMPP server.
     */
    public static final String FOCUS_USER_NAME_PNAME
        = "org.jitsi.jicofo.FOCUS_USER_NAME";

    /**
     * The name of configuration property that specifies login password of the
     * focus user. If not provided then anonymous login method is used.
     */
    public static final String FOCUS_USER_PASSWORD_PNAME
        = "org.jitsi.jicofo.FOCUS_USER_PASSWORD";

    /**
     * The XMPP domain used by the focus user to register to.
     */
    private String focusUserDomain;

    /**
     * The username used by the focus to login.
     */
    private String focusUserName;

    /**
     * The thread that expires {@link JitsiMeetConference}s.
     */
    private FocusExpireThread expireThread = new FocusExpireThread();

    /**
     * Jitsi Meet conferences mapped by MUC room names.
     */
    private Map<String, JitsiMeetConference> conferences
        = new HashMap<String, JitsiMeetConference>();

    // Convert to list when needed
    /**
     * FIXME: remove eventually if not used anymore
     * The list of {@link FocusAllocationListener}.
     */
    private FocusAllocationListener focusAllocListener;

    /**
     * XMPP protocol provider handler used by the focus.
     */
    private ProtocolProviderHandler protocolProviderHandler
        = new ProtocolProviderHandler();

    /**
     * <tt>JitsiMeetServices</tt> instance that recognizes currently available
     * conferencing services like Jitsi videobridge or SIP gateway.
     */
    private JitsiMeetServices jitsiMeetServices;

    /**
     * Observes and discovers JVB instances and other conference components on
     * our XMPP domain.
     */
    private ComponentsDiscovery componentsDiscovery;

    /**
     * Indicates if graceful shutdown mode has been enabled and
     * no new conference request will be accepted.
     */
    private boolean shutdownInProgress;

    /**
     * Handler that takes care of pre-processing various Jitsi Meet extensions
     * IQs sent from conference participants to the focus.
     */
    private MeetExtensionsHandler meetExtensionsHandler;

    /**
     * Starts this manager for given <tt>hostName</tt>.
     */
    public void start()
    {
        expireThread.start();

        ConfigurationService config = FocusBundleActivator.getConfigService();

        String hostName = config.getString(HOSTNAME_PNAME);

        String xmppDomain = config.getString(XMPP_DOMAIN_PNAME);

        focusUserDomain = config.getString(FOCUS_USER_DOMAIN_PNAME);

        focusUserName = config.getString(FOCUS_USER_NAME_PNAME);

        String focusUserPassword = config.getString(FOCUS_USER_PASSWORD_PNAME);

        protocolProviderHandler.start(
            hostName, focusUserDomain, focusUserPassword, focusUserName);

        jitsiMeetServices = new JitsiMeetServices(
            protocolProviderHandler.getOperationSet(
                OperationSetSubscription.class));

        componentsDiscovery = new ComponentsDiscovery(jitsiMeetServices);

        componentsDiscovery.start(xmppDomain, protocolProviderHandler);

        meetExtensionsHandler = new MeetExtensionsHandler(this);

        ProviderManager
            .getInstance()
                .addExtensionProvider(LogPacketExtension.LOG_ELEM_NAME,
                    LogPacketExtension.NAMESPACE,
                    new LogExtensionProvider());

        FocusBundleActivator
            .bundleContext.registerService(
                JitsiMeetServices.class, jitsiMeetServices, null);

        protocolProviderHandler.addRegistrationListener(this);

        protocolProviderHandler.register();
    }

    /**
     * Stops this instance.
     */
    public void stop()
    {
        expireThread.stop();

        if (componentsDiscovery != null)
        {
            componentsDiscovery.stop();
            componentsDiscovery = null;
        }

        meetExtensionsHandler.dispose();

        protocolProviderHandler.stop();
    }

    /**
     * Allocates new focus for given MUC room.
     * @param room the name of MUC room for which new conference has to be
     *             allocated.
     * @param properties configuration properties map included in the request.
     * @return <tt>true</tt> if conference focus is in the room and ready to
     *         handle session participants.
     *
     * @throws Exception if for any reason we have failed to create
     *                   the conference
     */
    public synchronized boolean conferenceRequest(
            String room, Map<String, String> properties)
        throws Exception
    {
        if (StringUtils.isNullOrEmpty(room))
            return false;

        if (shutdownInProgress && !conferences.containsKey(room))
            return false;

        if (!conferences.containsKey(room))
        {
            createConference(room, properties);
        }

        JitsiMeetConference conference = conferences.get(room);

        return conference.isInTheRoom();
    }

    /**
     * Makes sure that conference is allocated for given <tt>room</tt>.
     * @param room name of the MUC room of Jitsi Meet conference.
     * @param properties configuration properties, see {@link JitsiMeetConfig}
     *                   for the list of valid properties.
     *
     * @throws Exception if any error occurs.
     */
    private void createConference(String room, Map<String, String> properties)
        throws Exception
    {
        JitsiMeetConfig config = new JitsiMeetConfig(properties);

        JitsiMeetConference conference
            = new JitsiMeetConference(
                    room, focusUserName, protocolProviderHandler, this, config);

        conferences.put(room, conference);

        StringBuilder options = new StringBuilder();
        for (Map.Entry<String, String> option : properties.entrySet())
        {
            options.append("\n    ")
                .append(option.getKey())
                .append(": ")
                .append(option.getValue());

        }

       /* logger.info("Created new focus for " + room + "@" + focusUserDomain
                        + " conferences count: " + conferences.size()
                        + " options:" + options.toString());*/
        

        logger.audit("RTCServer:" +System.getProperty(FocusManager.HOSTNAME_PNAME)+", MucID:"
        			+room + ", RoutingID :" +null +", Message:"+
        			  "Created new focus for " + room + "@" + focusUserDomain
        			 + " conferences count: " + conferences.size()
        			 + " options:" + options.toString());

        
        
        
        

        // Send focus created event
        EventAdmin eventAdmin = FocusBundleActivator.getEventAdmin();
        if (eventAdmin != null)
        {
            eventAdmin.sendEvent(
                EventFactory.focusCreated(
                    conference.getId(), conference.getRoomName()));
        }

        try
        {
            conference.start();
        }
        catch (Exception e)
        {
            logger.info("Exception while trying to start the conference", e);

            conference.stop();

            throw e;
        }
    }

    /**
     * Destroys the conference for given room name.
     * @param roomName full MUC room name to destroy.
     * @param reason optional reason string that will be advertised to the
     *               users upon exit.
     */
    public synchronized void destroyConference(String roomName, String reason)
    {
        JitsiMeetConference conference = getConference(roomName);
        if (conference == null)
        {
            logger.error(
                "Unable to destroy the conference - not found: " + roomName);
            return;
        }

        conference.destroy(reason);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void conferenceEnded(JitsiMeetConference conference)
    {
        String roomName = conference.getRoomName();

        conferences.remove(roomName);

      /*  logger.info(
            "Disposed conference for room: " + roomName
            + " conference count: " + conferences.size());*/

        logger.audit("RTCServer:" +System.getProperty(FocusManager.HOSTNAME_PNAME)+", MucID:" +roomName + ", RoutingID :" +null +", Message:"+" Disposed conference for room: " + roomName
                + " conference count: " + conferences.size());
        
        if (focusAllocListener != null)
        {
            focusAllocListener.onFocusDestroyed(roomName);
        }

        // Send focus destroyed event
        FocusBundleActivator.getEventAdmin().sendEvent(
                EventFactory.focusDestroyed(
                        conference.getId(), conference.getRoomName()));

        maybeDoShutdown();
    }

    /**
     * Returns {@link JitsiMeetConference} for given MUC <tt>roomName</tt>
     * or <tt>null</tt> if no conference has been allocated yet.
     *
     * @param roomName the name of MUC room for which we want get the
     *        {@link JitsiMeetConference} instance.
     */
    public JitsiMeetConference getConference(String roomName)
    {
        return conferences.get(roomName);
    }

    /**
     * Enables shutdown mode which means that no new focus instances will
     * be allocated. After conference count drops to zero the process will exit.
     */
    public void enableGracefulShutdownMode()
    {
        if (!this.shutdownInProgress)
        {
            logger.info("Focus entered graceful shutdown mode");
        }
        this.shutdownInProgress = true;
        maybeDoShutdown();
    }

    private void maybeDoShutdown()
    {
        if (shutdownInProgress && conferences.size() == 0)
        {
            logger.info("Focus is shutting down NOW");

            ShutdownService shutdownService
                = ServiceUtils.getService(
                        FocusBundleActivator.bundleContext,
                        ShutdownService.class);

            shutdownService.beginShutdown();
        }
    }

    /**
     * Returns the number of currently allocated focus instances.
     */
    public int getConferenceCount()
    {
        return conferences.size();
    }

    /**
     * Returns <tt>true</tt> if graceful shutdown mode has been enabled and
     * the process is going to be finished once conference count drops to zero.
     */
    public boolean isShutdownInProgress()
    {
        return shutdownInProgress;
    }

    /**
     * Sets the listener that will be notified about conference focus
     * allocation/disposal.
     * @param l the listener instance to be registered.
     */
    public void setFocusAllocationListener(FocusAllocationListener l)
    {
        this.focusAllocListener = l;
    }

    /**
     * Returns instance of <tt>JitsiMeetServices</tt> used in conferences.
     */
    public JitsiMeetServices getJitsiMeetServices()
    {
        return jitsiMeetServices;
    }

    /**
     * Interface used to listen for focus lifecycle events.
     */
    public interface FocusAllocationListener
    {
        /**
         * Method fired when focus is destroyed.
         * @param roomName the name of the conference room for which focus
         *                 has been destroyed.
         */
        void onFocusDestroyed(String roomName);

        // Add focus allocated method if needed
    }

    /**
     * Returns operation set instance for focus XMPP connection.
     * @param opsetClass operation set class.
     * @param <T> the class of Operation Set to be reqturned
     * @return operation set instance of given class or <tt>null</tt> if
     * given operation set is not implemented by focus XMPP provider.
     */
    public <T extends OperationSet> T getOperationSet(Class<T> opsetClass)
    {
        return protocolProviderHandler.getOperationSet(opsetClass);
    }

    @Override
    public void registrationStateChanged(RegistrationStateChangeEvent evt)
    {
        RegistrationState registrationState = evt.getNewState();
        logger.info("XMPP provider reg state: " + registrationState);
        if (RegistrationState.REGISTERED.equals(registrationState))
        {
            // Do initializations which require valid connection
            meetExtensionsHandler.init();
        }
    }

    /**
     * Class takes care of stopping {@link JitsiMeetConference} if there is no
     * active session for too long.
     */
    class FocusExpireThread
    {
        private static final long POLL_INTERVAL = 5000;

        private final long timeout;

        private Thread timeoutThread;

        private final Object sleepLock = new Object();

        private boolean enabled;

        public FocusExpireThread()
        {
            timeout = FocusBundleActivator.getConfigService()
                        .getLong(IDLE_TIMEOUT_PROP_NAME, DEFAULT_IDLE_TIMEOUT);
        }

        void start()
        {
            if (timeoutThread != null)
            {
                throw new IllegalStateException();
            }

            timeoutThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    expireLoop();
                }
            }, "FocusExpireThread");

            enabled = true;

            timeoutThread.start();
        }

        void stop()
        {
            if (timeoutThread == null)
            {
                return;
            }

            enabled = false;

            synchronized (sleepLock)
            {
                sleepLock.notifyAll();
            }

            try
            {
                if (Thread.currentThread() != timeoutThread)
                {
                    timeoutThread.join();
                }
                timeoutThread = null;
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

        private void expireLoop()
        {
            while (enabled)
            {
                // Sleep
                try
                {
                    synchronized (sleepLock)
                    {
                        sleepLock.wait(POLL_INTERVAL);
                    }
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }

                if (!enabled)
                    break;

                // Loop over conferences
                for (JitsiMeetConference conference
                    : new ArrayList<JitsiMeetConference>(conferences.values()))
                {
                    long idleStamp = conference.getIdleTimestamp();
                    // Is active ?
                    if (idleStamp == -1)
                    {
                        continue;
                    }
                    if (System.currentTimeMillis() - idleStamp > timeout)
                    {
                        logger.info(
                            "Focus idle timeout for "
                                + conference.getRoomName());

                        conference.stop();
                    }
                }
            }
        }
    }
}
