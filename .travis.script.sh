if [ "$TRAVIS_SECURE_ENV_VARS" == "true" ]
then
	# Secure vars available, needed for TestBench tests
	# Pull request inside repository or branch build
	mvn -B -e -V -Dvaadin.testbench.developer.license=$TESTBENCH_LICENSE verify
fi