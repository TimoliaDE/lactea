rootProject.name = "lactea"
include(
    "initialize",
    "config",
    "core",
    "module-api"
)
includeLoaderProject("bukkit")
includeLoaderProject("nukkit")
includeLoaderProject("standalone")
fun includeLoaderProject(name: String) {
    val fullName = "loader:loader-$name"
    include(fullName)
    findProject(":$fullName")!!.name = "loader-$name"
}
