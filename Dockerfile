FROM adoptopenjdk:11-jre-openj9
ADD ./backend/target/drasyl-network-explorer-spring-boot.jar /root/drasyl-network-explorer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/root/drasyl-network-explorer.jar"]
