Data catalog repository is for deep learning platform and other purposes. It manages databases at the data collection, patient, study, and series/image-set levels. Also stores relevant notifications from COS. It provides APIs to access those databases. It also provides an annotation database where segmentation masks, ROI, and other curation artifacts can be stored.

MVN commands to make sure build and tests are passing:

mvn clean install -P all 
# - runs everything 
mvn clean install -P unit 
# - runs only unit tests 
mvn clean install -P component 
# - runs only component tests 
mvn clean install -P contract 
# - runs only contract tests 
mvn clean install -P integration 
# - runs only integration tests
