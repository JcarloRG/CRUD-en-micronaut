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
<<<<<<< HEAD
}
=======
}
>>>>>>> cbad2069f27b97efd7a15a8c6a4acea5d7d7b518
