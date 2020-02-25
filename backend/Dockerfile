FROM shinyay/adoptopenjdk11-minimum

RUN mkdir /opt/cdbg
RUN wget -qO- https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz | tar xvz -C /opt/cdbg

COPY target/lib/* /app/lib/
COPY target/helloworld-service-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.module=helloworld-service", "-Dcom.google.cdbg.version=1.0-SNAPSHOT","-jar", "/app/app.jar"]

FROM gradle:6.0.1-jdk11 as compile
COPY . /home/source/java
WORKDIR /home/source/java/backend
USER root
RUN chown -R gradle /home/source/java/backend
USER gradle
RUN ls -l && gradle clean assemble

FROM openjdk:8 as cdbg
RUN mkdir /opt/cdbg
RUN curl https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz | tar zxv -C /opt/cdbg

FROM shinyay/adoptopenjdk11-minimum
WORKDIR /home/application/java
RUN mkdir /opt/cdbg
COPY --from=cdbg "/opt/cdbg/*" /opt/cdbg/
COPY --from=compile "/home/source/java/guestbook/build/libs/backend-0.0.1-SNAPSHOT.jar" ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.module=backend", "-Dcom.google.cdbg.version=1.0-SNAPSHOT","-jar", "app.jar"]
