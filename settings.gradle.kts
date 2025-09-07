// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // For community plugins
    }
}

dependencyResolutionManagement {
    // This line enforces that repositories are declared only in settings.gradle.kts
    // and fails the build if they are found in project-level or module-level build.gradle files.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        // If you use libraries from JitPack or other custom repositories, add them here:
        // maven { url = uri("https://www.jitpack.io") }
        // maven { url = uri("https://your-custom-maven-repo.com/repository/maven-releases/") }
    }
}

rootProject.name = "YourProjectName" // Replace "YourProjectName" with your actual project name
include(":app") // Include your application module (and any other modules)
// include(":library_module_1")
// include(":feature_module_x")

