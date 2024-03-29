rootProject.name = "otusJava"
include("hw01-gradle")
include("hw02-collections")
include("hw03-annotations")
include("hw04-gc")
include("hw05-aop")
include("hw06-atm")
include("hw07-handler")
include("hw08-serd")
include("hw09-jdbc")
include("hw10-hibernate")
include("hw11-cache_engine")
include("hw12-web_server")
include("hw13-di")
include("hw14-jdbc")
include("hw15-sequence_of_numbers")
include("hw16-queue")
include("hw17-grpc")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val sonarlint: String by settings
    val spotless: String by settings
    val protobufVer: String by settings


    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
        id("com.google.protobuf") version protobufVer
    }
}
