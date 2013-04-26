package es.osoco.grails.plugins.presenter

import es.osoco.grails.plugins.presenter.test.autoscan.Author
import es.osoco.grails.plugins.presenter.test.autoscan.AuthorPresenter
import es.osoco.grails.plugins.presenter.test.autoscan.CreativePresenter
import es.osoco.grails.plugins.presenter.test.autoscan.Invoice
import es.osoco.grails.plugins.presenter.test.autoscan.Painter
import es.osoco.grails.plugins.presenter.test.autoscan.PersonPresenter
import es.osoco.grails.plugins.presenter.test.autoscan.Runner
import es.osoco.grails.plugins.presenter.test.manualconfig.AlternativeAuthorPresenter
import grails.plugin.spock.IntegrationSpec

class PresenterIntegrationSpec extends IntegrationSpec {
    def weirdService

    def "domain class has a decorate method that returns a default presenter"() {
        expect:
        authorPresenter().class == AuthorPresenter
    }

    def "custom presenter class can be passed to the decorate method"() {
        expect:
        alternativeAuthorPresenter().class == AlternativeAuthorPresenter
    }

    def "decorator for the parent class is used if no decorator is found by convention"() {
        expect:
        new Painter().decorate().class == CreativePresenter
        new Runner().decorate().class == PersonPresenter
    }

    def "exception is thrown if there is no presenter for the class and its parents"() {
        when:
        new Invoice().decorate()

        then:
        thrown ClassNotFoundException
    }

    def 'dependencies of annotated presenters are injected automatically'() {
        expect:
        authorPresenter().weirdService == weirdService
    }

    def 'dependencies of manually configured presenters are injected automatically'() {
        expect:
        alternativeAuthorPresenter().weirdService == weirdService
    }

    def 'annotated presenters are not singletons'() {
        given:
        def (presenter1, presenter2) = [authorPresenter(), authorPresenter()]

        expect:
        !presenter1.is(presenter2)
    }

    def 'manually configured presenters are not singletons'() {
        given:
        def (presenter1, presenter2) = [alternativeAuthorPresenter(), alternativeAuthorPresenter()]

        expect:
        !presenter1.is(presenter2)
    }

    private authorPresenter() {
        new Author().decorate()
    }

    private alternativeAuthorPresenter() {
        new Author().decorate AlternativeAuthorPresenter
    }
}
