pipeline {
    agent any
    
    stages {
        stage('Start Services') {
            steps {
                bat 'docker-compose up -d mysql'
                bat 'timeout /t 30 /nobreak' // Espera 30 segundos para que MySQL inicie
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