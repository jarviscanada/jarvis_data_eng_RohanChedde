# Linux Cluster Monitoring Agent

# Introduction
The purpose of this project is to store the server hardware specifications and on-going Linux resource usage data in a psql database dynamically. This system was automated using bash scripts and the usage script is ran periodically using Crontab. These bash scripts are essentially a file that executes a series of linux commands. In this case, the commands extract the server hardware info and its usage data. To extract the information, the scripts utilized Regex, a tool to search and extract specific information. Moreover, the PSQL instance was set up on a docker container using a given script and and SQL file was used to create tables to store this information.
Additionally, this project used the gitflow methodology by using different git branches for respective features and then were merged to the develop branch once finalized.

# Quick Start
- Start the psql instance using: ./scripts/psql_docker.sh
- To create the table run: psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
- Insert hardware specs data: bash ./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "password"
- Insert hardware usage data: bash scripts/host_usage.sh localhost 5432 host_agent postgres password
- Set up Crontab: 
	1) bash> crontab -e
	2) #add this to the crontab file: 
	  /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log

#Implementation
##Architecture
