package es.osoco.grails.plugins.presenter

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
