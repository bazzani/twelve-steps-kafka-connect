#!/bin/bash

log_message() {
  current_datetime=$(date +"%Y-%m-%d %H:%M:%S,%3N")
  echo "[$current_datetime] Connect Startup: $1"
}

config_files_dir=/connect-connector-configs
config_files=$(ls $config_files_dir | grep json)
config_count=$(echo "$config_files" | wc -l)

log_message "Creating [$config_count] connectors from config files at '$config_files_dir'..."

for config_file in $config_files; do
  config_file_location=$config_files_dir/"$config_file"
  connector_config=$(cat "$config_file_location" | jq -r)
  connector_name=$(echo $connector_config | jq -r .name)

  log_message "Creating connector from config file [$connector_name] @ $config_file_location"

  ########
  # Delete existing version of the connector
  ########
  log_message "Deleting connector [$connector_name]..."

  curl -s -X DELETE -H "Content-Type:application/json" http://localhost:8083/connectors/"${connector_name}"
  log_message ""
  log_message "Pausing for delete propagation to other Kafka Connect workers..."
  sleep 2
  ########

  ########
  # Create new version of the connector
  ########
  log_message "Creating connector [$connector_name]..."

  curl -s -X PUT -H "Content-Type:application/json" http://localhost:8083/connectors/"${connector_name}"/config \
    -d "$connector_config"
  log_message ""
  log_message "Pausing for config propagation to other Kafka Connect workers..."
  sleep 2
  ########
done

log_message "Finished creating [$config_count] connectors"
