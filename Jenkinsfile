pipeline {
    agent any
    
    environment {
        DOCKER_BUILDKIT = "1"
    }
    
    stages {
        stage('Start MySQL') {
            steps {
                script {
                    // Limpiar contenedores anteriores
                    bat 'docker-compose down -v || echo "Cleanup completed"'

                    // Levantar MySQL
                    bat 'docker-compose up -d mysql'

                    // Esperar a que MySQL esté "healthy"
                    sleep(time: 10, unit: 'SECONDS') // Espera corta inicial
                    def healthy = false
                    def retries = 12
                    def interval = 5 // 5 segundos entre cada intento
                    
                    for (int i = 0; i < retries; i++) {
                        def status = bat(
                            script: 'docker inspect --format="{{.State.Health.Status}}" mysql-db',
                            returnStdout: true
                        ).trim()

                        echo "Estado de MySQL: ${status}"
                        
                        if (status == 'healthy') {
                            healthy = true
                            break
                        }
                        
                        sleep(time: interval, unit: 'SECONDS')
                    }

                    if (!healthy) {
                        bat 'docker logs mysql-db'
                        error("MySQL no se inició correctamente después de varios intentos")
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
                bat 'docker-compose logs --no-color > docker-logs.txt || echo "No logs"'
                archiveArtifacts artifacts: 'docker-logs.txt', allowEmptyArchive: true
                bat 'docker-compose down -v'
            }
        }
        success {
            echo 'Pipeline ejecutado correctamente 🚀'
        }
        failure {
            echo 'Pipeline falló 😢. Revisar logs.'
        }
    }
}
