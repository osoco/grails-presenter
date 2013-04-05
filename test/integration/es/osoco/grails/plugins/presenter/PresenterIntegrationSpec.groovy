package es.osoco.grails.plugins.presenter

import es.osoco.grails.plugins.presenter.test.Author
import es.osoco.grails.plugins.presenter.test.AuthorPresenter
import spock.lang.Specification

class PresenterIntegrationSpec extends Specification {
    def "annotated class has a decorate method that returns a presenter"() {
        expect:
        new Author().decorate().class == AuthorPresenter
    }
}
