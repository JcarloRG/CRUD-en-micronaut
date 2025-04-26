pipeline {
    agent any
    
    stages {
        stage('Start Services') {
            steps {
                bat 'docker-compose up -d mysql'
                script {
                    // Espera hasta que MySQL esté saludable
                    def mysqlHealthy = false
                    def attempts = 0
                    while(!mysqlHealthy && attempts < 30) {
                        attempts++
                        sleep(time: 5, unit: 'SECONDS')
                        def result = bat(script: 'docker inspect --format "{{.State.Health.Status}}" crud-en-micronaut-mysql-1', returnStdout: true).trim()
                        mysqlHealthy = result == 'healthy'
                    }
                    if(!mysqlHealthy) {
                        error("MySQL no se inició correctamente después de 30 intentos")
                    }
                }
            }
        }
        stage('Build') {
            steps {
                bat 'docker-compose build app'
                bat 'docker-compose run app gradle build --no-daemon'
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
            bat 'docker-compose down'
        }
    }
}