### Build docker image

docker build --build-arg JAR_FILE=build/libs/fileBot-0.0.1.jar -t thorinhood/fileBot .

### Run docker container

docker run -d -p 8080:8080 -p 8090:8090 -v "path to logs in machine":/logs thorinhood/fileBot

### Run prometheus container

docker run -d --name=prometheus --net="host" -p 9090:9090 -v /etc/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
-v /etc/prometheus/rules.yml:/etc/prometheus/rules.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml

### Prometheus board

http://13.58.79.77:9090/graph?g0.expr=rate(request_count_total%5B1m%5D)&g0.tab=0&g0.stacked=0&g0.show_exemplars=1&g0.range_input=30m&g1.expr=sum(jvm_memory_used_bytes%7Barea%3D%22heap%22%7D)%2F1024%2F1024&g1.tab=0&g1.stacked=1&g1.show_exemplars=0&g1.range_input=15m&g2.expr=sum(jvm_memory_max_bytes%7Barea%3D%22heap%22%7D)%2F1024%2F1024&g2.tab=0&g2.stacked=1&g2.show_exemplars=0&g2.range_input=30m&g3.expr=sum(jvm_memory_used_bytes%7Barea%3D%22heap%22%7D)%20%2F%20sum(jvm_memory_max_bytes%7Barea%3D%22heap%22%7D)%20*%20100&g3.tab=0&g3.stacked=1&g3.show_exemplars=0&g3.range_input=1h&g4.expr=up&g4.tab=0&g4.stacked=1&g4.show_exemplars=0&g4.range_input=1h&g5.expr=sum(jvm_gc_pause_seconds_max)&g5.tab=0&g5.stacked=0&g5.show_exemplars=0&g5.range_input=1h&g6.expr=jvm_threads_states_threads&g6.tab=0&g6.stacked=0&g6.show_exemplars=0&g6.range_input=1h&g7.expr=jvm_threads_peak_threads&g7.tab=0&g7.stacked=0&g7.show_exemplars=0&g7.range_input=1h&g8.expr=log4j2_events_total&g8.tab=0&g8.stacked=0&g8.show_exemplars=0&g8.range_input=1d

### Run mongo

docker-compose up

docker exec -it mongo-container bash

mongo -u root_user -p password

### Sessions storages 

available two ways: mongo and in memory

spring.profiles.active=mongo (in this case you need to define propeties for mongo db connection)
\
spring.profiles.active=memory