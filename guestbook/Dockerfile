FROM gradle:6.0.1-jdk11 as compile
COPY . /home/source/java
WORKDIR /home/source/java/guestbook
USER root
RUN chown -R gradle /home/source/java/guestbook
USER gradle
RUN ls -l && gradle clean assemble

FROM openjdk:8 as cdbg
RUN mkdir /opt/cdbg
RUN curl https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz | tar zxv -C /opt/cdbg

FROM openjdk:11.0.5-jre-slim-buster
WORKDIR /home/application/java
RUN mkdir /opt/cdbg
COPY --from=cdbg "/opt/cdbg/*" /opt/cdbg/
COPY --from=compile "/home/source/java/guestbook/build/libs/guestbook-0.0.1-SNAPSHOT.jar" ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.module=guestbook-service", "-Dcom.google.cdbg.version=0.0.1-SNAPSHOT","-jar", "app.jar"]