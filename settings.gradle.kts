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
        jcenter()
        maven (url= "http://dl.bintray.com/dasar/maven"){
            isAllowInsecureProtocol = true
        }
        maven ( url= "https://jitpack.io" )
    }
}

rootProject.name = "Graphics Maker"
include(":app")
include(":library")
include(":cc_ads")
include(":shiftcolorpicker")
