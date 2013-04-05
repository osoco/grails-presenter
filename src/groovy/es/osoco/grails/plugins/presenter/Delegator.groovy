package es.osoco.grails.plugins.presenter

class Delegator {
    def destination

    def propertyMissing(String name) {
        destination.getProperty(name)
    }

    def methodMissing(String name, args) {
        destination.invokeMethod(name, args)
    }
}
