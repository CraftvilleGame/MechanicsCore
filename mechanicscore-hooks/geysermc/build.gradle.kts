plugins {
    `java-library`
}

dependencies {
    compileOnly(project(":mechanicscore-core"))
    compileOnly(libs.foliaScheduler)
    compileOnly(libs.paper)
    compileOnly(libs.geyser)
}
