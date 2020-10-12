run:
	cd ./user/ && pwd && mvn clean package -DskipTests
	cd ./finance/ && pwd && mvn clean package -DskipTests && cd ./
	cd ./category/ && pwd && mvn clean package -DskipTests && cd ./
	cd ./expense/ && pwd && mvn clean package -DskipTests && cd ./
	cd ./income/ && pwd && mvn clean package -DskipTests && cd ./
	docker-compose build
	docker-compose up -d
