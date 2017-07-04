pipeline {
  agent any
  stages {
    stage('Use maven 3') {
      steps {
        withMaven(maven: 'M3')
      }
    }
    stage('Compile and test') {
      steps {
        sh 'sh mvn clean test'
      }
    }
  }
}