package es.osoco.grails.plugins.presenter

import es.osoco.grails.plugins.presenter.test.Author
import es.osoco.grails.plugins.presenter.test.AuthorPresenter
import grails.plugin.spock.IntegrationSpec

class PresenterDependencyInjectionSpec extends IntegrationSpec {
    def weirdService

    def 'autowires on new'() {
        given:
        def author = new Author()
        def presenter = new AuthorPresenter()

        expect:
        author.weirdService == weirdService
        presenter.weirdService == weirdService
    }

    def 'presenters are prototypes'() {
        given:
        def (presenter1, presenter2) = [new AuthorPresenter(), new AuthorPresenter()]

        expect:
        !presenter1.is(presenter2)
    }
}
