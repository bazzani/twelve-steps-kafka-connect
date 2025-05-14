FROM confluentinc/cp-kafka-connect-base:7.6.0
# You can also use confluentinc/cp-server-connect image in production with a 30 day
# evaluation licensing limitation in a multi Kafka broker configuration per cluster

# - https://docs.confluent.io/platform/current/connect/license.html#license-for-self-managed-connectors
# - https://docs.confluent.io/platform/current/installation/license.html#developer-license
# - https://www.confluent.io/confluent-community-license-faq/
# - https://stackoverflow.com/questions/61532385/kafka-dependencies-ccs-vs-ce

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-datagen:0.6.5 && \
    confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.8.4
