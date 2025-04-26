pipeline {
    agent any
    
    environment {
        DOCKER_BUILDKIT = "1"  # Habilita BuildKit para mejores builds
    }
    
    stages {
        stage('Start MySQL') {
            steps {
                script {
                    // Limpiar cualquier contenedor previo
                    bat 'docker-compose down -v || echo "No hay contenedores para limpiar"'
                    
                    // Iniciar solo MySQL
                    bat 'docker-compose up -d mysql'
                    
                    // Esperar con timeout mejorado
                    def waitTime = 120  // 2 minutos máximo
                    def interval = 5
                    def attempts = waitTime / interval
                    def healthy = false
                    
                    for (int i = 0; i < attempts; i++) {
                        sleep(time: interval, unit: 'SECONDS')
                        def status = bat(
                            script: 'docker inspect --format "{{.State.Health.Status}}" mysql-db',
                            returnStdout: true
                        ).trim()
                        
                        if (status == 'healthy') {
                            healthy = true
                            break
                        }
                        echo "Esperando que MySQL esté listo... Intento ${i+1}/${attempts} - Estado: ${status}"
                    }
                    
                    if (!healthy) {
                        // Obtener logs para diagnóstico
                        bat 'docker logs mysql-db'
                        error("MySQL no se inició correctamente después de ${waitTime} segundos")
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
            // Limpieza con captura de logs para diagnóstico
            script {
                bat 'docker-compose logs --no-color > docker-logs.txt || echo "No se pudieron obtener logs"'
                archiveArtifacts artifacts: 'docker-logs.txt', allowEmptyArchive: true
                bat 'docker-compose down -v'
            }
        }
        success {
            echo 'Pipeline ejecutado con éxito!'
        }
        failure {
            echo 'Pipeline falló, revisar logs para diagnóstico'
        }
    }
}