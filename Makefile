run/envoy:
	sudo envoy -c ext/envoy.yaml \
		--service-cluster 'ingress' --service-node 'ingress'

init/%:
	mkdir -p $*/src/{main,test}/{java,kotlin,resources}