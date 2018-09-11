Path Guide
========
Main Code for RESTful Web Service Library is in the following path:

RESTful_Web_Service_Project/src/main/java/jayray/net/project_code

How-to run
==========
0.1) Pre-req

You will need the following installed:
Java
Gradle (optional)
A java web container, such as Tomcat.

If you are on a mac, I recommend to do the following:
install Homebrew (see: http://brew.sh/)
from command line, and then to install Java and Tomcat:
```
brew install java
brew install apache
```

If you will be using Tomcat, you will likely want to make sure you have CATALINA_HOME set.  
On a mac, edit your profile
```
vi ~/.bash_profile
```

and then add the following (replacing with the directory where you Tomcat instance is deployed:
```
export CATALINA_HOME=/usr/local/Cellar/tomcat/8.5.9/libexec
```

1) Compile
The project compiles using gradle.  If you already have gradle installed, compile using:
```
gradle build
```

If you do not have gradle installed, you can utilize the gradle wrapper included in the source
```
./gradlew war
```

The war file is compiled to: `build/libs/jersey-starterkit.war`


2) Deploy the war file to web container.  I've been using apache-tomcat [http://tomcat.apache.org], and typically copy the war to the tomcat webapps directory.  On my machine:
```
cp build/libs/jersey-starterkit.war /Applications/apache-tomcat-6.0.33/webapps/
```

Shortcut: if you are using tomcat, and $CATALINA_HOME is set, you can run: `./deploy.sh`


3) Confirm that it is running by fetching the URL at on webcontainer + /jersey-helloworld/rest/hello.  On my machine:
```
curl localhost:8080/jersey-starterkit/rest/hello
```

The supported endpoints are:
```
http://localhost:8080/jersey-starterkit/rest/customer/id/1
```
```
http://localhost:8080/jersey-starterkit/rest/echo?m=hello
```
```
http://localhost:8080/jersey-starterkit/rest/hello
```


Opening in Eclipse
==================
If you use Eclipse, the gradle scripts are nice enough to create your eclipse project and classpath files.

First time only
---------------
If you have gradle installed, run:
```
gradle eclipse
```

Now you can import the project into eclipse.


Updating classpath files
------------------------
If you update dependencies, pull the new libs into your classpath:
```
gradle eclipseClasspath
```

Logging
=======
There is a log4j configuration defined in `src/main/resources/log4j.properties`.  By default this will log to the STDOUT and to a series of log files.  Change the logging configuration as needed.

If you would like to use the default logging, create the logging folders:
```
> sudo mkdir /restapi
> chmod a+wr /restapi
````

