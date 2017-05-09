#! /bin/bash

## @Author : Martin Sebastian ##

# Rum mvn command for creating zip file.
mvn clean install -f ../pom.xml -Dmaven.test.skip=true

# Unzip the created zip.
unzip ../target/jicofo-linux-x64*.zip

# Cleaning up the existing code copy.
rm -rf centos/jicofo_centos/usr/share/jicofo/*

# Copying new code from new zip.
cp -r jicofo-linux-x64*/* centos/jicofo_centos/usr/share/jicofo/.

# Creating tar file for RPM creation.
cd centos/
rm -rf jicofo_centos.tar.gz
tar -cvzf jicofo_centos.tar.gz jicofo_centos
cd ../

# Is rpm-build is installed ?
# If no - Install it.
if [ "rpm -q rpm-build | wc -l" = 0 ]; then
    yum -y install rpm-build
fi

DIR=`pwd`

# Is the rpmbuild directory structure present ?
# If no - Create the directory structure.
if [ ! -d "$DIR/rpmbuild/" ]; then
    mkdir $DIR/rpmbuild/
    mkdir $DIR/rpmbuild/SOURCES/
    mkdir $DIR/rpmbuild/SPECS
else
    if [ ! -d "$DIR/rpmbuild/SOURCES/" ]; then
         mkdir $DIR/rpmbuild/SOURCES/
    fi
    if [ ! -d "$DIR/rpmbuild/SPECS/" ]; then
         mkdir $DIR/rpmbuild/SPECS/
    fi
fi

# Cleaning the sources and specs before copying new ones.
rm -rf $DIR/rpmbuild/SOURCES/*
rm -rf $DIR/rpmbuild/SPECS/*
cp centos/jicofo_centos.tar.gz $DIR/rpmbuild/SOURCES/.
cp centos/RPM_SPEC/jicofo_centos_install.spec $DIR/rpmbuild/SPECS/.

# Creating RPM.
rpmbuild --define "_topdir $DIR/rpmbuild" -ba $DIR/rpmbuild/SPECS/jicofo_centos_install.spec

# Moving the created RPM to the project folder.
mv $DIR/rpmbuild/RPMS/x86_64/jicofo*.rpm ../.

## Cleanup
rm -rf $DIR/rpmbuild/SOURCES/*
rm -rf $DIR/rpmbuild/SPECS/*
rm -rf $DIR/rpmbuild/RPMS/*
rm -rf centos/jicofo_centos/usr/share/jicofo/*
rm -rf jicofo-linux-x64*/
rm -rf centos/jicofo_centos.tar.gz
