package es.osoco.grails.plugins.presenter

class Delegator {
    def source

    def propertyMissing(String name) {
        source.getProperty(name)
    }

    def methodMissing(String name, args) {
        source.invokeMethod(name, args)
    }
}
