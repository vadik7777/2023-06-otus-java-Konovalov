dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("com.google.code.findbugs:jsr305")

    implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
}

