#! /bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5
export PGPASSWORD=$psql_password

hostname=$(hostname -f)

lscpu_out=`lscpu`
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | awk '{print $3}' | xargs)
cpu_mhz=$(awk -F: '/^cpu MHz/ {print $2; exit}' /proc/cpuinfo | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

echo "Hostname: $hostname"
echo "Cpu number: $cpu_number
Architecture: $cpu_architecture
CPU Model: $cpu_model
CPU mhz: $cpu_mhz
l2 cache: $l2_cache
Total Mem: $total_mem
Timestamp: $timestamp"

psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, \"timestamp\", total_mem) VALUES (1, '$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"

exit 0
