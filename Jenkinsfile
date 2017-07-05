pipeline {
  agent any
  stages {
    stage('Compile and test') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e clean test -Dsurefire.useFile=false
'''
        }
        
      }
    }
  }
}