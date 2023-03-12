rootProject.name = "trainingBot"
include(":engine")
include(":telegram")
project(":engine").projectDir = file("../engine")
project(":telegram").projectDir = file("../telegram")