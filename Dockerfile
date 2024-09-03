FROM amazoncorretto:21-alpine-jdk

COPY target/online-shop-cart-service-0.0.1-SNAPSHOT.jar online-shop-cart-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/online-shop-cart-service-0.0.1-SNAPSHOT.jar"]