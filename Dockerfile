FROM java:8
VOLUME /tmp
EXPOSE 8080
ADD gossip-protocol-java.jar gossip-protocol-java.jar
ENTRYPOINT ["java","-jar","gossip-protocol-java.jar"]