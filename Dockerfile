FROM payara/server-full:5.2022.5-jdk11
COPY portalweb-service/target/*.war $DEPLOY_DIR
COPY portalweb-webapp/target/*.war $DEPLOY_DIR