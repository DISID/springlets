pipeline {
  agent any
  stages {
    stage('Compile and test') {
      steps {
        sh 'sh mvn clean test'
      }
    }
  }
}