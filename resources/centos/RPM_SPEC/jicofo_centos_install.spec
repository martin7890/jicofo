Summary: JItsi COnference FOcus is a server side focus component used in  xrtc conferences.
Name: jicofo_centos7.1
Version: 2.0.17
Release: 0
Source: jicofo_centos.tar.gz
License: GPL
Group: Development/Tools
%description
Managing media sessions between each of the participants and the videobridge
%prep
# jicofo centos install
rm -rf $RPM_BUILD_DIR/jicofo_centos
zcat $RPM_SOURCE_DIR/jicofo_centos.tar.gz | tar -xvf -
%build
%install
mkdir -p $RPM_BUILD_ROOT/etc/init.d
mkdir -p $RPM_BUILD_ROOT/etc/jitsi
mkdir -p $RPM_BUILD_ROOT/etc/jitsi/jicofo
mkdir -p $RPM_BUILD_ROOT/usr/share
mkdir -p $RPM_BUILD_ROOT/usr/share/jicofo
mkdir -p $RPM_BUILD_ROOT/usr/share/jicofo/.sip-communicator
mkdir -p $RPM_BUILD_ROOT/usr/share/jicofo/lib
mkdir -p $RPM_BUILD_ROOT/usr/share/jicofo/lib/native
mkdir -p $RPM_BUILD_ROOT/usr/share/jicofo/lib/native/linux-64
#/etc/init.d
install -m 755 jicofo_centos/etc/init.d/jicofo $RPM_BUILD_ROOT/etc/init.d/
#/etc/jitsi/jicofo
install -m 755 jicofo_centos/etc/jitsi/jicofo/config.default $RPM_BUILD_ROOT/etc/jitsi/jicofo/
#/usr/share/jicofo
install -m 755 jicofo_centos/usr/share/jicofo/jicofo.jar $RPM_BUILD_ROOT/usr/share/jicofo/
install -m 755 jicofo_centos/usr/share/jicofo/jicofo.sh $RPM_BUILD_ROOT/usr/share/jicofo/
#/usr/share/jicofo/.sip-communicator
install -m 755 jicofo_centos/usr/share/jicofo/.sip-communicator/sip-communicator.properties $RPM_BUILD_ROOT/usr/share/jicofo/.sip-communicator/
#/usr/share/jicofo/lib
install -m 755 jicofo_centos/usr/share/jicofo/lib/agafua-syslog.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/bccontrib.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/bcpkix-jdk15on.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/bcprov-jdk15on.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/commons-codec.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/commons-lang.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/commons-lang3.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/commons-logging.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/concurrentlinkedhashmap-lru.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/core.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/dnsjava.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/dom4j.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/fmj.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
#install -m 755 jicofo_centos/usr/share/jicofo/lib/gson.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/guava.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/httpclient.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/httpcore.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/ice4j.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
#install -m 755 jicofo_centos/usr/share/jicofo/lib/influxdb-java.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/javax.servlet-api.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/javax.servlet.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jcip-annotations.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-ajp.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-client.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-continuation.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-http.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-io.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-proxy.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-security.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-server.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-servlet.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-util.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-webapp.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jetty-xml.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jicoco.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-android-osgi.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-configuration.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-credentialsstorage.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-dnsservice.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-netaddr.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-protocol-jabber.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-protocol.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-protocol-media.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-ui-service.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jitsi-util.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jna.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jnsapi.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/json-simple.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jzlib.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/log4j.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/libidn.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/libjitsi.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/logging.properties $RPM_BUILD_ROOT/usr/share/jicofo/lib/
#install -m 755 jicofo_centos/usr/share/jicofo/lib/okhttp.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
#install -m 755 jicofo_centos/usr/share/jicofo/lib/okio.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/orange-extensions.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/org.apache.felix.framework.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/org.apache.felix.main.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/org.osgi.core.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
#install -m 755 jicofo_centos/usr/share/jicofo/lib/retrofit.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/sdes4j.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/slf4j-api.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/slf4j-jdk14.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/smack.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/smackx.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/tinder.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/xml-apis.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/xmlpull.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/xpp3.jar  $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/zrtp4j-light.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/weupnp.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/jain-sip-ri-ossonly.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/java-sdp-nist-bridge.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
install -m 755 jicofo_centos/usr/share/jicofo/lib/sdp-api.jar $RPM_BUILD_ROOT/usr/share/jicofo/lib/
#/usr/share/jicofo/lib/native/linux-64
install -m 755 jicofo_centos/usr/share/jicofo/lib/native/linux-64/libhwaddressretriever.so $RPM_BUILD_ROOT/usr/share/jicofo/lib/native/linux-64/
%files
#/etc/init.d
/etc/init.d/jicofo
#/etc/jitsi/jicofo
/etc/jitsi/jicofo/config.default
#/usr/share/jicofo
/usr/share/jicofo/jicofo.jar
/usr/share/jicofo/jicofo.sh
#/usr/share/jicofo/.sip-communicator
/usr/share/jicofo/.sip-communicator/sip-communicator.properties
#/usr/share/jicofo/lib
/usr/share/jicofo/lib/agafua-syslog.jar
/usr/share/jicofo/lib/bccontrib.jar
/usr/share/jicofo/lib/bcpkix-jdk15on.jar
/usr/share/jicofo/lib/bcprov-jdk15on.jar
/usr/share/jicofo/lib/commons-codec.jar
/usr/share/jicofo/lib/commons-logging.jar
/usr/share/jicofo/lib/commons-lang3.jar
/usr/share/jicofo/lib/commons-lang.jar
/usr/share/jicofo/lib/concurrentlinkedhashmap-lru.jar
/usr/share/jicofo/lib/core.jar
/usr/share/jicofo/lib/dnsjava.jar
/usr/share/jicofo/lib/dom4j.jar
/usr/share/jicofo/lib/fmj.jar
#/usr/share/jicofo/lib/gson.jar
/usr/share/jicofo/lib/guava.jar
/usr/share/jicofo/lib/httpclient.jar
/usr/share/jicofo/lib/httpcore.jar
/usr/share/jicofo/lib/ice4j.jar
#/usr/share/jicofo/lib/influxdb-java.jar
/usr/share/jicofo/lib/javax.servlet-api.jar
/usr/share/jicofo/lib/javax.servlet.jar
/usr/share/jicofo/lib/jcip-annotations.jar
/usr/share/jicofo/lib/jetty-ajp.jar
/usr/share/jicofo/lib/jetty-client.jar
/usr/share/jicofo/lib/jetty-continuation.jar
/usr/share/jicofo/lib/jetty-http.jar
/usr/share/jicofo/lib/jetty-io.jar
/usr/share/jicofo/lib/jetty-proxy.jar
/usr/share/jicofo/lib/jetty-security.jar
/usr/share/jicofo/lib/jetty-server.jar
/usr/share/jicofo/lib/jetty-servlet.jar
/usr/share/jicofo/lib/jetty-util.jar
/usr/share/jicofo/lib/jetty-webapp.jar
/usr/share/jicofo/lib/jetty-xml.jar
/usr/share/jicofo/lib/jicoco.jar
/usr/share/jicofo/lib/jitsi-android-osgi.jar
/usr/share/jicofo/lib/jitsi-configuration.jar
/usr/share/jicofo/lib/jitsi-credentialsstorage.jar
/usr/share/jicofo/lib/jitsi-dnsservice.jar
/usr/share/jicofo/lib/jitsi-netaddr.jar
/usr/share/jicofo/lib/jitsi-protocol-jabber.jar
/usr/share/jicofo/lib/jitsi-protocol.jar
/usr/share/jicofo/lib/jitsi-protocol-media.jar
/usr/share/jicofo/lib/jitsi-ui-service.jar
/usr/share/jicofo/lib/jitsi-util.jar
/usr/share/jicofo/lib/jna.jar
/usr/share/jicofo/lib/jnsapi.jar
/usr/share/jicofo/lib/json-simple.jar
/usr/share/jicofo/lib/jzlib.jar
/usr/share/jicofo/lib/log4j.jar
/usr/share/jicofo/lib/libidn.jar
/usr/share/jicofo/lib/libjitsi.jar
/usr/share/jicofo/lib/logging.properties
/usr/share/jicofo/lib/native
#/usr/share/jicofo/lib/okhttp.jar
#/usr/share/jicofo/lib/okio.jar
/usr/share/jicofo/lib/orange-extensions.jar
/usr/share/jicofo/lib/org.apache.felix.framework.jar
/usr/share/jicofo/lib/org.apache.felix.main.jar
/usr/share/jicofo/lib/org.osgi.core.jar
#/usr/share/jicofo/lib/retrofit.jar
/usr/share/jicofo/lib/sdes4j.jar
/usr/share/jicofo/lib/weupnp.jar
/usr/share/jicofo/lib/slf4j-api.jar
/usr/share/jicofo/lib/slf4j-jdk14.jar
/usr/share/jicofo/lib/smack.jar
/usr/share/jicofo/lib/smackx.jar
/usr/share/jicofo/lib/xml-apis.jar
/usr/share/jicofo/lib/xmlpull.jar
/usr/share/jicofo/lib/xpp3.jar
/usr/share/jicofo/lib/tinder.jar
/usr/share/jicofo/lib/zrtp4j-light.jar
/usr/share/jicofo/lib/jain-sip-ri-ossonly.jar
/usr/share/jicofo/lib/java-sdp-nist-bridge.jar
/usr/share/jicofo/lib/sdp-api.jar
#/usr/share/jicofo/lib/native/linux-64
/usr/share/jicofo/lib/native/linux-64/libhwaddressretriever.so
%pre
if id -u "jicofo" >/dev/null 2>&1; then
	userdel jicofo
	rm -rf /usr/share/jicofo
	echo "account \"jicofo\" exists. Deleting account."
fi
if grep -q "^${jitsi}:" /etc/group; then
	echo "group \"jitsi\" exists"
else
	groupadd jitsi
fi
mkdir -p /usr/share/jicofo
useradd -c "jicofo ,,," -m -g jitsi -d /usr/share/jicofo jicofo
chown jicofo:jitsi /usr/share/jicofo
if [ ! -d "/var/log/jitsi" ]; then
	mkdir -p /var/log/jitsi
fi
chown jicofo:jitsi /var/log/jitsi
chown jicofo:jitsi /var/log/jitsi/jicofo*
chmod 775 /var/log/jitsi
