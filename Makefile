run/envoy:
	sudo envoy -c ext/envoy.yaml \
		--service-cluster 'ingress' --service-node 'ingress'


run/envoy-docker:
	docker rum --rm --name envoy 

init/%:
	mkdir -p $*/src/{main,test}/{java,kotlin,resources}