FROM confluentinc/cp-kafka-connect-base:7.6.0
# You can also use confluentinc/cp-server-connect image in production with a 30 day
# evaluation licensing limitation in a multi Kafka broker configuration per cluster

# - https://docs.confluent.io/platform/current/connect/license.html#license-for-self-managed-connectors
# - https://docs.confluent.io/platform/current/installation/license.html#developer-license
# - https://www.confluent.io/confluent-community-license-faq/
# - https://stackoverflow.com/questions/61532385/kafka-dependencies-ccs-vs-ce

###    Install JRE 21   ###
ENV ZULU_JRE_VERSION="zulu21-ca-jre-headless"

USER root
RUN microdnf install yum \
    && rpm --import https://www.azul.com/wp-content/uploads/2021/05/0xB1998361219BD9C9.txt \
    && yum install -y https://cdn.azul.com/zulu/bin/zulu-repo-1.0.0-1.noarch.rpm \
    && yum -y install ${ZULU_JRE_VERSION}
USER appuser
###########################

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-datagen:0.6.5 && \
    confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.8.4

COPY ./kafka-connect-smt-lib/build/libs/kafka-connect-smt-lib*.jar /usr/share/java/jjug/
