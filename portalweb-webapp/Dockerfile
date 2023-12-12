FROM payara/server-web:5.2022.5-jdk11
ENV URL_BASE=http://baseapi:8080
RUN echo "the ENV variable value is $URL_BASE"
COPY ./target/*.war $DEPLOY_DIR