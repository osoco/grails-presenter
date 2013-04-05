package es.osoco.grails.plugins.presenter

import es.osoco.grails.plugins.presenter.test.Author
import es.osoco.grails.plugins.presenter.test.AuthorPresenter
import spock.lang.Specification

class PresenterIntegrationSpec extends Specification{

    def"Method decorate exists in domain clases"(){
        given:
        new Author().decorate()

        expect:
        notThrown MissingMethodException
    }

    def"Check if decorate method return its presenter class"(){
        given:
        new Author().decorate() instanceof AuthorPresenter
    }
}
