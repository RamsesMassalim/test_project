until cqlsh -f /docker-entrypoint-initdb.d/init.cql
do
    now=$(date +%T)
    echo "[$now INIT CQLSH]: Node still unavailable, will retry another time"
    sleep 10
done &

# Execute the original Docker entry point
exec /usr/local/bin/docker-entrypoint.sh "$@"