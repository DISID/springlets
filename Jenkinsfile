pipeline {
  agent any
  stages {
    stage('Compile and test') {
      steps {
        withMaven(maven: 'M3') {
          sh 'sh mvn clean test'
        }
        
      }
    }
  }
}