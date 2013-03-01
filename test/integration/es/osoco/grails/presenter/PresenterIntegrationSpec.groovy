package es.osoco.grails.presenter

import spock.lang.Specification

class PresenterIntegrationSpec extends Specification{

    def"Method decorate exists in domain clases"(){
        given:
        new Book().decorate()

        expect:
        notThrown MissingMethodException
    }
}
