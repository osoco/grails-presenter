package es.osoco.grails.plugins.presenter

import es.osoco.grails.plugins.presenter.test.Author
import spock.lang.Specification

class PresenterIntegrationSpec extends Specification{

    def"Method decorate exists in domain clases"(){
        given:
        new Author().decorate()

        expect:
        notThrown MissingMethodException
    }
}
