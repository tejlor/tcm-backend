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
	if [[ $rc -ne 0 ]] ; then
		exit
	fi

	java -jar target/tcm.jar --spring.profiles.active=test
}

function prod {
	if [[ $rc -ne 0 ]] ; then
		exit
	fi

	java -jar target/tcm.jar --spring.profiles.active=prod
}

function eclipse {
	mvn eclipse:clean
	mvn eclipse:eclipse -DdownloadSources -DdownloadJavadocs
}

while getopts "cdtpev:sq" opt; do  
	case $opt in    
	c)      
		compile      
		;;    
	d)      
		deploy      
		;;
	t)      
		test      
		;;
	p)      
		prod
		;;
	e)
		eclipse
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
