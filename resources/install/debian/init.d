#! /bin/sh
#
# INIT script for Jitsi Conference Focus
# Version: 1.0  4-Dec-2014  pawel.domas@jitsi.org
#
### BEGIN INIT INFO
# Provides:          jicofo
# Required-Start:    $local_fs $remote_fs
# Required-Stop:     $local_fs $remote_fs
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Jitsi conference Focus
# Description:       Conference focus for Jitsi Meet application.
### END INIT INFO

. /lib/lsb/init-functions

# Include jicofo defaults if available
if [ -f /etc/jitsi/jicofo/config ]; then
    . /etc/jitsi/jicofo/config
fi
# Assign default host if not configured
if [ ! $JICOFO_HOST ]; then
    JICOFO_HOST=localhost
fi


PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin
DAEMON=/usr/share/jicofo/jicofo.sh
DEAMON_DIR=/usr/share/jicofo/
NAME=jicofo
USER=jicofo
PIDFILE=/var/run/jicofo.pid
LOGFILE=/var/log/jitsi/jicofo.log
DESC=jicofo

if test -z "$JICOFO_SUBDOMAIN"; then
DAEMON_OPTS=" --host=$JICOFO_HOST --domain=$JICOFO_HOSTNAME --port=$JICOFO_PORT --secret=$JICOFO_SECRET --user_domain=$JICOFO_AUTH_DOMAIN --user_name=xrtc_sp00f_f0cus --user_password=$JICOFO_AUTH_PASSWORD $JICOFO_OPTS"
else
DAEMON_OPTS=" --host=$JICOFO_HOST --domain=$JICOFO_HOSTNAME --port=$JICOFO_PORT --secret=$JICOFO_SECRET --subdomain=$JICOFO_SUBDOMAIN --user_domain=$JICOFO_AUTH_DOMAIN --user_name=xrtc_sp00f_f0cus --user_password=$JICOFO_AUTH_PASSWORD $JICOFO_OPTS"
fi

test -x $DAEMON || exit 0

set -e

killParentPid() {
    PARENT_PPID=$(ps -o pid --no-headers --ppid $1 || true)
    if [ $PARENT_PPID ]; then
        kill $PARENT_PPID
    fi
}

stop() {
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
    fi
    echo -n "Stopping $DESC: "
    if [ $PID ]; then
        killParentPid $PID
        rm $PIDFILE || true
        echo "$NAME stopped."
    elif [ $(ps -C jicofo.sh --no-headers -o pid) ]; then
        kill $(ps -o pid --no-headers --ppid $(ps -C jicofo.sh --no-headers -o pid))
        rm $PIDFILE || true
        echo "$NAME stopped."
    else
        echo "$NAME doesn't seem to be running."
    fi
}

start() {
    if [ -f $PIDFILE ]; then
        echo "$DESC seems to be already running, we found pidfile $PIDFILE."
        exit 1
    fi
    echo -n "Starting $DESC: "
    start-stop-daemon --start --quiet --background --chuid $USER --make-pidfile --pidfile $PIDFILE \
        --exec /bin/bash -- -c "cd $DEAMON_DIR; exec $DAEMON $DAEMON_OPTS < /dev/null >> $LOGFILE 2>&1"
    echo "$NAME started."
}

reload() {
    echo 'Not yet implemented.'
}

status() {
    status_of_proc -p $PIDFILE "$DAEMON" "$NAME" && exit 0 || exit $?
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    start
    ;;
  reload)
    reload
    ;;
  force-reload)
    reload
    ;;
  status)
    status
    ;;
  *)
    N=/etc/init.d/$NAME
    echo "Usage: $N {start|stop|restart|reload|status}" >&2
    exit 1
    ;;
esac

exit 0
