pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'echo Building Token Service'
        sh 'mvn package'
      }
    }
    stage('deploy') {
      steps {
      sh 'echo Deploying Token Service'
      sh 'docker-compose -f ./token-service/docker-compose.yml --build'
      }
    }
  }
}