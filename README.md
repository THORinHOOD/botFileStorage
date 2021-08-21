### Build docker image

docker build --build-arg JAR_FILE=build/libs/benchBot-0.0.1.jar -t thorinhood/benchbot .

### Run docker container

docker run -d -p 8080:8080 -p 8090:8090 -v "path to logs in machine":/logs thorinhood/benchbot

### Run prometheus container

docker run -d --name=prometheus --net="host" -p 9090:9090 -v /etc/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
-v /etc/prometheus/rules.yml:/etc/prometheus/rules.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml

### Prometheus board

http://13.58.79.77:9090/graph?g0.expr=sum(jvm_memory_used_bytes%7Barea%3D%22heap%22%7D)%2F1024%2F1024&g0.tab=0&g0.stacked=1&g0.show_exemplars=1&g0.range_input=30m&g1.expr=sum(jvm_memory_max_bytes%7Barea%3D%22heap%22%7D)%2F1024%2F1024&g1.tab=0&g1.stacked=1&g1.show_exemplars=0&g1.range_input=15m&g2.expr=sum(jvm_memory_used_bytes%7Barea%3D%22heap%22%7D)%20%2F%20sum(jvm_memory_max_bytes%7Barea%3D%22heap%22%7D)%20*%20100&g2.tab=0&g2.stacked=1&g2.show_exemplars=0&g2.range_input=15m&g3.expr=up&g3.tab=0&g3.stacked=1&g3.show_exemplars=0&g3.range_input=1h&g4.expr=sum(jvm_gc_pause_seconds_max)&g4.tab=0&g4.stacked=1&g4.show_exemplars=0&g4.range_input=1h&g5.expr=jvm_threads_states_threads&g5.tab=0&g5.stacked=0&g5.show_exemplars=0&g5.range_input=1h&g6.expr=jvm_threads_peak_threads&g6.tab=0&g6.stacked=1&g6.show_exemplars=0&g6.range_input=1h&g7.expr=log4j2_events_total&g7.tab=0&g7.stacked=0&g7.show_exemplars=0&g7.range_input=1h