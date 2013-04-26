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
        presenter.getaProperty() == 'value'
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

    def 'properties of the wrapped object can be set only by calling directly a setter'() {
        given:
        def delegatee = new Delegatee()
        def presenter = new DelegatingPresenter(destination: delegatee)

        when:
        presenter.setaProperty 'value'

        then:
        delegatee.aProperty == 'value'
    }

    def "throws MissingPropertyException if the delegatee doesn't have the requested property"() {
        given:
        def delegatee = new Delegatee()
        def presenter = new DelegatingPresenter(destination: delegatee)

        when:
        presenter.notExistingProperty

        then:
        thrown MissingPropertyException
    }

    def 'calls defined methods from the wrapped object'() {
        given:
        def delegatee = new Delegatee()
        def presenter = new DelegatingPresenter(destination: delegatee)

        expect:
        presenter.parameterless() == delegatee.parameterless()
        presenter.withParameters('param1', 2, [3]) == delegatee.withParameters('param1', 2, [3])
        presenter.defaultParameterValues('11', 22, 11..33, 44) == delegatee.defaultParameterValues('11', 22, 11..33, 44)
        presenter.defaultParameterValues('11', 22, 11..33) == delegatee.defaultParameterValues('11', 22, 11..33)
        presenter.defaultParameterValues('11') == delegatee.defaultParameterValues('11')
        presenter.defaultParameterValues() == delegatee.defaultParameterValues()
        presenter.firstParameterHasDefaultValue('11', 22, 11..33) == delegatee.firstParameterHasDefaultValue('11', 22, 11..33)
        presenter.firstParameterHasDefaultValue(22, 11..33) == delegatee.firstParameterHasDefaultValue(22, 11..33)
        presenter.namedParameters('1', 2, param3: '3', param4: 11..44) == delegatee.namedParameters('1', 2, param3: '3', param4: 11..44)
        presenter.varargs('1', 2, 3, 4) == delegatee.varargs('1', 2, 3, 4)
        presenter.lastClosureParameter('1') { 2 } == delegatee.lastClosureParameter('1') { 2 }
    }

    def 'calls dynamic methods from the wrapped object'()
    {
        given:
        def delegatee = new Delegatee()
        delegatee.metaClass.dynamic = { 'dynamic'}
        def presenter = new DelegatingPresenter(destination: delegatee)

        expect:
        presenter.dynamic() == delegatee.dynamic()
    }

    def "throws MissingMethodException if the delegatee doesn't respond to the method"() {
        given:
        def delegatee = new Delegatee()
        def presenter = new DelegatingPresenter(destination: delegatee)

        when:
        presenter.notExistingMethod()

        then:
        thrown MissingMethodException
    }
}
