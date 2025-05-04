pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Stox"
include(":app")
include(":app:core-ui")
include(":app:core-domain")
include(":app:core-data")
include(":app:feature-stocks-overview")
include(":app:feature-stock-list")
include(":app:feature-stock-detail")
include(":app:feature-add-stock")
