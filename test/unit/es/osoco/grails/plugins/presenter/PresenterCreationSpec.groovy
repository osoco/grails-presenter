package es.osoco.grails.plugins.presenter

import spock.lang.Specification

class PresenterCreationSpec extends Specification {
    def "can be created using a factory method called with a decoratee instance"() {
        given:
        def delegatee  = new Delegatee()

        when:
        def presenter = DelegatingPresenter.decorate(delegatee)

        then:
        presenter.destination.is delegatee
    }
}
