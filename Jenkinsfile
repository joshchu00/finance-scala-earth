node {
    def earthImage

    stage('Git Pull') {
        git url: 'https://github.com/joshchu00/finance-scala-earth.git', branch: 'develop'
    }
    stage('sbt Build') {
        sh "${tool name: 'sbt-1.1.4', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt docker:stage"
    }
    stage('Docker Build') {
        docker.withTool('docker-latest') {
            earthImage = docker.build('docker.io/joshchu00/finance-scala-earth', 'target/docker/stage')
        }
    }
    stage('Docker Push') {
        docker.withTool('docker-latest') {
            docker.withRegistry('', 'DockerHub') {
                earthImage.push()
            }
        }
    }
}