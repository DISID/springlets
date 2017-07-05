pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e clean compile -Dsurefire.useFile=false
'''
        }
        
      }
    }
    stage('Unit Test') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e test -Dsurefire.useFile=false
'''
        }
        
      }
    }
  }
}