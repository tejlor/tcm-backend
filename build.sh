#!/bin/bash

rc=0

function compile {    
	export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64"
	mvn clean package -Dmaven.test.skip=true
	rc=$?
}

function deploy {
	if [[ $rc -ne 0 ]] ; then
		exit
	fi

	java -jar target/tcm.jar --spring.profiles.active=dev
}

function test {
	export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64"
	mvn clean package
}

while getopts "cdtv:" opt; do  
	case $opt in    
	c)      
		compile $OPTARG
		;;    
	d)      
		deploy      
		;;
	t)      
		test      
		;;   
	v)
		mvn versions:set -DnewVersion=${OPTARG}
		eclipse
		;;
	\?)      
		echo "Dopuszczalne opcje to: c (compile), d (deploy), t (test), p (prod), e (eclipse), v [number] (version)."      
		;;  
	esac
done
