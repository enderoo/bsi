FROM java:8
COPY ./Main.java /usr/src/
COPY ./mysql-connector-java-5.1.6.jar /usr/src/
WORKDIR /usr/src/
RUN javac Main.java
CMD ["java", "-classpath", "mysql-connector-java-5.1.6.jar:.", "Main"]
