package es.osoco.grails.plugins.presenter

import es.osoco.grails.plugins.presenter.test.autoscan.AnnotatedPresenter
import es.osoco.grails.plugins.presenter.test.manualconfig.ManuallyConfiguredPresenter
import grails.plugin.spock.IntegrationSpec

class PresenterDependencyInjectionSpec extends IntegrationSpec {
    def weirdService

    def 'dependencies of annotated presenters are injected automatically'() {
        given:
        def presenter = new AnnotatedPresenter()

        expect:
        presenter.weirdService == weirdService
    }

    def 'dependencies of manually configured presenters are injected automatically'() {
        given:
        def presenter = new ManuallyConfiguredPresenter()

        expect:
        presenter.weirdService == weirdService
    }

    def 'annotated presenters are not singletons'() {
        given:
        def (presenter1, presenter2) = [new AnnotatedPresenter(), new AnnotatedPresenter()]

        expect:
        !presenter1.is(presenter2)
    }

    def 'manually configured presenters are not singletons'() {
        given:
        def (presenter1, presenter2) = [new ManuallyConfiguredPresenter(), new ManuallyConfiguredPresenter()]

        expect:
        !presenter1.is(presenter2)
    }
}
