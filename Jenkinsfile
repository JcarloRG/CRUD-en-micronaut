pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'docker-compose build'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completado - limpiando'
        }
        success {
            echo '¡Despliegue exitoso!'
        }
        failure {
            echo 'Pipeline fallido'
        }
    }
}
