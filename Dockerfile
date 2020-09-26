FROM payara/server-full
COPY portalweb-service/target/*.war $DEPLOY_DIR
COPY portalweb-webapp/target/*.war $DEPLOY_DIR

