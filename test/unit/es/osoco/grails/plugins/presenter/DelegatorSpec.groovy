package es.osoco.grails.plugins.presenter

import spock.lang.Specification

class Delegatee {
    def aProperty

    def parameterless() {
        'parameterless'
    }

    def withParameters(String param1, Number param2, param3) {
        "withParameters($param1, $param2, $param3)"
    }

    def defaultParameterValues(String param1 = '1', Number param2 = 2, List param3 = 1..3, param4 = '4') {
        "defaultParameterValues($param1, $param2, $param3, $param4)"
    }

    def firstParameterHasDefaultValue(String param1 = '1', Number param2, List param3) {
        "firstParameterHasDefaultValue($param1, $param2, $param3)"
    }

    def namedParameters(Map args, String param1, Number param2) {
        "namedParameters($args, $param1, $param2)"
    }

    def varargs(param1, ...varargs) {
        "varargs($param1, $varargs)"
    }

    def lastClosureParameter(param1, Closure param2) {
        "lastClosureParameter($param1, ${param2()})"
    }
}

class DelegatorSpec extends Specification {
    def 'gets properties from the wrapped object'() {
        given:
        def delegatee = new Delegatee(aProperty: 'value')
        def delegator = new Delegator(source: delegatee)

        expect:
        delegator.aProperty == 'value'
        delegator.getaProperty() == 'value'
    }

    def 'properties of the wrapped object cannot be set using assignment'() {
        given:
        def delegatee = new Delegatee()
        def delegator = new Delegator(source: delegatee)

        when:
        delegator.aProperty = 'value'

        then:
        thrown(MissingPropertyException)
    }

    def 'properties of the wrapped object can be set only by calling directly a setter'() {
        given:
        def delegatee = new Delegatee()
        def delegator = new Delegator(source: delegatee)

        when:
        delegator.setaProperty 'value'

        then:
        delegatee.aProperty == 'value'
    }

    def "throws MissingPropertyException if the delegatee doesn't have the requested property"() {
        given:
        def delegatee = new Delegatee()
        def delegator = new Delegator(source: delegatee)

        when:
        delegator.notExistingProperty

        then:
        thrown MissingPropertyException
    }

    def 'calls methods from the wrapped object'() {
        given:
        def delegatee = new Delegatee()
        def delegator = new Delegator(source: delegatee)

        expect:
        delegator.parameterless() == delegatee.parameterless()
        delegator.withParameters('param1', 2, [3]) == delegatee.withParameters('param1', 2, [3])
        delegator.defaultParameterValues('11', 22, 11..33, 44) == delegatee.defaultParameterValues('11', 22, 11..33, 44)
        delegator.defaultParameterValues('11', 22, 11..33) == delegatee.defaultParameterValues('11', 22, 11..33)
        delegator.defaultParameterValues('11') == delegatee.defaultParameterValues('11')
        delegator.defaultParameterValues() == delegatee.defaultParameterValues()
        delegator.firstParameterHasDefaultValue('11', 22, 11..33) == delegatee.firstParameterHasDefaultValue('11', 22, 11..33)
        delegator.firstParameterHasDefaultValue(22, 11..33) == delegatee.firstParameterHasDefaultValue(22, 11..33)
        delegator.namedParameters('1', 2, param3: '3', param4: 11..44) == delegatee.namedParameters('1', 2, param3: '3', param4: 11..44)
        delegator.varargs('1', 2, 3, 4) == delegatee.varargs('1', 2, 3, 4)
        delegator.lastClosureParameter('1') { 2 } == delegatee.lastClosureParameter('1') { 2 }
    }

    def "throws MissingMethodException if the delegatee doesn't respond to the method"() {
        given:
        def delegatee = new Delegatee()
        def delegator = new Delegator(source: delegatee)

        when:
        delegator.notExistingMethod()

        then:
        thrown MissingMethodException
    }
}
