pipeline {
    agent any
    
    environment {
        DOCKER_BUILDKIT = "1"
        COMPOSE_DOCKER_CLI_BUILD = "1"
    }
    
    stages {
        stage('Start MySQL') {
            steps {
                script {
                    // Limpieza previa
                    bat 'docker-compose down -v || echo "Cleanup completed"'
                    
                    // Iniciar MySQL
                    bat 'docker-compose up -d mysql'
                    
                    // Espera mejorada con verificación de logs
                    def maxAttempts = 24
                    def healthy = false
                    
                    for (int i = 1; i <= maxAttempts; i++) {
                        sleep(time: 5, unit: 'SECONDS')
                        
                        // Verificar estado del contenedor
                        def status = bat(
                            script: 'docker inspect -f {{.State.Status}} mysql-db',
                            returnStdout: true
                        ).trim()
                        
                        if (status != "running") {
                            error("MySQL container is not running. Status: ${status}")
                        }
                        
                        // Verificar healthcheck
                        def health = bat(
                            script: 'docker inspect -f {{.State.Health.Status}} mysql-db',
                            returnStdout: true
                        ).trim()
                        
                        echo "Intento ${i}/${maxAttempts} - Estado: ${health}"
                        
                        if (health == "healthy") {
                            // Verificación adicional ejecutando un comando en MySQL
                            def testQuery = bat(
                                script: 'docker exec mysql-db mysql -uroot -p123456 -e "SHOW DATABASES;" || exit 1',
                                returnStdout: true
                            )
                            
                            if (testQuery.contains("tienda")) {
                                healthy = true
                                break
                            }
                        }
                    }
                    
                    if (!healthy) {
                        // Capturar logs detallados
                        bat 'docker logs mysql-db > mysql-logs.txt'
                        archiveArtifacts artifacts: 'mysql-logs.txt'
                        error("MySQL no se inició correctamente después de ${maxAttempts * 5} segundos")
                    }
                }
            }
        }
        
        stage('Build Application') {
            steps {
                bat 'docker-compose build app'
                bat 'docker-compose run --rm app gradle build --no-daemon'
            }
        }
        
        stage('Deploy') {
            steps {
                bat 'docker-compose up -d app'
            }
        }
    }
    
    post {
        always {
            script {
                // Capturar logs antes de limpiar
                bat 'docker-compose logs --no-color > docker-logs.txt || echo "No logs"'
                archiveArtifacts artifacts: 'docker-logs.txt'
                
                // Limpieza
                bat 'docker-compose down -v'
            }
        }
    }
}