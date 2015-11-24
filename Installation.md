# Installation #

Tomboy Web is packaged in a java web app archive (war) file. It can be deployed in any J2EE compliant server. Here is an installation instruction for Apache Tomcat.

At least Java 5 is required.

Step by step for apache-tomcat:
  * Download tomboy.war from the Downloads tab.
  * In Apache Tomcat copy tomboy.war to the webapps directory
  * start or restart tomcat

Go to http://host:8080/tomboy


Tomboy Web uses the .tomboy folder in your home directory. To change this, unpack the
tomboy.war, edit the WEB-INF/web-xml file and change the context parameter tomboy\_dir.

To build Tomboy Web see the README file in the source package.