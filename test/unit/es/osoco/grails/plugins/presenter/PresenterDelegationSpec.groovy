package es.osoco.grails.plugins.presenter

import grails.plugin.spock.UnitSpec

class PresenterDelegationSpec extends UnitSpec {
    def 'has destination field'() {
        expect:
        new DelegatingPresenter(destination: new Delegatee())
    }

    def 'gets properties from the wrapped object'() {
        given:
        def delegatee = new Delegatee(aProperty: 'value')
        def presenter = new DelegatingPresenter(destination: delegatee)

        expect:
        presenter.aProperty == 'value'
    }

    def 'setting properties in the wrapped object is not supported'() {
        given:
        def delegatee = new Delegatee(aProperty: 'value')
        def presenter = new DelegatingPresenter(destination: delegatee)

        when:
        presenter.aProperty = 'value'

        then:
        thrown MissingPropertyException
    }
}
