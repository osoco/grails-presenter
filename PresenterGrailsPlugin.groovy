class PresenterGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.9 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Your name"
    def authorEmail = ""
    def title = "Plugin summary/headline"
    def description = 'Brief description of the plugin'

    def doWithDynamicMethods = { ctx ->
        for (domainClass in application.domainClasses) {
                domainClass.metaClass.decorate = {
            }
        }
    }
}
